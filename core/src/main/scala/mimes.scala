package repatch.github.request

trait Mime[R] {
  import MediaType._
  def mime(ms: Seq[MediaType]): R
  def raw       = mime(Seq(versionMediaType.rawBlob))
  def diff      = mime(Seq(versionMediaType.diff))
  def patch     = mime(Seq(versionMediaType.patch))
  def rawBody   = mime(Seq(json, versionMediaType.rawBody))
  def textBody  = mime(Seq(json, versionMediaType.textBody))
  def htmlBody  = mime(Seq(json, versionMediaType.htmlBody))
  def fullBody  = mime(Seq(json, versionMediaType.fullBody))
  def versionMediaType = v3
}

sealed trait MediaType {}

object MediaType {
  case class StringMediaType(mediaType: String) extends MediaType {}
  case class GithubMediaType(_version: Option[String], _param: Option[String], _format: Option[String]) extends MediaType {
    def version(v: String): GithubMediaType = copy(_version = Some(v))
    def param(p: String): GithubMediaType = copy(_param = Some(p))
    def format(f: String): GithubMediaType = copy(_format = Some(f))
    def formatJson: GithubMediaType = format("json")
    def noFormat: GithubMediaType = copy(_format = None)
    def diff: GithubMediaType = param("diff").noFormat
    def patch: GithubMediaType = param("patch").noFormat
    def rawBlob: GithubMediaType = param("raw").noFormat
    def rawBody: GithubMediaType = param("raw").formatJson
    def textBody: GithubMediaType = param("text").formatJson
    def htmlBody: GithubMediaType = param("html").formatJson
    def fullBody: GithubMediaType = param("full").formatJson
  }

  implicit val mediaTypeShow = new Show[MediaType] {
    def shows(a: MediaType): String = a match {
      case StringMediaType(x) => x
      case GithubMediaType(v, p, pl) =>
        val version = v map { "." + _ } getOrElse ""
        val param   = p map { "." + _ } getOrElse ""
        val plus    = pl map { "+" + _ } getOrElse ""
        s"""application/vnd.github.$version$param$plus"""
    }
  }

  /** THe basic json media type and version info:
   * application/json
   * application/vnd.github.v3+json
   */
  val default: Seq[MediaType] = Seq(json, v3.formatJson)
  lazy val json: MediaType = StringMediaType("application/json")
  lazy val v3 = githubMediaType.version("v3")
  lazy val githubMediaType = GithubMediaType(None, None, None)
}
