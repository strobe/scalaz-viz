package scalaz.viz.interpreter

import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import shapeless._
import shapeless.poly._

object HelpPrinter {

  def print[X, Y](dsl: GenDsl[X, Y]): String = printHelper(dsl).mkString("", "\n", "\n")

  // TODO:  Iterators are mutable - use something immutable instead.
  type Printer[A, B] = Iterator[String]

  // TODO:  Mutable state here.
  val tags = new scala.collection.mutable.HashSet[String]

  private[this] def printHelper[X, Y](dsl: GenDsl[X, Y]): Iterator[String] =
    dsl.apply(new Schema[Printer] {
      override def vString: Printer[String, String]    = Iterator("a string")
      override def vNumber: Printer[Double, Double]    = Iterator("a number")
      override def vBoolean: Printer[Boolean, Boolean] = Iterator("a boolean")
      override def vJson: Printer[Boolean, Boolean]    = Iterator("a JSON object")

      override def vConst(value: String): Printer[String, String] =
        Iterator(s"""A string constant "$value"""")

      override def vRep[A](item: Dsl[A]): Printer[Vector[A], Vector[A]] = {
        val firstLine  = "An array of the following:"
        val substrings = printHelper(item).map(line => "  " + line)
        Iterator(firstLine) ++ substrings
      }

      override def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): Printer[A, B] = {
        val firstLine  = s""""$key" ->"""
        val substrings = printHelper(value)
        Iterator(firstLine) ++ substrings
      }

      override def vDimap[A, B, C, D](fbc: GenDsl[B, C])(f: A => B)(g: C => D): Printer[A, D] =
        printHelper(fbc)

      override def vPair[A, B, C, D](
        ab: GenDsl[A, B]
      )(
        cd: GenDsl[C, D]
      ): Printer[(A, C), (B, D)] = {
        val substrings1 = printHelper(ab)
        val substrings2 = printHelper(cd)
        substrings1 ++ substrings2
      }

      override def vSum[A, B, C, D](
        ab: GenDsl[A, B]
      )(
        cd: GenDsl[C, D]
      ): Printer[A Either C, B Either D] = {
        val substrings1 = printHelper(ab)
        val substrings2 = printHelper(cd)
        substrings1 ++ substrings2
      }

      override def vAllOf[H <: HList, R <: HList, S <: HList](
        dsls: H
      )(implicit
        cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]
      ): Printer[R, S] = {
        val substrings = printHelper(ChangeAllOf(dsls)).map(line => "  " + line)
        Iterator("All of the following:") ++ substrings
      }

      override def vOneOf[H <: HList, R <: Coproduct, S <: Coproduct](
        dsls: H
      )(implicit
        cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]
      ): Printer[R, S] = {
        val substrings = printHelper(ChangeOneOf(dsls)).map(line => "  " + line)
        Iterator("Exactly one of the following:") ++ substrings
      }

      override def vTagged[A, B](tag: String)(dsl: => GenDsl[A, B]): Printer[A, B] =
        if (tags(tag))
          Iterator(s"[(See tag:  $tag)]")
        else {
          tags += tag
          val firstLine  = s"[Tagged: $tag]"
          val substrings = printHelper(dsl)
          Iterator(firstLine) ++ substrings
        }

      override def vOpt[A, B](dsl: GenDsl[A, B]): Printer[A, Option[B]] = {
        val substrings = printHelper(dsl).map(line => "  " + line)
        Iterator("An optional value:") ++ substrings
      }

      override def vDescription[A, B](desc: String)(dsl: GenDsl[A, B]): Printer[A, B] = {
        val substrings = printHelper(dsl)
        Iterator("Description:") ++ desc.lines.map(line => "  " + line) ++ substrings
      }

      override def vOpt[A, B](dsl: GenDsl[A, B], defaultValue: B): Printer[A, B] = {
        val substrings = printHelper(dsl).map(line => "  " + line)
        Iterator(s"An optional value with default $defaultValue:") ++ substrings
      }

//    def vRequire[A, B](dsl: GenDsl[A, B])(pred: B => Boolean, errorMessage: B => String): Printer[A, B] =
//      HelpPrinter.print(dsl)

      override def vGetKey[A, B](value: GenDsl[A, B]): Printer[A, (String, B)] = {
        val substrings = printHelper(value).map(line => "  " + line)
        Iterator("Key: [anything] Value:") ++ substrings
      }

      override def vBoundedInt(min: Option[Int], max: Option[Int]): Printer[Int, Int] = {
        val lower = min.map(_.toString).getOrElse("-∞")
        val upper = max.map(_.toString).getOrElse("+∞")
        Iterator(s"an integer in [$lower, $upper]")
      }

      override def vBoundedNumber(
        min: Option[Double],
        max: Option[Double]
      ): Printer[Double, Double] = {
        val lower = min.map(_.toString).getOrElse("-∞")
        val upper = max.map(_.toString).getOrElse("+∞")
        Iterator(s"a number in [$lower, $upper]")
      }

      override def vInt: Printer[Int, Int] = Iterator("an integer")

      override def vRep[A](
        item: Dsl[A],
        minItems: Option[Int],
        maxItems: Option[Int]
      ): Printer[Vector[A], Vector[A]] = {
        val lower      = minItems.map(_.toString).getOrElse("0")
        val upper      = maxItems.map(_.toString).getOrElse("+∞")
        val firstLine  = s"An array (length must be in [$lower, $upper]) of the following:"
        val substrings = printHelper(item).map(line => "  " + line)
        Iterator(firstLine) ++ substrings
      }
    })
}
