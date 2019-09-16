package openapigen.util

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

  def filterImports(filter: String => Boolean): ScalaCode =
    copy(imports = imports.filter(filter))
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

  def toLiteral(code: ScalaCode): String = {

    // TODO: Refactor and document this function.
    def beautify(code: String): String = {
      val lines = code.split("\n").toList

      val firstNonWhitespaceLine = lines.find(_.matches(""".*\S+.*"""))

      def numLeadingSpaces: List[Char] => Int = {
        case ' '::rest =>
          1 + numLeadingSpaces(rest)
        case _ =>
          0
      }

      val firstLineLeadingSpaces = firstNonWhitespaceLine.fold(ifEmpty = 0) {l =>
        numLeadingSpaces(l.toList)
      }

      def deIndentLine(l: String, deIndentSize: Int): String = {
        def deIndent(l: List[Char], deIndentSize: Int): List[Char] = (l, deIndentSize) match {
          case (_, 0)         => l
          case (Nil, _)       => Nil
          case (' '::rest, _) => deIndent(rest, deIndentSize - 1)
          case (_, _)         => l
        }

        deIndent(l.toList, deIndentSize).mkString
      }

      lines.map(
        deIndentLine(_, firstLineLeadingSpaces)
      )
        .mkString("\n")
        .trim
    }

    val rawString = code.fragments.foldLeft("") { case (codeSoFar, fragment) => fragment match {
      case Text(text) =>
        codeSoFar + text

      case ImportsAnchor =>
        val importStatements = code.imports.toList.sorted.map { i =>
          s"import ${sanitiseFqn(i)}"
        }.mkString("\n")

        codeSoFar + importStatements
    }}

    beautify(rawString)
  }

  object syntax extends ScalaCodeSyntax
}

trait ScalaCodeSyntax {
  implicit class StringOps(s: String) {
    def lit: ScalaCode =
      literal(s)

    def id: ScalaCode =
      literal(ident(s))

    def fqn: ScalaCode =
      literal(sanitiseFqn(s))
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