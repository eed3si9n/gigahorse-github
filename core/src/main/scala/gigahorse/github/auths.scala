package gigahorse.github

import gigahorse._
import scala.util.control.Exception.allCatch
import collection.immutable.Map

/** AbstractClient is a function to wrap API operations */
abstract class AbstractClient {
  def mimes: List[MediaType]
  def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] =
    if (mimes.isEmpty) Map()
    else Map(HeaderNames.ACCEPT -> (mimes map ev.shows).mkString(","))
  def complete(request: Request): Request =
    if (httpHeaders.isEmpty) request
    else request.addHeaders(httpHeaders.toList: _*)
  def apply(builder: request.RequestBuilder)(implicit ev: Show[MediaType]): Request = {
    val r = builder.build
    complete(r)
  }
}

case class NoAuthClient(mimes: List[MediaType]) extends AbstractClient with Mime[NoAuthClient] {
  def mime(ms: List[MediaType]): NoAuthClient = copy(mimes = ms)
}

case class BasicAuthClient(user: String, pass: String, mimes: List[MediaType]) extends AbstractClient with Mime[BasicAuthClient] {
  def mime(ms: List[MediaType]): BasicAuthClient = copy(mimes = ms)
  override def complete(request: Request): Request =
    super.complete(request).withAuth(user, pass)
}

object LocalConfigClient {
  import scala.collection.mutable
  private val cache: mutable.Map[String, String] = mutable.Map.empty

  def apply(): LocalConfigClient = LocalConfigClient("github.token", MediaType.default)
  def apply(key: String): LocalConfigClient = LocalConfigClient(key, MediaType.default)
  def apply(key: String, mimes: List[MediaType]): LocalConfigClient =
    LocalConfigClient(OAuthClient(cache.getOrElseUpdate(key,
      gitConfig(key) getOrElse sys.error("Token was not found in local config!")), mimes))

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
  override def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] = underlying.httpHeaders
  override def complete(request: Request): Request = underlying.complete(request)
  override def apply(builder: request.RequestBuilder)(implicit ev: Show[MediaType]): Request = underlying.apply(builder)
  override def mimes: List[MediaType] = underlying.mimes
  def mime(ms: List[MediaType]): LocalConfigClient =
    LocalConfigClient(underlying = underlying.mime(ms))
}

case class OAuthClient(token: String, mimes: List[MediaType]) extends AbstractClient with Mime[OAuthClient] {
  override def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] =
    super.httpHeaders ++ Map("Authorization" -> "bearer %s".format(token))
  def mime(ms: List[MediaType]): OAuthClient = copy(mimes = ms)
  override def toString: String =
    s"OAuthClient(****, $mimes)"
}

object OAuth {
  import scala.concurrent._
  import scala.concurrent.duration._
  def authorizations: Request = Gigahorse.url("https://api.github.com/authorizations")
  /* Fetches a new access token given a gh username and password
   *  and optional list of scopes */
  def accessToken(user: String, pass: String, scopes: Seq[String] = Nil): String =
    Gigahorse.withHttp { http =>
      val r = authorizations.withAuth(user, pass).post("""{"scopes":[%s]}""".format(
        scopes.mkString("\"","\",","\"")
      ))
      val f = http.run(r, Github.asAuthorization)
      Await.result(f, Duration.Inf).token
    }
}
