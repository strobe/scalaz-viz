package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class UrlData(
  format: Option[DataFormat],
  url: String
)

object UrlData {
  def schema: Dsl[UrlData] = allOf(
    opt("format" -> DataFormat.schema) ::
    "url" -> str ::
    HNil
  ).to[UrlData]
}