package kubegen

import org.specs2.mutable.Specification

import kubegen.ScalaStringContext.ScalaStringContextImplicit
import kubegen.ScalaCode._, syntax._
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
    val varType = "Person".id.withImport("io.my-app.models.Person")
    val varDecl = scala"val user: $varType"

    val code = scala"""
      package mypackage

      $importsAnchor

      $varDecl
    """.withImports(
      "other.import.foo",
      "other.import.bar"
    )

    code must beSameCodeAs("""
      |package mypackage
      |
      |import io.`my-app`.models.Person
      |import other.`import`.bar
      |import other.`import`.foo
      |
      |val user: Person
    """.stripMargin)
  }
}
