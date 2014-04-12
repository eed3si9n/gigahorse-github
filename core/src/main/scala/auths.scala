package repatch.github.request

import dispatch._
import org.json4s._
import scala.util.control.Exception.allCatch

/** AbstractClient is a function to wrap API operations */
abstract class AbstractClient extends (Method => Req) {
  def hostName = "api.github.com"
  def host = :/(hostName).secure
  def apply(method: Method): Req = method(host)
}

case object NoAuthClient extends AbstractClient {
}

case class BasicAuthClient(user: String, pass: String) extends AbstractClient {
  override def host = :/(hostName).secure as_! (user, pass)
}

/** OAuthClient using local github config (https://github.com/blog/180-local-github-config).
 */
case object LocalConfigClient extends AbstractClient {
  override def host = underlying.host
  override def apply(method: Method): Req = underlying.apply(method)
  
  lazy val underlying = underlyingOpt getOrElse sys.error("Token was not found in local config!")
  lazy val token: Option[String] = gitConfig("github.token")
  lazy val underlyingOpt: Option[OAuthClient] = token map { OAuthClient } 

  // https://github.com/defunkt/gist/blob/master/lib/gist.rb#L237
  def gitConfig(key: String): Option[String] =
    allCatch opt {
      Option(System.getenv(key.toUpperCase.replaceAll("""\.""", "_"))) map { Some(_) } getOrElse {
        val p = new java.lang.ProcessBuilder("git", "config", "--global", key).start()
        val reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
        Option(reader.readLine)
      }
    } getOrElse None
}

case class OAuthClient(token: String) extends AbstractClient {
  override def apply(method: Method): Req = method(host) <:< Map("Authorization" -> "bearer %s".format(token))
}

object OAuth {
  def authorizations = :/("api.github.com").secure / "authorizations"
  /* Fetches a new access token given a gh username and password
   *  and optional list of scopes */
  def accessToken(user: String, pass: String, scopes: Seq[String] = Nil): Future[String] = {
    import scala.concurrent.ExecutionContext.Implicits.global 
    Http(authorizations.POST.as_!(user, pass) << """{"scopes":[%s]}""".format(
      scopes.mkString("\"","\",","\"")
    ) > as.json4s.Json) map { _ \ "token" match {
      case JString(tok) => tok
      case _ => sys.error("Token was not found!")
    }}
  }
}
