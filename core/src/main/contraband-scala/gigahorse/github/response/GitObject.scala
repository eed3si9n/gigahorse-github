/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitObject private (
  val url: String,
  val sha: String,
  val `type`: String) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitObject => (this.url == x.url) && (this.sha == x.sha) && (this.`type` == x.`type`)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + "GitObject".##) + url.##) + sha.##) + `type`.##)
  }
  override def toString: String = {
    "GitObject(" + url + ", " + sha + ", " + `type` + ")"
  }
  protected[this] def copy(url: String = url, sha: String = sha, `type`: String = `type`): GitObject = {
    new GitObject(url, sha, `type`)
  }
  def withUrl(url: String): GitObject = {
    copy(url = url)
  }
  def withSha(sha: String): GitObject = {
    copy(sha = sha)
  }
  def withType(`type`: String): GitObject = {
    copy(`type` = `type`)
  }
}
object GitObject {
  
  def apply(url: String, sha: String, `type`: String): GitObject = new GitObject(url, sha, `type`)
}
