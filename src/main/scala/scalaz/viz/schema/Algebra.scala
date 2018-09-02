package scalaz.viz.schema

import argonaut.Json
import shapeless._
import shapeless.poly._

object Algebra {
    /*
   * Turns this:
   *   DslPro[A1, A2] :: DslPro[B1, B2] :: ... :: DslPro[Z1, Z2] :: HNil.
   * 
   * Into this:
   *   DslPro[
   *     A1 :: B1 :: ... :: Z1 :: HNil,
   *     A2 :: B2 :: ... :: Z2 :: HNil
   *   ]
   */
  object ChangeAllOf extends Poly1 {
    implicit def caseBase[A, B] = at[GenDsl[A, B] :: HNil] { hlist =>
      val f: A :: HNil => A = _.head
      val g: B => B :: HNil = _ :: HNil

      hlist.head.dimap(f)(g)
    }

    implicit def caseInd[A, B, C <: HList, D <: HList, T <: HList](implicit caseTail: Case.Aux[T, GenDsl[C, D]]) = at[GenDsl[A, B] :: T] { hlist =>
      val ab: GenDsl[A, B] = hlist.head
      val cd: GenDsl[C, D] = caseTail(hlist.tail)

      val pair = vPair(ab)(cd)
      val f: A :: C => (A, C) = hlist => (hlist.head, hlist.tail)
      val g: Tuple2[B, D] => B :: D = { case (b, d) => b :: d }

      val ret: GenDsl[A :: C, B :: D] = pair.dimap(f)(g)
      ret
    }
  }
  
  /*
   * Turns this:
   *   DslPro[A1, A2] :: DslPro[B1, B2] :: ... DslPro[Z1, Z2] :: HNil
   * 
   * Into this:
   *   DslPro[
   *     A1 :+: B1 :+: ... :+: CNil,
   *     A2 :+: B2 :+: ... :+: CNil
   *   ]
   */
  object ChangeOneOf extends Poly1 {
    implicit def caseBase[A, B] = at[GenDsl[A, B] :: HNil] { hlist =>
      val f: A :+: CNil => A = _.unify
      val g: B => B :+: CNil = Coproduct(_)

      val dsl: GenDsl[A, B] = hlist.head
      val ret = dsl.dimap(f)(g)
      ret
    }

    implicit def caseInd[A, B, C <: Coproduct, D <: Coproduct, T <: HList](implicit caseTail: Case.Aux[T, GenDsl[C, D]]) = at[GenDsl[A, B] :: T] { hlist =>
      val ab: GenDsl[A, B] = hlist.head
      val cd: GenDsl[C, D] = caseTail(hlist.tail)

      val sum = vSum(ab)(cd)

      val f: (A :+: C) => A Either C = _ match {
        case Inl(a) => Left(a)
        case Inr(c) => Right(c)
      }

      val g: B Either D => (B :+: D) = _ match {
        case Left(b)  => Coproduct[B :+: D](b)
        case Right(d) => d.extendLeft[B]
      }

      val ret = sum.dimap(f)(g)
      ret
    }
  }

  trait Schema[F[_, _]] {

    def vString: F[String, String]
    def vNumber: F[Double, Double]
    def vInt: F[Int, Int]
    
    def vBoundedNumber(min: Option[Double], max: Option[Double]): F[Double, Double]
    def vBoundedInt(min: Option[Int], max: Option[Int]): F[Int, Int]
    
    def vBoolean: F[Boolean, Boolean]
    def vRep[A](item: Dsl[A]): F[Vector[A], Vector[A]]
    def vRep[A](item: Dsl[A], minItems: Option[Int], maxItems: Option[Int]): F[Vector[A], Vector[A]]
    
    def vJson: F[Json, Json]
    def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): F[A, B]
    def vTagged[A, B](tag: String)(dsl: => GenDsl[A, B]): F[A, B]
    def vGetKey[A, B](value: GenDsl[A, B]): F[(String, A), (String, B)]
    
    def vDescription[A, B](description: String)(dsl: GenDsl[A, B]): F[A, B]

//    def vRequire[A, B](value: GenDsl[A, B])(pred: B => Boolean, description: String, getMessage: B => String): F[A, B]
    def vConst(value: String): F[String, String]

    def vOpt[A, B](value: GenDsl[A, B]): F[Option[A], Option[B]]
    def vOpt[A, B](value: GenDsl[A, B], defaultValue: B): F[A, B]

    /*
     * H looks like this:  
     *   DslPro[A1, A2] :: DslPro[B1, B2] :: ... :: DslPro[Z1, Z2] :: HNil.
     * 
     * The output looks like this:
     *   DslPro[
     *     A1 :: B1 :: ... :: Z1 :: HNil,
     *     A2 :: B2 :: ... :: Z2 :: HNil
     *   ]
     */
    def vAllOf[H <: HList, R <: HList, S <: HList](dsls: H)(implicit cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]): F[R, S]
    
    /*
     * H looks like this:
     *   DslPro[A1, A2] :: DslPro[B1, B2] :: ... :: DslPro[Z1, Z2] :: HNil.
     *   
     * The output looks like this:
     *   DslPro[
     *     A1 :+: B1 :+: ... :+: Z1 :+: CNil,
     *     A2 :+: B2 :+: ... :+: Z2 :+: CNil
     *   ] 
     */
    def vOneOf[H <: HList, R <: Coproduct, S <: Coproduct](dsls: H)(implicit cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]): F[R, S]

    def vPair[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): F[(A, C), (B, D)]

    def vSum[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): F[A Either C, B Either D]

    /*
     * Profunctor
     */
    def vDimap[A, B, C, D](fbc: GenDsl[B, C])(f: A => B)(g: C => D): F[A, D]

    /*
     * Contravariant functor
     */
    def vLmap[A, B, C](fbc: GenDsl[B, C])(f: A => B): F[A, C] = vDimap(fbc)(f)(identity)

    /*
     * Covariant functor
     */
    def vRmap[B, C, D](fbc: GenDsl[B, C])(g: C => D): F[B, D] = vDimap(fbc)(identity[B])(g)
  }
}