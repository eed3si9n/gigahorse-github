package dispatch.github

import dispatch._
import dispatch.Request._
import dispatch.liftjson.Js._
import net.liftweb.json._

/** represents a github repository from the request-side */
case class Repos(user: String, name: String) extends ResourceMethod {
  def git_refs = GitRefs(this, None)
  def git_commit(ref: GitRef): GitCommits = GitCommits(this, ref.git_object.sha)
  def git_commit(sha: String): GitCommits = GitCommits(this, sha)
  def git_trees(commit: GitCommit): GitTrees = GitTrees(this, commit.tree.sha)
  def git_trees(sha: String): GitTrees = GitTrees(this, sha)
  def git_blob(sha: String): GitBlobs = GitBlobs(this, sha, None)
  
  def complete = _ / "repos" / user / name
}

/** provides parsing support for a github repository response.
 * @see http://developer.github.com/v3/repos/
 */
object Repo extends Parsers {
  val full_name   = 'full_name ? str
  val description = 'description ? str
  val `private`   = 'private ? bool
  val fork        = 'fork ? bool
  val html_url    = 'html_url ? str
  val clone_url   = 'clone_url ? str
  val git_url     = 'git_url ? str
  val ssh_url     = 'ssh_url ? str
  val svn_url     = 'svn_url ? str
  val mirror_url  = 'mirror_url ? str
  val homepage    = 'homepage ? str
  val language    = 'language ? str
  val forks       = 'forks ? int
  val forks_count = 'forks_count ? int
  val watchers    = 'watchers ? int
  val watchers_count = 'watchers_count ? int
  val master_branch = 'master_branch ? str
  val open_issues = 'open_issues ? int
  val pushed_at   = parseDate("pushed_at")
}

object Response {
  val git_ref = GitRef.fromJson _
  val git_refs = GitRefs.fromJson _
  val git_commit = GitCommit.fromJson _
  val git_trees = GitTrees.fromJson _
  val git_blob = GitBlob.fromJson _
}

/** represents git blob request.
 * @see http://developer.github.com/v3/git/blobs/
 */
case class GitBlobs(repo: Repos, sha: String, mime: Option[String]) extends ResourceMethod {
  def raw = copy(mime = Some("application/vnd.github.raw"))
  
  val complete = { req: Request =>
    val request = repo.complete(req) / "git" / "blobs" / sha
    mime match {
      case Some(x) => request <:< Map("Accept" -> x) 
      case _ => request
    }
  }
}

/** provides parsing support for a git blob response. */
object GitBlob extends Parsers {
  def fromJson(json: JValue): GitBlob = GitBlob(json)
  
  def apply(json: JValue): GitBlob =
    GitBlob(sha = sha(json).head,
      url = url(json).head,
      encoding = encoding(json).head,
      content = content(json).head,
      size = size(json).head)
}

/** represents git blob response.
 * @see http://developer.github.com/v3/git/blobs/
 */
case class GitBlob(sha: String,
  url: String,
  encoding: String,
  content: String,
  size: BigInt) {
  def as_str(charset: String): String =
    encoding match {
      case "base64" => new String(bytes, charset)
      case _ => content
    }
    
  def as_utf8: String = as_str("UTF-8")
  
  def bytes: Array[Byte] =
    encoding match {
      case "utf-8" => content.getBytes
      case "base64" => (new sun.misc.BASE64Decoder()).decodeBuffer(content)
    }
}

object GitRefs {
  def fromJson(json: JValue): Seq[GitRef] =
    for {
      JArray(array) <- json
      v <- array
    } yield GitRef(v)
}

/** represents git reference request.
 * @see http://developer.github.com/v3/git/refs/ 
 */
case class GitRefs(repo: Repos, branch: Option[String]) extends ResourceMethod {
  def head(branch: String): GitRefs = copy(branch = Some(branch))
  
  val complete = { req: Request =>
    val request = repo.complete(req) / "git" / "refs"
    branch match {
      case Some(b) => request / "heads" / b
      case _ => request
    }
  } 
}

/** provides parsing support for a git reference response. */
object GitRef extends Parsers {
  def fromJson(json: JValue): GitRef = GitRef(json)
  
  def apply(json: JValue): GitRef =
    GitRef(ref = ref(json).head,
      url = url(json).head,
      git_object = git_object(json).head)
  
  def git_object(json: JValue): Seq[GitObject] = for { JField("object", v) <- json } yield GitObject(v)
  
  object GitObject extends Parsers {
    def apply(json: JValue): GitObject =
      GitObject(sha = sha(json).head,
        url = url(json).head,
        type_str = type_str(json).head)
  }
  
  case class GitObject(sha: String,
    url: String,
    type_str: String)
}

/** represents git reference response.
 * @see http://developer.github.com/v3/git/refs/
 */
case class GitRef(ref: String,
  url: String,
  git_object: GitRef.GitObject)

/** represents git commit request.
 * @see http://developer.github.com/v3/git/commits/
 */
case class GitCommits(repo: Repos, sha: String) extends ResourceMethod {
  def complete = repo.complete(_) / "git" / "commits" / sha
}

object GitCommit extends Parsers {
  def fromJson(json: JValue): GitCommit = GitCommit(json)
  
  def apply(json: JValue): GitCommit =
    GitCommit(sha = sha(json).head,
      url = url(json).head,
      author = author(json).head,
      committer = committer(json).head,
      message = message(json).head,
      tree = tree(json).head,
      parents = parents(json))
  
  def author(json: JValue): Seq[GitUser] = for { JField("author", v) <- json } yield GitUser(v)
  def committer(json: JValue): Seq[GitUser] = for { JField("committer", v) <- json } yield GitUser(v)
  def tree(json: JValue): Seq[GitShaUrl] = for { JField("tree", v) <- json } yield GitShaUrl(v)
  def parents(json: JValue): Seq[GitShaUrl] =
    for {
      JField("parents", JArray(a)) <- json
      v <- a
    } yield GitShaUrl(v)
  
  object GitUser {
    def apply(json: JValue): GitUser = 
      GitUser(date = parseDate("date")(json).head,
        name = name(json).head,
        email = email(json).head)
  }
  
  case class GitUser(date: java.util.Date,
    name: String,
    email: String)
}

/** represents git commit response.
 * @see http://developer.github.com/v3/git/commits/
 */
case class GitCommit(sha: String,
  url: String,
  author: GitCommit.GitUser,
  committer: GitCommit.GitUser,
  message: String,
  tree: GitShaUrl,
  parents: Seq[GitShaUrl])

object GitShaUrl extends Parsers {
  def fromJson(json: JValue): GitShaUrl = GitShaUrl(json)
  
  def apply(json: JValue): GitShaUrl =
    GitShaUrl(sha = sha(json).head,
      url = url(json).head)
}

case class GitShaUrl(sha: String,
  url: String)

object GitTrees {
  val tree = 'tree ? ary
  
  def fromJson(json: JValue): Seq[GitTree] = tree(json) map {GitTree(_)}
}

/** represents git tree request.
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends ResourceMethod {
  private def param(key: String)(value: Any): GitTrees = copy(params = params + (key -> value.toString))
  
  val recursive = param("recursive")_
  
  def complete = repo.complete(_) / "git" / "trees" / sha <<? params
}

object GitTree extends Parsers {  
  def apply(json: JValue): GitTree =
    GitTree(sha = sha(json).head,
      url = url(json).head,
      path = path(json).head,
      mode = mode(json).head,
      type_str = type_str(json).head,
      size = size(json).headOption)
}

/** represents git tree response
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTree(sha: String,
  url: String,
  path: String,
  mode: String,
  type_str: String,
  size: Option[BigInt])

private[github] trait Parsers {
  val id  = 'id ? int
  val sha = 'sha ? str
  val url = 'url ? str
  val ref = 'ref ? str
  
  val path = 'path ? str
  val mode = 'mode ? str
  val type_str = 'type ? str
  val size = 'size ? int
  val message = 'message ? str
  val name = 'name ? str
  val email = 'email ? str
  val created_at = parseDate("created_at")
  val updated_at = parseDate("updated_at")
  val encoding = 'encoding ? str
  val content = 'content ? str
  def parseDate(field: String) = (json: JValue) => 
    for {
      JField(field, JString(s)) <- json 
    } yield {
      val nocolon = if (s.length == 25 && s(22) == ':') s.slice(0, 22) + s.slice(23, 25)
                    else s
      val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
      formatter.parse(nocolon)
    }
}
