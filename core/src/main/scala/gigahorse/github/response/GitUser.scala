/**
 * This code is generated using sbt-datatype.
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class GitUser(
  val name: String,
  val email: String,
  val date: java.util.Calendar) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = o match {
    case x: GitUser => (this.name == x.name) && (this.email == x.email) && (this.date == x.date)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (17 + name.##) + email.##) + date.##)
  }
  override def toString: String = {
    "GitUser(" + name + ", " + email + ", " + date + ")"
  }
  private[this] def copy(name: String = name, email: String = email, date: java.util.Calendar = date): GitUser = {
    new GitUser(name, email, date)
  }
  def withName(name: String): GitUser = {
    copy(name = name)
  }
  def withEmail(email: String): GitUser = {
    copy(email = email)
  }
  def withDate(date: java.util.Calendar): GitUser = {
    copy(date = date)
  }
}
object GitUser {
  def apply(name: String, email: String, date: java.util.Calendar): GitUser = new GitUser(name, email, date)
}
