package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait FontStyle extends Product with Serializable
object FontStyle {
  case object Italic extends FontStyle
  case object Normal extends FontStyle

  def schema: Dsl[FontStyle] = oneOf(
    "italic".as(Italic) ::
    "normal".as(Normal) ::
    HNil
  ).to[FontStyle]
}
