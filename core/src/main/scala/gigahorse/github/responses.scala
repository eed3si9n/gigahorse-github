package gigahorse.github
package response

import scala.json.ast.unsafe.JValue
// import dispatch._
// import org.json4s._
// import java.util.{GregorianCalendar, Calendar, Locale}
// import com.ning.http.client.Response
// import collection.immutable.Map
// import repatch.github.request.Show

// object BlobRef extends Parse with CommonField {
//   def apply(json: JValue): BlobRef =
//     BlobRef(sha = sha(json),
//       url = url(json),
//       name = name(json),
//       path = path(json),
//       git_url = git_url(json),
//       html_url = html_url(json),
//       repository = Repo(repository(json))
//       )

//   val repository = 'repository.![JObject]
// }

// case class BlobRef(sha: String,
//   url: String,
//   name: String,
//   path: String,
//   git_url: String,
//   html_url: String,
//   repository: Repo)

// object TextMatches extends Parse {
//   def apply(json: JValue): TextMatches =
//     TextMatches(text_matches(json) map TextMatch.apply)

//   val text_matches = 'text_matches.![List[JValue]]
// }

// case class TextMatches(text_matches: Seq[TextMatch])

// object TextMatch extends Parse {
//   def apply(json: JValue): TextMatch =
//     TextMatch(object_url(json),
//       object_type(json),
//       property(json),
//       fragment(json),
//       matches(json) map SearchTerm.apply)

//   val object_url = 'object_url.![String]
//   val object_type = 'object_type.![String]
//   val property = 'property.![String]
//   val fragment = 'fragment.![String]
//   val matches = 'matches.![List[JValue]]
// }

// case class TextMatch(object_url: String,
//   object_type: String,
//   property: String,
//   fragment: String,
//   matches: Seq[SearchTerm])

// object SearchTerm extends Parse {
//   def apply(json: JValue): SearchTerm =
//     SearchTerm(text = text(json),
//       indices = indices(json))

//   val text = 'text.![String]
//   val indices = 'indices.![List[BigInt]]
// }

// case class SearchTerm(text: String,
//   indices: Seq[BigInt])

// trait CommonField { self: Parse =>
//   val id = 'id.![BigInt]
//   val sha = 'sha.![String]
//   val url = 'url.![String]
//   val ref = 'ref.![String]

//   val path = 'path.![String]
//   val mode = 'mode.![String]
//   val `type` = 'type.![String]
//   val type_opt = 'type.?[String]
//   val size = 'size.![BigInt]
//   val size_opt = 'size.?[BigInt]
//   val message = 'message.![String]
//   val name = 'name.![String]
//   val email = 'email.![String]
//   val date = 'date.![Calendar]
//   val created_at = 'created_at.![Calendar]
//   val created_at_opt = 'created_at.?[Calendar]
//   val updated_at = 'updated_at.![Calendar]
//   val updated_at_opt = 'updated_at.?[Calendar]
//   val encoding = 'encoding.![String]
//   val content = 'content.![String]
//   val git_object = 'object.![JObject]
//   val git_url     = 'git_url.![String]
//   val html_url = 'html_url.![String]

//   // description can always be null
//   val description_opt = 'description.?[String]
// }

// trait Parse {
//   def parse[A: ReadJs](js: JValue): Option[A] =
//     implicitly[ReadJs[A]].readJs.lift(js)
//   def parse_![A: ReadJs](js: JValue): A = parse(js).get
//   def parseField[A: ReadJs](key: String)(js: JValue): Option[A] = parse[A](js \ key)
//   def parseField_![A: ReadJs](key: String)(js: JValue): A =
//     parseField(key)(js) getOrElse sys.error(s"Key $key was not found in ${js.toString}")
//   implicit class SymOp(sym: Symbol) {
//     def ?[A: ReadJs](js: JValue): Option[A] = parseField[A](sym.name)(js)
//     def ?[A: ReadJs]: JValue => Option[A] = ?[A](_)
//     def ![A: ReadJs]: JValue => A = parseField_![A](sym.name)_
//   }
// }

// trait ReadJs[A] {
//   import ReadJs.=>?
//   val readJs: JValue =>? A
// }
// object ReadJs {
//   type =>?[-A, +B] = PartialFunction[A, B]
//   def readJs[A](pf: JValue =>? A): ReadJs[A] = new ReadJs[A] {
//     val readJs = pf
//   }
//   implicit val listRead: ReadJs[List[JValue]] = readJs { case JArray(v) => v }
//   implicit val objectRead: ReadJs[JObject]    = readJs { case JObject(v) => JObject(v) }
//   implicit val bigIntRead: ReadJs[BigInt]     = readJs { case JInt(v) => v }
//   implicit val intRead: ReadJs[Int]           = readJs { case JInt(v) => v.toInt }
//   implicit val stringRead: ReadJs[String]     = readJs { case JString(v) => v }
//   implicit val boolRead: ReadJs[Boolean]      = readJs { case JBool(v) => v }
//   implicit val calendarRead: ReadJs[Calendar] =
//     readJs { case JString(v) =>
//       // iso8601s
//       javax.xml.bind.DatatypeConverter.parseDateTime(v)
//     }
//   implicit def readJsListRead[A: ReadJs]: ReadJs[List[A]] = {
//     val f = implicitly[ReadJs[A]].readJs
//     readJs {
//       case JArray(xs) if xs forall {f.isDefinedAt} =>
//         xs map {f.apply}
//     }
//   }  
// }
