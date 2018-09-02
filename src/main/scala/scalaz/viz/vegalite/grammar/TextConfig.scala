package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class TextConfig(
  align: Option[HorizontalAlign],
  angle: Option[Double],
  baseline: Option[VerticalAlign],
  color: Option[String],
  dx: Option[Double],
  dy: Option[Double],
  fill: Option[String],
  fillOpacity: Option[Double],
  filled: Option[Boolean],
  font: Option[String],
  fontSize: Option[Double],
  fontStyle: Option[FontStyle],
  fontWeight: Option[FontWeight],
  interpolate: Option[Interpolate],
  maxFontSize: Option[Double],
  maxOpacity: Option[Double],
  maxStrokeWidth: Option[Double],
  minFontSize: Option[Double],
  minOpacity: Option[Double],
  minStrokeWidth: Option[Double],
  opacity: Option[Double],
  orient: Option[Orient],
  radius: Option[Double],
  shape: Option[String],
  shortTimeLabels: Option[Boolean],
  size: Option[Double],
  stroke: Option[String],
  strokeDash: Option[Vector[Double]],
  strokeDashOffset: Option[Double],
  strokeOpacity: Option[Double],
  strokeWidth: Option[Double],
  tension: Option[Double],
  text: Option[String],
  theta: Option[Double]
)

object TextConfig {
  def schema: Dsl[TextConfig] = allOf(
    opt("align" -> HorizontalAlign.schema) ::
    opt("angle" -> boundedNumber(Some(0), Some(360))) ::
    opt("baseline" -> VerticalAlign.schema) ::
    opt("color" -> str) ::
    opt("dx" -> number) ::
    opt("dy" -> number) ::
    opt("fill" -> str) ::
    opt("fillOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("filled" -> bool) ::
    opt("font" -> str) ::
    opt("fontSize" -> boundedNumber(Some(0), None)) ::
    opt("fontStyle" -> FontStyle.schema) ::
    opt("fontWeight" -> FontWeight.schema) ::
    opt("interpolate" -> Interpolate.schema) ::
    opt("maxFontSize" -> boundedNumber(Some(0), None)) ::
    opt("maxOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("maxStrokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("minFontSize" -> boundedNumber(Some(0), None)) ::
    opt("minOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("minStrokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("opacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("orient" -> Orient.schema) ::
    opt("radius" -> boundedNumber(Some(0), None)) ::
    opt("shape" -> str) ::
    opt("shortTimeLabels" -> bool) ::
    opt("size" -> boundedNumber(Some(0), None)) ::
    opt("stroke" -> str) ::
    opt("strokeDash" -> rep(number)) ::
    opt("strokeDashOffset" -> number) ::
    opt("strokeOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("strokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("tension" -> boundedNumber(Some(0), Some(1))) ::
    opt("text" -> str) ::
    opt("theta" -> number) ::
    HNil
  ).to[TextConfig]
}