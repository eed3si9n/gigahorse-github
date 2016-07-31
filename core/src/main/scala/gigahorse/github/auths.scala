package gigahorse.github

import gigahorse._
import scala.util.control.Exception.allCatch
import collection.immutable.Map

/** AbstractClient is a function to wrap API operations */
abstract class AbstractClient {
 //  def host = :/(hostName).secure
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
    if (httpHeaders.isEmpty) r
    else r
  }
}

case class NoAuthClient(mimes: List[MediaType]) extends AbstractClient with Mime[NoAuthClient] {
  def mime(ms: List[MediaType]): NoAuthClient = copy(mimes = ms)
}

// case class BasicAuthClient(user: String, pass: String, mimes: Seq[MediaType]) extends AbstractClient with Mime[BasicAuthClient] {
//   override def host = :/(hostName).secure as_! (user, pass)
//   def mime(ms: Seq[MediaType]): BasicAuthClient = copy(mimes = ms)
// }

// object LocalConfigClient {
//   lazy val token: Option[String] = gitConfig("github.token")
//   def apply(): LocalConfigClient = LocalConfigClient(MediaType.default)
//   def apply(mimes: Seq[MediaType]): LocalConfigClient =
//     LocalConfigClient(OAuthClient(token getOrElse sys.error("Token was not found in local config!"), mimes))

//   // https://github.com/defunkt/gist/blob/master/lib/gist.rb#L237
//   def gitConfig(key: String): Option[String] =
//     allCatch opt {
//       Option(System.getenv(key.toUpperCase.replaceAll("""\.""", "_"))) map { Some(_) } getOrElse {
//         val p = new java.lang.ProcessBuilder("git", "config", "--global", key).start()
//         val reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream))
//         Option(reader.readLine)
//       }
//     } getOrElse None
// }

// /** OAuthClient using local github config (https://github.com/blog/180-local-github-config).
//  */
// case class LocalConfigClient(underlying: OAuthClient) extends AbstractClient with Mime[LocalConfigClient] {
//   override def host = underlying.host
//   override def apply(method: Method)(implicit ev: Show[MediaType]): Req = underlying.apply(method)
//   override def mimes = underlying.mimes
//   def mime(ms: Seq[MediaType]): LocalConfigClient = LocalConfigClient(underlying = OAuthClient(underlying.token, ms))
// }

// case class OAuthClient(token: String, mimes: Seq[MediaType]) extends AbstractClient with Mime[OAuthClient] {
//   override def httpHeaders(implicit ev: Show[MediaType]): Map[String, String] =
//     super.httpHeaders ++ Map("Authorization" -> "bearer %s".format(token))
//   def mime(ms: Seq[MediaType]): OAuthClient = copy(mimes = ms)
// }

// object OAuth {
//   def authorizations = :/("api.github.com").secure / "authorizations"
//   /* Fetches a new access token given a gh username and password
//    *  and optional list of scopes */
//   def accessToken(user: String, pass: String, scopes: Seq[String] = Nil): Future[String] = {
//     import scala.concurrent.ExecutionContext.Implicits.global 
//     Http(authorizations.POST.as_!(user, pass) << """{"scopes":[%s]}""".format(
//       scopes.mkString("\"","\",","\"")
//     ) > as.json4s.Json) map { _ \ "token" match {
//       case JString(tok) => tok
//       case _ => sys.error("Token was not found!")
//     }}
//   }
// }
