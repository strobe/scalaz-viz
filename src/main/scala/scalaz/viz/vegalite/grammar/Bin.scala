package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Bin(
  base: Option[Double],
  divide: Option[Vector[Double]],
  extent: Option[Vector[Double]], 
  maxbins: Option[Double],
  minstep: Option[Double],
  step: Option[Double],
  steps: Option[Vector[Double]]
)

object Bin {
  def schema: Dsl[Bin] = allOf(
    opt("base" -> number) ::
    opt("divide" -> rep(number, Some(1), None)) ::
    opt("extent" -> rep(number, Some(2), Some(2))) ::
    opt("maxbins" -> boundedNumber(Some(2), None)) ::
    opt("minstep" -> number) ::
    opt("step" -> number) ::
    opt("steps" -> rep(number, Some(1), None)) ::
    HNil
  ).to[Bin] 
}