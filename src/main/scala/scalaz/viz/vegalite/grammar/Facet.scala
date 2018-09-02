package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Facet(
  column: Option[PositionFieldDef],
  row: Option[PositionFieldDef]
)

object Facet {
  def schema: Dsl[Facet] = allOf(
    opt("column" -> PositionFieldDef.schema) ::
    opt("row" -> PositionFieldDef.schema) ::
    HNil
  ).to[Facet]
}