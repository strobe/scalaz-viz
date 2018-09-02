package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class FacetGridConfig(
  color: Option[String],
  offset: Option[Double],
  opacity: Option[Double]
)

object FacetGridConfig {
  def schema: Dsl[FacetGridConfig] = allOf(
    opt("color" -> str) ::
    opt("offset" -> number) ::
    opt("opacity" -> number) ::
    HNil
  ).to[FacetGridConfig]
}