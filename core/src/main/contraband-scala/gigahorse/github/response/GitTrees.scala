/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitTrees private (
  val url: String,
  val sha: String,
  val tree: Vector[gigahorse.github.response.GitTree]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitTrees => (this.url == x.url) && (this.sha == x.sha) && (this.tree == x.tree)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.GitTrees".##) + url.##) + sha.##) + tree.##)
  }
  override def toString: String = {
    "GitTrees(" + url + ", " + sha + ", " + tree + ")"
  }
  private[this] def copy(url: String = url, sha: String = sha, tree: Vector[gigahorse.github.response.GitTree] = tree): GitTrees = {
    new GitTrees(url, sha, tree)
  }
  def withUrl(url: String): GitTrees = {
    copy(url = url)
  }
  def withSha(sha: String): GitTrees = {
    copy(sha = sha)
  }
  def withTree(tree: Vector[gigahorse.github.response.GitTree]): GitTrees = {
    copy(tree = tree)
  }
}
object GitTrees {
  
  def apply(url: String, sha: String, tree: Vector[gigahorse.github.response.GitTree]): GitTrees = new GitTrees(url, sha, tree)
}
