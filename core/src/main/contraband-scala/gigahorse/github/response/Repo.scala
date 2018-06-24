/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Repo private (
  val url: String,
  val name: String,
  val id: Long,
  val owner: gigahorse.github.response.User,
  val full_name: String,
  val description: Option[String],
  val `private`: Boolean,
  val fork: Boolean,
  val html_url: Option[String],
  val clone_url: Option[String],
  val git_url: Option[String],
  val ssh_url: Option[String],
  val homepage: Option[String],
  val language: Option[String],
  val forks_count: Option[Long],
  val watchers_count: Option[Long],
  val size: Option[Long],
  val default_branch: Option[String],
  val open_issues_count: Option[Long],
  val pushed_at: Option[java.util.Calendar],
  val created_at: Option[java.util.Calendar],
  val updated_at: Option[java.util.Calendar]) extends Serializable {
  
  private def this(url: String, name: String, id: Long, owner: gigahorse.github.response.User, full_name: String, description: Option[String], `private`: Boolean, fork: Boolean) = this(url, name, id, owner, full_name, description, `private`, fork, None, None, None, None, None, None, None, None, None, None, None, None, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: Repo => (this.url == x.url) && (this.name == x.name) && (this.id == x.id) && (this.owner == x.owner) && (this.full_name == x.full_name) && (this.description == x.description) && (this.`private` == x.`private`) && (this.fork == x.fork) && (this.html_url == x.html_url) && (this.clone_url == x.clone_url) && (this.git_url == x.git_url) && (this.ssh_url == x.ssh_url) && (this.homepage == x.homepage) && (this.language == x.language) && (this.forks_count == x.forks_count) && (this.watchers_count == x.watchers_count) && (this.size == x.size) && (this.default_branch == x.default_branch) && (this.open_issues_count == x.open_issues_count) && (this.pushed_at == x.pushed_at) && (this.created_at == x.created_at) && (this.updated_at == x.updated_at)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.Repo".##) + url.##) + name.##) + id.##) + owner.##) + full_name.##) + description.##) + `private`.##) + fork.##) + html_url.##) + clone_url.##) + git_url.##) + ssh_url.##) + homepage.##) + language.##) + forks_count.##) + watchers_count.##) + size.##) + default_branch.##) + open_issues_count.##) + pushed_at.##) + created_at.##) + updated_at.##)
  }
  override def toString: String = {
    "Repo(" + url + ", " + name + ", " + id + ", " + owner + ", " + full_name + ", " + description + ", " + `private` + ", " + fork + ", " + html_url + ", " + clone_url + ", " + git_url + ", " + ssh_url + ", " + homepage + ", " + language + ", " + forks_count + ", " + watchers_count + ", " + size + ", " + default_branch + ", " + open_issues_count + ", " + pushed_at + ", " + created_at + ", " + updated_at + ")"
  }
  private[this] def copy(url: String = url, name: String = name, id: Long = id, owner: gigahorse.github.response.User = owner, full_name: String = full_name, description: Option[String] = description, `private`: Boolean = `private`, fork: Boolean = fork, html_url: Option[String] = html_url, clone_url: Option[String] = clone_url, git_url: Option[String] = git_url, ssh_url: Option[String] = ssh_url, homepage: Option[String] = homepage, language: Option[String] = language, forks_count: Option[Long] = forks_count, watchers_count: Option[Long] = watchers_count, size: Option[Long] = size, default_branch: Option[String] = default_branch, open_issues_count: Option[Long] = open_issues_count, pushed_at: Option[java.util.Calendar] = pushed_at, created_at: Option[java.util.Calendar] = created_at, updated_at: Option[java.util.Calendar] = updated_at): Repo = {
    new Repo(url, name, id, owner, full_name, description, `private`, fork, html_url, clone_url, git_url, ssh_url, homepage, language, forks_count, watchers_count, size, default_branch, open_issues_count, pushed_at, created_at, updated_at)
  }
  def withUrl(url: String): Repo = {
    copy(url = url)
  }
  def withName(name: String): Repo = {
    copy(name = name)
  }
  def withId(id: Long): Repo = {
    copy(id = id)
  }
  def withOwner(owner: gigahorse.github.response.User): Repo = {
    copy(owner = owner)
  }
  def withFull_name(full_name: String): Repo = {
    copy(full_name = full_name)
  }
  def withDescription(description: Option[String]): Repo = {
    copy(description = description)
  }
  def withDescription(description: String): Repo = {
    copy(description = Option(description))
  }
  def withPrivate(`private`: Boolean): Repo = {
    copy(`private` = `private`)
  }
  def withFork(fork: Boolean): Repo = {
    copy(fork = fork)
  }
  def withHtml_url(html_url: Option[String]): Repo = {
    copy(html_url = html_url)
  }
  def withHtml_url(html_url: String): Repo = {
    copy(html_url = Option(html_url))
  }
  def withClone_url(clone_url: Option[String]): Repo = {
    copy(clone_url = clone_url)
  }
  def withClone_url(clone_url: String): Repo = {
    copy(clone_url = Option(clone_url))
  }
  def withGit_url(git_url: Option[String]): Repo = {
    copy(git_url = git_url)
  }
  def withGit_url(git_url: String): Repo = {
    copy(git_url = Option(git_url))
  }
  def withSsh_url(ssh_url: Option[String]): Repo = {
    copy(ssh_url = ssh_url)
  }
  def withSsh_url(ssh_url: String): Repo = {
    copy(ssh_url = Option(ssh_url))
  }
  def withHomepage(homepage: Option[String]): Repo = {
    copy(homepage = homepage)
  }
  def withHomepage(homepage: String): Repo = {
    copy(homepage = Option(homepage))
  }
  def withLanguage(language: Option[String]): Repo = {
    copy(language = language)
  }
  def withLanguage(language: String): Repo = {
    copy(language = Option(language))
  }
  def withForks_count(forks_count: Option[Long]): Repo = {
    copy(forks_count = forks_count)
  }
  def withForks_count(forks_count: Long): Repo = {
    copy(forks_count = Option(forks_count))
  }
  def withWatchers_count(watchers_count: Option[Long]): Repo = {
    copy(watchers_count = watchers_count)
  }
  def withWatchers_count(watchers_count: Long): Repo = {
    copy(watchers_count = Option(watchers_count))
  }
  def withSize(size: Option[Long]): Repo = {
    copy(size = size)
  }
  def withSize(size: Long): Repo = {
    copy(size = Option(size))
  }
  def withDefault_branch(default_branch: Option[String]): Repo = {
    copy(default_branch = default_branch)
  }
  def withDefault_branch(default_branch: String): Repo = {
    copy(default_branch = Option(default_branch))
  }
  def withOpen_issues_count(open_issues_count: Option[Long]): Repo = {
    copy(open_issues_count = open_issues_count)
  }
  def withOpen_issues_count(open_issues_count: Long): Repo = {
    copy(open_issues_count = Option(open_issues_count))
  }
  def withPushed_at(pushed_at: Option[java.util.Calendar]): Repo = {
    copy(pushed_at = pushed_at)
  }
  def withPushed_at(pushed_at: java.util.Calendar): Repo = {
    copy(pushed_at = Option(pushed_at))
  }
  def withCreated_at(created_at: Option[java.util.Calendar]): Repo = {
    copy(created_at = created_at)
  }
  def withCreated_at(created_at: java.util.Calendar): Repo = {
    copy(created_at = Option(created_at))
  }
  def withUpdated_at(updated_at: Option[java.util.Calendar]): Repo = {
    copy(updated_at = updated_at)
  }
  def withUpdated_at(updated_at: java.util.Calendar): Repo = {
    copy(updated_at = Option(updated_at))
  }
}
object Repo {
  
  def apply(url: String, name: String, id: Long, owner: gigahorse.github.response.User, full_name: String, description: Option[String], `private`: Boolean, fork: Boolean): Repo = new Repo(url, name, id, owner, full_name, description, `private`, fork)
  def apply(url: String, name: String, id: Long, owner: gigahorse.github.response.User, full_name: String, description: String, `private`: Boolean, fork: Boolean): Repo = new Repo(url, name, id, owner, full_name, Option(description), `private`, fork)
  def apply(url: String, name: String, id: Long, owner: gigahorse.github.response.User, full_name: String, description: Option[String], `private`: Boolean, fork: Boolean, html_url: Option[String], clone_url: Option[String], git_url: Option[String], ssh_url: Option[String], homepage: Option[String], language: Option[String], forks_count: Option[Long], watchers_count: Option[Long], size: Option[Long], default_branch: Option[String], open_issues_count: Option[Long], pushed_at: Option[java.util.Calendar], created_at: Option[java.util.Calendar], updated_at: Option[java.util.Calendar]): Repo = new Repo(url, name, id, owner, full_name, description, `private`, fork, html_url, clone_url, git_url, ssh_url, homepage, language, forks_count, watchers_count, size, default_branch, open_issues_count, pushed_at, created_at, updated_at)
  def apply(url: String, name: String, id: Long, owner: gigahorse.github.response.User, full_name: String, description: String, `private`: Boolean, fork: Boolean, html_url: String, clone_url: String, git_url: String, ssh_url: String, homepage: String, language: String, forks_count: Long, watchers_count: Long, size: Long, default_branch: String, open_issues_count: Long, pushed_at: java.util.Calendar, created_at: java.util.Calendar, updated_at: java.util.Calendar): Repo = new Repo(url, name, id, owner, full_name, Option(description), `private`, fork, Option(html_url), Option(clone_url), Option(git_url), Option(ssh_url), Option(homepage), Option(language), Option(forks_count), Option(watchers_count), Option(size), Option(default_branch), Option(open_issues_count), Option(pushed_at), Option(created_at), Option(updated_at))
}
