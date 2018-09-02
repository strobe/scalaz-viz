package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class FacetConfig(
  axis: Option[AxisConfig],
  cell: Option[CellConfig],
  grid: Option[FacetGridConfig]
)

object FacetConfig {
  def schema: Dsl[FacetConfig] = allOf(
    opt("axis" -> AxisConfig.schema) ::
    opt("cell" -> CellConfig.schema) ::
    opt("grid" -> FacetGridConfig.schema) ::
    HNil
  ).to[FacetConfig]
}