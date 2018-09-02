package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class LegendConfig(
//  encode: Option[VgLegendEncode] // TODO encode
    entryPadding: Option[Double],
    gradientHeight: Option[Double],
    gradientStrokeColor: Option[String],
    gradientStrokeWidth: Option[Double],
    gradientWidth: Option[Double],
    labelAlign: Option[String],
    labelBaseline: Option[String],
    labelColor: Option[String],
    labelFont: Option[String],
    labelFontSize: Option[Double],
    labelOffset: Option[Double],
    margin: Option[Double],
    offset: Option[Double],
    orient: Option[String],
    padding: Option[Double],
    shortTimeLabels: Option[Boolean],
    symbolColor: Option[String],
    symbolShape: Option[String],  // TODO: This is constrained to be 'circle' 'square' 'cross' 'diamond' 'triangle-up' 'triangle-down' or custom SVG.
    symbolSize: Option[Double],
    symbolStrokeWidth: Option[Double],
    tickCount: Option[Double],
    titleColor: Option[String],
    titleFont: Option[String],
    titleFontSize: Option[Double],
    titleFontWeight: Option[String Either Double],
    titlePadding: Option[Double],
    zindex: Option[Int]
)

object LegendConfig {
  def schema: Dsl[LegendConfig] = allOf(
    opt("entryPadding" -> number) ::
    opt("gradientHeight" -> number) ::
    opt("gradientStrokeColor" -> str) ::
    opt("gradientStrokeWidth" -> number) ::
    opt("gradientWidth" -> number) ::
    opt("labelAlign" -> str) ::
    opt("labelBaseline" -> str) ::
    opt("labelColor" -> str) ::
    opt("labelFont" -> str) ::
    opt("labelFontSize" -> number) ::
    opt("labelOffset" -> number) ::
    opt("margin" -> number) ::
    opt("offset" -> number) ::
    opt("orient" -> str) ::
    opt("padding" -> number) ::
    opt("shortTimeLabels" -> bool) ::
    opt("symbolColor" -> str) ::
    opt("symbolShape" -> str) ::
    opt("symbolSize" -> number) ::
    opt("symbolStrokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("tickCount" -> number) ::
    opt("titleColor" -> str) ::
    opt("titleFont" -> str) ::
    opt("titleFontSize" -> number) ::
    opt("titleFontWeight" -> oneOf(str :: number :: HNil).toEither) ::
    opt("titlePadding" -> number) ::
    opt("zindex" -> boundedInt(Some(0), None)) ::

    HNil
  ).to[LegendConfig]
}