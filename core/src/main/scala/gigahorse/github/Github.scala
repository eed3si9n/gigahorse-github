package gigahorse.github

import gigahorse._
import github.{ response => res }
import request.{ Repos, Issues, UrlBuilder, Users, Search }
import sjsonnew.JsonFormat
import java.nio.ByteBuffer
// import sjsonnew.support.scalajson.unsafe.Converter
// import scalajson.ast.unsafe.JValue
import sjsonnew.support.spray.Converter
import spray.json.JsValue

abstract class Github {
  import res.CustomJsonProtocol._

  def noAuthClient = NoAuthClient()
  def noAuthClient(mimes: List[MediaType]) = NoAuthClient(mimes)
  def basicAuthClient(user: String, pass: String) =
    BasicAuthClient(user, pass)
  def basicAuthClient(user: String, pass: String, mimes: List[MediaType]) =
    BasicAuthClient(user, pass, mimes)
  def oauthClient(token: String) =
    OAuthClient(token)
  def oauthClient(token: String, mimes: List[MediaType]) =
    OAuthClient(token, mimes)
  def localConfigClient: LocalConfigClient = LocalConfigClient()
  def localConfigClient(key: String) = LocalConfigClient(key)
  def localConfigClient(key: String, mimes: List[MediaType]) = LocalConfigClient(key, mimes)

  def repo(owner: String, name: String): Repos = Repos(owner, name)
  def issues: Issues = Issues(Map())
  def user: Users = Users(None)
  def user(name: String): Users = Users(Some(name))
  def url(u: String): UrlBuilder = UrlBuilder(u)
  def search: Search = Search()

  type IssueState = gigahorse.github.response.IssueState
  val IssueState = gigahorse.github.response.IssueState

  val asString: FullResponse => String = GigahorseSupport.asString
  val asJson: FullResponse => JsValue =
    (r: FullResponse) => {
      // import sjsonnew.support.scalajson.unsafe.Parser
      import jawn.support.spray.Parser
      val buffer = r.bodyAsByteBuffer
      Parser.parseFromByteBuffer(buffer).get
    }
  def as[A: JsonFormat]: FullResponse => A =
    asJson andThen Converter.fromJsonUnsafe[A]
  val asAuthorization: FullResponse => res.Authorization = as[res.Authorization]
  val asToken: FullResponse => String                    = asAuthorization andThen { _.token }
  val asRepo: FullResponse => res.Repo                   = as[res.Repo]
  val asRepos: FullResponse => res.Paged[res.Repo]       = res.Paged.parseArray[res.Repo]
  val asGitRef: FullResponse => res.GitRef               = as[res.GitRef]
  val asGitRefs: FullResponse => res.Paged[res.GitRef]   = res.Paged.parseArray[res.GitRef]
  val asGitCommit: FullResponse => res.GitCommit         = as[res.GitCommit]
  val asGitTrees: FullResponse => res.GitTrees           = as[res.GitTrees]
  val asGitBlob: FullResponse => res.GitBlob             = as[res.GitBlob]
  val asIssues: FullResponse => res.Paged[res.Issue]     = res.Paged.parseArray[res.Issue]
  val asUser: FullResponse => res.User                   = as[res.User]
  val asUsers: FullResponse => res.Paged[res.User]       = res.Paged.parseArray[res.User]
  val asOrgs: FullResponse => res.Paged[res.User]        = asUsers

  val asReposSearch: FullResponse => res.Paged[res.Repo] =
    res.Paged.parseSearchResult[res.Repo]
  val asCodeSearch: FullResponse => res.Paged[res.BlobRef] =
    res.Paged.parseSearchResult[res.BlobRef]
  val asIssuesSearch: FullResponse => res.Paged[res.Issue] =
    res.Paged.parseSearchResult[res.Issue]
  val asUsersSearch: FullResponse => res.Paged[res.User] =
    res.Paged.parseSearchResult[res.User]
  val asTextMatches: FullResponse => res.Paged[res.TextMatches] =
    res.Paged.parseSearchResult[res.TextMatches]
}

object Github extends Github
