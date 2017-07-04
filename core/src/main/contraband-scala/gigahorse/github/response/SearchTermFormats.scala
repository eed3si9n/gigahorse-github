/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait SearchTermFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val SearchTermFormat: JsonFormat[gigahorse.github.response.SearchTerm] = new JsonFormat[gigahorse.github.response.SearchTerm] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.SearchTerm = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val text = unbuilder.readField[String]("text")
      val indices = unbuilder.readField[Vector[Int]]("indices")
      unbuilder.endObject()
      gigahorse.github.response.SearchTerm(text, indices)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.SearchTerm, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("text", obj.text)
    builder.addField("indices", obj.indices)
    builder.endObject()
  }
}
}
