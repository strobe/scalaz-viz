package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait ScaleType extends Product with Serializable
object ScaleType {
  case object Band extends ScaleType
  case object Linear extends ScaleType
  case object Log extends ScaleType
  case object Ordinal extends ScaleType
  case object Point extends ScaleType
  case object Pow extends ScaleType
  case object Sequential extends ScaleType
  case object Sqrt extends ScaleType
  case object Time extends ScaleType
  case object Utc extends ScaleType

  def schema: Dsl[ScaleType] = oneOf(
    "band".as(Band) ::
    "linear".as(Linear) ::
    "log".as(Log) ::
    "ordinal".as(Ordinal) ::
    "point".as(Point) ::
    "pow".as(Pow) ::
    "sequential".as(Sequential) ::
    "sqrt".as(Sqrt) ::
    "time".as(Time) ::
    "utc".as(Utc) ::
    HNil
  ).to[ScaleType]
}