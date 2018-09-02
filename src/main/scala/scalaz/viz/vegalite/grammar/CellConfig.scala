package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class CellConfig(
  clip: Option[Boolean],
  fill: Option[String],
  fillOpacity: Option[Double],
  height: Option[Double],
  stroke: Option[String],
  strokeDash: Option[Vector[Double]],
  strokeDashOffset: Option[Double],
  strokeOpacity: Option[Double],
  strokeWidth: Option[Double],
  width: Option[Double]
)

object CellConfig {
  def schema: Dsl[CellConfig] = allOf(
    opt("clip" -> bool) ::
    opt("fill" -> str) ::
    opt("fillOpacity" -> number) ::
    opt("height" -> number) ::
    opt("stroke" -> str) ::
    opt("strokeDash" -> rep(number)) ::
    opt("strokeDashOffset" -> number) ::
    opt("strokeOpacity" -> number) ::
    opt("strokeWidth" -> number) ::
    opt("width" -> number) ::
    HNil
  ).to[CellConfig]
}