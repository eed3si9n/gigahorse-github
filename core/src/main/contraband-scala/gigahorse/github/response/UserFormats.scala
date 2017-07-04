/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait UserFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val UserFormat: JsonFormat[gigahorse.github.response.User] = new JsonFormat[gigahorse.github.response.User] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.User = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val login = unbuilder.readField[String]("login")
      val id = unbuilder.readField[Long]("id")
      val html_url = unbuilder.readField[Option[String]]("html_url")
      val avatar_url = unbuilder.readField[Option[String]]("avatar_url")
      val gravatar_id = unbuilder.readField[Option[String]]("gravatar_id")
      val `type` = unbuilder.readField[Option[String]]("type")
      val site_admin = unbuilder.readField[Option[Boolean]]("site_admin")
      val name = unbuilder.readField[Option[String]]("name")
      val email = unbuilder.readField[Option[String]]("email")
      unbuilder.endObject()
      gigahorse.github.response.User(url, login, id, html_url, avatar_url, gravatar_id, `type`, site_admin, name, email)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.User, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("login", obj.login)
    builder.addField("id", obj.id)
    builder.addField("html_url", obj.html_url)
    builder.addField("avatar_url", obj.avatar_url)
    builder.addField("gravatar_id", obj.gravatar_id)
    builder.addField("type", obj.`type`)
    builder.addField("site_admin", obj.site_admin)
    builder.addField("name", obj.name)
    builder.addField("email", obj.email)
    builder.endObject()
  }
}
}
