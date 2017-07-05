/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitCommit private (
  val url: String,
  val sha: String,
  val committer: gigahorse.github.response.GitUser,
  val message: String,
  val tree: gigahorse.github.response.GitShaUrl,
  val parents: Vector[gigahorse.github.response.GitShaUrl]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitCommit => (this.url == x.url) && (this.sha == x.sha) && (this.committer == x.committer) && (this.message == x.message) && (this.tree == x.tree) && (this.parents == x.parents)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "GitCommit".##) + url.##) + sha.##) + committer.##) + message.##) + tree.##) + parents.##)
  }
  override def toString: String = {
    "GitCommit(" + url + ", " + sha + ", " + committer + ", " + message + ", " + tree + ", " + parents + ")"
  }
  protected[this] def copy(url: String = url, sha: String = sha, committer: gigahorse.github.response.GitUser = committer, message: String = message, tree: gigahorse.github.response.GitShaUrl = tree, parents: Vector[gigahorse.github.response.GitShaUrl] = parents): GitCommit = {
    new GitCommit(url, sha, committer, message, tree, parents)
  }
  def withUrl(url: String): GitCommit = {
    copy(url = url)
  }
  def withSha(sha: String): GitCommit = {
    copy(sha = sha)
  }
  def withCommitter(committer: gigahorse.github.response.GitUser): GitCommit = {
    copy(committer = committer)
  }
  def withMessage(message: String): GitCommit = {
    copy(message = message)
  }
  def withTree(tree: gigahorse.github.response.GitShaUrl): GitCommit = {
    copy(tree = tree)
  }
  def withParents(parents: Vector[gigahorse.github.response.GitShaUrl]): GitCommit = {
    copy(parents = parents)
  }
}
object GitCommit {
  
  def apply(url: String, sha: String, committer: gigahorse.github.response.GitUser, message: String, tree: gigahorse.github.response.GitShaUrl, parents: Vector[gigahorse.github.response.GitShaUrl]): GitCommit = new GitCommit(url, sha, committer, message, tree, parents)
}
