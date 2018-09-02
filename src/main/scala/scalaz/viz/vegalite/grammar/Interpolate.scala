package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Interpolate extends Product with Serializable
object Interpolate {
  case object Basis extends Interpolate
  case object BasisClosed extends Interpolate
  case object BasisOpen extends Interpolate
  case object Bundle extends Interpolate
  case object Cardinal extends Interpolate
  case object CardinalClosed extends Interpolate
  case object CardinalOpen extends Interpolate
  case object Linear extends Interpolate
  case object LinearClosed extends Interpolate
  case object Monotone extends Interpolate
  case object Step extends Interpolate
  case object StepAfter extends Interpolate
  case object StepBefore extends Interpolate

  def schema: Dsl[Interpolate] = oneOf(
    "basis".as(Basis) ::
    "basis-closed".as(BasisClosed) ::
    "basis-open".as(BasisOpen) ::
    "bundle".as(Bundle) ::
    "cardinal".as(Cardinal) ::
    "cardinal-closed".as(CardinalClosed) ::
    "cardinal-open".as(CardinalOpen) ::
    "linear".as(Linear) ::
    "linear-closed".as(LinearClosed) ::
    "monotone".as(Monotone) ::
    "step".as(Step) ::
    "step-after".as(StepAfter) ::
    "step-before".as(StepBefore) ::
    HNil
  ).to[Interpolate]
}
