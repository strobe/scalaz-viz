package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait AreaOverlay extends Product with Serializable
object AreaOverlay {
  case object Line extends AreaOverlay
  case object LinePoint extends AreaOverlay
  case object None extends AreaOverlay

  def schema: Dsl[AreaOverlay] = oneOf(
    "line".as(Line) ::
    "linepoint".as(LinePoint) ::
    "none".as(None) :: HNil
  ).to[AreaOverlay]
}