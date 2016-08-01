/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class SearchTerm(
  val text: String,
  val indices: Vector[Int]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: SearchTerm => (this.text == x.text) && (this.indices == x.indices)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (17 + text.##) + indices.##)
  }
  override def toString: String = {
    "SearchTerm(" + text + ", " + indices + ")"
  }
  private[this] def copy(text: String = text, indices: Vector[Int] = indices): SearchTerm = {
    new SearchTerm(text, indices)
  }
  def withText(text: String): SearchTerm = {
    copy(text = text)
  }
  def withIndices(indices: Vector[Int]): SearchTerm = {
    copy(indices = indices)
  }
}
object SearchTerm {
  def apply(text: String, indices: Vector[Int]): SearchTerm = new SearchTerm(text, indices)
}
