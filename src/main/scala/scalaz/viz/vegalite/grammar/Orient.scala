package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Orient extends Product with Serializable
object Orient {
  case object Horizontal extends Orient
  case object Vertical extends Orient

  def schema: Dsl[Orient] = oneOf(
    "horizontal".as(Horizontal) ::
    "vertical".as(Vertical) ::
    HNil
  ).to[Orient]
}
