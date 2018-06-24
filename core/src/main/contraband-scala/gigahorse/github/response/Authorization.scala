/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Authorization private (
  val url: String,
  val id: Long,
  val scopes: Vector[String],
  val token: String,
  val token_last_eight: String,
  val hashed_token: String,
  val note: Option[String],
  val note_url: Option[String],
  val created_at: Option[java.util.Calendar],
  val updated_at: Option[java.util.Calendar]) extends Serializable {
  
  private def this(url: String, id: Long, scopes: Vector[String], token: String, token_last_eight: String, hashed_token: String) = this(url, id, scopes, token, token_last_eight, hashed_token, None, None, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: Authorization => (this.url == x.url) && (this.id == x.id) && (this.scopes == x.scopes) && (this.token == x.token) && (this.token_last_eight == x.token_last_eight) && (this.hashed_token == x.hashed_token) && (this.note == x.note) && (this.note_url == x.note_url) && (this.created_at == x.created_at) && (this.updated_at == x.updated_at)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.Authorization".##) + url.##) + id.##) + scopes.##) + token.##) + token_last_eight.##) + hashed_token.##) + note.##) + note_url.##) + created_at.##) + updated_at.##)
  }
  override def toString: String = {
    "Authorization(" + url + ", " + id + ", " + scopes + ", " + token + ", " + token_last_eight + ", " + hashed_token + ", " + note + ", " + note_url + ", " + created_at + ", " + updated_at + ")"
  }
  private[this] def copy(url: String = url, id: Long = id, scopes: Vector[String] = scopes, token: String = token, token_last_eight: String = token_last_eight, hashed_token: String = hashed_token, note: Option[String] = note, note_url: Option[String] = note_url, created_at: Option[java.util.Calendar] = created_at, updated_at: Option[java.util.Calendar] = updated_at): Authorization = {
    new Authorization(url, id, scopes, token, token_last_eight, hashed_token, note, note_url, created_at, updated_at)
  }
  def withUrl(url: String): Authorization = {
    copy(url = url)
  }
  def withId(id: Long): Authorization = {
    copy(id = id)
  }
  def withScopes(scopes: Vector[String]): Authorization = {
    copy(scopes = scopes)
  }
  def withToken(token: String): Authorization = {
    copy(token = token)
  }
  def withToken_last_eight(token_last_eight: String): Authorization = {
    copy(token_last_eight = token_last_eight)
  }
  def withHashed_token(hashed_token: String): Authorization = {
    copy(hashed_token = hashed_token)
  }
  def withNote(note: Option[String]): Authorization = {
    copy(note = note)
  }
  def withNote(note: String): Authorization = {
    copy(note = Option(note))
  }
  def withNote_url(note_url: Option[String]): Authorization = {
    copy(note_url = note_url)
  }
  def withNote_url(note_url: String): Authorization = {
    copy(note_url = Option(note_url))
  }
  def withCreated_at(created_at: Option[java.util.Calendar]): Authorization = {
    copy(created_at = created_at)
  }
  def withCreated_at(created_at: java.util.Calendar): Authorization = {
    copy(created_at = Option(created_at))
  }
  def withUpdated_at(updated_at: Option[java.util.Calendar]): Authorization = {
    copy(updated_at = updated_at)
  }
  def withUpdated_at(updated_at: java.util.Calendar): Authorization = {
    copy(updated_at = Option(updated_at))
  }
}
object Authorization {
  
  def apply(url: String, id: Long, scopes: Vector[String], token: String, token_last_eight: String, hashed_token: String): Authorization = new Authorization(url, id, scopes, token, token_last_eight, hashed_token)
  def apply(url: String, id: Long, scopes: Vector[String], token: String, token_last_eight: String, hashed_token: String, note: Option[String], note_url: Option[String], created_at: Option[java.util.Calendar], updated_at: Option[java.util.Calendar]): Authorization = new Authorization(url, id, scopes, token, token_last_eight, hashed_token, note, note_url, created_at, updated_at)
  def apply(url: String, id: Long, scopes: Vector[String], token: String, token_last_eight: String, hashed_token: String, note: String, note_url: String, created_at: java.util.Calendar, updated_at: java.util.Calendar): Authorization = new Authorization(url, id, scopes, token, token_last_eight, hashed_token, Option(note), Option(note_url), Option(created_at), Option(updated_at))
}
