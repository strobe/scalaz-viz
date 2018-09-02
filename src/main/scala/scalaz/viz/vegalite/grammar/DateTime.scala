package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class DateTime(
  date: Option[Int],
  day: Option[String Either Double],
  hours: Option[Int],
  milliseconds: Option[Int],
  minutes: Option[Int],
  month: Option[String Either Double],
  quarter: Option[Int],
  seconds: Option[Int],
  year: Option[Int]
)

object DateTime {
  def schema: Dsl[DateTime] = allOf(
    opt("date" -> boundedInt(Some(1), Some(31))) ::
    opt("day" -> oneOf(str :: number :: HNil).toEither) ::
    opt("hours" -> boundedInt(Some(0), Some(23))) ::
    opt("milliseconds" -> boundedInt(Some(0), Some(999))) ::
    opt("minutes" -> boundedInt(Some(0), Some(59))) ::
    opt("month" -> oneOf(str :: number :: HNil).toEither) ::
    opt("quarter" -> boundedInt(Some(1), Some(4))) ::
    opt("seconds" -> boundedInt(Some(0), Some(59))) ::
    opt("year" -> int) ::
    HNil
  ).to[DateTime]
}