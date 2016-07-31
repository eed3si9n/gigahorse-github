/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait RepoFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val RepoFormat: JsonFormat[gigahorse.github.response.Repo] = new JsonFormat[gigahorse.github.response.Repo] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Repo = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val id = unbuilder.readField[Long]("id")
      val url = unbuilder.readField[String]("url")
      val name = unbuilder.readField[String]("name")
      val full_name = unbuilder.readField[String]("full_name")
      val description = unbuilder.readField[Option[String]]("description")
      val `private` = unbuilder.readField[Boolean]("private")
      val fork = unbuilder.readField[Boolean]("fork")
      val html_url = unbuilder.readField[String]("html_url")
      unbuilder.endObject()
      new gigahorse.github.response.Repo(id, url, name, full_name, description, `private`, fork, html_url)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Repo, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("id", obj.id)
    builder.addField("url", obj.url)
    builder.addField("name", obj.name)
    builder.addField("full_name", obj.full_name)
    builder.addField("description", obj.description)
    builder.addField("private", obj.`private`)
    builder.addField("fork", obj.fork)
    builder.addField("html_url", obj.html_url)
    builder.endObject()
  }
}
}
