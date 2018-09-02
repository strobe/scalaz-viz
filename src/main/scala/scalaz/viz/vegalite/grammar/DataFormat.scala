package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class DataFormat(
  feature: Option[String],
  mesh: Option[String],
//  parse: Option[  // TODO parse
  property: Option[String],
  `type`: Option[DataFormatType]
)

object DataFormat {
  def schema: Dsl[DataFormat] = allOf(
    opt("feature" -> str) ::
    opt("mesh" -> str) ::
    opt("property" -> str) ::
    opt("type" -> DataFormatType.schema) ::
    HNil
  ).to[DataFormat]
}