package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class OverlayConfig(
  area: Option[AreaOverlay],
  line: Option[Boolean],
  lineStyle: Option[MarkConfig],
  pointStyle: Option[MarkConfig]
)

object OverlayConfig {
  def schema: Dsl[OverlayConfig] = allOf(
    opt("area" -> AreaOverlay.schema) ::
    opt("line" -> bool) ::
    opt("lineStyle" -> MarkConfig.schema) ::
    opt("pointStyle" -> MarkConfig.schema) ::
    HNil
  ).to[OverlayConfig]
}