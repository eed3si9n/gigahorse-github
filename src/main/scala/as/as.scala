package dispatch.as.github

import dispatch._
import net.liftweb.json.{JsonParser, JValue}

object Json extends (Res => JValue) {
  def apply(r: Res) = (dispatch.as.String andThen JsonParser.parse)(r)
}

object Repo extends (Res => github.Repo) {
  def apply(r: Res) = (Json andThen github.Repo.fromJson)(r)
}

object GitRef extends (Res => github.GitRef) {
  def apply(r: Res) = (Json andThen github.GitRef.fromJson)(r)
}

object GitRefs extends (Res => Seq[github.GitRef]) {
  def apply(r: Res) = (Json andThen github.GitRefs.fromJson)(r)
}

object GitCommit extends (Res => github.GitCommit) {
  def apply(r: Res) = (Json andThen github.GitCommit.fromJson)(r)
}

object GitTrees extends (Res => Seq[github.GitTree]) {
  def apply(r: Res) = (Json andThen github.GitTrees.fromJson)(r)
}

object GitBlob extends (Res => github.GitBlob) {
  def apply(r: Res) = (Json andThen github.GitBlob.fromJson)(r)
}
