package repatch.github.response

import dispatch._
import org.json4s._
import java.util.{GregorianCalendar, Calendar, Locale}
import com.ning.http.client.Response
import collection.immutable.Map
import repatch.github.request.Show

/** provides parsing support for a github repository response.
 * @see http://developer.github.com/v3/repos/
 */
object Repo extends Parse with CommonField {
  def apply(json: JValue): Repo =
    Repo(id = id(json),
      url = url(json),
      owner = User(owner(json)),
      name = name(json),
      full_name = full_name(json),
      description_opt = description_opt(json),
      `private` = `private`(json),
      fork = fork(json),
      html_url = html_url(json),
      clone_url_opt = clone_url_opt(json),
      git_url_opt = git_url_opt(json),
      ssh_url_opt = ssh_url_opt(json),
      homepage_opt = homepage_opt(json),
      language_opt = language_opt(json),
      forks_count_opt = forks_count_opt(json),
      watchers_count_opt = watchers_count_opt(json),
      size_opt = size_opt(json),
      default_branch_opt = default_branch_opt(json),
      open_issues_count_opt = open_issues_count_opt(json),
      pushed_at_opt = pushed_at_opt(json),
      created_at_opt = created_at_opt(json),
      updated_at_opt = updated_at_opt(json))

  val full_name   = 'full_name.![String]
  val `private`   = 'private.![Boolean]
  val fork        = 'fork.![Boolean]
  val clone_url_opt   = 'clone_url.?[String]
  val git_url_opt     = 'git_url.?[String]
  val ssh_url_opt     = 'ssh_url.?[String]
  val svn_url_opt     = 'svn_url.?[String]
  val mirror_url_opt  = 'mirror_url.?[String]
  val homepage_opt    = 'homepage.?[String]
  val language_opt    = 'language.?[String]
  val forks_count_opt = 'forks_count.?[BigInt]
  val watchers_count_opt  = 'watchers_count.?[BigInt]
  val default_branch_opt  = 'default_branch.?[String]
  val open_issues_count_opt = 'open_issues_count.?[BigInt]
  val owner           = 'owner.![JObject]
  val pushed_at_opt   = 'pushed_at.?[Calendar]
}

/** represents repository response.
 * @see http://developer.github.com/v3/repos/
 */
case class Repo(id: BigInt,
  url: String,
  owner: User,
  name: String,
  full_name: String,
  description_opt: Option[String],
  `private`: Boolean,
  fork: Boolean,
  html_url: String,
  clone_url_opt: Option[String], // does not return during code search
  git_url_opt: Option[String], // does not return during code search 
  ssh_url_opt: Option[String], // does not return during code search
  homepage_opt: Option[String],
  language_opt: Option[String],
  forks_count_opt: Option[BigInt],
  watchers_count_opt: Option[BigInt],
  size_opt: Option[BigInt],
  default_branch_opt: Option[String],
  open_issues_count_opt: Option[BigInt],
  pushed_at_opt: Option[java.util.Calendar],
  created_at_opt: Option[java.util.Calendar],
  updated_at_opt: Option[java.util.Calendar])

/** represents git reference response.
 * @see http://developer.github.com/v3/git/refs/
 */
case class GitRef(ref: String,
  url: String,
  git_object: GitObject)

/** provides parsing support for a git reference response. */
object GitRef extends Parse with CommonField {
  def apply(json: JValue): GitRef =
    GitRef(ref = ref(json),
      url = url(json),
      git_object = GitObject(git_object(json)))
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
    GitUser(name = name(json),
      email = email(json),
      date = date(json))
}

case class GitUser(name: String,
  email: String,
  date: java.util.Calendar)

object GitShaUrl extends Parse with CommonField {
  def apply(json: JValue): GitShaUrl =
    GitShaUrl(sha = sha(json),
      url = url(json))
}

case class GitShaUrl(sha: String,
  url: String)

object GitTrees extends Parse with CommonField {
  val tree = 'tree.![List[JValue]]
  
  def apply(json: JValue): GitTrees =
    GitTrees(sha = sha(json),
      url = url(json),
      tree = tree(json) map GitTree.apply)
}

case class GitTrees(sha: String,
  url: String,
  tree: Seq[GitTree])

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

object Issue extends Parse with CommonField {
  def apply(json: JValue): Issue =
    Issue(url = url(json),
      html_url_opt = html_url_opt(json),
      number_opt = number_opt(json),
      state_opt = state_opt(json),
      title_opt = title_opt(json),
      body_opt = body_opt(json),
      user_opt = user_opt(json) map User.apply,
      labels = for {
        xs <- labels_opt(json).toSeq
        x <- xs
      } yield Label(x),
      assignee_opt = assignee_opt(json) map User.apply,
      milestone_opt = milestone_opt(json) map Milestone.apply,
      comments_opt = comments_opt(json),
      pull_request_opt = pull_request_opt(json) flatMap PullRequest.opt,
      closed_at_opt = closed_at_opt(json),
      created_at_opt = created_at_opt(json),
      updated_at_opt = updated_at_opt(json)
    )

  val html_url_opt = 'html_url.?[String]
  val number_opt = 'number.?[BigInt]
  val state_opt = 'state.?[IssueState]
  val title_opt = 'title.?[String]
  val body_opt = 'body.?[String]
  val user_opt = 'user.?[JObject]
  val labels_opt = 'labels.?[List[JValue]]
  val assignee_opt = 'assignee.?[JObject]
  val milestone_opt = 'milestone.?[JObject]
  val comments_opt = 'comments.?[BigInt]
  val pull_request_opt = 'pull_request.?[JObject]
  val closed_at_opt = 'closed_at.?[Calendar]
}

case class Issue(url: String,
  html_url_opt: Option[String],
  number_opt: Option[BigInt],
  state_opt: Option[IssueState],
  title_opt: Option[String],
  body_opt: Option[String],
  user_opt: Option[User],
  labels: Seq[Label],
  assignee_opt: Option[User],
  milestone_opt: Option[Milestone],
  comments_opt: Option[BigInt],
  pull_request_opt: Option[PullRequest],
  closed_at_opt: Option[Calendar],
  created_at_opt: Option[Calendar],
  updated_at_opt: Option[Calendar])

object IssueState {
  def open: IssueState = Open
  def closed: IssueState = Closed
  def all: IssueState = All

  case object Open extends IssueState {
    override def toString: String = "open"
  }
  case object Closed extends IssueState {
    override def toString: String = "closed"
  }
  case object All extends IssueState {
    override def toString: String = "all"
  }

  implicit val stateRead: ReadJs[IssueState] = ReadJs.readJs {
    case JString("open")   => Open
    case JString("closed") => Closed
    case JString("all")    => All
  }
  implicit val stateShow: Show[IssueState] = Show.showA
}

sealed trait IssueState {}

object Label extends Parse with CommonField {
  def apply(json: JValue): Label =
    Label(url = url(json),
      name = name(json),
      color = color(json))

  val color = 'color.![String]
}

case class Label(url: String,
  name: String,
  color: String)

object Milestone extends Parse with CommonField {
  def apply(json: JValue): Milestone =
    Milestone(url = url(json),
      number = number(json),
      state = state(json),
      title = title(json),
      description_opt = description_opt(json))

  val number = 'number.![BigInt]
  val state = 'state.![String]
  val title = 'title.![String]
}

case class Milestone(url: String,
  number: BigInt,
  state: String,
  title: String,
  description_opt: Option[String])

object PullRequest extends Parse with CommonField {
  def opt(json: JValue): Option[PullRequest] =
    (for {
      JObject(fs) <- json
      JField("url", JString(_)) <- fs.toList
    } yield apply(json)).headOption

  def apply(json: JValue): PullRequest =
    PullRequest(url = url(json),
      html_url = html_url(json),
      diff_url = diff_url(json),
      patch_url = patch_url(json))

  val diff_url = 'diff_url.![String]
  val patch_url = 'patch_url.![String]
}

case class PullRequest(url: String,
  html_url: String,
  diff_url: String,
  patch_url: String)

object User extends Parse with CommonField {
  def apply(json: JValue): User =
    User(url = url(json),
      login = login(json),
      id = id(json),
      html_url_opt = html_url_opt(json),
      avatar_url_opt = avatar_url_opt(json),
      gravatar_id_opt = gravatar_id_opt(json),
      type_opt = type_opt(json),
      site_admin_opt = site_admin_opt(json),
      name_opt = name_opt(json),
      email_opt = email_opt(json)
    )

  val login = 'login.![String]
  val html_url_opt = 'html_url.?[String]
  val avatar_url_opt = 'avatar_url.?[String]
  val gravatar_id_opt = 'gravatar_id.?[String]
  val site_admin_opt = 'site_admin.?[Boolean]
  val name_opt = 'name.?[String]
  val email_opt = 'email.?[String]
}

case class User(url: String,
  login: String,
  id: BigInt,
  html_url_opt: Option[String],
  avatar_url_opt: Option[String],
  gravatar_id_opt: Option[String],
  type_opt: Option[String],
  site_admin_opt: Option[Boolean],
  name_opt: Option[String],
  email_opt: Option[String])

object BlobRef extends Parse with CommonField {
  def apply(json: JValue): BlobRef =
    BlobRef(sha = sha(json),
      url = url(json),
      name = name(json),
      path = path(json),
      git_url = git_url(json),
      html_url = html_url(json),
      repository = Repo(repository(json))
      )

  val repository = 'repository.![JObject]
}

case class BlobRef(sha: String,
  url: String,
  name: String,
  path: String,
  git_url: String,
  html_url: String,
  repository: Repo)

object TextMatches extends Parse {
  def apply(json: JValue): TextMatches =
    TextMatches(text_matches(json) map TextMatch.apply)

  val text_matches = 'text_matches.![List[JValue]]
}

case class TextMatches(text_matches: Seq[TextMatch])

object TextMatch extends Parse {
  def apply(json: JValue): TextMatch =
    TextMatch(object_url(json),
      object_type(json),
      property(json),
      fragment(json),
      matches(json) map SearchTerm.apply)

  val object_url = 'object_url.![String]
  val object_type = 'object_type.![String]
  val property = 'property.![String]
  val fragment = 'fragment.![String]
  val matches = 'matches.![List[JValue]]
}

case class TextMatch(object_url: String,
  object_type: String,
  property: String,
  fragment: String,
  matches: Seq[SearchTerm])

object SearchTerm extends Parse {
  def apply(json: JValue): SearchTerm =
    SearchTerm(text = text(json),
      indices = indices(json))

  val text = 'text.![String]
  val indices = 'indices.![List[BigInt]]
}

case class SearchTerm(text: String,
  indices: Seq[BigInt])

/** represents pagination.
 */
case class Paged[A](items: Seq[A],
    links: Map[String, String],
    total_count_opt: Option[BigInt],
    incomplete_results_opt: Option[Boolean]) {
  def next_page: Option[String] = links.get("next")
  def last_page: Option[String] = links.get("last")
  def first_page: Option[String] = links.get("first")
  def prev_page: Option[String] = links.get("prev")
}

object Paged extends Parse {
  implicit def pageToSeq[A](paged: Paged[A]): Seq[A] = paged.items
  val items = 'items.![List[JValue]]
  val total_count_opt = 'total_count.?[BigInt]
  val incomplete_results_opt = 'incomplete_results.?[Boolean]

  def parseArray[A](f: JValue => A): Response => Paged[A] = { (res: Response) =>
    val json = as.json4s.Json(res)
    val links = linkHeader(res)
    Paged(
      for {
        JArray(array) <- json
        v <- array
      } yield f(v),
      links,
      None,
      None)
  }

  def parseSearchResult[A](f: JValue => A): Response => Paged[A] = { (res: Response) =>
    val json = as.json4s.Json(res)
    val links = linkHeader(res)
    val xs = items(json).toSeq map f
    Paged(xs, links, total_count_opt(json), incomplete_results_opt(json)) 
  }

  def linkHeader(res: Response): Map[String, String] =
    Map((Option(res.getHeader("Link")) match {
      case Some(s) =>
        s.split(",").toList flatMap { x => x.split(";").toList match {
          case v :: k :: Nil =>
            Some(k.trim.replaceAllLiterally("rel=", "").replaceAllLiterally("\"", "") ->
              v.trim.replaceAllLiterally(">", "").replaceAllLiterally("<", ""))
          case _ => None
        }}
      case None => Nil
    }): _*)
}

trait CommonField { self: Parse =>
  val id = 'id.![BigInt]
  val sha = 'sha.![String]
  val url = 'url.![String]
  val ref = 'ref.![String]

  val path = 'path.![String]
  val mode = 'mode.![String]
  val `type` = 'type.![String]
  val type_opt = 'type.?[String]
  val size = 'size.![BigInt]
  val size_opt = 'size.?[BigInt]
  val message = 'message.![String]
  val name = 'name.![String]
  val email = 'email.![String]
  val date = 'date.![Calendar]
  val created_at = 'created_at.![Calendar]
  val created_at_opt = 'created_at.?[Calendar]
  val updated_at = 'updated_at.![Calendar]
  val updated_at_opt = 'updated_at.?[Calendar]
  val encoding = 'encoding.![String]
  val content = 'content.![String]
  val git_object = 'object.![JObject]
  val git_url     = 'git_url.![String]
  val html_url = 'html_url.![String]

  // description can always be null
  val description_opt = 'description.?[String]
}

trait Parse {
  def parse[A: ReadJs](js: JValue): Option[A] =
    implicitly[ReadJs[A]].readJs.lift(js)
  def parse_![A: ReadJs](js: JValue): A = parse(js).get
  def parseField[A: ReadJs](key: String)(js: JValue): Option[A] = parse[A](js \ key)
  def parseField_![A: ReadJs](key: String)(js: JValue): A =
    parseField(key)(js) getOrElse sys.error(s"Key $key was not found in ${js.toString}")
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
