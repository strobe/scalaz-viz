package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class ExtendedScheme(
  count: Option[Double],
  extent: Option[Vector[Double]],
  name: String
)

object ExtendedScheme {
  def schema: Dsl[ExtendedScheme] = allOf(
    opt("count" -> number) ::
    opt("extent" -> rep(number)) ::
    "name" -> str ::
    HNil
  ).to[ExtendedScheme]
}