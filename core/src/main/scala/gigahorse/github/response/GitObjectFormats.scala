/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitObjectFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val GitObjectFormat: JsonFormat[gigahorse.github.response.GitObject] = new JsonFormat[gigahorse.github.response.GitObject] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitObject = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val `type` = unbuilder.readField[String]("type")
      unbuilder.endObject()
      new gigahorse.github.response.GitObject(url, sha, `type`)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitObject, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("type", obj.`type`)
    builder.endObject()
  }
}
}
