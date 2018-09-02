package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Tpe extends Product with Serializable
object Tpe {
  case object Nominal extends Tpe
  case object Ordinal extends Tpe
  case object Quantitative extends Tpe
  case object Temporal extends Tpe

  def schema: Dsl[Tpe] = oneOf(
    "nominal".as(Nominal) ::
    "ordinal".as(Ordinal) ::
    "quantitative".as(Quantitative) ::
    "temporal".as(Temporal) ::
    HNil
  ).to[Tpe]
}
