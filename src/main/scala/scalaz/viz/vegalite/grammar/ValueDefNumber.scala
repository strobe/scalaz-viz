package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._

case class ValueDefNumber(
  value: Option[Double]    
)

object ValueDefNumber {
  def schema: Dsl[ValueDefNumber] = 
    opt("value" -> number)
      .rmap(ValueDefNumber(_))
      .lmap(_.value) 
}