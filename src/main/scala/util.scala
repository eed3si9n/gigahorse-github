package repatch.github

import dispatch._
import net.liftweb.json._
import scala.util.control.Exception.allCatch

/** Client is a function to wrap API operations */
abstract class AbstractClient extends ((Req => Req) => Req) {
  def hostname = "api.github.com"
  def host: Req
  def apply(block: Req => Req): Req = block(host)  
}

case object NoAuthClient extends AbstractClient {
  def host = :/(hostname).secure
}

case class BasicAuthClient(user: String, pass: String) extends AbstractClient {
  def host = :/(hostname).secure as_! (user, pass)
}

case object Client extends AbstractClient {
  def host = underlying.host
  override def apply(block: Req => Req): Req = underlying.apply(block)
  
  lazy val underlying = token map { case (tkn) =>
    OAuthClient(tkn) } getOrElse NoAuthClient
  
  lazy val token: Option[(String)] = gitConfig("github.token")
  
  // https://github.com/defunkt/gist/blob/master/lib/gist.rb#L237
  def gitConfig(key: String): Option[String] =
    allCatch opt {
      Option(System.getenv(key.toUpperCase.replaceAll("""\.""", "_"))) map { Some(_) } getOrElse {
        val p = new java.lang.ProcessBuilder("git", "config", "--global", key).start()
        val reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
        Option(reader.readLine)
      }
    } getOrElse {None}
}

object OAuth {
  def authorizations = :/("api.github.com").secure / "authorizations"
  /** Fetches a new access token given a gh username and password
   *  and optional list of scopes */
  def accessToken(user: String, pass: String, scopes: Seq[String] = Nil): Option[String] = {
    val tok = Http(authorizations.POST.as_!(user, pass) << """{"scopes":[%s]}""".format(
      scopes.mkString("\"","\",","\"")
    ) > as.lift.Json) map { _ \ "token" match {
      case JString(tok) => Some(tok)
      case _ => None
    }}
    tok()
  }
}

case class OAuthClient(token: String) extends AbstractClient {
  def host = :/(hostname).secure
  override def apply(block: Req => Req): Req = block(host) <:< Map("Authorization" -> "bearer %s".format(token))
}

trait Method extends (Req => Req) {
  def mime: Option[String] = Some("application/json")
  def complete: Req => Req
  def apply(req: Req): Req = {
    val r = complete(req)
    mime match {
      case Some(x) => r <:< Map("Accept" -> x)
      case _ => r
    }
  }
}
