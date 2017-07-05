/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait BlobRefFormats { self: gigahorse.github.response.RepoFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val BlobRefFormat: JsonFormat[gigahorse.github.response.BlobRef] = new JsonFormat[gigahorse.github.response.BlobRef] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.BlobRef = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val sha = unbuilder.readField[String]("sha")
      val name = unbuilder.readField[String]("name")
      val path = unbuilder.readField[String]("path")
      val repository = unbuilder.readField[gigahorse.github.response.Repo]("repository")
      val git_url = unbuilder.readField[Option[String]]("git_url")
      val html_url = unbuilder.readField[Option[String]]("html_url")
      unbuilder.endObject()
      gigahorse.github.response.BlobRef(url, sha, name, path, repository, git_url, html_url)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.BlobRef, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("sha", obj.sha)
    builder.addField("name", obj.name)
    builder.addField("path", obj.path)
    builder.addField("repository", obj.repository)
    builder.addField("git_url", obj.git_url)
    builder.addField("html_url", obj.html_url)
    builder.endObject()
  }
}
}
