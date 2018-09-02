package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class PositionFieldDef(
  aggregate: Option[AggregateOp],
  // NOTE: anyOf replaced with oneOf.  Removed "null" as an option.
  axis: Option[Axis],
  // NOTE: anyOf replaced with oneOf
  bin: Option[Bin Either Boolean],
  field: Option[String],
  scale: Option[Scale],
  // NOTE: anyOf replaced with oneOf
  sort: Option[SortField Either SortOrder],
  timeUnit: Option[TimeUnit],
  title: Option[String],
  `type`: Option[Tpe]
)

object PositionFieldDef {
  def schema: Dsl[PositionFieldDef] = allOf(
    opt("aggregate" -> AggregateOp.schema) ::
    opt("axis" -> Axis.schema) ::
    opt("bin" -> oneOf(Bin.schema :: bool :: HNil).toEither) ::
    opt("field" -> str) ::
    opt("scale" -> Scale.schema) ::
    opt("sort" -> oneOf(SortField.schema :: SortOrder.schema :: HNil).toEither) ::
    opt("timeUnit" -> TimeUnit.schema) ::
    opt("title" -> str) ::
    opt("type" -> Tpe.schema) ::
    HNil
  ).to[PositionFieldDef]
}