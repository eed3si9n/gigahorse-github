/**
 * This code is generated using [[http://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package gigahorse.github.response
sealed abstract class IssueState extends Serializable
object IssueState {
  implicit val issueStateShow: gigahorse.github.Show[IssueState] = gigahorse.github.Show.showA[IssueState]
  
  case object open extends IssueState
  case object closed extends IssueState
  case object all extends IssueState
}
