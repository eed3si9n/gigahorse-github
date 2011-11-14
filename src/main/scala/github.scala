package dispatch.github

import dispatch._
import dispatch.Request._
import dispatch.liftjson.Js._
import net.liftweb.json._

case class Repos(user: String, name: String) extends ResourceMethod {
  def trees(sha: String) = Trees(this, sha)
  
  def complete = _ / "repos" / user / name
}

object Trees {
  val tree = 'tree ? ary
  
  def fromJson(json: JValue): Seq[Tree] = tree(json) map {Tree.fromJson}    
  def apply(repo: Repos, sha: String) = new Trees(repo, sha)
}

class Trees(val repo: Repos, val sha: String) extends ResourceMethod {
  def complete = repo.complete(_) / "git" / "trees" / sha
}

object Tree {
  val tree_type = 'type ? str
  val url = 'url ? str
  val size = 'size ? int
  val path = 'path ? str
  val sha = 'sha ? str
  val mode = 'mode ? str
  
  def fromJson(json: JValue): Tree =
    Tree(tree_type(json).head,
      url(json).head,
      size(json).headOption,
      path(json).head,
      sha(json).head,
      mode(json).head)
}

case class Tree(tree_type: String,
  url: String,
  size: Option[BigInt],
  path: String,
  sha: String,
  mode: String)
