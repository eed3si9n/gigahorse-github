/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitTreesFormats { self: gigahorse.github.response.GitTreeFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val GitTreesFormat: JsonFormat[gigahorse.github.response.GitTrees] = new JsonFormat[gigahorse.github.response.GitTrees] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitTrees = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val tree = unbuilder.readField[Vector[gigahorse.github.response.GitTree]]("tree")
      unbuilder.endObject()
      new gigahorse.github.response.GitTrees(url, sha, tree)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitTrees, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("tree", obj.tree)
    builder.endObject()
  }
}
}
