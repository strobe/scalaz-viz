package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._

case class ValueDefString(
  value: Option[String]    
)

object ValueDefString {
  def schema: Dsl[ValueDefString] = 
    opt("value" -> str)
      .rmap(ValueDefString(_))
      .lmap(_.value) 
}