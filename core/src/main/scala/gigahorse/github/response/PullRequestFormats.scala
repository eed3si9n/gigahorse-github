/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ deserializationError, serializationError, Builder, JsonFormat, Unbuilder }
trait PullRequestFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val PullRequestFormat: JsonFormat[gigahorse.github.response.PullRequest] = new JsonFormat[gigahorse.github.response.PullRequest] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.PullRequest = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val html_url = unbuilder.readField[Option[String]]("html_url")
      val diff_url = unbuilder.readField[Option[String]]("diff_url")
      val patch_url = unbuilder.readField[Option[String]]("patch_url")
      unbuilder.endObject()
      new gigahorse.github.response.PullRequest(url, html_url, diff_url, patch_url)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.PullRequest, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("html_url", obj.html_url)
    builder.addField("diff_url", obj.diff_url)
    builder.addField("patch_url", obj.patch_url)
    builder.endObject()
  }
}
}
