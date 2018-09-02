import scalaz.viz.schema._
import shapeless._

sealed trait Padding

object Padding {
  case class Uniform(amount: Double) extends Padding
  case class NonUniform(top: Double, right: Double, bottom: Double, left: Double) extends Padding

  def schema: Dsl[Padding] = 
    oneOf(
      allOf(
        "top"    -> number ::
        "right"  -> number ::
        "bottom" -> number ::
        "left"   -> number :: 
        HNil
      ).to[NonUniform] ::
      number.dimap((pn: Uniform) => pn.amount)(amount => Uniform(amount)) ::
      HNil
    ).to[Padding]
}