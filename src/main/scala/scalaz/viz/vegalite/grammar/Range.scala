package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Range extends Product with Serializable
object Range {
  // These names are made up.
  case class Range1(
    count: Option[Double],
    extent: Option[Vector[Double]],
    scheme: String
  ) extends Range
  
  // NOTE:  Can have additional properties!
  case class Range2(
    // NOTE: This was not marked as required in the grammar...
    items: Vector[String Either Double]    
  ) extends Range
  
  case class Range3(
    step: Double    
  ) extends Range

  def schema: Dsl[Range] = oneOf(
    allOf(
      opt("count" -> number) ::
      opt("extent" -> rep(number)) ::
      "scheme" -> str ::
      HNil
    ).to[Range1] ::
    
    "items" -> rep(oneOf(str :: number :: HNil).toEither).imap(Range2(_))(_.items) ::
    ("step" -> number).imap(Range3(_))(_.step) ::
    HNil
  ).to[Range]
}
