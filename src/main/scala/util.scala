package dispatch.github

import dispatch._
import dispatch.Request._
import dispatch.liftjson.Js._
import net.liftweb.json._
import scala.util.control.Exception.allCatch

/** Client is a function to wrap API operations */
abstract class AbstractClient extends ((Request => Request) => Request) {
  def hostname = "api.github.com"
  def host: Request
  def handle[T](method: Method[T]) = method.default_handler(apply(method))
  def apply(block: Request => Request): Request = block(host)  
}

case object NoAuthClient extends AbstractClient {
  val host = :/(hostname).secure
}

case class BasicAuthClient(user: String, pass: String) extends AbstractClient {
  val host = :/(hostname).secure as_! (user, pass)
}

case object Client extends AbstractClient {
  def host = underlying.host
  override def handle[T](method: Method[T]) = underlying.handle(method)
  override def apply(block: Request => Request): Request = underlying.apply(block)
  
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
  val authorizations = :/("api.github.com").secure / "authorizations"
  /** Fetches a new access token given a gh username and password
   *  and optional list of scopes */
  def accessToken(user: String, pass: String, scopes: Seq[String] = Nil): Option[String] = {
    val client = new Http with NoLogging
    try {
      client(authorizations.POST.as_!(user, pass) << """{"scopes":[%s]}""".format(
        scopes.mkString("\"","\",","\"")
      ) ># { _ \ "token" match {
        case JString(tok) => Some(tok)
        case _ => None
      }})
    } finally {
      client.shutdown()
    }
  }
}

case class OAuthClient(token: String) extends AbstractClient {
  val host = :/(hostname).secure
  override def apply(block: Request => Request): Request = block(host) <:< Map("Authorization" -> "bearer %s".format(token))
}

trait MethodBuilder extends Builder[Request => Request] {
  final def product = setup andThen complete
  def setup = identity[Request] _
  def complete: Request => Request
}

trait Method[T] extends MethodBuilder {
  /** default handler used by AbstractClient#handle. You can also apply the client
      to a Method and define your own request handler. */
  def default_handler: Request => Handler[T]
}

trait ResourceMethod extends Method[JValue] {
  def default_handler = _ ># identity[JValue]
}

// trait QueryMethod extends Method[(List[JValue],List[JValue])] {
//   def default_handler = _ ># (Response.results ~ Response.meta)
// }
