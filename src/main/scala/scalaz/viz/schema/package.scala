package scalaz.viz

import argonaut.Json
import scalaz.viz.util.HasCoproductExtensions
import shapeless._
import shapeless.poly._

package object schema extends HasCoproductExtensions { outer =>
  import Algebra._

  type Dsl[A] = GenDsl[A, A]

  def str: Dsl[String] = new GenDsl[String, String] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vString
    override def toString = "vString"
  }

  def number: Dsl[Double] = new GenDsl[Double, Double] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vNumber
    override def toString = "vNumber"
  }
  
  def int: Dsl[Int] = new GenDsl[Int, Int] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vInt
    override def toString = "vInt"
  }
  
  def boundedNumber(min: Option[Double], max: Option[Double]): Dsl[Double] = new GenDsl[Double, Double] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vBoundedNumber(min, max)
    override def toString = s"vBoundedNumber($min, $max)"
  }
  
  def boundedInt(min: Option[Int], max: Option[Int]): Dsl[Int] = new GenDsl[Int, Int] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vBoundedInt(min, max)
    override def toString = s"vBoundedInt($min, $max)"
  }

  def bool: Dsl[Boolean] = new GenDsl[Boolean, Boolean] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vBoolean
    override def toString = "vBoolean"
  }
  
  def json: Dsl[Json] = new GenDsl[Json, Json] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vJson
    override def toString = "vJson"
  }

  def rep[A](item: Dsl[A]) = new GenDsl[Vector[A], Vector[A]] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vRep(item)
    override def toString = s"vArray[${item.toString}]"
  }
  
  def rep[A](item: Dsl[A], minItems: Option[Int], maxItems: Option[Int]) = new GenDsl[Vector[A], Vector[A]] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vRep(item, minItems, maxItems)
    override def toString = s"vArray($minItems-$maxItems)[${item.toString}]"
  }

  def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): GenDsl[A, B] = new GenDsl[A, B] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vKeyValue(key)(value)
    override def toString = s"$key -> ${value.toString}"
  }

  def vDimap[A, B, C, D](value: GenDsl[B, C], f: A => B, g: C => D): GenDsl[A, D] = new GenDsl[A, D] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vDimap(value)(f)(g)
    override def toString = value.toString
  }

  def vLmap[A, B, C](fbc: GenDsl[B, C])(f: A => B): GenDsl[A, C] = new GenDsl[A, C] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vLmap(fbc)(f)
    override def toString = fbc.toString
  }

  def vRmap[B, C, D](fbc: GenDsl[B, C])(g: C => D): GenDsl[B, D] = new GenDsl[B, D] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vRmap(fbc)(g)
    override def toString = fbc.toString
  }

  def allOf[H <: HList, R <: HList, S <: HList](dsls: H)(implicit cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]): GenDsl[R, S] = new GenDsl[R, S] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vAllOf(dsls)
    override def toString = s"allOf(${dsls.toString})"
  }

  def oneOf[H <: HList, R <: Coproduct, S <: Coproduct](dsls: H)(implicit cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]): GenDsl[R, S] = new GenDsl[R, S] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vOneOf(dsls)
    override def toString = s"oneOf(${dsls.toString})"
  }

  def vPair[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): GenDsl[(A, C), (B, D)] = new GenDsl[(A, C), (B, D)] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vPair(ab)(cd)
    override def toString = s"vPair(${ab.toString}, ${cd.toString})"
  }

  def vSum[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): GenDsl[A Either C, B Either D] = new GenDsl[A Either C, B Either D] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vSum(ab)(cd)
    override def toString = s"vSum(${ab.toString}, ${cd.toString})"
  }

  def tag[A, B](tag: String)(dsl: => GenDsl[A, B]): GenDsl[A, B] = new GenDsl[A, B] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vTagged(tag)(dsl)
    override def toString = s"[Tag: $tag] ${dsl.toString}"
  }

  def opt[A, B](dsl: GenDsl[A, B]): GenDsl[Option[A], Option[B]] = new GenDsl[Option[A], Option[B]] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vOpt(dsl)
    override def toString = s"opt(${dsl.toString})"
  }

  def opt[A, B](dsl: GenDsl[A, B], defaultValue: B): GenDsl[A, B] = new GenDsl[A, B] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vOpt(dsl, defaultValue)
    override def toString = s"vOpt(${dsl.toString}, default=${defaultValue.toString})"
  }

  def vGetKey[A, B](dsl: GenDsl[A, B]): GenDsl[(String, A), (String, B)] = new GenDsl[(String, A), (String, B)] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vGetKey(dsl)
    override def toString = s"vGetKey(${dsl})"
  }
  
  def const(value: String): Dsl[String] = new Dsl[String] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vConst(value)
    override def toString = s"vConst($value)"
  }
  
  def description[A, B](desc: String)(dsl: GenDsl[A,B]): GenDsl[A, B] = new GenDsl[A, B] {
    def apply[F[_, _]](vf: Schema[F]) = vf.vDescription(desc)(dsl)
    override def toString = s"vDescription($desc, ${dsl.toString})"
  }

  implicit class DslExtensions[B, C](self: GenDsl[B, C]) {
    def dimap[A, D](f: A => B)(g: C => D): GenDsl[A, D] = vDimap(self, f, g)
    def lmap[A](f: A => B): GenDsl[A, C] = vLmap(self)(f)
    def rmap[D](g: C => D): GenDsl[B, D] = vRmap(self)(g)

    // NOTE: Arguments g and f are flipped here for better type inference.
    def imap[D](g: C => D)(f: D => B): GenDsl[D, D] = self.rmap(g).lmap[D](f)
    
    def toEither[P, Q](implicit ev1: (P :+: Q :+: CNil) =:= B, ev2: C =:= (P :+: Q :+: CNil)): Dsl[P Either Q] =
      self.imap(c => ev2(c).foldCases[P Either Q].atCase(Left(_)).atCase(Right(_)))(
        epq => ev1(epq.fold(Coproduct[P :+: Q :+: CNil](_), Coproduct[P :+: Q :+: CNil](_)))
      )
      
    def to[R](implicit ev: C =:= B, gen: Generic.Aux[R, C]): GenDsl[R, R] = self.imap(gen.from)(thing => ev(gen.to(thing)))
    
    def as[R](implicit ev: C =:= B, gen: Generic.Aux[R, C :: HNil]): GenDsl[R, R] = self.imap(c => gen.from(c :: HNil))(r => ev(gen.to(r).head))
  }
  
  implicit class DslExtensionsByValue[B, C](self: => GenDsl[B, C]) {
    def tag(s: String): GenDsl[B, C] = outer.tag(s)(self)
  }
  
  implicit class StringExtensions(self: String) {
    def ->[A, B](dsl: GenDsl[A, B]): GenDsl[A, B] = vKeyValue(self)(dsl)
    
    def as[A](value: A): Dsl[A] = 
      const(self)
      .imap(_ => value)(_ => self)
  }
}