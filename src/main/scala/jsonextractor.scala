package dispatch.github

import dispatch._
import net.liftweb.json._

/** Json Extractor, extracts a value of type T from the given JsValue. */
trait Extract[T] {
  def unapply(js: JValue): Option[T]
  override def toString = getClass.getName
}

/** Namespace and context for Json extraction. Client extraction 
    objects, e.g. dispatch.twitter.Search, may extend this trait to 
    receive all implicit functions and values. */
trait Js {
  object str extends Extract[String] {
    def unapply(js: JValue) = js match {
      case JString(v) => Some(v)
      case _ => None
    }
  }
  object int extends Extract[BigInt] {
    def unapply(js: JValue) = js match {
      case JInt(v) => Some(v)
      case _ => None
    }
  }
  object bool extends Extract[Boolean] {
    def unapply(js: JValue) = js match {
      case JBool(v) => Some(v)
      case _ => None
    }
  }
  // keep in wrapped type to allow nested extractors
  object obj extends Extract[JObject] {
    def unapply(js: JValue) = js match {
      case JObject(v) => Some(JObject(v))
      case _ => None
    }
  }
  object ary extends Extract[List[JValue]] {
    def unapply(js: JValue) = js match {
      case JArray(v) => Some(v)
      case _ => None
    }
  }
  object iso8601datetime extends Extract[java.util.Calendar] {
    def unapply(js: JValue) = js match {
      case JString(v) =>
        Some(javax.xml.bind.DatatypeConverter.parseDateTime(v))
      case _ => None
    }
  }

  /** Add operators to Symbol. */
  implicit def sym_add_operators[T](sym: Symbol) = new SymOp(sym)

  /** For ! and ? on Symbol. */
  case class SymOp(sym: Symbol) {
    /** @return an extractor */
    def ? [T](cst: Extract[T]): JValue => Option[T] = { js =>
      cst.unapply(js \ sym.name)
    } 
    
    /** @return an assertion extracting function */
    def ! [T](cst: Extract[T]): JValue => T = { js =>
      cst.unapply(js \ sym.name).get
    }
  }
}

object Js extends Js {

}
