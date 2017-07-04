/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait GitBlobFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val GitBlobFormat: JsonFormat[gigahorse.github.response.GitBlob] = new JsonFormat[gigahorse.github.response.GitBlob] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitBlob = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val encoding = unbuilder.readField[String]("encoding")
      val content = unbuilder.readField[String]("content")
      val size = unbuilder.readField[Option[Long]]("size")
      unbuilder.endObject()
      gigahorse.github.response.GitBlob(url, sha, encoding, content, size)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitBlob, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("encoding", obj.encoding)
    builder.addField("content", obj.content)
    builder.addField("size", obj.size)
    builder.endObject()
  }
}
}
