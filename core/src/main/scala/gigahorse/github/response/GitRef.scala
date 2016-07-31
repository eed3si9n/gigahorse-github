/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitRef(
  val url: String,
  val ref: String,
  val `object`: gigahorse.github.response.GitObject) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitRef => (this.url == x.url) && (this.ref == x.ref) && (this.`object` == x.`object`)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (17 + url.##) + ref.##) + `object`.##)
  }
  override def toString: String = {
    "GitRef(" + url + ", " + ref + ", " + `object` + ")"
  }
  private[this] def copy(url: String = url, ref: String = ref, `object`: gigahorse.github.response.GitObject = `object`): GitRef = {
    new GitRef(url, ref, `object`)
  }
  def withUrl(url: String): GitRef = {
    copy(url = url)
  }
  def withRef(ref: String): GitRef = {
    copy(ref = ref)
  }
  def withObject(`object`: gigahorse.github.response.GitObject): GitRef = {
    copy(`object` = `object`)
  }
}
object GitRef {
  def apply(url: String, ref: String, `object`: gigahorse.github.response.GitObject): GitRef = new GitRef(url, ref, `object`)
}
