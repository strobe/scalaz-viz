package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class FieldDef(
  aggregate: Option[AggregateOp],
  // NOTE: anyOf switch to oneOf
  bin: Option[Bin Either Boolean],
  field: Option[String],
  timeUnit: Option[TimeUnit],
  title: Option[String],
  `type`: Option[Tpe]
)

object FieldDef {
  def schema: Dsl[FieldDef] = allOf(
    opt("aggregate" -> AggregateOp.schema) ::
    opt("bin" -> oneOf(Bin.schema :: bool :: HNil).toEither) ::
    opt("field" -> str) ::
    opt("timeUnit" -> TimeUnit.schema) ::
    opt("title" -> str) ::
    opt("type" -> Tpe.schema) ::
    HNil
  ).to[FieldDef]
}