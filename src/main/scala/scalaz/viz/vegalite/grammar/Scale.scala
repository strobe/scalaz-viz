package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait InterpolateType extends Product with Serializable
object InterpolateType {
  case object CubeHelix extends InterpolateType
  case object CubeHelixLong extends InterpolateType
  case object Hcl extends InterpolateType
  case object HclLong extends InterpolateType
  case object Hsl extends InterpolateType
  case object HslLong extends InterpolateType
  case object Lab extends InterpolateType
  case object Rgb extends InterpolateType

  def schema: Dsl[InterpolateType] = oneOf(
    "cubehelix".as(CubeHelix) ::
    "cubehelix-long".as(CubeHelixLong) ::
    "hcl".as(Hcl) ::
    "hcl-long".as(HclLong) ::
    "hsl".as(Hsl) ::
    "hsl-long".as(HslLong) ::
    "lab".as(Lab) ::
    "rgb".as(Rgb) ::
    HNil
  ).to[InterpolateType]
}

sealed trait NiceType extends Product with Serializable
object NiceType {
  case object Day extends NiceType
  case object Hour extends NiceType
  case object Minute extends NiceType
  case object Month extends NiceType
  case object Second extends NiceType
  case object Week extends NiceType
  case object Year extends NiceType

  def schema: Dsl[NiceType] = oneOf(
    "day".as(Day) ::
    "hour".as(Hour) ::
    "minute".as(Minute) ::
    "month".as(Month) ::
    "second".as(Second) ::
    "week".as(Week) ::
    "year".as(Year) ::
    HNil
  ).to[NiceType]
}

case class Scale(
  clamp: Option[Boolean],
  // NOTE: anyOf replaced with oneOf
  domain: Option[Vector[String] Either Vector[Double] Either Vector[DateTime]],
  exponent: Option[Double],
  interpolate: Option[InterpolateType],
  nice: Option[NiceType Either Boolean],
  padding: Option[Double],
  paddingInner: Option[Double],
  paddingOuter: Option[Double],
  //NOTE: anyOf replaced with oneOf
  range: Option[Vector[String] Either Vector[Double] Either String],
  // NOTE: Removed "null" as an alternative here.
  rangeStep: Option[Double],
  round: Option[Boolean],
//  // NOTE: anyOf replaced with oneOf
  scheme: Option[ExtendedScheme Either String],
  spacing: Option[Int],
  `type`: Option[ScaleType],
  useRawDomain: Option[Boolean],
  zero: Option[Boolean]
)

object Scale {
  def schema: Dsl[Scale] = allOf(
    opt("clamp" -> bool) ::
    opt("domain" -> oneOf(oneOf(rep(str) :: rep(number) :: HNil).toEither :: rep(DateTime.schema) :: HNil).toEither) ::
    opt("exponent" -> number) ::
    opt("interpolate" -> InterpolateType.schema) ::
    opt("nice" -> oneOf(NiceType.schema :: bool :: HNil).toEither) ::
    opt("padding" -> boundedNumber(Some(0), Some(1))) ::
    opt("paddingInner" -> boundedNumber(Some(0), Some(1))) ::
    opt("paddingOuter" -> boundedNumber(Some(0), Some(1))) ::
    opt("range" -> oneOf(oneOf(rep(str) :: rep(number) :: HNil).toEither :: str :: HNil).toEither) ::
    opt("rangeStep" -> boundedNumber(Some(0), None)) ::
    opt("round" -> bool) ::
    opt("scheme" -> oneOf(ExtendedScheme.schema :: str :: HNil).toEither) ::
    opt("spacing" -> int) ::
    opt("type" -> ScaleType.schema) ::
    opt("useRawDomain" -> bool) ::
    opt("zero" -> bool) ::
    HNil    
  ).to[Scale]
}