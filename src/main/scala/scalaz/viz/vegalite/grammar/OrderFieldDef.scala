package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class OrderFieldDef(
  aggregate: Option[AggregateOp],
  // NOTE: anyOf replaced with oneOf
  bin: Option[Bin Either Boolean],
  field: Option[String],
  sort: Option[SortOrder],
  timeUnit: Option[TimeUnit],
  title: Option[String],
  `type`: Option[Tpe]
)

object OrderFieldDef {
  def schema: Dsl[OrderFieldDef] = allOf(
    opt("aggregate" -> AggregateOp.schema) ::
    opt("bin" -> oneOf(Bin.schema :: bool :: HNil).toEither) ::
    opt("field" -> str) ::
    opt("sort" -> SortOrder.schema) ::
    opt("timeUnit" -> TimeUnit.schema) ::
    opt("title" -> str) ::
    opt("type" -> Tpe.schema) ::
    HNil
  ).to[OrderFieldDef]
}