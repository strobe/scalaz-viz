package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait SortOrder extends Product with Serializable
object SortOrder {
  case object Ascending extends SortOrder
  case object Descending extends SortOrder

  def schema: Dsl[SortOrder] = oneOf(
    "ascending".as(Ascending) ::
    "descending".as(Descending) ::
    HNil
  ).to[SortOrder]
}
