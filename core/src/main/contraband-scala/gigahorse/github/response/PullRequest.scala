/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class PullRequest private (
  val url: String,
  val html_url: Option[String],
  val diff_url: Option[String],
  val patch_url: Option[String]) extends Serializable {
  
  private def this(url: String, html_url: Option[String]) = this(url, html_url, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: PullRequest => (this.url == x.url) && (this.html_url == x.html_url) && (this.diff_url == x.diff_url) && (this.patch_url == x.patch_url)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.PullRequest".##) + url.##) + html_url.##) + diff_url.##) + patch_url.##)
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
  def withHtml_url(html_url: String): PullRequest = {
    copy(html_url = Option(html_url))
  }
  def withDiff_url(diff_url: Option[String]): PullRequest = {
    copy(diff_url = diff_url)
  }
  def withDiff_url(diff_url: String): PullRequest = {
    copy(diff_url = Option(diff_url))
  }
  def withPatch_url(patch_url: Option[String]): PullRequest = {
    copy(patch_url = patch_url)
  }
  def withPatch_url(patch_url: String): PullRequest = {
    copy(patch_url = Option(patch_url))
  }
}
object PullRequest {
  
  def apply(url: String, html_url: Option[String]): PullRequest = new PullRequest(url, html_url)
  def apply(url: String, html_url: String): PullRequest = new PullRequest(url, Option(html_url))
  def apply(url: String, html_url: Option[String], diff_url: Option[String], patch_url: Option[String]): PullRequest = new PullRequest(url, html_url, diff_url, patch_url)
  def apply(url: String, html_url: String, diff_url: String, patch_url: String): PullRequest = new PullRequest(url, Option(html_url), Option(diff_url), Option(patch_url))
}
