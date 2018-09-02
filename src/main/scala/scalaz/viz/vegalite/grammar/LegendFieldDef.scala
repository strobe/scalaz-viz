package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class LegendFieldDef(
  aggregate: Option[AggregateOp],
  // NOTE: anyOf switched to oneOf
  bin: Option[Bin Either Boolean],
  field: Option[String],
  // NOTE: anyOf switched to oneOf
  // NOTE: Actually we took out Null alternative here.
  legend: Option[Legend],
  scale: Option[Scale],
  // NOTE: anyOf switched to oneOf
  sort: Option[SortField Either SortOrder],
  timeUnit: Option[TimeUnit],
  title: Option[String],
  `type`: Option[Tpe]
)

object LegendFieldDef {
  def schema: Dsl[LegendFieldDef] = allOf(
    opt("aggregate" -> AggregateOp.schema) ::
    opt("bin" -> oneOf(Bin.schema :: bool :: HNil).toEither) ::
    opt("field" -> str) ::
    opt("legend" -> Legend.schema) ::
    opt("scale" -> Scale.schema) ::
    opt("sort" -> oneOf(SortField.schema :: SortOrder.schema :: HNil).toEither) ::
    opt("timeUnit" -> TimeUnit.schema) ::
    opt("title" -> str) ::
    opt("type" -> Tpe.schema) ::
    HNil
  ).to[LegendFieldDef]
}