package scalaz.viz.interpreter

import argonaut._
import Argonaut._
import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import shapeless._
import shapeless.poly._

object JsonEncoder {
  type Result[A, B] = EncodeJson[A]

  def getEncoder[X, Y](dsl: GenDsl[X, Y]): EncodeJson[X] =
    dsl.apply(new Schema[Result] {
      override def vString: EncodeJson[String]   = EncodeJson(_.asJson)
      override def vNumber: EncodeJson[Double]   = EncodeJson(_.asJson)
      override def vBoolean: EncodeJson[Boolean] = EncodeJson(_.asJson)
      override def vJson: EncodeJson[Json]       = EncodeJson(identity)

      override def vRep[A](item: Dsl[A]): EncodeJson[Vector[A]] = {
        val c = getEncoder(item)
        EncodeJson[Vector[A]] { vec =>
          val jsons = vec.map(c.encode)
          Json.array(jsons: _*)
        }
      }

      override def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): EncodeJson[A] =
        EncodeJson(a => (key := getEncoder(value).encode(a)) ->: jEmptyObject)

      override def vDimap[A, B, C, D](fbc: GenDsl[B, C])(f: A => B)(g: C => D): EncodeJson[A] =
        getEncoder(fbc).contramap(f)

      override def vPair[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): EncodeJson[(A, C)] = {
        val c1 = getEncoder(ab)
        val c2 = getEncoder(cd)

        EncodeJson[(A, C)] {
          case (a, c) =>
            c1(a).deepmerge(c2(c))
        }
      }

      override def vSum[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): EncodeJson[A Either C] = {
        val c1 = getEncoder(ab)
        val c2 = getEncoder(cd)

        EncodeJson[A Either C] {
          case Left(a)  => c1(a)
          case Right(c) => c2(c)
        }
      }

      override def vAllOf[H <: HList, R <: HList, S <: HList](
        dsls: H
      )(implicit
        cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]
      ): EncodeJson[R] = getEncoder(ChangeAllOf(dsls))

      override def vOneOf[H <: HList, R <: Coproduct, S <: Coproduct](
        dsls: H
      )(implicit
        cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]
      ): EncodeJson[R] =
        getEncoder(ChangeOneOf(dsls))

      override def vTagged[A, B](tag: String)(dsl: => GenDsl[A, B]): EncodeJson[A] =
        JsonEncoder.getEncoder(dsl)

      // TODO:  What happens here when the const does not match the input string?
      override def vConst(const: String): EncodeJson[String] = EncodeJson { _ =>
        const.asJson
      }

      override def vOpt[A, B](dsl: GenDsl[A, B]): EncodeJson[Option[A]] = EncodeJson { aOpt =>
        aOpt match {
          case Some(a) => getEncoder(dsl).encode(a)
          case None    => jEmptyObject
        }
      }

      override def vOpt[A, B](dsl: GenDsl[A, B], defaultValue: B): EncodeJson[A] = getEncoder(dsl)

      override def vGetKey[A, B](value: GenDsl[A, B]): EncodeJson[(String, A)] =
        EncodeJson[(String, A)] { pair =>
          implicit val aEnc = getEncoder(value)
          val ret           = pair.asJson
          ret
        }

      override def vDescription[A, B](desc: String)(dsl: GenDsl[A, B]): EncodeJson[A] =
        getEncoder(dsl)

      override def vBoundedInt(min: Option[Int], max: Option[Int]): EncodeJson[Int] =
        EncodeJson(_.asJson)

      override def vBoundedNumber(min: Option[Double], max: Option[Double]): EncodeJson[Double] =
        EncodeJson(_.asJson)

      override def vInt: EncodeJson[Int] = EncodeJson(_.asJson)

      override def vRep[A](
        item: Dsl[A],
        minItems: Option[Int],
        maxItems: Option[Int]
      ): EncodeJson[Vector[A]] = {
        val c = getEncoder(item)
        EncodeJson[Vector[A]] { vec =>
          val jsons = vec.map(c.encode)
          Json.array(jsons: _*)
        }
      }
    })
}
