package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait VerticalAlign extends Product with Serializable
object VerticalAlign {
  case object Bottom extends VerticalAlign
  case object Middle extends VerticalAlign
  case object Top extends VerticalAlign

  def schema: Dsl[VerticalAlign] = oneOf(
    "bottom".as(Bottom) ::
    "middle".as(Middle) ::
    "top".as(Top) ::
    HNil
  ).to[VerticalAlign]
}
