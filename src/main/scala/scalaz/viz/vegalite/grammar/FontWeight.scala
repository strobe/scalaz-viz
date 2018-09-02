package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait FontWeight extends Product with Serializable
object FontWeight {
  case object Bold extends FontWeight
  case object Normal extends FontWeight
  case class Weight(weight: Double) extends FontWeight

  def schema: Dsl[FontWeight] = oneOf(
    "bold".as(Bold) ::
    "normal".as(Normal) ::
    number.imap(Weight)(_.weight) ::
    HNil
  ).to[FontWeight]
}
