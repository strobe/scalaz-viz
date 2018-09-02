package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Config(
  area: Option[MarkConfig],
  axis: Option[AxisConfig],
  background: Option[String],
  bar: Option[BarConfig],
  cell: Option[CellConfig],
  circle: Option[SymbolConfig],
  countTitle: Option[String],
  facet: Option[FacetConfig],
  legend: Option[LegendConfig],
  line: Option[MarkConfig],
  mark: Option[MarkConfig],
  numberFormat: Option[String],
  overlay: Option[OverlayConfig],
  padding: Option[Padding],
  point: Option[PointConfig],
  range: Option[Range],
  rect: Option[MarkConfig],
  rule: Option[MarkConfig],
  scale: Option[ScaleConfig],
//  selection: Option[SelectionConfig]  // TODO selection
  square: Option[SymbolConfig],
  stack: Option[StackOffset],
  text: Option[TextConfig],
  tick: Option[TickConfig],
  timeFormat: Option[String],
  viewport: Option[Double]
)

object Config {
  def schema: Dsl[Config] = allOf(
    opt("area" -> MarkConfig.schema) ::
    opt("axis" -> AxisConfig.schema) ::
    opt("background" -> str) ::
    opt("bar" -> BarConfig.schema) ::
    opt("cell" -> CellConfig.schema) ::
    opt("circle" -> SymbolConfig.schema) ::
    opt("countTitle" -> str) ::
    opt("facet" -> FacetConfig.schema) ::
    opt("legend" -> LegendConfig.schema) ::
    opt("line" -> MarkConfig.schema) ::
    opt("mark" -> MarkConfig.schema) ::
    opt("numberFormat" -> str) ::
    opt("overlay" -> OverlayConfig.schema) ::
    opt("padding" -> Padding.schema) ::
    opt("point" -> PointConfig.schema) ::
    opt("range" -> Range.schema) ::
    opt("rect" -> MarkConfig.schema) ::
    opt("rule" -> MarkConfig.schema) ::
    opt("scale" -> ScaleConfig.schema) ::
//    opt("selection" -> Se// TODO selection
    opt("square" -> SymbolConfig.schema) ::
    opt("stack" -> StackOffset.schema) ::
    opt("text" -> TextConfig.schema) ::
    opt("tick" -> TickConfig.schema) ::
    opt("timeFormat" -> str) ::
    opt("viewport" -> number) ::
    HNil
  ).to[Config]
}