package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait TimeUnit extends Product with Serializable
object TimeUnit {
  case object Date extends TimeUnit
  case object Day extends TimeUnit
  case object Hours extends TimeUnit
  case object HoursMinutes extends TimeUnit
  case object HoursMinutesSeconds extends TimeUnit
  case object Milliseconds extends TimeUnit
  case object Minutes extends TimeUnit
  case object MinutesSeconds extends TimeUnit
  case object Month extends TimeUnit
  case object MonthDate extends TimeUnit
  case object Quarter extends TimeUnit
  case object QuarterMonth extends TimeUnit
  case object Seconds extends TimeUnit
  case object SecondsMilliseconds extends TimeUnit
  case object Year extends TimeUnit
  case object YearMonth extends TimeUnit
  case object YearMonthDate extends TimeUnit
  case object YearMonthDateHours extends TimeUnit
  case object YearMonthDateHoursMinutes extends TimeUnit
  case object YearMonthDateHoursMinutesSeconds extends TimeUnit
  case object YearQuarter extends TimeUnit
  case object YearQuarterMonth extends TimeUnit

  def schema: Dsl[TimeUnit] = oneOf(
    "date".as(Date) ::
    "day".as(Day) ::
    "hours".as(Hours) ::
    "hoursminutes".as(HoursMinutes) ::
    "hoursminutesseconds".as(HoursMinutesSeconds) ::
    "milliseconds".as(Milliseconds) ::
    "minutes".as(Minutes) ::
    "minutesseconds".as(MinutesSeconds) ::
    "month".as(Month) ::
    "monthdate".as(MonthDate) ::
    "quarter".as(Quarter) ::
    "quartermonth".as(QuarterMonth) ::
    "seconds".as(Seconds) ::
    "secondsmilliseconds".as(SecondsMilliseconds) ::
    "year".as(Year) ::
    "yearmonth".as(YearMonth) ::
    "yearmonthdate".as(YearMonthDate) ::
    "yearmontdatehours".as(YearMonthDateHours) ::
    "yearmontdatehoursminutes".as(YearMonthDateHoursMinutes) ::
    "yearmontdatehoursminutesseconds".as(YearMonthDateHoursMinutesSeconds) ::
    "yearquarter".as(YearQuarter) ::
    "yearquartermonth".as(YearQuarterMonth) ::
    HNil
  ).to[TimeUnit]
}
