import org.specs2._
import org.specs2.matcher._
import dispatch._
import github._
import dispatch.liftjson.Js._

class GithubSpec extends Specification { def is =
  "This is a specification to check the github handler"                       ^
                                                                              p^
  "`trees(:sha)` should"                                                      ^
    "return trees"                                                            ! trees1^
                                                                              p^
  "`trees(:sha).recursive(10)` should"                                        ^
    "return trees recursively"                                                ! recursive1^                                                                                                     
                                                                              end
  
  def trees1: MatchResult[Any] = withHttp { http =>
    // this returns a seqence of Tree case class
    val trees = http(Client(Repos("dispatch", "dispatch").trees("563c7dcea4bbb71e49313e92c01337a0a4b7ce72")) ># (
      Trees.fromJson ))
    trees must have (_.path == ".gitignore")
  }
  
  def recursive1: MatchResult[Any] = withHttp { http =>
    // this returns a sequence of Tree case class
    val trees = http(Client(Repos("dispatch", "dispatch").trees("563c7dcea4bbb71e49313e92c01337a0a4b7ce72").recursive(10)) ># (
      Trees.fromJson ))
    trees must have (_.path == "twitter/src/main/scala/dispatch/Twitter.scala")
  }
  
  def withHttp[A](f: Http => A): A = {
    val http = new Http
    try {   
      f(http)
    } finally {
      http.shutdown
    }
  }
}
