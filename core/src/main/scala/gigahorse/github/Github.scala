package gigahorse.github

import gigahorse._
import github.{ response => res }
import request.{ Repos, Issues, UrlBuilder, Users, Search }
import sjsonnew.JsonFormat
import sjsonnew.support.scalajson.unsafe.Converter
import scala.json.ast.unsafe.JValue
import java.nio.ByteBuffer

abstract class Github {
  import res.CustomJsonProtocol._

  def noAuthClient(mimes: List[MediaType]) = NoAuthClient(mimes)
  def basicAuthClient(user: String, pass: String, mimes: List[MediaType]) =
    BasicAuthClient(user, pass, mimes)
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

  val asString: Response => String = Gigahorse.asString
  val asJson: Response => JValue =
    (r: Response) => {
      import sjsonnew.support.scalajson.unsafe.Parser
      val buffer = ByteBuffer.wrap(r.bodyAsBytes)
      Parser.parseFromByteBuffer(buffer).get
    }
  def as[A: JsonFormat]: Response => A =
    asJson andThen Converter.fromJsonUnsafe[A]
  val asAuthorization: Response => res.Authorization = as[res.Authorization]
  val asRepo: Response => res.Repo                   = as[res.Repo]
  val asRepos: Response => res.Paged[res.Repo]       = res.Paged.parseArray[res.Repo]
  val asGitRef: Response => res.GitRef               = as[res.GitRef]
  val asGitRefs: Response => res.Paged[res.GitRef]   = res.Paged.parseArray[res.GitRef]
  val asGitCommit: Response => res.GitCommit         = as[res.GitCommit]
  val asGitTrees: Response => res.GitTrees           = as[res.GitTrees]
  val asGitBlob: Response => res.GitBlob             = as[res.GitBlob]
  val asIssues: Response => res.Paged[res.Issue]     = res.Paged.parseArray[res.Issue]
  val asUser: Response => res.User                   = as[res.User]
  val asUsers: Response => res.Paged[res.User]       = res.Paged.parseArray[res.User]
  val asOrgs: Response => res.Paged[res.User]        = asUsers

  val asReposSearch: Response => res.Paged[res.Repo] =
    res.Paged.parseSearchResult[res.Repo]
  val asCodeSearch: Response => res.Paged[res.BlobRef] =
    res.Paged.parseSearchResult[res.BlobRef]
  val asIssuesSearch: Response => res.Paged[res.Issue] =
    res.Paged.parseSearchResult[res.Issue]
  val asUsersSearch: Response => res.Paged[res.User] =
    res.Paged.parseSearchResult[res.User]
  val asTextMatches: Response => res.Paged[res.TextMatches] =
    res.Paged.parseSearchResult[res.TextMatches]
}

object Github extends Github
