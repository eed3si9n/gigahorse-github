/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Milestone(
  val url: String,
  val number: Long,
  val state: String,
  val title: String) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: Milestone => (this.url == x.url) && (this.number == x.number) && (this.state == x.state) && (this.title == x.title)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + url.##) + number.##) + state.##) + title.##)
  }
  override def toString: String = {
    "Milestone(" + url + ", " + number + ", " + state + ", " + title + ")"
  }
  private[this] def copy(url: String = url, number: Long = number, state: String = state, title: String = title): Milestone = {
    new Milestone(url, number, state, title)
  }
  def withUrl(url: String): Milestone = {
    copy(url = url)
  }
  def withNumber(number: Long): Milestone = {
    copy(number = number)
  }
  def withState(state: String): Milestone = {
    copy(state = state)
  }
  def withTitle(title: String): Milestone = {
    copy(title = title)
  }
}
object Milestone {
  def apply(url: String, number: Long, state: String, title: String): Milestone = new Milestone(url, number, state, title)
}
