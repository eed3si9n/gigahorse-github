/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait GitCommitFormats { self: gigahorse.github.response.GitUserFormats with gigahorse.github.response.GitShaUrlFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val GitCommitFormat: JsonFormat[gigahorse.github.response.GitCommit] = new JsonFormat[gigahorse.github.response.GitCommit] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.GitCommit = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val committer = unbuilder.readField[gigahorse.github.response.GitUser]("committer")
      val message = unbuilder.readField[String]("message")
      val tree = unbuilder.readField[gigahorse.github.response.GitShaUrl]("tree")
      val parents = unbuilder.readField[Vector[gigahorse.github.response.GitShaUrl]]("parents")
      unbuilder.endObject()
      new gigahorse.github.response.GitCommit(url, sha, committer, message, tree, parents)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.GitCommit, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("committer", obj.committer)
    builder.addField("message", obj.message)
    builder.addField("tree", obj.tree)
    builder.addField("parents", obj.parents)
    builder.endObject()
  }
}
}
