package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait AxisOrient extends Product with Serializable
object AxisOrient {
  case object Bottom extends AxisOrient
  case object Left extends AxisOrient
  case object Right extends AxisOrient
  case object Top extends AxisOrient

  def schema: Dsl[AxisOrient] =
    oneOf(
      "bottom".as(Bottom) ::
      "left".as(Left) ::
      "right".as(Right) ::
      "top".as(Top) ::
      HNil
    ).to[AxisOrient]
}
