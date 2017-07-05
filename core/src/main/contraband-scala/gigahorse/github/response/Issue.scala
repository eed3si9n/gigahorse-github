/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
final class Issue private (
  val url: String,
  val html_url: Option[String],
  val number: Option[Long],
  val state: Option[gigahorse.github.response.IssueState],
  val title: Option[String],
  val body: Option[String],
  val user: Option[gigahorse.github.response.User],
  val labels: Vector[gigahorse.github.response.Label],
  val assignee: Option[gigahorse.github.response.User],
  val Milestone: Option[gigahorse.github.response.Milestone],
  val comments: Option[Long],
  val pull_request: Option[gigahorse.github.response.PullRequest],
  val closed_at: Option[java.util.Calendar],
  val created_at: Option[java.util.Calendar],
  val updated_at: Option[java.util.Calendar]) extends Serializable {
  
  private def this(url: String) = this(url, None, None, None, None, None, None, Vector(), None, None, None, None, None, None, None)
  
  override def equals(o: Any): Boolean = o match {
    case x: Issue => (this.url == x.url) && (this.html_url == x.html_url) && (this.number == x.number) && (this.state == x.state) && (this.title == x.title) && (this.body == x.body) && (this.user == x.user) && (this.labels == x.labels) && (this.assignee == x.assignee) && (this.Milestone == x.Milestone) && (this.comments == x.comments) && (this.pull_request == x.pull_request) && (this.closed_at == x.closed_at) && (this.created_at == x.created_at) && (this.updated_at == x.updated_at)
    case _ => false
  }
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "Issue".##) + url.##) + html_url.##) + number.##) + state.##) + title.##) + body.##) + user.##) + labels.##) + assignee.##) + Milestone.##) + comments.##) + pull_request.##) + closed_at.##) + created_at.##) + updated_at.##)
  }
  override def toString: String = {
    "Issue(" + url + ", " + html_url + ", " + number + ", " + state + ", " + title + ", " + body + ", " + user + ", " + labels + ", " + assignee + ", " + Milestone + ", " + comments + ", " + pull_request + ", " + closed_at + ", " + created_at + ", " + updated_at + ")"
  }
  protected[this] def copy(url: String = url, html_url: Option[String] = html_url, number: Option[Long] = number, state: Option[gigahorse.github.response.IssueState] = state, title: Option[String] = title, body: Option[String] = body, user: Option[gigahorse.github.response.User] = user, labels: Vector[gigahorse.github.response.Label] = labels, assignee: Option[gigahorse.github.response.User] = assignee, Milestone: Option[gigahorse.github.response.Milestone] = Milestone, comments: Option[Long] = comments, pull_request: Option[gigahorse.github.response.PullRequest] = pull_request, closed_at: Option[java.util.Calendar] = closed_at, created_at: Option[java.util.Calendar] = created_at, updated_at: Option[java.util.Calendar] = updated_at): Issue = {
    new Issue(url, html_url, number, state, title, body, user, labels, assignee, Milestone, comments, pull_request, closed_at, created_at, updated_at)
  }
  def withUrl(url: String): Issue = {
    copy(url = url)
  }
  def withHtml_url(html_url: Option[String]): Issue = {
    copy(html_url = html_url)
  }
  def withHtml_url(html_url: String): Issue = {
    copy(html_url = Option(html_url))
  }
  def withNumber(number: Option[Long]): Issue = {
    copy(number = number)
  }
  def withNumber(number: Long): Issue = {
    copy(number = Option(number))
  }
  def withState(state: Option[gigahorse.github.response.IssueState]): Issue = {
    copy(state = state)
  }
  def withState(state: gigahorse.github.response.IssueState): Issue = {
    copy(state = Option(state))
  }
  def withTitle(title: Option[String]): Issue = {
    copy(title = title)
  }
  def withTitle(title: String): Issue = {
    copy(title = Option(title))
  }
  def withBody(body: Option[String]): Issue = {
    copy(body = body)
  }
  def withBody(body: String): Issue = {
    copy(body = Option(body))
  }
  def withUser(user: Option[gigahorse.github.response.User]): Issue = {
    copy(user = user)
  }
  def withUser(user: gigahorse.github.response.User): Issue = {
    copy(user = Option(user))
  }
  def withLabels(labels: Vector[gigahorse.github.response.Label]): Issue = {
    copy(labels = labels)
  }
  def withAssignee(assignee: Option[gigahorse.github.response.User]): Issue = {
    copy(assignee = assignee)
  }
  def withAssignee(assignee: gigahorse.github.response.User): Issue = {
    copy(assignee = Option(assignee))
  }
  def withMilestone(Milestone: Option[gigahorse.github.response.Milestone]): Issue = {
    copy(Milestone = Milestone)
  }
  def withMilestone(Milestone: gigahorse.github.response.Milestone): Issue = {
    copy(Milestone = Option(Milestone))
  }
  def withComments(comments: Option[Long]): Issue = {
    copy(comments = comments)
  }
  def withComments(comments: Long): Issue = {
    copy(comments = Option(comments))
  }
  def withPull_request(pull_request: Option[gigahorse.github.response.PullRequest]): Issue = {
    copy(pull_request = pull_request)
  }
  def withPull_request(pull_request: gigahorse.github.response.PullRequest): Issue = {
    copy(pull_request = Option(pull_request))
  }
  def withClosed_at(closed_at: Option[java.util.Calendar]): Issue = {
    copy(closed_at = closed_at)
  }
  def withClosed_at(closed_at: java.util.Calendar): Issue = {
    copy(closed_at = Option(closed_at))
  }
  def withCreated_at(created_at: Option[java.util.Calendar]): Issue = {
    copy(created_at = created_at)
  }
  def withCreated_at(created_at: java.util.Calendar): Issue = {
    copy(created_at = Option(created_at))
  }
  def withUpdated_at(updated_at: Option[java.util.Calendar]): Issue = {
    copy(updated_at = updated_at)
  }
  def withUpdated_at(updated_at: java.util.Calendar): Issue = {
    copy(updated_at = Option(updated_at))
  }
}
object Issue {
  
  def apply(url: String): Issue = new Issue(url, None, None, None, None, None, None, Vector(), None, None, None, None, None, None, None)
  def apply(url: String, html_url: Option[String], number: Option[Long], state: Option[gigahorse.github.response.IssueState], title: Option[String], body: Option[String], user: Option[gigahorse.github.response.User], labels: Vector[gigahorse.github.response.Label], assignee: Option[gigahorse.github.response.User], Milestone: Option[gigahorse.github.response.Milestone], comments: Option[Long], pull_request: Option[gigahorse.github.response.PullRequest], closed_at: Option[java.util.Calendar], created_at: Option[java.util.Calendar], updated_at: Option[java.util.Calendar]): Issue = new Issue(url, html_url, number, state, title, body, user, labels, assignee, Milestone, comments, pull_request, closed_at, created_at, updated_at)
  def apply(url: String, html_url: String, number: Long, state: gigahorse.github.response.IssueState, title: String, body: String, user: gigahorse.github.response.User, labels: Vector[gigahorse.github.response.Label], assignee: gigahorse.github.response.User, Milestone: gigahorse.github.response.Milestone, comments: Long, pull_request: gigahorse.github.response.PullRequest, closed_at: java.util.Calendar, created_at: java.util.Calendar, updated_at: java.util.Calendar): Issue = new Issue(url, Option(html_url), Option(number), Option(state), Option(title), Option(body), Option(user), labels, Option(assignee), Option(Milestone), Option(comments), Option(pull_request), Option(closed_at), Option(created_at), Option(updated_at))
}
