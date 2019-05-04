package kubegen

import Scala.ident
import ScalaCode._

object ScalaStringContext {
  implicit class ScalaStringContextImplicit(val sc: StringContext) extends AnyVal {
    def scala(args: ScalaCode*): ScalaCode = {
      val strings = sc.parts.toList
      val codes   = args.toList

      def combine(strings: List[String], codes: List[ScalaCode]): List[ScalaCode] = (strings, codes) match {
        case (s::ss, c::cs) =>
          Literal(s) :: c :: combine(ss, cs)
        case (s::ss, Nil) =>
          Literal(s) :: combine(ss, Nil)
        case (Nil, c::cs) =>
          c::cs
        case (Nil, Nil) =>
          Nil
      }

      Composite(combine(strings, codes))
    }
  }
}

sealed trait ScalaCode

object ScalaCode {
  case class  Composite(fragments: List[ScalaCode]) extends ScalaCode
  case class  Literal(code: String)                 extends ScalaCode
  case class  Identifier(raw: String)               extends ScalaCode
  case object ImportsAnchor                         extends ScalaCode 

  def empty: ScalaCode =
    Composite(Nil)
  
  def importsAnchor: ScalaCode =
    ImportsAnchor

  def toLiteral(code: ScalaCode): String = code match {
    case Literal(s) =>
      s
    case Identifier(raw) =>
      ident(raw)
    case Composite(fragments) =>
      fragments.map(toLiteral).mkString
    case ImportsAnchor =>
      ???
  }

  def concat(c1: ScalaCode, c2: ScalaCode): ScalaCode =
    Composite(List(c1, c2))

  object syntax extends ScalaCodeSyntax
}

trait ScalaCodeSyntax {
  implicit class StringOps(s: String) {
    def id: Identifier =
      Identifier(s)
    
    def lit: Literal =
      Literal(s)
  }

  implicit class ScalaCodeOps(code: ScalaCode) {
    def toLiteral: String =
      ScalaCode.toLiteral(code)

    def +(other: ScalaCode): ScalaCode =
      concat(code, other)
  }

  implicit class IterableOps(it: Iterable[ScalaCode]) {
    def mkScala(separator: ScalaCode): ScalaCode =
     it.reduceOption(_ + separator + _).getOrElse(empty)

    def mkScala: ScalaCode =
      mkScala(empty)
  }
}