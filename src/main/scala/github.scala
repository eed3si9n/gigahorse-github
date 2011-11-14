package dispatch.github

import dispatch._
import dispatch.Request._
import dispatch.liftjson.Js._
import net.liftweb.json._

case class Repos(user: String, name: String) extends ResourceMethod {
  def git_refs = GitRefs(this, None)
  def git_trees(sha: String) = GitTrees(this, sha)
  
  def complete = _ / "repos" / user / name
}

object Response {
  val git_ref = GitRef.fromJson _
  val git_refs = GitRefs.fromJson _
  val git_trees = GitTrees.fromJson _
}

object GitRefs {
  def fromJson(json: JValue): Seq[GitRef] =
    for {
      JArray(array) <- json
      v <- array
    } yield GitRef(v)
}

case class GitRefs(repo: Repos, branch: Option[String]) extends ResourceMethod {
  def heads(branch: String): GitRefs = copy(branch = Some(branch))
  
  val complete = { req: Request =>
    val request = repo.complete(req) / "git" / "refs"
    branch match {
      case Some(b) => request / "heads" / b
      case _ => request
    }
  }
}

object GitRef extends Parsers {
  def fromJson(json: JValue): GitRef = GitRef(json)
  
  def apply(json: JValue): GitRef =
    GitRef(git_object(json).head,
      ref(json).head,
      url(json).head)
}

case class GitRef(git_object: GitObject,
  ref: String,
  url: String)

object GitTrees {
  val tree = 'tree ? ary
  
  def fromJson(json: JValue): Seq[GitTree] = tree(json) map {GitTree(_)}
}

case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends ResourceMethod {
  private def param(key: String)(value: Any): GitTrees = copy(params = params + (key -> value.toString))
  
  val recursive = param("recursive")_
  
  def complete = repo.complete(_) / "git" / "trees" / sha <<? params
}

object GitTree extends Parsers {  
  def apply(json: JValue): GitTree =
    GitTree(type_str(json).head,
      url(json).head,
      size(json).headOption,
      path(json).head,
      sha(json).head,
      mode(json).head)
}

case class GitTree(type_str: String,
  url: String,
  size: Option[BigInt],
  path: String,
  sha: String,
  mode: String)

object GitObject extends Parsers {  
  def apply(json: JValue): GitObject =
    GitObject(type_str(json).head,
      url(json).head,
      sha(json).head)
}

case class GitObject(type_str: String,
  url: String,
  sha: String)

private[github] trait Parsers {
  def git_object(json: JValue): Seq[GitObject] = for { JField("object", v) <- json } yield GitObject(v)
  val ref = 'ref ? str  
  val type_str = 'type ? str
  val url = 'url ? str
  val size = 'size ? int
  val path = 'path ? str
  val sha = 'sha ? str
  val mode = 'mode ? str  
}
