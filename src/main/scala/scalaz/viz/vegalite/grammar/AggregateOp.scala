package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait AggregateOp extends Product with Serializable
object AggregateOp {
  case object ArgMax extends AggregateOp
  case object ArgMin extends AggregateOp
  case object Average extends AggregateOp
  case object Count extends AggregateOp
  case object Distinct extends AggregateOp
  case object Max extends AggregateOp
  case object Mean extends AggregateOp
  case object Median extends AggregateOp
  case object Min extends AggregateOp
  case object Missing extends AggregateOp
  case object Modeskew extends AggregateOp
  case object Q1 extends AggregateOp
  case object Q3 extends AggregateOp
  case object Stdev extends AggregateOp
  case object Stdevp extends AggregateOp
  case object Sum extends AggregateOp
  case object Valid extends AggregateOp
  case object Values extends AggregateOp
  case object Variance extends AggregateOp
  case object VarianceP extends AggregateOp

  def schema: Dsl[AggregateOp] = oneOf(
    "argmax".as(ArgMax) ::
    "argmin".as(ArgMin) :: 
    "average".as(Average) ::
    "count".as(Count) ::
    "distinct".as(Distinct) ::
    "max".as(Max) ::
    "mean".as(Mean) ::
    "median".as(Median) ::
    "min".as(Min) ::
    "missing".as(Missing) ::
    "modeskew".as(Modeskew) ::
    "q1".as(Q1) ::
    "q3".as(Q3) ::
    "stdev".as(Stdev) ::
    "stdevp".as(Stdevp) ::
    "sum".as(Sum) ::
    "valid".as(Valid) ::
    "values".as(Values) ::
    "variance".as(Variance) ::
    "variancep".as(VarianceP) ::
    HNil
  ).to[AggregateOp]
}
