/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class TextMatches private (
  val text_matches: Vector[gigahorse.github.response.TextMatch]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: TextMatches => (this.text_matches == x.text_matches)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (17 + "TextMatches".##) + text_matches.##)
  }
  override def toString: String = {
    "TextMatches(" + text_matches + ")"
  }
  protected[this] def copy(text_matches: Vector[gigahorse.github.response.TextMatch] = text_matches): TextMatches = {
    new TextMatches(text_matches)
  }
  def withText_matches(text_matches: Vector[gigahorse.github.response.TextMatch]): TextMatches = {
    copy(text_matches = text_matches)
  }
}
object TextMatches {
  
  def apply(text_matches: Vector[gigahorse.github.response.TextMatch]): TextMatches = new TextMatches(text_matches)
}
