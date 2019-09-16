package openapigen.testing

import java.util.StringTokenizer

import org.specs2.matcher.Matcher
import org.specs2.matcher.MatchersImplicits._

import openapigen.util.ScalaCode
import openapigen.util.ScalaCode.toLiteral

object ScalaCodeMatchers {
  def beSameCodeAs(expectedCode: String): Matcher[ScalaCode] = (code: ScalaCode) => (
    tokenised(toLiteral(code)) == tokenised(expectedCode),
    s"""
      |===========
      |${toLiteral(code)}
      |===========
      |
      |is not the same code as:
      |
      |===========
      |$expectedCode
      |===========""".stripMargin
  )

  // TODO: Doesn't take into account newlines (which can be significant in scala) properly.
  private def tokenised(s: String): List[String] = {
    val delimiters = " \n()[]{},;:"  // TODO: Not adjusted for scala.

    def toList(t: StringTokenizer): List[String] =
      if(t.hasMoreTokens)
        t.nextToken :: toList(t)
      else
        Nil

    toList(new StringTokenizer(s, delimiters, true)).filter(_.trim.nonEmpty)
  }
}