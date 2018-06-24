/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Label private (
  val url: String,
  val name: String,
  val color: String) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: Label => (this.url == x.url) && (this.name == x.name) && (this.color == x.color)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.Label".##) + url.##) + name.##) + color.##)
  }
  override def toString: String = {
    "Label(" + url + ", " + name + ", " + color + ")"
  }
  private[this] def copy(url: String = url, name: String = name, color: String = color): Label = {
    new Label(url, name, color)
  }
  def withUrl(url: String): Label = {
    copy(url = url)
  }
  def withName(name: String): Label = {
    copy(name = name)
  }
  def withColor(color: String): Label = {
    copy(color = color)
  }
}
object Label {
  
  def apply(url: String, name: String, color: String): Label = new Label(url, name, color)
}
