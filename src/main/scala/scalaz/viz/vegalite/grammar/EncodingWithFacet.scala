package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class EncodingWithFacet(
  // NOTE: anyOf switch to oneOf for simplicity
  color: Option[LegendFieldDef Either ValueDefString],
  column: Option[PositionFieldDef],
  // NOTE: anyOf switch to oneOf for simplicity
  detail: Option[FieldDef Either Vector[FieldDef]],
  // NOTE: anyOf switch to oneOf for simplicity
  opacity: Option[LegendFieldDef Either ValueDefNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  order: Option[OrderFieldDef Either Vector[OrderFieldDef]],
  row: Option[PositionFieldDef],
  // NOTE: anyOf switch to oneOf for simplicity
  shape: Option[LegendFieldDef Either ValueDefString],
  // NOTE: anyOf switch to oneOf for simplicity
  size: Option[LegendFieldDef Either ValueDefNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  text: Option[TextFieldDef Either ValueDefStringNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  x: Option[PositionFieldDef Either ValueDefNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  x2: Option[FieldDef Either ValueDefNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  y: Option[PositionFieldDef Either ValueDefNumber],
  // NOTE: anyOf switch to oneOf for simplicity
  y2: Option[FieldDef Either ValueDefNumber]
)

object EncodingWithFacet {
  def schema: Dsl[EncodingWithFacet] = allOf(
    opt("color" -> oneOf(LegendFieldDef.schema :: ValueDefString.schema :: HNil).toEither) ::
    opt("column" -> PositionFieldDef.schema) ::
    opt("detail" -> oneOf(FieldDef.schema :: rep(FieldDef.schema) :: HNil).toEither) ::
    opt("opacity" -> oneOf(LegendFieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    opt("order" -> oneOf(OrderFieldDef.schema :: rep(OrderFieldDef.schema) :: HNil).toEither) ::
    opt("row" -> PositionFieldDef.schema) ::
    opt("shape" -> oneOf(LegendFieldDef.schema :: ValueDefString.schema :: HNil).toEither) ::
    opt("size" -> oneOf(LegendFieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    opt("text" -> oneOf(TextFieldDef.schema :: ValueDefStringNumber.schema :: HNil).toEither) ::
    opt("x" -> oneOf(PositionFieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    opt("x2" -> oneOf(FieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    opt("y" -> oneOf(PositionFieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    opt("y2" -> oneOf(FieldDef.schema :: ValueDefNumber.schema :: HNil).toEither) ::
    HNil
  ).to[EncodingWithFacet]
}