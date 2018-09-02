package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class TextFieldDef(
  aggregate: Option[AggregateOp],
  // NOTE: anyOf replaced
  bin: Option[Bin Either Boolean],
  field: Option[String],
  format: Option[String],
  timeUnit: Option[TimeUnit],
  title: Option[String],
  `type`: Option[Tpe]
)

object TextFieldDef {
  def schema: Dsl[TextFieldDef] = allOf(
    opt("aggregate" -> AggregateOp.schema) ::
    opt("bin" -> oneOf(Bin.schema :: bool :: HNil).toEither) ::
    opt("field" -> str) ::
    opt("format" -> str) ::
    opt("timeUnit" -> TimeUnit.schema) ::
    opt("title" -> str) ::
    opt("type" -> Tpe.schema) ::
    HNil
  ).to[TextFieldDef]
}