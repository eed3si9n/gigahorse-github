import org.specs2._
import org.specs2.matcher._
import dispatch._
import github._

class GithubSpec extends Specification { def is = sequential                  ^
  "This is a specification to check the github handler"                       ^
                                                                              p^
  "`Respos(user, repo)` should"                                               ^
    "return a json object that can be parsed using `as.github.Repo`"          ! repos1^
    "return a json object that can be parsed manually"                        ! repos2^
                                                                              p^
  "`git_refs` should"                                                         ^
    "return a json array that can be parsed using `as.github.GitRefs`"        ! references1^
                                                                              p^
  "`git_refs.head(\"master\")` should"                                        ^
    "return a json object that can be parsed using `as.github.GitRef`"        ! reference1^
                                                                              p^
  "`git_commit(:sha)` should"                                                 ^
    "return a json object that can be parsed using `as.github.GitCommit`"     ! commit1^
    "return a json object that can be parsed manually"                        ! commit2^    
                                                                              p^
  "`git_commit(git_ref)` should"                                              ^
    "return a commit json object for the given `GitRef`"                      ! commit3^                                                                              
                                                                              p^
  "`git_trees(:sha)` should"                                                  ^
    "return a json object that can be parsed using `as.github.GitTrees`"      ! trees1^
                                                                              p^
  "`git_trees(:sha).recursive(10)` should"                                    ^
    "return a json object that contains subdir blobs"                         ! recursive1^
                                                                              p^
  "`git_trees(commit)` should"                                                ^
      "return a tree json object for the given `GitCommit`"                   ! trees2^
                                                                              p^
  "`git_blob(:sha)` should"                                                   ^
      "return a json object that can be parsed using `as.github.GitBlob`"     ! blob1^
                                                                              p^
  "`git_blob(:sha).raw` should"                                               ^
      "return raw blob bytes"                                                 ! raw1^                                                                                    
                                                                              end
  def repos1: MatchResult[Any] = {
    // `Client(Repos(user, repo)` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch
    // Returned json object can then be parsed using `as.github.Repo`,
    // which returns a Repo case class
    val repos = Http(Client(Repos(user, repo)) > as.github.Repo)

    repos().full_name must_== "dispatch/dispatch"
  }
  
  def repos2: MatchResult[Any] = {
    // Returned json object can also be parsed field-by-field using an extractor
    val json = Http(Client(Repos(user, repo)) > as.github.Json)
    val o = json map { js =>
      import Repo._
      owner(js)
    }
    o().head.login must_== "dispatch"
  }

  def references1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_refs)` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/refs
    // Returned json array can then be parsed using `as.github.GitRefs`,  
    // which returns a seqence of GitRef case classes
    val refs = Http(Client(Repos(user, repo).git_refs) > as.github.GitRefs)
    
    val master = (refs() find {_.ref == "refs/heads/master"}).head
    master.git_object.`type` must_== "commit"
  }
  
  def reference1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_refs.head("master")` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/refs/heads/master
    // Returned json object can then be parsed using `as.github.GitRef`,
    // which returns a GitRef case class
    val master = Http(Client(Repos(user, repo).git_refs.head("master")) > as.github.GitRef)
    master().git_object.`type` must_== "commit"
  }
  
  def commit1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_commit(commit_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/commits/02d638afcd5b155a335db2e8262ffd852290c17c
    // Returned json object can then be parsed using `as.github.GitCommit`,
    // which returns a GitCommit case class
    val commit = Http(Client(Repos(user, repo).git_commit(commit_sha)) > as.github.GitCommit)
    commit().committer.name must_== "softprops"
  }
  
  def commit2: MatchResult[Any] = {
    // Returned json object can also be parsed field-by-field using an extractor
    val json = Http(Client(Repos(user, repo).git_commit(commit_sha)) > as.github.Json)
    val msg = json map { js =>
      import GitCommit._
      message(js)
    }
    msg().head.startsWith("add") must_== true
  }
  
  def commit3: MatchResult[Any] = {
    // this returns a GitRef case class
    val master = Http(Client(Repos(user, repo).git_refs.head("master")) > as.github.GitRef)
    
    // this returns a GitCommit case class
    val commit = Http(Client(Repos(user, repo).git_commit(master())) > as.github.GitCommit)
    commit().sha must_== master().git_object.sha
  }
      
  def trees1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_trees(tree_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/trees/563c7dcea4bbb71e49313e92c01337a0a4b7ce72
    // Returned json object can then be parsed using `as.github.GitTrees`,
    // which returns a seqence of GitTree case class
    val trees = Http(Client(Repos(user, repo).git_trees(tree_sha)) > as.github.GitTrees)
    trees() must have (_.path == ".gitignore")
  }
  
  def trees2: MatchResult[Any] = {
    // this returns a GitCommit case class
    val commit = Http(Client(Repos(user, repo).git_commit(commit_sha)) > as.github.GitCommit)    
    
    // this returns a seqence of GitTree case class
    val trees = Http(Client(Repos(user, repo).git_trees(commit())) > as.github.GitTrees)
    trees() must have (_.path == ".gitignore")
  }
  
  def recursive1: MatchResult[Any] = {
    // this returns a sequence of GitTree case class
    val trees = Http(Client(Repos(user, repo).git_trees(tree_sha).recursive(10)) > as.github.GitTrees)
    trees() must have (_.path == "twitter/src/main/scala/dispatch/Twitter.scala")
  }
  
  def blob1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_blob(blob_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/blobs/fb4c8b459f05bcc5296d9c13a3f6757597786f1d
    // Returned json object can then be parsed using `as.github.GitBlob`,
    // which returns a GitBlob case class
    val blob = Http(Client(Repos(user, repo).git_blob(blob_sha)) > as.github.GitBlob)
    
    // `as_utf8` method makes the assumption that the contained content is encoded in UTF-8.
    (blob().as_utf8 startsWith ".classpath") must_== true
  }
  
  def raw1: MatchResult[Any] = {
    // `Client(Repos(user, repo).git_blob(blob_sha).raw)` constructs a request to
    // https://api.github.com/repos/dispatch/dispatch/git/blobs/fb4c8b459f05bcc5296d9c13a3f6757597786f1d
    // with "application/vnd.github.raw" as http Accept header.
    // This returns raw bytes. You are responsible for figuring out the charset.
    val raw = Http(Client(Repos(user, repo).git_blob(blob_sha).raw) > as.String)
    
    (raw() startsWith ".classpath") must_== true
  }
  
  val user = "dispatch"
  val repo = "dispatch"
  val tree_sha = "563c7dcea4bbb71e49313e92c01337a0a4b7ce72"
  val commit_sha = "02d638afcd5b155a335db2e8262ffd852290c17c"
  val blob_sha = "fb4c8b459f05bcc5296d9c13a3f6757597786f1d"
}
