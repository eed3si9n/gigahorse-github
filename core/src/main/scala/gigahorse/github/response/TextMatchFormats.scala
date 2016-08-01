/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait TextMatchFormats { self: sjsonnew.BasicJsonProtocol with gigahorse.github.response.SearchTermFormats =>
implicit lazy val TextMatchFormat: JsonFormat[gigahorse.github.response.TextMatch] = new JsonFormat[gigahorse.github.response.TextMatch] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.TextMatch = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val object_url = unbuilder.readField[String]("object_url")
      val object_type = unbuilder.readField[String]("object_type")
      val property = unbuilder.readField[String]("property")
      val fragment = unbuilder.readField[String]("fragment")
      val matches = unbuilder.readField[Vector[gigahorse.github.response.SearchTerm]]("matches")
      unbuilder.endObject()
      new gigahorse.github.response.TextMatch(object_url, object_type, property, fragment, matches)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.TextMatch, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("object_url", obj.object_url)
    builder.addField("object_type", obj.object_type)
    builder.addField("property", obj.property)
    builder.addField("fragment", obj.fragment)
    builder.addField("matches", obj.matches)
    builder.endObject()
  }
}
}
