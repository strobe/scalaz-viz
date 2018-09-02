package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Formula(
  field: String,
  expr: String
)

object Formula {
  def schema: Dsl[Formula] = allOf(
    // NOTE:  This is called "as" in the grammar!  (https://raw.githubusercontent.com/vega/vega-lite/master/vega-lite-schema.json)
    "field" -> str ::
    "expr" -> str ::
    HNil
  ).to[Formula]
}