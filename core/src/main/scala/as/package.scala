package dispatch.as.repatch.github

package object response {
  import com.ning.http.client.Response    
  import repatch.github.{response => res}
  import dispatch.as.json4s.Json

  val Repo: Response => res.Repo = Json andThen res.Repo.apply
  val GitRefs: Response => Seq[res.GitRef] = Json andThen res.GitRefs.apply
  val GitRef: Response => res.GitRef = Json andThen res.GitRef.apply
  val GitCommit: Response => res.GitCommit = Json andThen res.GitCommit.apply
  val GitTrees: Response => Seq[res.GitTree] = Json andThen res.GitTrees.apply
  val GitBlob: Response => res.GitBlob = Json andThen res.GitBlob.apply
}
