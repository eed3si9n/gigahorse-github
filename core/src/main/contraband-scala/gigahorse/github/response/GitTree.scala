/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitTree private (
  val url: String,
  val sha: String,
  val path: String,
  val mode: String,
  val `type`: String,
  val size: Option[Long]) extends Serializable {
  
  private def this(url: String, sha: String, path: String, mode: String, `type`: String) = this(url, sha, path, mode, `type`, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: GitTree => (this.url == x.url) && (this.sha == x.sha) && (this.path == x.path) && (this.mode == x.mode) && (this.`type` == x.`type`) && (this.size == x.size)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.GitTree".##) + url.##) + sha.##) + path.##) + mode.##) + `type`.##) + size.##)
  }
  override def toString: String = {
    "GitTree(" + url + ", " + sha + ", " + path + ", " + mode + ", " + `type` + ", " + size + ")"
  }
  private[this] def copy(url: String = url, sha: String = sha, path: String = path, mode: String = mode, `type`: String = `type`, size: Option[Long] = size): GitTree = {
    new GitTree(url, sha, path, mode, `type`, size)
  }
  def withUrl(url: String): GitTree = {
    copy(url = url)
  }
  def withSha(sha: String): GitTree = {
    copy(sha = sha)
  }
  def withPath(path: String): GitTree = {
    copy(path = path)
  }
  def withMode(mode: String): GitTree = {
    copy(mode = mode)
  }
  def withType(`type`: String): GitTree = {
    copy(`type` = `type`)
  }
  def withSize(size: Option[Long]): GitTree = {
    copy(size = size)
  }
  def withSize(size: Long): GitTree = {
    copy(size = Option(size))
  }
}
object GitTree {
  
  def apply(url: String, sha: String, path: String, mode: String, `type`: String): GitTree = new GitTree(url, sha, path, mode, `type`)
  def apply(url: String, sha: String, path: String, mode: String, `type`: String, size: Option[Long]): GitTree = new GitTree(url, sha, path, mode, `type`, size)
  def apply(url: String, sha: String, path: String, mode: String, `type`: String, size: Long): GitTree = new GitTree(url, sha, path, mode, `type`, Option(size))
}
