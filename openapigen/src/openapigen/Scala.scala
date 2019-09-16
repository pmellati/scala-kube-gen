package openapigen

object Scala {
  /** Escape the input in back-ticks if it's not a valid scala identifier.
   *
   * Class, object, variable & method names are called identifiers.
   */
  def ident(s: String): String =
    if(isValidIdent(s)) s
    else                s"`$s`"

  /** Sanitise a fully qualified name. */
  def sanitiseFqn(s: String): String =
    s.split('.').toList.map(ident).mkString(".")

  /**
   * Class, object, variable & method names are called identifiers.
   */
  def isValidIdent(s: String): Boolean = {
    val validIdentChars = """[a-zA-Z_$][\w_$]+"""
    val alreadyEscaped  = """`[^`]+`"""

    s.matches(alreadyEscaped) || (
      s.matches(validIdentChars) && !isReservedWord(s)
    )
  }

  def isReservedWord: String => Boolean =
    ReservedWords.contains

  final val ReservedWords = List(
    "abstract",
    "case",
    "catch",
    "class",
    "def",
    "do",
    "else",
    "extends",
    "false",
    "final",
    "finally",
    "for",
    "forSome",
    "if",
    "implicit",
    "import",
    "lazy",
    "match",
    "new",
    "null",
    "object",
    "override",
    "package",
    "private",
    "protected",
    "return",
    "sealed",
    "super",
    "this",
    "throw",
    "trait",
    "try",
    "true",
    "type",
    "val",
    "var",
    "while",
    "with",
    "yield"
  )
}