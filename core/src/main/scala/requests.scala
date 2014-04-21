package repatch.github.request

import dispatch._
import org.json4s._
import repatch.github.{response => res}
import java.util.Calendar
import collection.immutable.Map

/** represents a github repository from the request-side
 * @see https://developer.github.com/v3/repos/
 */
case class Repos(owner: String, name: String) extends Method {
  def git_refs = GitRefs(this, None)
  def git_commit(ref: res.GitRef): GitCommits = GitCommits(this, ref.git_object.sha)
  def git_commit(sha: String): GitCommits = GitCommits(this, sha)
  def git_trees(commit: res.GitCommit): GitTrees = GitTrees(this, commit.tree.sha)
  def git_trees(sha: String): GitTrees = GitTrees(this, sha)
  def git_blob(sha: String): GitBlobs = GitBlobs(this, sha, MediaType.default)
  def issues: ReposIssues = ReposIssues(this, Map())

  def complete = _ / "repos" / owner / name
}

/** represents git reference request.
 * @see http://developer.github.com/v3/git/refs/ 
 */
case class GitRefs(repo: Repos, ref: Option[String], params: Map[String, String] = Map()) extends Method {
  def param[A: Show](key: String)(value: A): GitRefs =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  def heads: GitRefs = copy(ref = Some("heads"))
  def heads(branch: String): GitRefs = copy(ref = Some("heads/" + branch))
  def tags: GitRefs = copy(ref = Some("tags"))
  def tags(tag: String): GitRefs = copy(ref = Some("tags/" + tag))

  val complete = { req: Req =>
    val request = repo.complete(req) / "git" / "refs"
    ref match {
      case Some(r) => request / r
      case _ => request
    }
  } 
}

/** represents git commit request.
 * @see http://developer.github.com/v3/git/commits/
 */
case class GitCommits(repo: Repos, sha: String) extends Method {
  def complete = repo.complete(_) / "git" / "commits" / sha
}

/** represents git tree request.
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends Method with Param[GitTrees] {
  def param[A: Show](key: String)(value: A): GitTrees =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  
  val recursive = 'recursive.?[Int]
  
  def complete = repo.complete(_) / "git" / "trees" / sha <<? params
}

/** represents git blob request.
 * @see http://developer.github.com/v3/git/blobs/
 */
case class GitBlobs(repo: Repos, sha: String, mimes: Seq[MediaType]) extends Method {
  def complete = repo.complete(_) / "git" / "blobs" / sha
}

/** represents issues request.
 * @see https://developer.github.com/v3/issues/
 */
case class Issues(params: Map[String, String] = Map()) extends Method with Param[Issues]
    with SortParam[Issues] with PageParam[Issues] {
  def complete = _ / "issues" <<? params
  def param[A: Show](key: String)(value: A): Issues =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  val filter  = 'filter.?[String]
  val state   = 'state.?[String]
  val labels  = 'labels.?[Seq[String]]
}

case class ReposIssues(repo: Repos, params: Map[String, String] = Map()) extends Method
    with Param[ReposIssues] with SortParam[ReposIssues] with PageParam[ReposIssues] {
  def complete = repo.complete(_) / "issues" <<? params
  def param[A: Show](key: String)(value: A): ReposIssues =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  val milestone = 'milestone.?[String]
  val state     = 'state.?[String]
  val assignee  = 'assignee.?[String]
  val creator   = 'creator.?[String]
  val mentioned = 'mentioned.?[String]
  val labels    = 'labels.?[Seq[String]]
}

case class User() extends Method {
  def complete = _ / "user"
  def orgs: UserOrgs = UserOrgs() 
}

case class Users(name: String) extends Method {
  def complete = _ / "users" / name
  def orgs: UsersOrgs = UsersOrgs(this)
}

case class UserOrgs() extends Method {
  def complete = _ / "user" / "orgs"
}

case class UsersOrgs(user: Users) extends Method {
  def complete = user.complete(_) / "orgs"
}

/** @see https://developer.github.com/v3/search/
 */
case class Search() {
  def repos(q: String): SearchRepos = SearchRepos(q)
  def code(q: String): SearchCode = SearchCode(q)
  def issues(q: String): SearchIssues = SearchIssues(q)
  def users(q: String): SearchUsers = SearchUsers(q)
}

case class SearchRepos(q: String, params: Map[String, String] = Map()) extends Method
    with Param[SearchRepos] with SortParam[SearchRepos] with PageParam[SearchRepos] {
  def complete = _ / "search" / "repositories" <<? (params + ("q" -> q))
  def param[A: Show](key: String)(value: A): SearchRepos =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
}

case class SearchCode(q: String, params: Map[String, String] = Map()) extends Method
    with Param[SearchCode] with SortParam[SearchCode] with PageParam[SearchCode] {
  def complete = _ / "search" / "code" <<? (params + ("q" -> q))
  def param[A: Show](key: String)(value: A): SearchCode =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
}

case class SearchIssues(q: String, params: Map[String, String] = Map()) extends Method
    with Param[SearchIssues] with SortParam[SearchIssues] with PageParam[SearchIssues] {
  def complete = _ / "search" / "issues" <<? (params + ("q" -> q))
  def param[A: Show](key: String)(value: A): SearchIssues =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
}

case class SearchUsers(q: String, params: Map[String, String] = Map()) extends Method
    with Param[SearchUsers] with SortParam[SearchUsers] with PageParam[SearchUsers] {
  def complete = _ / "search" / "users" <<? (params + ("q" -> q))
  def param[A: Show](key: String)(value: A): SearchUsers =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
}

/** represents raw URL. used for navigating to the next page in paginated results.
 */
case class UrlMethod(url: String, params: Map[String, String] = Map()) extends Method
    with Param[UrlMethod] with SortParam[UrlMethod] with PageParam[UrlMethod] {
  def complete = { _ => dispatch.url(url) }
  def param[A: Show](key: String)(value: A): UrlMethod =
    copy(params = params + (key -> implicitly[Show[A]].shows(value)))
}

trait PageParam[R] { self: Param[R] =>
  val page    = 'page.?[Int]
  val per_page = 'per_page.?[Int]
}

trait SortParam[R] { self: Param[R] =>
  val sort    = 'sort.?[String]
  val direction = 'direction.?[String]
  val since   = 'since.?[Calendar]
}

trait Param[R] {
  val params: Map[String, String]
  def param[A: Show](key: String)(value: A): R
  implicit class SymOp(sym: Symbol) {
    def ?[A: Show]: A => R = param(sym.name)_
  }
}

trait Method extends (Req => Req) {
  def complete: Req => Req
  def apply(req: Req): Req = complete(req)
}
