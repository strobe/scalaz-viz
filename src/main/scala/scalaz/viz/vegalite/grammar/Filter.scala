package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Filter
object Filter {
  // Helper class
  sealed trait StringNumberBooleanValue extends Product with Serializable
  object StringNumberBooleanValue {
    case class BooleanValue(value: Boolean) extends StringNumberBooleanValue
    case class NumberValue(value: Double) extends StringNumberBooleanValue
    case class StringValue(value: String) extends StringNumberBooleanValue

    def schema: Dsl[StringNumberBooleanValue] = oneOf(
        bool.imap(BooleanValue)(_.value) ::
        number.imap(NumberValue)(_.value) ::
        str.imap(StringValue)(_.value) ::
      HNil
    ).to[StringNumberBooleanValue]
  }
  
  case class EqualFilter(
    // NOTE: We changed this from "anyOf" to "oneOf"
    equal: DateTime Either StringNumberBooleanValue,
    field: String,
    timeUnit: Option[TimeUnit]
  ) extends Filter
  
  object EqualFilter {
    def schema: Dsl[EqualFilter] = allOf(
      "equal" -> oneOf(DateTime.schema :: StringNumberBooleanValue.schema :: HNil).toEither ::
      "field" -> str ::
      opt("timeUnit" -> TimeUnit.schema) ::
      HNil
    ).to[EqualFilter]
  }
  
  case class RangeFilter(
    field: String,
    range: Vector[DateTime Either Double],
    timeUnit: Option[TimeUnit]
  ) extends Filter
  
  object RangeFilter {
    def schema: Dsl[RangeFilter] = allOf(
       "field" -> str ::
       "range" -> rep(oneOf(DateTime.schema :: number :: HNil).toEither) ::
       opt("timeUnit" -> TimeUnit.schema) ::
       HNil
    ).to[RangeFilter]
  }
  
  case class OneOfFilter(
    field: String,
    oneOf: Vector[DateTime Either StringNumberBooleanValue],
    timeUnit: Option[TimeUnit]
  ) extends Filter
  
  object OneOfFilter {
    def schema: Dsl[OneOfFilter] = allOf(
       "field" -> str ::
       "oneOf" -> rep(oneOf(DateTime.schema :: StringNumberBooleanValue.schema :: HNil).toEither) ::
       opt("timeUnit" -> TimeUnit.schema) ::
       HNil
    ).to[OneOfFilter]
  }
  
  case class StringFilter(
      valueFilter: String
  ) extends Filter

  def schema: Dsl[Filter] = oneOf(
     EqualFilter.schema ::
     OneOfFilter.schema ::
     RangeFilter.schema ::
     str.imap(StringFilter(_))(_.valueFilter) ::
     HNil
  ).to[Filter]
}
