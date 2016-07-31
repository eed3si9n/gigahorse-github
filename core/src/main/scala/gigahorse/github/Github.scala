package gigahorse.github

import gigahorse._
import github.{ response => res }
import request.Repos
import sjsonnew.support.scalajson.unsafe.Converter
import scala.json.ast.unsafe.JValue
import java.nio.ByteBuffer

object Github {
  import res.CustomJsonProtocol._
  // val hostName = "api.github.com"
  def noAuthClient(mimes: List[MediaType]) = NoAuthClient(mimes)
  def repo(owner: String, name: String): Repos = Repos(owner, name)
//   def issues: Issues = Issues(Map())
//   def user: Users = Users(None)
//   def user(name: String): Users = Users(Some(name))
//   def url(u: String): UrlMethod = UrlMethod(u)
//   def search: Search = Search()

//   type IssueState = repatch.github.response.IssueState
//   val IssueState = repatch.github.response.IssueState

  val asJson: Response => JValue =
    (r: Response) => {
      import sjsonnew.support.scalajson.unsafe.Parser
      val buffer = ByteBuffer.wrap(r.bodyAsBytes)
      Parser.parseFromByteBuffer(buffer).get
    }
  val asRepo: Response => res.Repo =
    asJson andThen Converter.fromJsonUnsafe[res.Repo]

  val asGitRefs: Response => res.Paged[res.GitRef] =
    res.Paged.parseArray(Converter.fromJsonUnsafe[res.GitRef])
}

// package object response {
//   import com.ning.http.client.Response    
//   import repatch.github.{response => res}
//   import dispatch.as.json4s.Json

//   val Repo:         Response => res.Repo = Json andThen res.Repo.apply
//   val Repos:        Response => res.Paged[res.Repo] = res.Paged.parseArray(res.Repo.apply)
//   val GitRef:       Response => res.GitRef = Json andThen res.GitRef.apply
//   val GitRefs:      Response => res.Paged[res.GitRef] = res.Paged.parseArray(res.GitRef.apply)
//   val GitCommit:    Response => res.GitCommit = Json andThen res.GitCommit.apply
//   val GitTrees:     Response => res.GitTrees = Json andThen res.GitTrees.apply
//   val GitBlob:      Response => res.GitBlob = Json andThen res.GitBlob.apply
//   val Issues:       Response => res.Paged[res.Issue] = res.Paged.parseArray(res.Issue.apply)
//   val User:         Response => res.User = Json andThen res.User.apply
//   val Users:        Response => res.Paged[res.User] = res.Paged.parseArray(res.User.apply)
//   val Orgs:         Response => res.Paged[res.User] = Users

//   val ReposSearch:  Response => res.Paged[res.Repo] = res.Paged.parseSearchResult(res.Repo.apply)
//   val CodeSearch:   Response => res.Paged[res.BlobRef] = res.Paged.parseSearchResult(res.BlobRef.apply)
//   val IssuesSearch: Response => res.Paged[res.Issue] = res.Paged.parseSearchResult(res.Issue.apply)
//   val UsersSearch:  Response => res.Paged[res.User] = res.Paged.parseSearchResult(res.User.apply)
//   val TextMatches:  Response => res.Paged[res.TextMatches] = res.Paged.parseSearchResult(res.TextMatches.apply)
// }
