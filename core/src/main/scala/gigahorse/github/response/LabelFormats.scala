/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait LabelFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val LabelFormat: JsonFormat[gigahorse.github.response.Label] = new JsonFormat[gigahorse.github.response.Label] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Label = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val name = unbuilder.readField[String]("name")
      val color = unbuilder.readField[String]("color")
      unbuilder.endObject()
      new gigahorse.github.response.Label(url, name, color)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Label, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("name", obj.name)
    builder.addField("color", obj.color)
    builder.endObject()
  }
}
}
