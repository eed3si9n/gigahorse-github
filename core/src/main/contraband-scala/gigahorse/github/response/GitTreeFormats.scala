/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait GitTreeFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val GitTreeFormat: JsonFormat[gigahorse.github.response.GitTree] = new JsonFormat[gigahorse.github.response.GitTree] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitTree = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val path = unbuilder.readField[String]("path")
      val mode = unbuilder.readField[String]("mode")
      val `type` = unbuilder.readField[String]("type")
      val size = unbuilder.readField[Option[Long]]("size")
      unbuilder.endObject()
      gigahorse.github.response.GitTree(url, sha, path, mode, `type`, size)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitTree, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("path", obj.path)
    builder.addField("mode", obj.mode)
    builder.addField("type", obj.`type`)
    builder.addField("size", obj.size)
    builder.endObject()
  }
}
}
