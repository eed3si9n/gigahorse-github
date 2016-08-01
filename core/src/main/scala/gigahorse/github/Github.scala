package gigahorse.github

import gigahorse._
import github.{ response => res }
import request.{ Repos, Issues, UrlBuilder, Users, Search }
import sjsonnew.support.scalajson.unsafe.Converter
import scala.json.ast.unsafe.JValue
import java.nio.ByteBuffer

object Github {
  import res.CustomJsonProtocol._

  def noAuthClient(mimes: List[MediaType]) = NoAuthClient(mimes)
  def basicAuthClient(user: String, pass: String, mimes: List[MediaType]) =
    BasicAuthClient(user, pass, mimes)
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
  val asAuthorization: Response => res.Authorization =
    asJson andThen Converter.fromJsonUnsafe[res.Authorization]
  val asRepo: Response => res.Repo =
    asJson andThen Converter.fromJsonUnsafe[res.Repo]
  val asRepos: Response => res.Paged[res.Repo] =
    res.Paged.parseArray(Converter.fromJsonUnsafe[res.Repo])
  val asGitRef: Response => res.GitRef =
    asJson andThen Converter.fromJsonUnsafe[res.GitRef]
  val asGitRefs: Response => res.Paged[res.GitRef] =
    res.Paged.parseArray(Converter.fromJsonUnsafe[res.GitRef])
  val asGitCommit: Response => res.GitCommit =
    asJson andThen Converter.fromJsonUnsafe[res.GitCommit]
  val asGitTrees: Response => res.GitTrees =
    asJson andThen Converter.fromJsonUnsafe[res.GitTrees]
  val asGitBlob: Response => res.GitBlob =
    asJson andThen Converter.fromJsonUnsafe[res.GitBlob]
  val asIssues: Response => res.Paged[res.Issue] =
    res.Paged.parseArray(Converter.fromJsonUnsafe[res.Issue])
  val asUser: Response => res.User =
    asJson andThen Converter.fromJsonUnsafe[res.User]
  val asUsers: Response => res.Paged[res.User] =
    res.Paged.parseArray(Converter.fromJsonUnsafe[res.User])
  val asOrgs: Response => res.Paged[res.User] = asUsers

  val asReposSearch: Response => res.Paged[res.Repo] =
    res.Paged.parseSearchResult(Converter.fromJsonUnsafe[res.Repo])
  val asCodeSearch: Response => res.Paged[res.BlobRef] =
    res.Paged.parseSearchResult(Converter.fromJsonUnsafe[res.BlobRef])
  val asIssuesSearch: Response => res.Paged[res.Issue] =
    res.Paged.parseSearchResult(Converter.fromJsonUnsafe[res.Issue])
  val asUsersSearch: Response => res.Paged[res.User] =
    res.Paged.parseSearchResult(Converter.fromJsonUnsafe[res.User])
  val asTextMatches: Response => res.Paged[res.TextMatches] =
    res.Paged.parseSearchResult(Converter.fromJsonUnsafe[res.TextMatches])
}
