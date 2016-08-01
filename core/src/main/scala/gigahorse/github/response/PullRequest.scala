/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class PullRequest(
  val url: String,
  val html_url: Option[String],
  val diff_url: Option[String],
  val patch_url: Option[String]) extends Serializable {
  
  def this(url: String, html_url: Option[String]) = this(url, html_url, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: PullRequest => (this.url == x.url) && (this.html_url == x.html_url) && (this.diff_url == x.diff_url) && (this.patch_url == x.patch_url)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + url.##) + html_url.##) + diff_url.##) + patch_url.##)
  }
  override def toString: String = {
    "PullRequest(" + url + ", " + html_url + ", " + diff_url + ", " + patch_url + ")"
  }
  private[this] def copy(url: String = url, html_url: Option[String] = html_url, diff_url: Option[String] = diff_url, patch_url: Option[String] = patch_url): PullRequest = {
    new PullRequest(url, html_url, diff_url, patch_url)
  }
  def withUrl(url: String): PullRequest = {
    copy(url = url)
  }
  def withHtml_url(html_url: Option[String]): PullRequest = {
    copy(html_url = html_url)
  }
  def withDiff_url(diff_url: Option[String]): PullRequest = {
    copy(diff_url = diff_url)
  }
  def withPatch_url(patch_url: Option[String]): PullRequest = {
    copy(patch_url = patch_url)
  }
}
object PullRequest {
  def apply(url: String, html_url: Option[String]): PullRequest = new PullRequest(url, html_url, None, None)
  def apply(url: String, html_url: Option[String], diff_url: Option[String], patch_url: Option[String]): PullRequest = new PullRequest(url, html_url, diff_url, patch_url)
}
