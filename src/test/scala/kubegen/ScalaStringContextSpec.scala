package kubegen

import org.specs2.mutable.Specification

import ScalaStringContext.ScalaStringContextImplicit
import ScalaCode._
import ScalaCode.syntax._
import kubegen.testing.ScalaCodeMatchers._

class ScalaStringContextSpec extends Specification {
  "Creation" in {
    "hello".lit.toLiteral must_== "hello"
    "type".id.toLiteral   must_== "`type`"

    scala"".toLiteral     must_== ""
  }

  "ScalaCodes can be composed" in {
    val className = "MyClass"

    val fields = Map(
      "id"   -> "String",
      "type" -> "String"
    )

    val fieldsScala = fields.map { case (fieldName, fieldType) =>
      scala"${fieldName.id}: ${fieldType.id}"
    }.mkScala(",\n".lit)

    val composedCode = scala"""
      case class ${className.id} {
        $fieldsScala
      }"""
    
    composedCode must beSameCodeAs("""
      case class MyClass {
        id: String,
        `type`: String
      }
    """)
  }

  "Can define imports" in {
    val varDecl = scala"val user: Person".withImport("io.models.Person")

    val code = scala"""
      package mypackage

      $importsAnchor

      $varDecl
    """

    code must beSameCodeAs("Hello world!")
  }
}
