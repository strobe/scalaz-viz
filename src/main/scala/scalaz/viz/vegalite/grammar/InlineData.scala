package scalaz.viz.vegalite.grammar

import argonaut.Json
import scalaz.viz.schema._

case class InlineData(
  values: Vector[Json]    
)

object InlineData {
  def schema: Dsl[InlineData] = 
    ("values" -> rep(json))
      .rmap(InlineData(_))
      .lmap(_.values)
}