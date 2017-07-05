/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait AuthorizationFormats { self: sjsonnew.BasicJsonProtocol =>
implicit lazy val AuthorizationFormat: JsonFormat[gigahorse.github.response.Authorization] = new JsonFormat[gigahorse.github.response.Authorization] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Authorization = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val id = unbuilder.readField[Long]("id")
      val scopes = unbuilder.readField[Vector[String]]("scopes")
      val token = unbuilder.readField[String]("token")
      val token_last_eight = unbuilder.readField[String]("token_last_eight")
      val hashed_token = unbuilder.readField[String]("hashed_token")
      val note = unbuilder.readField[Option[String]]("note")
      val note_url = unbuilder.readField[Option[String]]("note_url")
      val created_at = unbuilder.readField[Option[java.util.Calendar]]("created_at")
      val updated_at = unbuilder.readField[Option[java.util.Calendar]]("updated_at")
      unbuilder.endObject()
      gigahorse.github.response.Authorization(url, id, scopes, token, token_last_eight, hashed_token, note, note_url, created_at, updated_at)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Authorization, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("id", obj.id)
    builder.addField("scopes", obj.scopes)
    builder.addField("token", obj.token)
    builder.addField("token_last_eight", obj.token_last_eight)
    builder.addField("hashed_token", obj.hashed_token)
    builder.addField("note", obj.note)
    builder.addField("note_url", obj.note_url)
    builder.addField("created_at", obj.created_at)
    builder.addField("updated_at", obj.updated_at)
    builder.endObject()
  }
}
}
