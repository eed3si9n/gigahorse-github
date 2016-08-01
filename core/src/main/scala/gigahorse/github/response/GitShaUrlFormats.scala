/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitShaUrlFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val GitShaUrlFormat: JsonFormat[gigahorse.github.response.GitShaUrl] = new JsonFormat[gigahorse.github.response.GitShaUrl] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitShaUrl = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      unbuilder.endObject()
      new gigahorse.github.response.GitShaUrl(url, sha)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitShaUrl, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.endObject()
  }
}
}
