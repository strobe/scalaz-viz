package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait HorizontalAlign extends Product with Serializable
object HorizontalAlign {
  case object Center extends HorizontalAlign
  case object Left extends HorizontalAlign
  case object Right extends HorizontalAlign

  def schema: Dsl[HorizontalAlign] = oneOf(
    "center".as(Center) ::
    "left".as(Left) ::
    "right".as(Right) ::
    HNil
  ).to[HorizontalAlign]
}
