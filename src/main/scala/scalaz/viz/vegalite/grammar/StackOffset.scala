package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait StackOffset extends Product with Serializable
object StackOffset {
  case object Center extends StackOffset
  case object None extends StackOffset
  case object Normalize extends StackOffset
  case object Zero extends StackOffset

  def schema: Dsl[StackOffset] = oneOf(
    "center".as(Center) ::
    "none".as(None) ::
    "normalize".as(Normalize) ::
    "zero".as(Zero) ::
    HNil
  ).to[StackOffset]
}
