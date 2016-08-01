/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait TextMatchesFormats { self: gigahorse.github.response.TextMatchFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val TextMatchesFormat: JsonFormat[gigahorse.github.response.TextMatches] = new JsonFormat[gigahorse.github.response.TextMatches] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.TextMatches = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val text_matches = unbuilder.readField[Vector[gigahorse.github.response.TextMatch]]("text_matches")
      unbuilder.endObject()
      new gigahorse.github.response.TextMatches(text_matches)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.TextMatches, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("text_matches", obj.text_matches)
    builder.endObject()
  }
}
}
