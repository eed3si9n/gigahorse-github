package gigahorse.githubtest

import org.scalatest._
import scala.concurrent._
import scala.concurrent.duration._
import java.io.File
import gigahorse._
import gigahorse.github.Github
import scala.json.ast.unsafe._
import sjsonnew.support.scalajson.unsafe.CompactPrinter

class GithubSpec extends AsyncFlatSpec with Matchers {
  lazy val client = Github.localConfigClient("gigahorse.token")
  val user = "eed3si9n"
  val name = "gigahorse"
  val tree_sha = "d19f416669ea6a2ffc22ab91bed8a9feff48e778"
  val commit_sha = "d7b8bb43d003e58f55af7b3592e7ce1fb986d0f3"
  val blob_sha = "ac28ec8ee30e89ae807f3ef52f471ffc68783b28"
  "Github.repo(:owner, :repo)" should "return a json object that can be parsed manually" in
    withHttp { http =>
      // `client(repo(user, name))` constructs a request to
      // https://api.github.com/repos/dispatch/reboot
      // Returned json object
      val f = http.run(client(Github.repo(user, name)), Github.asJson)
      f map { json =>
        // println(CompactPrinter(json))
        val o = json match {
          case o: JObject => o
          case _          => sys.error("JObject expected")
        }
        val login =
          for {
            JField("owner", JObject(owner)) <- o.value.toList
            JField("login", JString(login)) <- owner
          } yield login
        assert(login.headOption == Some("eed3si9n"))
      }
    }

  it should "return a json object that can be parsed using `asRepo`" in
    withHttp { http =>
      // Returned json object can then be parsed using `as.repatch.github.response.Repo`,
      // which returns a Repo case class
      val f = http.run(client(Github.repo(user, name)), Github.asRepo)
      f map { repo =>
        // println(repo)
        assert(repo.full_name == "eed3si9n/gigahorse")
      }
    }

  // "Github.user.repos" should "return a json object that can be parsed using `asRepos`" in
  //   withHttp { http =>
  //     val repos = http(client(gh.user.repos.asc) > as.repatch.github.response.Repos)
  //     repos().head.full_name must_!= "foo"
  //   }

  "Github.repo(:owner, :repo).git_refs" should "return a json array that can be parsed using asGitRefs" in
    withHttp { http =>
      // `client(repos(user, repo).git_refs)` constructs a request to
      // https://api.github.com/repos/dispatch/reboot/git/refs
      // Returned json array can then be parsed using `GitRefs`,
      // which returns a seqence of GitRef case classes
      val refs = http.run(client(Github.repo(user, name).git_refs), Github.asGitRefs)
      refs map { rs =>
        val master = (rs find {_.ref == "refs/heads/0.1.x"}).head
        assert(master.`object`.`type` == "commit")
      }
    }

  "Github.repo(:owner, :repo).git_refs.heads(\"master\")" should "return a json object that can be parsed using asGitRef" in
    withHttp { http =>
      val ref = http.run(client(Github.repo(user, name).git_refs.heads("0.1.x")), Github.asGitRef)
      ref map { master =>
        assert(master.ref == "refs/heads/0.1.x")
      }
    }

  "Github.repo(:owner, :repo).git_refs.tags" should "return a json object that can be parsed using asGitRefs" in
    withHttp { http =>
      val refs = http.run(client(Github.repo(user, name).git_refs.tags), Github.asGitRefs)
      refs map { rs =>
        val zeroOneZero = (rs find {_.ref == "refs/tags/v0.1.0"}).head
        assert(zeroOneZero.`object`.`type` == "tag")
      }
    }

  "Github.repo(:owner, :repo).git_commit(:sha)" should "return a json object that can be parsed using asGitCommit" in
    withHttp { http =>
      // `client(repos(user, name).git_commit(commit_sha))` constructs a request to
      // https://api.github.com/repos/dispatch/reboot/git/commits/bcf6d255317088ca1e32c6e6ecd4dce1979ac718
      // Returned json object can then be parsed using `GitCommit`,
      // which returns a GitCommit case class
      val f = http.run(client(Github.repo(user, name).git_commit(commit_sha)), Github.asGitCommit)
      f map { commit =>
        assert(commit.committer.name == "Eugene Yokota")
      }
    }

  "Github.repo(:owner, :repo).git_commit(git_ref)" should "return a commit json object for the given asGitRef" in
    withHttp { http =>
      for {
        // this returns a GitRef case class
        master <- http.run(client(Github.repo(user, name).git_refs.heads("0.1.x")), Github.asGitRef)
        // this returns a GitCommit case class
        commit <- http.run(client(Github.repo(user, name).git_commit(master)), Github.asGitCommit)
      } yield {
        // println(commit)
        assert(commit.sha == master.`object`.sha)
      }
    }

  "Github.repo(:owner, :repo).git_trees(:sha)" should "return a json object that can be parsed using asGitTrees" in
    withHttp { http =>
      // `client(repos(user, name).git_trees(tree_sha))` constructs a request to
      // https://api.github.com/repos/dispatch/reboot/git/trees/563c7dcea4bbb71e49313e92c01337a0a4b7ce72
      // Returned json object can then be parsed using `GitTrees`,
      // which returns a seqence of GitTree case class
      val f = http.run(client(Github.repo(user, name).git_trees(tree_sha)), Github.asGitTrees)
      import gigahorse.github.response.GitTree
      f map { trees =>
        println(trees.toString)
        assert((trees.tree find { tree: GitTree => tree.path == ".gitignore" }).isDefined)
      }
    }

  "Github.repo(:owner, :repo).git_trees(:sha).recursive(10))" should "return a json object that contains subdir blobs" in
    withHttp { http =>
      // this returns a sequence of GitTree case class
      val f = http.run(client(Github.repo(user, name).git_trees(tree_sha).recursive(10)), Github.asGitTrees)
      import gigahorse.github.response.GitTree
      f map { trees =>
        assert((trees.tree find { tree: GitTree => tree.path == "core/src/test/scala/gigahorsetest/HttpClientSpec.scala" }).isDefined)
      }
    }

  "Github.repo(:owner, :repo).git_trees(commit)" should "return a tree json object for the given `GitCommit`" in
    withHttp { http =>
      for {
        // this returns a GitCommit case class
        commit <- http.run(client(Github.repo(user, name).git_commit(commit_sha)), Github.asGitCommit)
        // this returns a seqence of GitTree case class
        trees <- http.run(client(Github.repo(user, name).git_trees(commit)), Github.asGitTrees)
      } yield {
        import gigahorse.github.response.GitTree
        assert((trees.tree find { tree: GitTree => tree.path == ".gitignore" }).isDefined)
      }
    }

/*s2"""

  `Github.repo(:owner, :repo).git_blob(:sha)` should
    return a json object that can be parsed using `GitBlob`                   ${blob1}

  `Github.repo(:owner, :repo).git_blob(:sha).raw` should
    return raw blob bytes                                                     ${raw1}

  `Github.issues` should
    return a json array that can be parsed using `Issues`                     ${issues1}

  `Github.issues.labels("bug").asc` should
    return a json array that can be parsed using `Issues`                     ${issues2}

  `Github.repo(:owner, :repo).issues` should
    return a json array that can be parsed using `Issues`                     ${issues3}    

  `Github.repo(:owner, :repo).issues.page(1).per_page(1)` should
    return a json array with Link HTTP header for the next page`              ${pagination1}    

  `Github.user` should
    return a json object that can be parsed using `User`                      ${user1}

  `Github.user(:user)` should
    return a json object that can be parsed using `User`                      ${user2}

  `Github.user.orgs` should
    return a json object that can be parsed using `Orgs`                      ${orgs1}

  `Github.user(:user).orgs` should
    return a json object that can be parsed using `Orgs`                      ${orgs2}

  `Github.search.repos("reboot language:scala")` should
    return a json object that can be parsed using `ReposSearch`               ${search1}

  `Github.search.code("case class Req in:file repo:dispatch/reboot")` should
    return a json object that can be parsed using `CodeSearch`                ${search2}
    return a json object that can be parsed using `TextMatches` given
    HTTP header Accept: application/vnd.github.v3.text-match+json             ${search3}

  `Github.search.issues("oauth client access repo:eed3si9n/repatch-github")` should
    return a json object that can be parsed using `IssuesSearch`              ${search4}

  `Github.search.users("eed3si9n")` should
    return a json object that can be parsed using `UsersSearch`               ${search5}
                                                                              """
*/

  // def blob1 = {
  //   // `client(repos(user, name).git_blob(blob_sha))` constructs a request to
  //   // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
  //   // Returned json object can then be parsed using `GitBlob`,
  //   // which returns a GitBlob case class
  //   val blob = http(client(gh.repo(user, name).git_blob(blob_sha)) > as.repatch.github.response.GitBlob)
    
  //   // `as_utf8` method makes the assumption that the contained content is encoded in UTF-8.
  //   (blob().as_utf8 startsWith ".classpath") must_== true
  // }
  
  // def raw1 = {
  //   // `client.raw(repo(user, name).git_blob(blob_sha))` constructs a request to
  //   // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
  //   // with "application/vnd.github.raw" as http Accept header.
  //   // This returns raw bytes. You are responsible for figuring out the charset.
  //   val raw = http(client.raw(gh.repo(user, name).git_blob(blob_sha)) > as.String)
    
  //   (raw() startsWith ".classpath") must_== true
  // }

  // def issues1 = {
  //   import repatch.github.response.IssueState._
  //   val iss = http(client(gh.issues) > as.repatch.github.response.Issues)
  //   iss().head.state_opt must_== Some(open)
  // }

  // def issues2 = {
  //   import gh.IssueState._
  //   val iss = http(client(gh.issues.state(closed).labels("bug").asc) > as.repatch.github.response.Issues)
  //   iss().head.state_opt must_== Some(closed)
  // }

  // def issues3 = {
  //   import repatch.github.response.IssueState._
  //   val iss = http(client(gh.repo(user, name).issues) > as.repatch.github.response.Issues)
  //   iss().head.state_opt must_== Some(open)
  // }

  // def pagination1 = {
  //   import repatch.github.response.IssueState._
  //   val iss = http(client(gh.repo(user, name).issues.page(1).per_page(1)) > as.repatch.github.response.Issues)
  //   iss().next_page match {
  //     case Some(next) =>
  //       val iss2 = http(client(gh.url(next)) > as.repatch.github.response.Issues)
  //       iss2().head.state_opt must_== Some(open)
  //     case _ => sys.error("next page was not found")
  //   }

  // }

  // def user1 = {
  //   val usr = http(client(gh.user) > as.repatch.github.response.User)
  //   usr().login must_!= "foo" 
  // }

  // def user2 = {
  //   val usr = http(client(gh.user("eed3si9n")) > as.repatch.github.response.User)
  //   usr().login must_== "eed3si9n"
  // }

  // def orgs1 = {
  //   val orgs = http(client(gh.user.orgs) > as.repatch.github.response.Orgs)
  //   orgs().head.login must_!= "foo"
  // }

  // def orgs2 = {
  //   val orgs = http(client(gh.user("eed3si9n").orgs) > as.repatch.github.response.Orgs)
  //   orgs().head.login must_== "ny-scala"
  // }

  // def search1 = {
  //   val repos = http(client(gh.search.repos("reboot language:scala")) > as.repatch.github.response.ReposSearch)
  //   repos().head.full_name must_== "dispatch/reboot"
  // }

  // def search2 = {
  //   val code = http(client(gh.search.code("\"case class Req\" in:file repo:dispatch/reboot")) > 
  //     as.repatch.github.response.CodeSearch)
  //   code().head.path must_== "core/src/main/scala/requests.scala"
  // }

  // def search3 = {
  //   val code = http(client.text_match(gh.search.code("\"case class Req\" in:file repo:dispatch/reboot")) >
  //     as.repatch.github.response.TextMatches)
  //   code().head.text_matches.head.fragment must contain("case class Req") 
  // }

  // def search4 = {
  //   val iss = http(client(gh.search.issues("oauth client access repo:eed3si9n/repatch-github")) > as.repatch.github.response.IssuesSearch)
  //   iss().head.number_opt must_== Some(1)
  // }

  // def search5 = {
  //   val users = http(client(gh.search.users("eed3si9n")) > as.repatch.github.response.UsersSearch)
  //   users().head.login must_== "eed3si9n"
  // }

  // custom loan pattern
  def withHttp(testCode: gigahorse.HttpClient => Future[Assertion]): Future[Assertion] =
    {
      val http = Gigahorse.http(Gigahorse.config)
      complete {
        testCode(http)
      } lastly {
        http.close()
      }
    }
}
