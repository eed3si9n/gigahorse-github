import org.specs2._
import org.specs2.matcher._
import dispatch._
import github._
import dispatch.liftjson.Js._

class GithubSpec extends Specification { def is =
  "This is a specification to check the github handler"                       ^
                                                                              p^
  "`trees(:sha)` should"                                                      ^
    "contain trees"                                                           ! trees1^
                                                                              end
  
  def trees1: MatchResult[Any] = {
    val http = new Http
    val trees = http(Client(Repos("dispatch", "dispatch").trees("563c7dcea4bbb71e49313e92c01337a0a4b7ce72")) ># (
      Trees.fromJson ))
    trees(0).path must_== ".gitignore"
  }
}
