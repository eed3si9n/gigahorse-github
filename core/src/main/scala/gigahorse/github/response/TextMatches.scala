/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class TextMatches(
  val text_matches: Vector[gigahorse.github.response.TextMatch]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: TextMatches => (this.text_matches == x.text_matches)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (17 + text_matches.##)
  }
  override def toString: String = {
    "TextMatches(" + text_matches + ")"
  }
  private[this] def copy(text_matches: Vector[gigahorse.github.response.TextMatch] = text_matches): TextMatches = {
    new TextMatches(text_matches)
  }
  def withText_matches(text_matches: Vector[gigahorse.github.response.TextMatch]): TextMatches = {
    copy(text_matches = text_matches)
  }
}
object TextMatches {
  def apply(text_matches: Vector[gigahorse.github.response.TextMatch]): TextMatches = new TextMatches(text_matches)
}
