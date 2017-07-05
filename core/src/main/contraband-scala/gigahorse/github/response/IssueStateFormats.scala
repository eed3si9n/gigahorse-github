/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait IssueStateFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val IssueStateFormat: JsonFormat[gigahorse.github.response.IssueState] = new JsonFormat[gigahorse.github.response.IssueState] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.IssueState = {
    jsOpt match {
      case Some(js) =>
      unbuilder.readString(js) match {
        case "open" => gigahorse.github.response.IssueState.open
        case "closed" => gigahorse.github.response.IssueState.closed
        case "all" => gigahorse.github.response.IssueState.all
      }
      case None =>
      deserializationError("Expected JsString but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.IssueState, builder: Builder[J]): Unit = {
    val str = obj match {
      case gigahorse.github.response.IssueState.open => "open"
      case gigahorse.github.response.IssueState.closed => "closed"
      case gigahorse.github.response.IssueState.all => "all"
    }
    builder.writeString(str)
  }
}
}
