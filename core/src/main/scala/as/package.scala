package dispatch.as.repatch.github

package object response {
  import com.ning.http.client.Response    
  import repatch.github.{response => res}
  import dispatch.as.json4s.Json

  val Repo:         Response => res.Repo = Json andThen res.Repo.apply
  val GitRefs:      Response => res.Paged[res.GitRef] = res.Paged.parseArray(res.GitRef.apply)
  val GitRef:       Response => res.GitRef = Json andThen res.GitRef.apply
  val GitCommit:    Response => res.GitCommit = Json andThen res.GitCommit.apply
  val GitTrees:     Response => res.GitTrees = Json andThen res.GitTrees.apply
  val GitBlob:      Response => res.GitBlob = Json andThen res.GitBlob.apply
  val Issues:       Response => res.Paged[res.Issue] = res.Paged.parseArray(res.Issue.apply)
  val User:         Response => res.User = Json andThen res.User.apply
  val ReposSearch:  Response => res.Paged[res.Repo] = res.Paged.parseSearchResult(res.Repo.apply)
}
