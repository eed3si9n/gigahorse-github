/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Repo(
  val id: Long,
  val url: String,
  val name: String,
  val full_name: String,
  val description: Option[String],
  val `private`: Boolean,
  val fork: Boolean,
  val html_url: String) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: Repo => (this.id == x.id) && (this.url == x.url) && (this.name == x.name) && (this.full_name == x.full_name) && (this.description == x.description) && (this.`private` == x.`private`) && (this.fork == x.fork) && (this.html_url == x.html_url)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + id.##) + url.##) + name.##) + full_name.##) + description.##) + `private`.##) + fork.##) + html_url.##)
  }
  override def toString: String = {
    "Repo(" + id + ", " + url + ", " + name + ", " + full_name + ", " + description + ", " + `private` + ", " + fork + ", " + html_url + ")"
  }
  private[this] def copy(id: Long = id, url: String = url, name: String = name, full_name: String = full_name, description: Option[String] = description, `private`: Boolean = `private`, fork: Boolean = fork, html_url: String = html_url): Repo = {
    new Repo(id, url, name, full_name, description, `private`, fork, html_url)
  }
  def withId(id: Long): Repo = {
    copy(id = id)
  }
  def withUrl(url: String): Repo = {
    copy(url = url)
  }
  def withName(name: String): Repo = {
    copy(name = name)
  }
  def withFull_name(full_name: String): Repo = {
    copy(full_name = full_name)
  }
  def withDescription(description: Option[String]): Repo = {
    copy(description = description)
  }
  def withPrivate(`private`: Boolean): Repo = {
    copy(`private` = `private`)
  }
  def withFork(fork: Boolean): Repo = {
    copy(fork = fork)
  }
  def withHtml_url(html_url: String): Repo = {
    copy(html_url = html_url)
  }
}
object Repo {
  def apply(id: Long, url: String, name: String, full_name: String, description: Option[String], `private`: Boolean, fork: Boolean, html_url: String): Repo = new Repo(id, url, name, full_name, description, `private`, fork, html_url)
}
