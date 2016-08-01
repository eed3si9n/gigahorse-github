/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitShaUrl(
  val url: String,
  val sha: String) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitShaUrl => (this.url == x.url) && (this.sha == x.sha)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (17 + url.##) + sha.##)
  }
  override def toString: String = {
    "GitShaUrl(" + url + ", " + sha + ")"
  }
  private[this] def copy(url: String = url, sha: String = sha): GitShaUrl = {
    new GitShaUrl(url, sha)
  }
  def withUrl(url: String): GitShaUrl = {
    copy(url = url)
  }
  def withSha(sha: String): GitShaUrl = {
    copy(sha = sha)
  }
}
object GitShaUrl {
  def apply(url: String, sha: String): GitShaUrl = new GitShaUrl(url, sha)
}
