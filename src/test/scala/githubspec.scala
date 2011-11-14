import org.specs2._
import org.specs2.matcher._
import dispatch._
import github._
import dispatch.liftjson.Js._

class GithubSpec extends Specification { def is =
  "This is a specification to check the github handler"                       ^
                                                                              p^
  "`git_refs` should"                                                         ^
    "return references"                                                       ! references1^
    "return a reference with object"                                          ! references2^
                                                                              p^                                                                                                                                              
  "`git_refs.head(\"master\")` should"                                        ^
    "return a reference with object"                                          ! references3^
                                                                              p^
  "`git_commit(:sha)` should"                                                 ^
    "return a commit"                                                         ! commit1^
                                                                              p^
  "`git_refs.head(\"master\").git_object.url` should"                         ^
    "return a commit"                                                         ! commit2^                                                                              
                                                                              p^
  "`git_trees(:sha)` should"                                                  ^
    "return trees"                                                            ! trees1^
                                                                              p^
  "`git_trees(:sha).recursive(10)` should"                                    ^
    "return trees recursively"                                                ! recursive1^
                                                                              p^
  "`git_trees(commit)` should"                                                ^
      "return trees"                                                          ! trees2^                       
                                                                              end
  
  def references1: MatchResult[Any] = withHttp { http =>
    // this returns a seqence of GitRef case class
    val refs = http(Client(Repos(user, repo).git_refs) ># Response.git_refs)
    refs must have (_.ref == "refs/heads/master")
  }
  
  def references2: MatchResult[Any] = withHttp { http =>
    // this returns a seqence of GitRef case class
    val refs = http(Client(Repos(user, repo).git_refs) ># Response.git_refs)
    val master = (refs find {_.ref == "refs/heads/master"}).head
    master.git_object.type_str must_== "commit"
  }
  
  def references3: MatchResult[Any] = withHttp { http =>
    // this returns a GitRef case class
    val master = http(Client(Repos(user, repo).git_refs.head("master")) ># Response.git_ref)
    master.git_object.type_str must_== "commit"
  }
  
  def commit1: MatchResult[Any] = withHttp { http =>
    // this returns a GitCommit case class
    val commit = http(Client(Repos(user, repo).git_commit(commit_sha)) ># Response.git_commit)
    commit.committer.name must_== "softprops"
  }
  
  def commit2: MatchResult[Any] = withHttp { http =>
    // this returns a GitRef case class
    val master = http(Client(Repos(user, repo).git_refs.head("master")) ># Response.git_ref)
    
    // this returns a GitCommit case class
    val commit = http(Client(Repos(user, repo).git_commit(master)) ># Response.git_commit)
    commit.sha must_== master.git_object.sha
  }
  
  def trees1: MatchResult[Any] = withHttp { http =>
    // this returns a seqence of GitTree case class
    val trees = http(Client(Repos(user, repo).git_trees(tree_sha)) ># Response.git_trees)
    trees must have (_.path == ".gitignore")
  }
  
  def trees2: MatchResult[Any] = withHttp { http =>
    // this returns a GitCommit case class
    val commit = http(Client(Repos(user, repo).git_commit(commit_sha)) ># Response.git_commit)    
    
    // this returns a seqence of GitTree case class
    val trees = http(Client(Repos(user, repo).git_trees(commit)) ># Response.git_trees)
    trees must have (_.path == ".gitignore")
  }
  
  def recursive1: MatchResult[Any] = withHttp { http =>
    // this returns a sequence of GitTree case class
    val trees = http(Client(Repos(user, repo).git_trees(tree_sha).recursive(10)) ># Response.git_trees)
    trees must have (_.path == "twitter/src/main/scala/dispatch/Twitter.scala")
  }
  
  def withHttp[A](f: Http => A): A = {
    val http = new Http
    try {   
      f(http)
    } finally {
      http.shutdown
    }
  }
  
  val user = "dispatch"
  val repo = "dispatch"
  val tree_sha = "563c7dcea4bbb71e49313e92c01337a0a4b7ce72"
  val commit_sha = "02d638afcd5b155a335db2e8262ffd852290c17c"
}
