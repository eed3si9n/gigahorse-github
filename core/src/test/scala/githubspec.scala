package repatch.github

import org.specs2._
import org.specs2.matcher._
import dispatch._, Defaults._
import repatch.github.request._
import org.json4s._

class GithubSpec extends Specification { def is = args(sequential = true) ^ s2"""
  This is a specification to check the github handler
  
  `repo(:owner, :repo)` should
    return a json object that can be parsed manually                          ${repos1}
    return a json object that can be parsed with extractors                   ${repos2}
    return a json object that can be parsed using `Repo`"                     ${repos3}

  `repo(:owner, :repo).git_refs` should
    return a json array that can be parsed using `GitRefs`                    ${references1}

  `repo(:owner, :repo).git_refs.heads(\"master\")` should
    return a json object that can be parsed using `GitRef`                    ${references2}

  `repo(:owner, :repo).git_refs.tags` should
    return a json array that can be parsed using `GitRefs`                    ${reftags1}

  `repo(:owner, :repo).git_commit(:sha)` should
    return a json object that can be parsed using `GitCommit`                 ${commit1}
    return a json object that can be parsed manually                          ${commit2}

  `repo(:owner, :repo).git_commit(git_ref)` should
    return a commit json object for the given `GitRef`                        ${commit3}

  `repo(:owner, :repo).git_trees(:sha)` should
    return a json object that can be parsed using `GitTrees`                  ${trees1}

  `repo(:owner, :repo).git_trees(:sha).recursive(10)` should
    return a json object that contains subdir blobs                           ${recursive1}

  `repo(:owner, :repo).git_trees(commit)` should
    return a tree json object for the given `GitCommit`                       ${trees2}

  `repo(:owner, :repo).git_blob(:sha)` should
    return a json object that can be parsed using `GitBlob`                   ${blob1}

  `repo(:owner, :repo).git_blob(:sha).raw` should
    return raw blob bytes                                                     ${raw1}

  `issues` should
    return a json array that can be parsed using `Issues`                     ${issues1}

  `issues.labels(Seq("bug")).direction("asc")` should
    return a json array that can be parsed using `Issues`                     ${issues2}

  `repo(:owner, :repo).issues` should
    return a json array that can be parsed using `Issues`                     ${issues3}    
  
                                                                              """

  lazy val http = new Http
  lazy val client = LocalConfigClient()
  val user = "dispatch"
  val name = "reboot"
  val tree_sha = "b1193d20d761654b7fc35a48cd64b53aedc7a697"
  val commit_sha = "bcf6d255317088ca1e32c6e6ecd4dce1979ac718"
  val blob_sha = "3baebe52555bc73ad1c9a94261c4552fb8d771cd"

  def repos1 = {
    // `client(repo(user, name))` constructs a request to
    // https://api.github.com/repos/dispatch/reboot
    // Returned json object
    val x = http(client(repo(user, name)) > as.json4s.Json)
    val login = x map { json =>
      for {
        JObject(fs) <- json
        JField("owner", JObject(owner)) <- fs
        JField("login", JString(login)) <- owner
      } yield login
    }
    login().headOption must_== Some("dispatch")
  }

  def repos2 = {
    // Returned json object can also be parsed field-by-field using an extractor
    val x = http(client(repo(user, name)) > as.json4s.Json)
    val o = x map { json =>  
      import repatch.github.response.Repo._
      owner(json)
    }
    {
      import repatch.github.response.User._
      login(o()) must_== "dispatch"
    }
  }

  def repos3 = {
    // Returned json object can then be parsed using `as.repatch.github.response.Repo`,
    // which returns a Repo case class
    val repos = http(client(repo(user, name)) > as.repatch.github.response.Repo)

    repos().full_name must_== "dispatch/reboot"
  }
  
  def references1 = {
    // `client(repos(user, repo).git_refs)` constructs a request to
    // https://api.github.com/repos/dispatch/reboot/git/refs
    // Returned json array can then be parsed using `GitRefs`,  
    // which returns a seqence of GitRef case classes
    val refs = http(client(repo(user, name).git_refs) > as.repatch.github.response.GitRefs)
    val master = (refs() find {_.ref == "refs/heads/master"}).head
    master.git_object.`type` must_== "commit"
  }

  def references2 = {
    val ref = http(client(repo(user, name).git_refs.heads("master")) > as.repatch.github.response.GitRef)
    val master = ref()
    master.ref must_== "refs/heads/master"
  }

  def reftags1 = {
    val refs = http(client(repo(user, name).git_refs.tags) > as.repatch.github.response.GitRefs)
    val zeroEleven = (refs() find {_.ref == "refs/tags/0.11.0"}).head
    zeroEleven.git_object.`type` must_== "commit"
  }
  
  def commit1 = {
    // `client(repos(user, name).git_commit(commit_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/reboot/git/commits/bcf6d255317088ca1e32c6e6ecd4dce1979ac718
    // Returned json object can then be parsed using `GitCommit`,
    // which returns a GitCommit case class
    val commit = http(client(repo(user, name).git_commit(commit_sha)) > as.repatch.github.response.GitCommit)
    commit().committer.name must_== "softprops"
  }
  
  def commit2 = {
    // Returned json object can also be parsed field-by-field using an extractor
    val json = http(client(repo(user, name).git_commit(commit_sha)) > as.json4s.Json)
    val msg = json map { js =>
      import repatch.github.response.GitCommit._
      message(js)
    }
    msg().startsWith("send") must_== true
  }
  
  def commit3 = {
    // this returns a GitRef case class
    val master = http(client(repo(user, name).git_refs.heads("master")) > as.repatch.github.response.GitRef)
    
    // this returns a GitCommit case class
    val commit = http(client(repo(user, name).git_commit(master())) > as.repatch.github.response.GitCommit)
    commit().sha must_== master().git_object.sha
  }
      
  def trees1 = {
    // `client(repos(user, name).git_trees(tree_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/reboot/git/trees/563c7dcea4bbb71e49313e92c01337a0a4b7ce72
    // Returned json object can then be parsed using `GitTrees`,
    // which returns a seqence of GitTree case class
    val trees = http(client(repo(user, name).git_trees(tree_sha)) > as.repatch.github.response.GitTrees)
    import repatch.github.response.GitTree
    trees() must contain { tree: GitTree => tree.path must be_==(".gitignore") }
  }

  def recursive1 = {
    // this returns a sequence of GitTree case class
    val trees = http(client(repo(user, name).git_trees(tree_sha).recursive(10)) > as.repatch.github.response.GitTrees)
    import repatch.github.response.GitTree
    trees() must contain { tree: GitTree => tree.path must be_==("core/src/main/scala/retry/retries.scala") }
  }
  
  def trees2 = {
    // this returns a GitCommit case class
    val commit = http(client(repo(user, name).git_commit(commit_sha)) > as.repatch.github.response.GitCommit)    
    
    // this returns a seqence of GitTree case class
    val trees = http(client(repo(user, name).git_trees(commit())) > as.repatch.github.response.GitTrees)
    import repatch.github.response.GitTree
    trees() must contain { tree: GitTree => tree.path must be_==(".gitignore") }
  }
    
  def blob1 = {
    // `client(repos(user, name).git_blob(blob_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
    // Returned json object can then be parsed using `GitBlob`,
    // which returns a GitBlob case class
    val blob = http(client(repo(user, name).git_blob(blob_sha)) > as.repatch.github.response.GitBlob)
    
    // `as_utf8` method makes the assumption that the contained content is encoded in UTF-8.
    (blob().as_utf8 startsWith ".classpath") must_== true
  }
  
  def raw1 = {
    // `client.raw(repo(user, name).git_blob(blob_sha))` constructs a request to
    // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
    // with "application/vnd.github.raw" as http Accept header.
    // This returns raw bytes. You are responsible for figuring out the charset.
    val raw = http(client.raw(repo(user, name).git_blob(blob_sha)) > as.String)
    
    (raw() startsWith ".classpath") must_== true
  }

  def issues1 = {
    val iss = http(client(issues) > as.repatch.github.response.Issues)
    iss().head.state_opt must_== Some("open")
  }

  def issues2 = {
    val iss = http(client(issues.labels(Seq("bug")).direction("asc")) > as.repatch.github.response.Issues)
    iss().head.state_opt must_== Some("open")
  }

  def issues3 = {
    val iss = http(client(repo(user, name).issues) > as.repatch.github.response.Issues)
    iss().head.state_opt must_== Some("open")
  }
}
