package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait LegendType extends Product with Serializable
object LegendType {
  case object Gradient extends LegendType
  case object Symbol extends LegendType

  def schema: Dsl[LegendType] = oneOf(
    "gradient".as(Gradient) ::
    "symbol".as(Symbol) ::
    HNil
  ).to[LegendType]
}

case class Legend(
//  encode: Option[VgLegendEncode], // TODO encode
  entryPadding: Option[Double],
  format: Option[String],
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
  shape: Option[String],
  shortTimeLabels: Option[Boolean],
  symbolColor: Option[String],
  symbolShape: Option[String],
  symbolSize: Option[Double],
  symbolStrokeWidth: Option[Double],
  tickCount: Option[Double],
  title: Option[String],
  titleColor: Option[String],
  titleFont: Option[String],
  titleFontSize: Option[Double],
  titleFontWeight: Option[String Either Double],
  titlePadding: Option[Double],
  `type`: Option[LegendType],
  // NOTE: anyOf switched to oneOf
  values: Option[Vector[String] Either Vector[Double] Either Vector[DateTime]],
  zindex: Option[Int]
)

object Legend {
  def schema: Dsl[Legend] = allOf(
//    opt("Encode" // TODO encode
    opt("entryPadding" -> number) ::
    opt("format" -> str) ::
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
    opt("shape" -> str) ::
    opt("shortTimeLabels" -> bool) ::
    opt("symbolColor" -> str) ::
    opt("symbolShape" -> str) ::
    opt("symbolSize" -> number) ::
    opt("symbolStrokeWidth" -> number) ::
    opt("tickCount" -> number) ::
    opt("title" -> str) ::
    opt("titleColor" -> str) ::
    opt("titleFont" -> str) ::
    opt("titleFontSize" -> number) ::
    opt("titleFontWeight" -> oneOf(str :: number :: HNil).toEither) ::
    opt("titlePadding" -> number) ::
    opt("type" -> LegendType.schema) ::
    opt("values" -> oneOf(oneOf(rep(str) :: rep(number) :: HNil).toEither :: rep(DateTime.schema) :: HNil).toEither) ::
    opt("zindex" -> int) ::
    
    HNil
  ).to[Legend]
}