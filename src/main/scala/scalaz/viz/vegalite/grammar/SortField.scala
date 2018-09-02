package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class SortField(
  field: String,
  op: AggregateOp,
  order: Option[SortOrder]
)

object SortField {
  def schema: Dsl[SortField] = allOf(
    "field" -> str ::
    "op" -> AggregateOp.schema ::
    opt("order" -> SortOrder.schema) ::
    HNil
  ).to[SortField]
}