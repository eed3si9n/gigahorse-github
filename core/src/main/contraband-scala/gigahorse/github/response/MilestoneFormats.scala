/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait MilestoneFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val MilestoneFormat: JsonFormat[gigahorse.github.response.Milestone] = new JsonFormat[gigahorse.github.response.Milestone] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Milestone = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val number = unbuilder.readField[Long]("number")
      val state = unbuilder.readField[String]("state")
      val title = unbuilder.readField[String]("title")
      unbuilder.endObject()
      gigahorse.github.response.Milestone(url, number, state, title)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Milestone, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("number", obj.number)
    builder.addField("state", obj.state)
    builder.addField("title", obj.title)
    builder.endObject()
  }
}
}
