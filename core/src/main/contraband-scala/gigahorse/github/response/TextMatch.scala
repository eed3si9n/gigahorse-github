/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class TextMatch private (
  val object_url: String,
  val object_type: String,
  val property: String,
  val fragment: String,
  val matches: Vector[gigahorse.github.response.SearchTerm]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: TextMatch => (this.object_url == x.object_url) && (this.object_type == x.object_type) && (this.property == x.property) && (this.fragment == x.fragment) && (this.matches == x.matches)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (17 + "gigahorse.github.response.TextMatch".##) + object_url.##) + object_type.##) + property.##) + fragment.##) + matches.##)
  }
  override def toString: String = {
    "TextMatch(" + object_url + ", " + object_type + ", " + property + ", " + fragment + ", " + matches + ")"
  }
  private[this] def copy(object_url: String = object_url, object_type: String = object_type, property: String = property, fragment: String = fragment, matches: Vector[gigahorse.github.response.SearchTerm] = matches): TextMatch = {
    new TextMatch(object_url, object_type, property, fragment, matches)
  }
  def withObject_url(object_url: String): TextMatch = {
    copy(object_url = object_url)
  }
  def withObject_type(object_type: String): TextMatch = {
    copy(object_type = object_type)
  }
  def withProperty(property: String): TextMatch = {
    copy(property = property)
  }
  def withFragment(fragment: String): TextMatch = {
    copy(fragment = fragment)
  }
  def withMatches(matches: Vector[gigahorse.github.response.SearchTerm]): TextMatch = {
    copy(matches = matches)
  }
}
object TextMatch {
  
  def apply(object_url: String, object_type: String, property: String, fragment: String, matches: Vector[gigahorse.github.response.SearchTerm]): TextMatch = new TextMatch(object_url, object_type, property, fragment, matches)
}
