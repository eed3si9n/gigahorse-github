/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class User private (
  val url: String,
  val login: String,
  val id: Long,
  val html_url: Option[String],
  val avatar_url: Option[String],
  val gravatar_id: Option[String],
  val `type`: Option[String],
  val site_admin: Option[Boolean],
  val name: Option[String],
  val email: Option[String]) extends Serializable {
  
  private def this(url: String, login: String, id: Long) = this(url, login, id, None, None, None, None, None, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: User => (this.url == x.url) && (this.login == x.login) && (this.id == x.id) && (this.html_url == x.html_url) && (this.avatar_url == x.avatar_url) && (this.gravatar_id == x.gravatar_id) && (this.`type` == x.`type`) && (this.site_admin == x.site_admin) && (this.name == x.name) && (this.email == x.email)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.User".##) + url.##) + login.##) + id.##) + html_url.##) + avatar_url.##) + gravatar_id.##) + `type`.##) + site_admin.##) + name.##) + email.##)
  }
  override def toString: String = {
    "User(" + url + ", " + login + ", " + id + ", " + html_url + ", " + avatar_url + ", " + gravatar_id + ", " + `type` + ", " + site_admin + ", " + name + ", " + email + ")"
  }
  private[this] def copy(url: String = url, login: String = login, id: Long = id, html_url: Option[String] = html_url, avatar_url: Option[String] = avatar_url, gravatar_id: Option[String] = gravatar_id, `type`: Option[String] = `type`, site_admin: Option[Boolean] = site_admin, name: Option[String] = name, email: Option[String] = email): User = {
    new User(url, login, id, html_url, avatar_url, gravatar_id, `type`, site_admin, name, email)
  }
  def withUrl(url: String): User = {
    copy(url = url)
  }
  def withLogin(login: String): User = {
    copy(login = login)
  }
  def withId(id: Long): User = {
    copy(id = id)
  }
  def withHtml_url(html_url: Option[String]): User = {
    copy(html_url = html_url)
  }
  def withHtml_url(html_url: String): User = {
    copy(html_url = Option(html_url))
  }
  def withAvatar_url(avatar_url: Option[String]): User = {
    copy(avatar_url = avatar_url)
  }
  def withAvatar_url(avatar_url: String): User = {
    copy(avatar_url = Option(avatar_url))
  }
  def withGravatar_id(gravatar_id: Option[String]): User = {
    copy(gravatar_id = gravatar_id)
  }
  def withGravatar_id(gravatar_id: String): User = {
    copy(gravatar_id = Option(gravatar_id))
  }
  def withType(`type`: Option[String]): User = {
    copy(`type` = `type`)
  }
  def withType(`type`: String): User = {
    copy(`type` = Option(`type`))
  }
  def withSite_admin(site_admin: Option[Boolean]): User = {
    copy(site_admin = site_admin)
  }
  def withSite_admin(site_admin: Boolean): User = {
    copy(site_admin = Option(site_admin))
  }
  def withName(name: Option[String]): User = {
    copy(name = name)
  }
  def withName(name: String): User = {
    copy(name = Option(name))
  }
  def withEmail(email: Option[String]): User = {
    copy(email = email)
  }
  def withEmail(email: String): User = {
    copy(email = Option(email))
  }
}
object User {
  
  def apply(url: String, login: String, id: Long): User = new User(url, login, id)
  def apply(url: String, login: String, id: Long, html_url: Option[String], avatar_url: Option[String], gravatar_id: Option[String], `type`: Option[String], site_admin: Option[Boolean], name: Option[String], email: Option[String]): User = new User(url, login, id, html_url, avatar_url, gravatar_id, `type`, site_admin, name, email)
  def apply(url: String, login: String, id: Long, html_url: String, avatar_url: String, gravatar_id: String, `type`: String, site_admin: Boolean, name: String, email: String): User = new User(url, login, id, Option(html_url), Option(avatar_url), Option(gravatar_id), Option(`type`), Option(site_admin), Option(name), Option(email))
}
