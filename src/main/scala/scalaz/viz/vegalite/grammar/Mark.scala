package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Mark extends Product with Serializable
object Mark {
  case object Area extends Mark
  case object Bar extends Mark
  case object Circle extends Mark
  case object Line extends Mark
  case object Point extends Mark
  case object Rect extends Mark
  case object Rule extends Mark
  case object Square extends Mark
  case object Text extends Mark
  case object Tick extends Mark

  def schema: Dsl[Mark] = oneOf(
    "area".as(Area) ::
    "bar".as(Bar) ::
    "circle".as(Circle) ::
    "line".as(Line) ::
    "point".as(Point) ::
    "rect".as(Rect) ::
    "rule".as(Rule) ::
    "square".as(Square) ::
    "text".as(Text) ::
    "tick".as(Tick) ::
    HNil
  ).to[Mark]
}
