package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Axis(
  domain: Option[Boolean],
//  encode: Option[Unit],  // TODO encode
  format: Option[String],
  grid: Option[Boolean],
  labelAngle: Option[Double],
  labelMaxLength: Option[Int],
  labelPadding: Option[Double],
  labels: Option[Boolean],
  maxExtent: Option[Double],
  minExtent: Option[Double],
  offset: Option[Double],
  orient: Option[AxisOrient],
  position: Option[Double],
  tickCount: Option[Int],
  tickSize: Option[Double],
  ticks: Option[Boolean],
  title: Option[String],
  titleMaxLength: Option[Int],
  titlePadding: Option[Double],
  // NOTE: anyOf switched to oneOf
  values: Option[Either[Vector[Double], Vector[DateTime]]],
  zIndex: Option[Int]
)

object Axis {
  def schema: Dsl[Axis] = allOf(
    opt("domain" -> bool) :: 
//    opt("encode" -> 
    opt("format" -> str) ::
    opt("grid" -> bool) ::
    opt("labelAngle" -> boundedNumber(Some(0), Some(360))) ::
    opt("labelMaxLength" -> boundedInt(Some(1), None)) ::
    opt("labelPadding" -> number) ::
    opt("labels" -> bool) ::
    opt("maxExtent" -> number) ::
    opt("minExtent" -> number) ::
    opt("offset" -> number) ::
    opt("orient" -> AxisOrient.schema) ::
    opt("position" -> number) ::
    opt("tickCount" -> boundedInt(Some(0), None)) ::
    opt("tickSize" -> boundedNumber(Some(0), None)) ::
    opt("ticks" -> bool) ::
    opt("title" -> str) ::
    opt("titleMaxLength" -> boundedInt(Some(0), None)) ::
    opt("titlePadding" -> number) ::
    opt("values" -> oneOf(rep(number) :: rep(DateTime.schema) :: HNil).toEither) ::
    opt("zindex" -> boundedInt(Some(0), None)) ::
    HNil
  ).to[Axis]
}