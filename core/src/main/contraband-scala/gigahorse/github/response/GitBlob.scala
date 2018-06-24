/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitBlob private (
  val url: String,
  val sha: String,
  val encoding: String,
  val content: String,
  val size: Option[Long]) extends Serializable {
  def asStr(charset: String): String =
  encoding match {
    case "base64" => new String(bytes, charset)
    case _ => content
  }
  def asUtf8: String = asStr("UTF-8")
  def bytes: Array[Byte] =
  encoding match {
    case "utf-8"  => content.getBytes
    case "base64" => (new sun.misc.BASE64Decoder()).decodeBuffer(content)
  }
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitBlob => (this.url == x.url) && (this.sha == x.sha) && (this.encoding == x.encoding) && (this.content == x.content) && (this.size == x.size)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.GitBlob".##) + url.##) + sha.##) + encoding.##) + content.##) + size.##)
  }
  override def toString: String = {
    "GitBlob(" + url + ", " + sha + ", " + encoding + ", " + content + ", " + size + ")"
  }
  private[this] def copy(url: String = url, sha: String = sha, encoding: String = encoding, content: String = content, size: Option[Long] = size): GitBlob = {
    new GitBlob(url, sha, encoding, content, size)
  }
  def withUrl(url: String): GitBlob = {
    copy(url = url)
  }
  def withSha(sha: String): GitBlob = {
    copy(sha = sha)
  }
  def withEncoding(encoding: String): GitBlob = {
    copy(encoding = encoding)
  }
  def withContent(content: String): GitBlob = {
    copy(content = content)
  }
  def withSize(size: Option[Long]): GitBlob = {
    copy(size = size)
  }
  def withSize(size: Long): GitBlob = {
    copy(size = Option(size))
  }
}
object GitBlob {
  
  def apply(url: String, sha: String, encoding: String, content: String, size: Option[Long]): GitBlob = new GitBlob(url, sha, encoding, content, size)
  def apply(url: String, sha: String, encoding: String, content: String, size: Long): GitBlob = new GitBlob(url, sha, encoding, content, Option(size))
}
