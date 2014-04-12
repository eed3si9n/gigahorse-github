package repatch.github.response

import dispatch._
import org.json4s._
import java.util.{GregorianCalendar, Calendar, Locale}

/** provides parsing support for a github repository response.
 * @see http://developer.github.com/v3/repos/
 */
object Repo extends Parse with CommonField {
  def apply(json: JValue): Repo =
    Repo(id = id(json),
      owner = Owner(owner(json)),
      name = name(json),
      full_name = full_name(json),
      description = description(json),
      `private` = `private`(json),
      fork = fork(json),
      url = url(json),
      html_url = html_url(json),
      clone_url = clone_url(json),
      git_url = git_url(json),
      ssh_url = ssh_url(json),
      // svn_url = svn_url(json).head,
      // mirror_url: Option[String],
      homepage = homepage(json),
      language_opt = language_opt(json),
      // forks: BigInt,
      forks_count = forks_count(json),
      // watchers: BigInt,
      watchers_count = watchers_count(json),
      size = size(json),
      master_branch = master_branch(json),
      open_issues_count = open_issues_count(json),
      pushed_at = pushed_at(json),
      created_at = created_at(json),
      updated_at = updated_at(json))

  val full_name   = 'full_name.![String]
  val description = 'description.![String]
  val `private`   = 'private.![Boolean]
  val fork        = 'fork.![Boolean]
  val html_url    = 'html_url.![String]
  val clone_url   = 'clone_url.![String]
  val git_url     = 'git_url.![String]
  val ssh_url     = 'ssh_url.![String]
  val svn_url     = 'svn_url.![String]
  val mirror_url_opt = 'mirror_url.?[String]
  val homepage    = 'homepage.![String]
  val language    = 'language.![String]
  val language_opt = 'language.?[String]
  val forks       = 'forks.![BigInt]
  val forks_count = 'forks_count.![BigInt]
  val watchers    = 'watchers.![BigInt]
  val watchers_count = 'watchers_count.![BigInt]
  val master_branch = 'master_branch.![String]
  val open_issues_count = 'open_issues_count.![BigInt]
  val owner       = 'owner.![JObject]
  val pushed_at   = 'pushed_at.![Calendar]
}

/** represents repository response.
 * @see http://developer.github.com/v3/repos/
 */
case class Repo(id: BigInt,
  owner: Owner,
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
  language_opt: Option[String],
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

object Owner extends Parse with CommonField {
  val login       = 'login.![String]
  val avatar_url  = 'avatar_url.![String]
  val gravatar_id = 'gravatar_id.![String]

  def apply(json: JValue): Owner =
    Owner(id = id(json),
      `type` = `type`(json),
      login = login(json),
      avatar_url = avatar_url(json),
      gravatar_id = gravatar_id(json),
      url = url(json))
}

case class Owner(id: BigInt,
  `type`: String,
  login: String,
  avatar_url: String,
  gravatar_id: String,
  url: String)

object GitRefs {
  def apply(json: JValue): Seq[GitRef] =
    for {
      JArray(array) <- json
      v <- array
    } yield GitRef(v)
}

/** represents git reference response.
 * @see http://developer.github.com/v3/git/refs/
 */
case class GitRef(ref: String,
  url: String,
  git_object: GitObject)

/** provides parsing support for a git reference response. */
object GitRef extends Parse with CommonField {
  def apply(json: JValue): GitRef = {
    val x = GitObject(git_object(json))
    GitRef(ref = ref(json),
      url = url(json),
      git_object = x)
  }
  
  val git_object = 'object.![JObject]
}

object GitObject extends Parse with CommonField {
  def apply(json: JValue): GitObject =
    GitObject(sha = sha(json),
      url = url(json),
      `type` = `type`(json))
}

case class GitObject(sha: String,
  url: String,
  `type`: String)

object GitCommit extends Parse with CommonField {  
  def apply(json: JValue): GitCommit =
    GitCommit(sha = sha(json),
      url = url(json),
      author = GitUser(author(json)),
      committer = GitUser(committer(json)),
      message = message(json),
      tree = GitShaUrl(tree(json)),
      parents = parents(json) map {GitShaUrl.apply})
  
  val author = 'author.![JObject]
  val committer = 'committer.![JObject]
  val tree = 'tree.![JObject]
  val parents = 'parents.![List[JValue]]
}

/** represents git commit response.
 * @see http://developer.github.com/v3/git/commits/
 */
case class GitCommit(sha: String,
  url: String,
  author: GitUser,
  committer: GitUser,
  message: String,
  tree: GitShaUrl,
  parents: Seq[GitShaUrl])

object GitUser extends Parse with CommonField {
  def apply(json: JValue): GitUser = 
    GitUser(date = date(json),
      name = name(json),
      email = email(json))
}

case class GitUser(date: java.util.Calendar,
  name: String,
  email: String)

object GitShaUrl extends Parse with CommonField {
  def apply(json: JValue): GitShaUrl =
    GitShaUrl(sha = sha(json),
      url = url(json))
}

case class GitShaUrl(sha: String,
  url: String)

object GitTrees extends Parse {
  val tree = 'tree.![List[JValue]]
  
  def apply(json: JValue): Seq[GitTree] = tree(json) map GitTree.apply
}

object GitTree extends Parse with CommonField {  
  def apply(json: JValue): GitTree =
    GitTree(sha = sha(json),
      url = url(json),
      path = path(json),
      mode = mode(json),
      `type` = `type`(json),
      size_opt = size_opt(json))
}

/** represents git tree response
 * @see http://developer.github.com/v3/git/trees/
 */
case class GitTree(sha: String,
  url: String,
  path: String,
  mode: String,
  `type`: String,
  size_opt: Option[BigInt])

/** provides parsing support for a git blob response. */
object GitBlob extends Parse with CommonField {  
  def apply(json: JValue): GitBlob =
    GitBlob(sha = sha(json),
      url = url(json),
      encoding = encoding(json),
      content = content(json),
      size = size(json))
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
      case "utf-8"  => content.getBytes
      case "base64" => (new sun.misc.BASE64Decoder()).decodeBuffer(content)
    }
}

trait CommonField { self: Parse =>
  val id = 'id.![BigInt]
  val sha = 'sha.![String]
  val url = 'url.![String]
  val ref = 'ref.![String]

  val path = 'path.![String]
  val mode = 'mode.![String]
  val `type` = 'type.![String]
  val size = 'size.![BigInt]
  val size_opt = 'size.?[BigInt]
  val message = 'message.![String]
  val name = 'name.![String]
  val email = 'email.![String]
  val date = 'date.![Calendar]
  val created_at = 'created_at.![Calendar]
  val updated_at = 'updated_at.![Calendar]
  val encoding = 'encoding.![String]
  val content = 'content.![String]
}

trait Parse {
  def parse[A: ReadJs](js: JValue): Option[A] =
    implicitly[ReadJs[A]].readJs.lift(js)
  def parse_![A: ReadJs](js: JValue): A = parse(js).get
  def parseField[A: ReadJs](key: String)(js: JValue): Option[A] = parse[A](js \ key)
  def parseField_![A: ReadJs](key: String)(js: JValue): A = parseField(key)(js).get
  implicit class SymOp(sym: Symbol) {
    def ?[A: ReadJs](js: JValue): Option[A] = parseField[A](sym.name)(js)
    def ?[A: ReadJs]: JValue => Option[A] = ?[A](_)
    def ![A: ReadJs]: JValue => A = parseField_![A](sym.name)_
  }
}

trait ReadJs[A] {
  import ReadJs.=>?
  val readJs: JValue =>? A
}
object ReadJs {
  type =>?[-A, +B] = PartialFunction[A, B]
  def readJs[A](pf: JValue =>? A): ReadJs[A] = new ReadJs[A] {
    val readJs = pf
  }
  implicit val listRead: ReadJs[List[JValue]] = readJs { case JArray(v) => v }
  implicit val objectRead: ReadJs[JObject]    = readJs { case JObject(v) => JObject(v) }
  implicit val bigIntRead: ReadJs[BigInt]     = readJs { case JInt(v) => v }
  implicit val intRead: ReadJs[Int]           = readJs { case JInt(v) => v.toInt }
  implicit val stringRead: ReadJs[String]     = readJs { case JString(v) => v }
  implicit val boolRead: ReadJs[Boolean]      = readJs { case JBool(v) => v }
  implicit val calendarRead: ReadJs[Calendar] =
    readJs { case JString(v) =>
      // iso8601s
      javax.xml.bind.DatatypeConverter.parseDateTime(v)
    }
  implicit def readJsListRead[A: ReadJs]: ReadJs[List[A]] = {
    val f = implicitly[ReadJs[A]].readJs
    readJs {
      case JArray(xs) if xs forall {f.isDefinedAt} =>
        xs map {f.apply}
    }
  }  
}
