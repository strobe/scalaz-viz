package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class ScaleConfig(
  bandPaddingInner: Option[Double],
  bandPaddingOuter: Option[Double],
  clamp: Option[Boolean],
  facetSpacing: Option[Int],
  pointPadding: Option[Double],
  // NOTE: This used to have "null" as an alternative.
  rangeStep: Option[Double],
  round: Option[Boolean],
  textXRangeStep: Option[Double],
  useRawDomain: Option[Boolean]
)

object ScaleConfig {
  def schema: Dsl[ScaleConfig] = allOf(
    opt("bandPaddingInner" -> boundedNumber(Some(0), Some(1))) ::
    opt("bandPaddingOuter" -> boundedNumber(Some(0), Some(1))) ::
    opt("clamp" -> bool) ::
    opt("facetSpacing" -> boundedInt(Some(0), None)) ::
    opt("pointPadding" -> boundedNumber(Some(0), Some(1))) ::
    opt("rangeStep" -> boundedNumber(Some(0), None)) ::
    opt("round" -> bool) ::
    opt("textXRangeStep" -> boundedNumber(Some(0), None)) ::
    opt("useRawDomain" -> bool) ::
    HNil
  ).to[ScaleConfig]
}