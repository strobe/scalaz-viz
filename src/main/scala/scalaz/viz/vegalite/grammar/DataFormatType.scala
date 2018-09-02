package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait DataFormatType extends Product with Serializable
object DataFormatType {
  case object Csv extends DataFormatType
  case object Json extends DataFormatType
  case object TopoJson extends DataFormatType
  case object Tsv extends DataFormatType

  def schema: Dsl[DataFormatType] = oneOf(
    "csv".as(Csv) ::
    "json".as(Json) ::
    "topojson".as(TopoJson) ::
    "tsv".as(Tsv) ::
    HNil
  ).to[DataFormatType]
}
