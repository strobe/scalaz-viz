package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

case class Transform(
  calculate: Option[Vector[Formula]],
  filter: Option[Filter Either Vector[Filter]],
  filterInvalid: Option[Boolean]
)

object Transform {
  def schema: Dsl[Transform] = allOf(
    opt("calculate" -> rep(Formula.schema)) ::
    opt("filter" -> oneOf(Filter.schema :: rep(Filter.schema) :: HNil).toEither) ::
    opt("filterInvalid" -> bool) ::
    HNil
  ).to[Transform]
}