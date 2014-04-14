package repatch.github.request

import dispatch._
import org.json4s._
import repatch.github.{response => res}

/** represents a github repository from the request-side
 * @see https://developer.github.com/v3/repos/
 */
case class Repos(owner: String, name: String) extends Method {
  def git_refs = GitRefs(this, None)
  def git_commit(ref: res.GitRef): GitCommits = GitCommits(this, ref.git_object.sha)
  def git_commit(sha: String): GitCommits = GitCommits(this, sha)
  def git_trees(commit: res.GitCommit): GitTrees = GitTrees(this, commit.tree.sha)
  def git_trees(sha: String): GitTrees = GitTrees(this, sha)
  def git_blob(sha: String): GitBlobs = GitBlobs(this, sha, None)
  
  def complete = _ / "repos" / owner / name
}

/** represents git reference request.
 * @see http://developer.github.com/v3/git/refs/ 
 */
case class GitRefs(repo: Repos, ref: Option[String]) extends Method {
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
case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends Method {
  private def param(key: String)(value: Any): GitTrees = copy(params = params + (key -> value.toString))
  
  val recursive = param("recursive")_
  
  def complete = repo.complete(_) / "git" / "trees" / sha <<? params
}

/** represents git blob request.
 * @see http://developer.github.com/v3/git/blobs/
 */
case class GitBlobs(repo: Repos, sha: String, override val mime: Option[String]) extends Method {
  def raw = copy(mime = Some("application/vnd.github.raw"))
  
  def complete = repo.complete(_) / "git" / "blobs" / sha
}

trait Method extends (Req => Req) {
  def mime: Option[String] = Some("application/json")
  def complete: Req => Req
  def apply(req: Req): Req = {
    val r = complete(req)
    mime match {
      case Some(x) => r <:< Map("Accept" -> x)
      case _ => r
    }
  }
}
