package repatch.github.request

import dispatch._
import org.json4s._
import scala.util.control.Exception.allCatch
import collection.immutable.Map

/** AbstractClient is a function to wrap API operations */
abstract class AbstractClient {
  def hostName = "api.github.com"
  def host = :/(hostName).secure
  def mimes: Seq[MediaType]
  def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] =
    if (mimes.isEmpty) Map()
    else Map("Accept" -> (mimes map ev.shows).mkString(","))
  def apply(method: Method)(implicit ev: Show[MediaType]): Req = {
    val r = method(host)
    if (httpHeaders.isEmpty) r
    else r <:< httpHeaders
  }
}

case class NoAuthClient(mimes: Seq[MediaType]) extends AbstractClient with Mime[NoAuthClient] {
  def mime(ms: Seq[MediaType]): NoAuthClient = copy(mimes = ms)
}

case class BasicAuthClient(user: String, pass: String, mimes: Seq[MediaType]) extends AbstractClient with Mime[BasicAuthClient] {
  override def host = :/(hostName).secure as_! (user, pass)
  def mime(ms: Seq[MediaType]): BasicAuthClient = copy(mimes = ms)
}

object LocalConfigClient {
  lazy val token: Option[String] = gitConfig("github.token")
  def apply(): LocalConfigClient = LocalConfigClient(MediaType.default)
  def apply(mimes: Seq[MediaType]): LocalConfigClient =
    LocalConfigClient(OAuthClient(token getOrElse sys.error("Token was not found in local config!"), mimes))

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

/** OAuthClient using local github config (https://github.com/blog/180-local-github-config).
 */
case class LocalConfigClient(underlying: OAuthClient) extends AbstractClient with Mime[LocalConfigClient] {
  override def host = underlying.host
  override def apply(method: Method)(implicit ev: Show[MediaType]): Req = underlying.apply(method)
  override def mimes = underlying.mimes
  def mime(ms: Seq[MediaType]): LocalConfigClient = LocalConfigClient(underlying = OAuthClient(underlying.token, ms))
}

case class OAuthClient(token: String, mimes: Seq[MediaType]) extends AbstractClient with Mime[OAuthClient] {
  override def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] =
    super.httpHeaders ++ Map("Authorization" -> "bearer %s".format(token))
  def mime(ms: Seq[MediaType]): OAuthClient = copy(mimes = ms)
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
