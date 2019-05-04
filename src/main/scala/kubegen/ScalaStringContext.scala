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
          literal(s) :: c :: combine(ss, cs)
        case (s::ss, Nil) =>
          literal(s) :: combine(ss, Nil)
        case (Nil, c::cs) =>
          c::cs
        case (Nil, Nil) =>
          Nil
      }

      flatten(combine(strings, codes))
    }
  }
}

case class ScalaCode(fragments: List[Fragment])

object ScalaCode {
  trait Fragment
  case class  Text(rendered: String, imports: Set[String]) extends Fragment 
  case object ImportsAnchor                                extends Fragment  

  def empty: ScalaCode =
    ScalaCode(Nil)
  
  def literal(s: String): ScalaCode =
    ScalaCode(List(Text(s, Set.empty)))
  
  def importsAnchor: ScalaCode =
    ScalaCode(List(ImportsAnchor))

  def flatten(codes: List[ScalaCode]): ScalaCode =
    ScalaCode(codes.flatMap(_.fragments))

  def concat(c1: ScalaCode, c2: ScalaCode): ScalaCode =
    ScalaCode(c1.fragments ++ c2.fragments)
  
  def toLiteral(code: ScalaCode): String = {
    val imports = code.fragments.foldLeft[Set[String]](Set.empty) { case (imports, fragment) =>
      fragment match {
        case Text(_, newImports) =>
          imports ++ newImports
        case ImportsAnchor =>
          imports
      }
    }

    code.fragments.foldLeft("") {
      case (codeSoFar, Text(text, _)) =>
        codeSoFar + text
      case (codeSoFar, ImportsAnchor) =>
        codeSoFar + "\n" + imports.mkString("\n")
    }
  }

  object syntax extends ScalaCodeSyntax
}

trait ScalaCodeSyntax {
  implicit class StringOps(s: String) {
    def id: ScalaCode =
      ScalaCode(List(Text(ident(s), Set.empty)))
    
    def id(`import`: String) =
      ScalaCode(List(Text(ident(s), Set(`import`))))

    def lit: ScalaCode =
      literal(s)
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