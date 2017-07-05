package gigahorse.githubtest

import org.scalatest._
import scala.concurrent._
import scala.concurrent.duration._
import java.io.File
import gigahorse._, support.okhttp.Gigahorse
import gigahorse.github.{ Github, response => res }
import scalajson.ast.unsafe._
import sjsonnew.support.scalajson.unsafe.CompactPrinter

class GithubSpec extends AsyncFlatSpec with Matchers {
  lazy val client =
    sys.env.get("GIGAHORSE_TOKEN") match {
      case Some(x) => Github.oauthClient(x)
      case _       => Github.localConfigClient("gigahorse.token")
    }
  val user = "eed3si9n"
  val name = "gigahorse"
  val tree_sha = "d19f416669ea6a2ffc22ab91bed8a9feff48e778"
  val commit_sha = "d7b8bb43d003e58f55af7b3592e7ce1fb986d0f3"
  val blob_sha = "ac28ec8ee30e89ae807f3ef52f471ffc68783b28"

  "Github.repo(:owner, :repo)" should "return OK" in
    withHttp { http =>
      val f = http.processFull(client(Github.repo(user, name)))
      f map { response =>
        assert(response.status == 200, response.bodyAsString)
      }
    }

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

  "Github.user.repos" should "return a json object that can be parsed using `asRepos`" in
    withHttp { http =>
      val f = http.run(client(Github.user.repos.asc), Github.asRepos)
      f map { repos =>
        assert(repos.head.full_name != "foo")
      }
    }

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
        // println(trees.toString)
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

  "Github.repo(:owner, :repo).git_trees(commit)" should "return a tree json object for the given asGitCommit" in
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

  "Github.repo(:owner, :repo).git_blob(:sha)" should "return a json object that can be parsed using asGitBlob" in
    withHttp { http =>
      // `client(repos(user, name).git_blob(blob_sha))` constructs a request to
      // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
      // Returned json object can then be parsed using `GitBlob`,
      // which returns a GitBlob case class
      val f = http.run(client(Github.repo(user, name).git_blob(blob_sha)), Github.asGitBlob)
      // `as_utf8` method makes the assumption that the contained content is encoded in UTF-8.
      f map { blob =>
        assert(blob.asUtf8 startsWith "project/pf.sbt")
      }
    }

  "client.raw(Github.repo(:owner, :repo).git_blob(:sha), f)" should "return raw blob bytes" in
    withHttp { http =>
      // `client.raw(repo(user, name).git_blob(blob_sha))` constructs a request to
      // https://api.github.com/repos/dispatch/reboot/git/blobs/3baebe52555bc73ad1c9a94261c4552fb8d771cd
      // with "application/vnd.github.raw" as http Accept header.
      // This returns raw bytes. You are responsible for figuring out the charset.
      val f = http.run(client.raw(Github.repo(user, name).git_blob(blob_sha)), Github.asString)
      f map { raw =>
        assert(raw startsWith "project/pf.sbt")
      }
    }

  "Github.issues" should "return a json array that can be parsed using asIssues" in
    withHttp { http =>
      val f = http.run(client(Github.issues), Github.asIssues)
      f map { iss =>
        assert(iss.head.state == Some(Github.IssueState.open))
      }
    }

  "Github.issues.labels(\"bug\").asc" should "return a json array that can be parsed using asIssues" in
    withHttp { http =>
      val f = http.run(client(Github.issues.state(Github.IssueState.closed).labels("bug").asc), Github.asIssues)
      f map { iss =>
        assert(iss.head.state == Some(Github.IssueState.closed))
      }
    }

  "Github.repo(:owner, :repo).issues" should "return a json array that can be parsed using asIssues" in
    withHttp { http =>
      val f = http.run(client(Github.repo("sbt", "sbt").issues), Github.asIssues)
      f map { iss =>
        assert(iss.head.state == Some(Github.IssueState.open))
      }
    }

  "Github.repo(:owner, :repo).issues.page(1).per_page(1)" should "return a json array with Link HTTP header for the next page" in
    withHttp { http =>
      for {
        iss        <- http.run(client(Github.repo("sbt", "sbt").issues.page(1).per_page(1)), Github.asIssues)
        Some(next) = iss.next_page
        iss2       <- http.run(client(Github.url(next)), Github.asIssues)
      } yield {
        assert(iss2.head.state == Some(Github.IssueState.open))
      }
    }

  "Github.user" should "return a json object that can be parsed using asUser" in
    withHttp { http =>
      val f = http.run(client(Github.user), Github.asUser)
      f map { usr =>
        assert(usr.login != "foo")
      }
    }

  "Github.user(:user)" should "return a json object that can be parsed using asUser" in
    withHttp { http =>
      val f = http.run(client(Github.user("eed3si9n")), Github.asUser)
      f map { usr =>
        assert(usr.login == "eed3si9n")
      }
    }

  "Github.user(:user).orgs" should "return a json object that can be parsed using asOrgs" in
    withHttp { http =>
      val f = http.run(client(Github.user("eed3si9n").orgs), Github.asOrgs)
      f map { orgs =>
        assert(orgs.head.login == "scala")
      }
    }

  "Github.search.repos(:q)" should "return a json object that can be parsed using asReposSearch" in
    withHttp { http =>
      val f = http.run(client(Github.search.repos("gigahorse language:scala")), Github.asReposSearch)
      f map { repos =>
        assert(repos.head.full_name == "eed3si9n/gigahorse")
      }
    }

  "Github.search.code(:q)" should "return a json object that can be parsed using asCodeSearch"
    withHttp { http =>
      val f = http.run(client(Github.search.code("\"abstract class Gigahorse\" in:file repo:eed3si9n/gigahorse")),
        Github.asCodeSearch)
      f map { code =>
        assert(code.head.path == "core/src/main/scala/gigahorse/Gigahorse.scala")
      }
    }

  it should "return a json object that can be parsed using asTextMatches given the right HTTP header" in
    withHttp { http =>
      val f = http.run(client.text_match(Github.search.code("\"abstract class Gigahorse\" in:file repo:eed3si9n/gigahorse")),
        Github.asTextMatches)
      f map { code =>
        assert(code.head.text_matches.head.fragment contains "abstract class Gigahorse")
      }
    }

  "Github.search.issues(:q)" should "return a json object that can be parsed using asIssuesSearch" in
    withHttp { http =>
      val f = http.run(client(Github.search.issues("sbt-datatype 0.2.3 repo:eed3si9n/gigahorse")), Github.asIssuesSearch)
      f map { iss =>
        assert(iss.head.number == Some(1))
      }
    }

  "Github.search.users(:q)" should "return a json object that can be parsed using asUsersSearch" in
    withHttp { http =>
      val f = http.run(client(Github.search.users("eed3si9n")), Github.asUsersSearch)
      f map { users =>
        assert(users.head.login == "eed3si9n")
      }
    }

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
