/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait IssueFormats { self: gigahorse.github.response.IssueStateFormats with gigahorse.github.response.UserFormats with gigahorse.github.response.LabelFormats with gigahorse.github.response.MilestoneFormats with gigahorse.github.response.PullRequestFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val IssueFormat: JsonFormat[gigahorse.github.response.Issue] = new JsonFormat[gigahorse.github.response.Issue] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Issue = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val html_url = unbuilder.readField[Option[String]]("html_url")
      val number = unbuilder.readField[Option[Long]]("number")
      val state = unbuilder.readField[Option[gigahorse.github.response.IssueState]]("state")
      val title = unbuilder.readField[Option[String]]("title")
      val body = unbuilder.readField[Option[String]]("body")
      val user = unbuilder.readField[Option[gigahorse.github.response.User]]("user")
      val labels = unbuilder.readField[Vector[gigahorse.github.response.Label]]("labels")
      val assignee = unbuilder.readField[Option[gigahorse.github.response.User]]("assignee")
      val Milestone = unbuilder.readField[Option[gigahorse.github.response.Milestone]]("Milestone")
      val comments = unbuilder.readField[Option[Long]]("comments")
      val pull_request = unbuilder.readField[Option[gigahorse.github.response.PullRequest]]("pull_request")
      val closed_at = unbuilder.readField[Option[java.util.Calendar]]("closed_at")
      val created_at = unbuilder.readField[Option[java.util.Calendar]]("created_at")
      val updated_at = unbuilder.readField[Option[java.util.Calendar]]("updated_at")
      unbuilder.endObject()
      gigahorse.github.response.Issue(url, html_url, number, state, title, body, user, labels, assignee, Milestone, comments, pull_request, closed_at, created_at, updated_at)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Issue, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("html_url", obj.html_url)
    builder.addField("number", obj.number)
    builder.addField("state", obj.state)
    builder.addField("title", obj.title)
    builder.addField("body", obj.body)
    builder.addField("user", obj.user)
    builder.addField("labels", obj.labels)
    builder.addField("assignee", obj.assignee)
    builder.addField("Milestone", obj.Milestone)
    builder.addField("comments", obj.comments)
    builder.addField("pull_request", obj.pull_request)
    builder.addField("closed_at", obj.closed_at)
    builder.addField("created_at", obj.created_at)
    builder.addField("updated_at", obj.updated_at)
    builder.endObject()
  }
}
}
