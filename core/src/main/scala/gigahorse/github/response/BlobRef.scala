/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class BlobRef(
  val url: String,
  val sha: String,
  val name: String,
  val path: String,
  val repository: gigahorse.github.response.Repo,
  val git_url: Option[String],
  val html_url: Option[String]) extends Serializable {
  
  def this(url: String, sha: String, name: String, path: String, repository: gigahorse.github.response.Repo) = this(url, sha, name, path, repository, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: BlobRef => (this.url == x.url) && (this.sha == x.sha) && (this.name == x.name) && (this.path == x.path) && (this.repository == x.repository) && (this.git_url == x.git_url) && (this.html_url == x.html_url)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + url.##) + sha.##) + name.##) + path.##) + repository.##) + git_url.##) + html_url.##)
  }
  override def toString: String = {
    "BlobRef(" + url + ", " + sha + ", " + name + ", " + path + ", " + repository + ", " + git_url + ", " + html_url + ")"
  }
  private[this] def copy(url: String = url, sha: String = sha, name: String = name, path: String = path, repository: gigahorse.github.response.Repo = repository, git_url: Option[String] = git_url, html_url: Option[String] = html_url): BlobRef = {
    new BlobRef(url, sha, name, path, repository, git_url, html_url)
  }
  def withUrl(url: String): BlobRef = {
    copy(url = url)
  }
  def withSha(sha: String): BlobRef = {
    copy(sha = sha)
  }
  def withName(name: String): BlobRef = {
    copy(name = name)
  }
  def withPath(path: String): BlobRef = {
    copy(path = path)
  }
  def withRepository(repository: gigahorse.github.response.Repo): BlobRef = {
    copy(repository = repository)
  }
  def withGit_url(git_url: Option[String]): BlobRef = {
    copy(git_url = git_url)
  }
  def withHtml_url(html_url: Option[String]): BlobRef = {
    copy(html_url = html_url)
  }
}
object BlobRef {
  def apply(url: String, sha: String, name: String, path: String, repository: gigahorse.github.response.Repo): BlobRef = new BlobRef(url, sha, name, path, repository, None, None)
  def apply(url: String, sha: String, name: String, path: String, repository: gigahorse.github.response.Repo, git_url: Option[String], html_url: Option[String]): BlobRef = new BlobRef(url, sha, name, path, repository, git_url, html_url)
}
