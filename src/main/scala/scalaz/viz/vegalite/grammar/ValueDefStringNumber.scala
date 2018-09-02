package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class ValueDefStringNumber(
  value: Option[String Either Double]    
)

object ValueDefStringNumber {
  def schema: Dsl[ValueDefStringNumber] = 
    opt("value" -> oneOf(str :: number :: HNil).toEither)
      .rmap(ValueDefStringNumber(_))
      .lmap(_.value) 
}