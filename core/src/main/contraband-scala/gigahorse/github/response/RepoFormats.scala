/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait RepoFormats { self: gigahorse.github.response.UserFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val RepoFormat: JsonFormat[gigahorse.github.response.Repo] = new JsonFormat[gigahorse.github.response.Repo] {
  override def read[J](jsOpt: Option[J], unbuilder: Unbuilder[J]): gigahorse.github.response.Repo = {
    jsOpt match {
      case Some(js) =>
      unbuilder.beginObject(js)
      val url = unbuilder.readField[String]("url")
      val name = unbuilder.readField[String]("name")
      val id = unbuilder.readField[Long]("id")
      val owner = unbuilder.readField[gigahorse.github.response.User]("owner")
      val full_name = unbuilder.readField[String]("full_name")
      val description = unbuilder.readField[Option[String]]("description")
      val `private` = unbuilder.readField[Boolean]("private")
      val fork = unbuilder.readField[Boolean]("fork")
      val html_url = unbuilder.readField[Option[String]]("html_url")
      val clone_url = unbuilder.readField[Option[String]]("clone_url")
      val git_url = unbuilder.readField[Option[String]]("git_url")
      val ssh_url = unbuilder.readField[Option[String]]("ssh_url")
      val homepage = unbuilder.readField[Option[String]]("homepage")
      val language = unbuilder.readField[Option[String]]("language")
      val forks_count = unbuilder.readField[Option[Long]]("forks_count")
      val watchers_count = unbuilder.readField[Option[Long]]("watchers_count")
      val size = unbuilder.readField[Option[Long]]("size")
      val default_branch = unbuilder.readField[Option[String]]("default_branch")
      val open_issues_count = unbuilder.readField[Option[Long]]("open_issues_count")
      val pushed_at = unbuilder.readField[Option[java.util.Calendar]]("pushed_at")
      val created_at = unbuilder.readField[Option[java.util.Calendar]]("created_at")
      val updated_at = unbuilder.readField[Option[java.util.Calendar]]("updated_at")
      unbuilder.endObject()
      gigahorse.github.response.Repo(url, name, id, owner, full_name, description, `private`, fork, html_url, clone_url, git_url, ssh_url, homepage, language, forks_count, watchers_count, size, default_branch, open_issues_count, pushed_at, created_at, updated_at)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: gigahorse.github.response.Repo, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("url", obj.url)
    builder.addField("name", obj.name)
    builder.addField("id", obj.id)
    builder.addField("owner", obj.owner)
    builder.addField("full_name", obj.full_name)
    builder.addField("description", obj.description)
    builder.addField("private", obj.`private`)
    builder.addField("fork", obj.fork)
    builder.addField("html_url", obj.html_url)
    builder.addField("clone_url", obj.clone_url)
    builder.addField("git_url", obj.git_url)
    builder.addField("ssh_url", obj.ssh_url)
    builder.addField("homepage", obj.homepage)
    builder.addField("language", obj.language)
    builder.addField("forks_count", obj.forks_count)
    builder.addField("watchers_count", obj.watchers_count)
    builder.addField("size", obj.size)
    builder.addField("default_branch", obj.default_branch)
    builder.addField("open_issues_count", obj.open_issues_count)
    builder.addField("pushed_at", obj.pushed_at)
    builder.addField("created_at", obj.created_at)
    builder.addField("updated_at", obj.updated_at)
    builder.endObject()
  }
}
}
