package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class AxisConfig(
  domain: Option[Boolean],
  domainColor: Option[String],
  domainWidth: Option[Double],
  grid: Option[Boolean],
  gridColor: Option[String],
  gridDash: Option[Vector[Double]],
  gridOpacity: Option[Double],
  gridWidth: Option[Double],
  labelAngle: Option[Double],
  labelColor: Option[String],
  labelFont: Option[String],
  labelFontSize: Option[Double],
  labelMaxLength: Option[Int],
  labels: Option[Boolean],
  maxExtent: Option[Double],
  minExtent: Option[Double],
  shortTimeLabel: Option[Boolean],
  tickColor: Option[String],
  tickSize: Option[Double],
  tickWidth: Option[Double],
  ticks: Option[Boolean],
  titleColor: Option[String],
  titleFont: Option[String],
  titleFontSize: Option[Double],
  titleFontWeight: Option[String Either Double],
  titleMaxLength: Option[Int],
  titlePadding: Option[Double]
)

object AxisConfig {
  def schema: Dsl[AxisConfig] = allOf(
    opt("domain" -> bool) ::
    opt("domainColor" -> str) ::
    opt("domainWidth" -> number) ::
    opt("grid" -> bool) ::
    opt("gridColor" -> str) ::
    opt("gridDash" -> rep(number)) ::
    opt("gridOpacity" -> boundedNumber(Some(0), Some(1))) ::
    opt("gridWidth" -> boundedNumber(Some(0), None)) ::
    opt("labelAngle" -> boundedNumber(Some(0), Some(360))) ::
    opt("labelColor" -> str) ::
    opt("labelFont" -> str) ::
    opt("labelFontSize" -> boundedNumber(Some(0), None)) ::
    opt("labelMaxLength" -> boundedInt(Some(1), None)) ::
    opt("labels" -> bool) ::
    opt("maxExtent" -> number) ::
    opt("minExtent" -> number) ::
    opt("shortTimeLabels" -> bool) ::
    opt("tickColor" -> str) ::
    opt("tickSize" -> boundedNumber(Some(0), None)) ::
    opt("tickWidth" -> boundedNumber(Some(0), None)) ::
    opt("ticks" -> bool) ::
    opt("titleColor" -> str) ::
    opt("titleFont" -> str) ::
    opt("titleFontSize" -> boundedNumber(Some(0), None)) ::
    opt("titleFontWeight" -> oneOf(str :: number :: HNil).toEither) ::
    opt("titleMaxLength" -> boundedInt(Some(0), None)) ::
    opt("titlePadding" -> number) ::
    HNil
  ).to[AxisConfig]
}