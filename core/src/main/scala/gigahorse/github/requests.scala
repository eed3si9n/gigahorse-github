package gigahorse.github
package request

import gigahorse._
import gigahorse.github.{response => res}
// import org.json4s._
// import repatch.github.{response => res}
// import java.util.Calendar
// import collection.immutable.Map

abstract class RequestBuilder {
  protected val baseUrl = "https://api.github.com"
  def build: Request
}

object RequestBuilder {

}

/** represents a github repository from the request-side
 * @see https://developer.github.com/v3/repos/
 */
case class Repos(owner: String, name: String) extends RequestBuilder {
  def git_refs = GitRefs(this, None)
  def git_commit(ref: res.GitRef): GitCommits = GitCommits(this, ref.`object`.sha)
  def git_commit(sha: String): GitCommits = GitCommits(this, sha)
  // def git_trees(commit: res.GitCommit): GitTrees = GitTrees(this, commit.tree.sha)
  // def git_trees(sha: String): GitTrees = GitTrees(this, sha)
  // def git_blob(sha: String): GitBlobs = GitBlobs(this, sha, MediaType.default)
  // def issues: ReposIssues = ReposIssues(this, Map())

  def build: Request = Gigahorse.url(s"$baseUrl/repos/$owner/$name")
}

/** represents git reference request.
 * @see http://developer.github.com/v3/git/refs/
 */
case class GitRefs(repo: Repos, ref: Option[String], params: Map[String, String] = Map()) extends RequestBuilder {
  def param[A: Show](key: String)(value: A): GitRefs =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  def heads: GitRefs = copy(ref = Some("heads"))
  def heads(branch: String): GitRefs = copy(ref = Some("heads/" + branch))
  def tags: GitRefs = copy(ref = Some("tags"))
  def tags(tag: String): GitRefs = copy(ref = Some("tags/" + tag))

  def build: Request =
    Gigahorse.url(s"${repo.build.url}/git/refs" + (ref match {
      case Some(r) => s"/$r"
      case None    => ""
    })).
      addQueryString(params.toList: _*)
}

/** represents git commit request.
 * @see http://developer.github.com/v3/git/commits/
 */
case class GitCommits(repo: Repos, sha: String) extends RequestBuilder {
  def build: Request =
    Gigahorse.url(s"${repo.build.url}/git/commits/$sha")
}

// /** represents git tree request.
//  * @see http://developer.github.com/v3/git/trees/
//  */
// case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends Method with Param[GitTrees] {
//   def param[A: Show](key: String)(value: A): GitTrees =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  
//   val recursive = 'recursive.?[Int]
  
//   def complete = repo.complete(_) / "git" / "trees" / sha <<? params
// }

// /** represents git blob request.
//  * @see http://developer.github.com/v3/git/blobs/
//  */
// case class GitBlobs(repo: Repos, sha: String, mimes: Seq[MediaType]) extends Method {
//   def complete = repo.complete(_) / "git" / "blobs" / sha
// }

// /** represents issues request.
//  * @see https://developer.github.com/v3/issues/
//  */
// case class Issues(params: Map[String, String] = Map()) extends Method with Param[Issues]
//     with SortParam[Issues] with PageParam[Issues] {
//   def complete = _ / "issues" <<? params
//   def param[A: Show](key: String)(value: A): Issues =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
//   val filter  = 'filter.?[String]
//   val state   = 'state.?[IssueState]
//   def labels(xs: String*) = param("labels")(xs.toSeq)
// }

// case class ReposIssues(repo: Repos, params: Map[String, String] = Map()) extends Method
//     with Param[ReposIssues] with SortParam[ReposIssues] with PageParam[ReposIssues] {
//   def complete = repo.complete(_) / "issues" <<? params
//   def param[A: Show](key: String)(value: A): ReposIssues =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
//   val milestone = 'milestone.?[String]
//   val state     = 'state.?[IssueState]
//   val assignee  = 'assignee.?[String]
//   val creator   = 'creator.?[String]
//   val mentioned = 'mentioned.?[String]
//   def labels(xs: String*) = param("labels")(xs.toSeq)
// }

// /** represents users request.
//  * @see https://developer.github.com/v3/users/
//  */
// case class Users(name: Option[String]) extends Method {
//   def complete = { req: Req =>
//     name match {
//       case Some(n) => req / "users" / n
//       case None    => req / "user"
//     }
//   }

//   def repos: UsersRepos = UsersRepos(this)
//   def orgs: UsersOrgs = UsersOrgs(this)
// }

// case class UsersRepos(user: Users, params: Map[String, String] = Map()) extends Method
//     with Param[UsersRepos] with SortParam[UsersRepos] with PageParam[UsersRepos] {
//   def complete = user.complete(_) / "repos" <<? params
//   def param[A: Show](key: String)(value: A): UsersRepos =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// case class UsersOrgs(user: Users, params: Map[String, String] = Map()) extends Method
//     with Param[UsersOrgs] with SortParam[UsersOrgs] with PageParam[UsersOrgs] {
//   def complete = user.complete(_) / "orgs" <<? params
//   def param[A: Show](key: String)(value: A): UsersOrgs =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// /** @see https://developer.github.com/v3/search/
//  */
// case class Search() {
//   def repos(q: String): SearchRepos = SearchRepos(q)
//   def code(q: String): SearchCode = SearchCode(q)
//   def issues(q: String): SearchIssues = SearchIssues(q)
//   def users(q: String): SearchUsers = SearchUsers(q)
// }

// case class SearchRepos(q: String, params: Map[String, String] = Map()) extends Method
//     with Param[SearchRepos] with SortParam[SearchRepos] with PageParam[SearchRepos] {
//   def complete = _ / "search" / "repositories" <<? (params + ("q" -> q))
//   def param[A: Show](key: String)(value: A): SearchRepos =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// case class SearchCode(q: String, params: Map[String, String] = Map()) extends Method
//     with Param[SearchCode] with SortParam[SearchCode] with PageParam[SearchCode] {
//   def complete = _ / "search" / "code" <<? (params + ("q" -> q))
//   def param[A: Show](key: String)(value: A): SearchCode =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// case class SearchIssues(q: String, params: Map[String, String] = Map()) extends Method
//     with Param[SearchIssues] with SortParam[SearchIssues] with PageParam[SearchIssues] {
//   def complete = _ / "search" / "issues" <<? (params + ("q" -> q))
//   def param[A: Show](key: String)(value: A): SearchIssues =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// case class SearchUsers(q: String, params: Map[String, String] = Map()) extends Method
//     with Param[SearchUsers] with SortParam[SearchUsers] with PageParam[SearchUsers] {
//   def complete = _ / "search" / "users" <<? (params + ("q" -> q))
//   def param[A: Show](key: String)(value: A): SearchUsers =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// /** represents raw URL. used for navigating to the next page in paginated results.
//  */
// case class UrlMethod(url: String, params: Map[String, String] = Map()) extends Method
//     with Param[UrlMethod] with SortParam[UrlMethod] with PageParam[UrlMethod] {
//   def complete = { _ => dispatch.url(url) <<? params }
//   def param[A: Show](key: String)(value: A): UrlMethod =
//     copy(params = params + (key -> implicitly[Show[A]].shows(value)))
// }

// trait PageParam[R] { self: Param[R] =>
//   val page    = 'page.?[Int]
//   val per_page = 'per_page.?[Int]
// }

// trait SortParam[R] { self: Param[R] =>
//   val sort      = 'sort.?[String]
//   val direction = 'direction.?[String]
//   val `type`    = 'type.?[String]
//   val since   = 'since.?[Calendar]
//   def asc: R  = direction("asc")
//   def desc: R = direction("desc") 
// }

// trait Param[R] {
//   val params: Map[String, String]
//   def param[A: Show](key: String)(value: A): R
//   implicit class SymOp(sym: Symbol) {
//     def ?[A: Show]: A => R = param(sym.name)_
//   }
// }
