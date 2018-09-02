package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class MarkDef(
  interpolate: Option[Interpolate],
  orient: Option[Orient],
  tension: Option[Double],
  `type`: Mark
)

object MarkDef {
  def schema: Dsl[MarkDef] = allOf(
    opt("interpolate" -> Interpolate.schema) ::
    opt("orient" -> Orient.schema) ::
    opt("tension" -> boundedNumber(Some(0), Some(1))) ::
    "type" -> Mark.schema ::
    HNil
  ).to[MarkDef]
}