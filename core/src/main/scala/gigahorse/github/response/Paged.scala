package gigahorse.github
package response

import gigahorse.Response
import scala.json.ast.unsafe._

/** represents pagination.
 */
case class Paged[A](items: Vector[A],
    links: Map[String, String],
    total_count_opt: Option[Long],
    incomplete_results_opt: Option[Boolean]) {
  def next_page: Option[String] = links.get("next")
  def last_page: Option[String] = links.get("last")
  def first_page: Option[String] = links.get("first")
  def prev_page: Option[String] = links.get("prev")
}

object Paged {
  implicit def pageToSeq[A](paged: Paged[A]): Vector[A] = paged.items
  // val items = 'items.![List[JValue]]
  // val total_count_opt = 'total_count.?[BigInt]
  // val incomplete_results_opt = 'incomplete_results.?[Boolean]

  def parseArray[A](f: JValue => A): Response => Paged[A] =
    (res: Response) => {
      val json = Github.asJson(res)
      val links = linkHeader(res)
      val ary = json match {
        case JArray(ary) => ary
        case _           => sys.error(s"JArray expected but found: $json")
      }
      Paged(ary.toVector map f, links, None, None)
    }

  def parseSearchResult[A](f: JValue => A): Response => Paged[A] = 
    (res: Response) => {
      val json = Github.asJson(res)
      val links = linkHeader(res)
      val fields = json match {
        case JObject(fields) => fields.toVector
        case _               => sys.error(s"JObject expected but found: $json")
      }
      val items: Vector[JValue] = (for {
        JField("items", v) <- fields
      } yield (v match {
        case JArray(ary) => ary.toVector
        case _           => sys.error(s"JArray expected but found: $v")
      })).flatten
      val xs = items map f
      val total_count: Option[Long] = (for {
        JField("total_count", v) <- fields
      } yield (v match {
        case JNumber(num) => num.toLong
        case _            => sys.error(s"JNumber expected but found: $v")
      })).headOption
      val incomplete_results: Option[Boolean] = (for {
        JField("incomplete_results", v) <- fields
      } yield (v match {
        case JBoolean(b) => b
        case _           => sys.error(s"JBoolean expected but found: $v")
      })).headOption
      Paged(xs, links, total_count, incomplete_results)
    }

  def linkHeader(res: Response): Map[String, String] =
    Map((res.header("Link") match {
      case Some(s) =>
        s.split(",").toList flatMap { x => x.split(";").toList match {
          case v :: k :: Nil =>
            Some(k.trim.replaceAllLiterally("rel=", "").replaceAllLiterally("\"", "") ->
              v.trim.replaceAllLiterally(">", "").replaceAllLiterally("<", ""))
          case _ => None
        }}
      case None => Nil
    }): _*)
}
