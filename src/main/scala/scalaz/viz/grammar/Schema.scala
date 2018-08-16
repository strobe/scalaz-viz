package scalaz.viz.grammar

sealed trait Schema
case class VegaSchema(url: String) extends Schema
