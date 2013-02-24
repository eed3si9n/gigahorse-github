package repatch.github

import dispatch._
import net.liftweb.json._

/** represents a github repository from the request-side */
case class Repos(user: String, name: String) extends Method {
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
  def fromJson(json: JValue): Repo = Repo(json)
  
  def apply(json: JValue): Repo =
    Repo(id = id(json).head,
      owner = owner(json).head,
      name = name(json).head,
      full_name = full_name(json).head,
      description = description(json).head,
      `private` = `private`(json).head,
      fork = fork(json).head,
      url = url(json).head,
      html_url = html_url(json).head,
      clone_url = clone_url(json).head,
      git_url = git_url(json).head,
      ssh_url = ssh_url(json).head,
      // svn_url = svn_url(json).head,
      // mirror_url: Option[String],
      homepage = homepage(json).head,
      language = language(json),
      // forks: BigInt,
      forks_count = forks_count(json).head,
      // watchers: BigInt,
      watchers_count = watchers_count(json).head,
      size = size(json).head,
      master_branch = master_branch(json).head,
      open_issues_count = open_issues_count(json).head,
      pushed_at = pushed_at(json).head,
      created_at = created_at(json).head,
      updated_at = updated_at(json).head)

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
  val open_issues_count = 'open_issues_count ? int
  val pushed_at   = 'pushed_at ? iso8601datetime

  def owner(json: JValue): Option[Owner] = (for { JField("owner", v) <- json } yield Owner(v)).headOption
  
  object Owner extends Parsers {
    val login       = 'login ? str
    val avatar_url  = 'avatar_url ? str
    val gravatar_id = 'gravatar_id ? str

    def apply(json: JValue): Owner =
      Owner(id = id(json).head,
        `type` = `type`(json).head,
        login = login(json).head,
        avatar_url = avatar_url(json).head,
        gravatar_id = gravatar_id(json).head,
        url = url(json).head)
  }

  case class Owner(id: BigInt,
    `type`: String,
    login: String,
    avatar_url: String,
    gravatar_id: String,
    url: String)
}

/** represents repository response.
 * @see http://developer.github.com/v3/repos/
 */
case class Repo(id: BigInt,
  owner: Repo.Owner,
  name: String,
  full_name: String,
  description: String,
  `private`: Boolean,
  fork: Boolean,
  url: String,
  html_url: String,
  clone_url: String,
  git_url: String,
  ssh_url: String,
  // svn_url: String,
  // mirror_url: Option[String],
  homepage: String,
  language: Option[String],
  // forks: BigInt,
  forks_count: BigInt,
  // watchers: BigInt,
  watchers_count: BigInt,
  size: BigInt,
  master_branch: String,
  open_issues_count: BigInt,
  pushed_at: java.util.Calendar,
  created_at: java.util.Calendar,
  updated_at: java.util.Calendar)

/** represents git blob request.
 * @see http://developer.github.com/v3/git/blobs/
 */
case class GitBlobs(repo: Repos, sha: String, override val mime: Option[String]) extends Method {
  def raw = copy(mime = Some("application/vnd.github.raw"))
  
  val complete = { req: Req =>
    repo.complete(req) / "git" / "blobs" / sha
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
case class GitRefs(repo: Repos, branch: Option[String]) extends Method {
  def head(branch: String): GitRefs = copy(branch = Some(branch))
  
  val complete = { req: Req =>
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
        `type` = `type`(json).head)
  }
  
  case class GitObject(sha: String,
    url: String,
    `type`: String)
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
case class GitCommits(repo: Repos, sha: String) extends Method {
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
      GitUser(date = date(json).head,
        name = name(json).head,
        email = email(json).head)
  }
  
  case class GitUser(date: java.util.Calendar,
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
  import Js._
  val tree = 'tree ? ary
  
  def fromJson(json: JValue): Seq[GitTree] = tree(json).getOrElse(Nil) map {GitTree(_)}
}

/** represents git tree request.
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTrees(repo: Repos, sha: String, params: Map[String, String] = Map()) extends Method {
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
      `type` = `type`(json).head,
      size = size(json).headOption)
}

/** represents git tree response
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTree(sha: String,
  url: String,
  path: String,
  mode: String,
  `type`: String,
  size: Option[BigInt])

trait Parsers extends Js {
  val id  = 'id ? int
  val sha = 'sha ? str
  val url = 'url ? str
  val ref = 'ref ? str
  
  val path = 'path ? str
  val mode = 'mode ? str
  val `type` = 'type ? str
  val size = 'size ? int
  val message = 'message ? str
  val name = 'name ? str
  val email = 'email ? str
  val date = 'date ? iso8601datetime
  val created_at = 'created_at ? iso8601datetime
  val updated_at = 'updated_at ? iso8601datetime
  val encoding = 'encoding ? str
  val content = 'content ? str
}
