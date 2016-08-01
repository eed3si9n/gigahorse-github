/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitRefFormats { self: gigahorse.github.response.GitObjectFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val GitRefFormat: JsonFormat[gigahorse.github.response.GitRef] = new JsonFormat[gigahorse.github.response.GitRef] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitRef = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val ref = unbuilder.readField[String]("ref")
      val `object` = unbuilder.readField[gigahorse.github.response.GitObject]("object")
      unbuilder.endObject()
      new gigahorse.github.response.GitRef(url, ref, `object`)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitRef, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("ref", obj.ref)
    builder.addField("object", obj.`object`)
    builder.endObject()
  }
}
}
