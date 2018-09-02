package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class PointConfig(
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
  maxOpacity: Option[Double],
  maxSize: Option[Double],
  maxStrokeWidth: Option[Double],
  minOpacity: Option[Double],
  minSize: Option[Double],
  minStrokeWidth: Option[Double],
  opacity: Option[Double],
  orient: Option[Orient],
  radius: Option[Double],
  shape: Option[String],
  shapes: Option[Vector[String]],
  size: Option[Double],
  stroke: Option[String],
  strokeDash: Option[Vector[Double]],
  strokeDashOffset: Option[Double],
  strokeOpacity: Option[Double],
  strokeWidth: Option[Double],
  tension: Option[Double],
  theta: Option[Double]
)

object PointConfig {
  def schema: Dsl[PointConfig] = allOf(
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
    opt("maxOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("maxSize" -> boundedNumber(Some(0), None)) ::
    opt("maxStrokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("minOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("minSize" -> boundedNumber(Some(0), None)) ::
    opt("minStrokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("opacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("orient" -> Orient.schema) ::
    opt("radius" -> boundedNumber(Some(0), None)) ::
    opt("shape" -> str) ::
    opt("shapes" -> rep(str)) ::
    opt("size" -> boundedNumber(Some(0), None)) ::
    opt("stroke" -> str) ::
    opt("strokeDash" -> rep(number)) ::
    opt("strokeDashOffset" -> number) ::
    opt("strokeOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("strokeWidth" -> boundedNumber(Some(0), None)) ::
    opt("tension" -> boundedNumber(Some(0), Some(1))) ::
    opt("theta" -> number) ::
    HNil
  ).to[PointConfig]
}