package kubegen

import Scala.{ident, sanitiseFqn}
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

case class ScalaCode(fragments: List[Fragment], imports: Set[String]) {
  def withImport(`import`: String): ScalaCode =
    withImports(`import`)

  def withImports(newImports: String*): ScalaCode =
    copy(imports = imports ++ newImports)
}

object ScalaCode {
  trait Fragment
  case class  Text(rendered: String) extends Fragment 
  case object ImportsAnchor          extends Fragment  

  def empty: ScalaCode =
    ScalaCode(Nil, Set.empty)
  
  def literal(s: String): ScalaCode =
    ScalaCode(List(Text(s)), Set.empty)
  
  def importsAnchor: ScalaCode =
    ScalaCode(List(ImportsAnchor), Set.empty)

  def flatten(codes: List[ScalaCode]): ScalaCode =
    ScalaCode(codes.flatMap(_.fragments), codes.flatMap(_.imports).toSet)

  def concat(c1: ScalaCode, c2: ScalaCode): ScalaCode =
    ScalaCode(c1.fragments ++ c2.fragments, c1.imports ++ c2.imports)
  
  def toLiteral(code: ScalaCode): String =
    code.fragments.foldLeft("") { case (codeSoFar, fragment) => fragment match {
      case Text(text) =>
        codeSoFar + text
      case ImportsAnchor =>
        val importStatements = code.imports.map { i =>
          s"import ${sanitiseFqn(i)}"
        }.mkString("\n")

        codeSoFar + importStatements
    }}

  object syntax extends ScalaCodeSyntax
}

trait ScalaCodeSyntax {
  implicit class StringOps(s: String) {
    def id: ScalaCode =
      literal(ident(s))

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