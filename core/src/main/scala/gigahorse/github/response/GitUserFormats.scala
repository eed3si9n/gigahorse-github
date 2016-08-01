/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitUserFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val GitUserFormat: JsonFormat[gigahorse.github.response.GitUser] = new JsonFormat[gigahorse.github.response.GitUser] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitUser = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val name = unbuilder.readField[String]("name")
      val email = unbuilder.readField[String]("email")
      val date = unbuilder.readField[java.util.Calendar]("date")
      unbuilder.endObject()
      new gigahorse.github.response.GitUser(name, email, date)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitUser, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("name", obj.name)
    builder.addField("email", obj.email)
    builder.addField("date", obj.date)
    builder.endObject()
  }
}
}
