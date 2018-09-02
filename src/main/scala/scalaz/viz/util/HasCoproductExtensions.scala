package scalaz.viz.util

import shapeless._
import shapeless.ops.coproduct._

trait HasCoproductExtensions {

  /**
   * Provides a `foldCases` extension method to Shapeless coproducts.
   * (I submitted this as a pull request to Shapeless, but it ended up in limbo.
   *  https://github.com/milessabin/shapeless/pull/632)
   */
  implicit class CoproductExtensions[C <: Coproduct](self: C) {
    def foldCases[R](implicit foldCases: FoldCasesInit[C, R]): foldCases.Out = foldCases(self, HNil)
  }
  
  implicit class EitherExtensions[A, B](self: Either[A, B]) {
    def toCoproduct: A :+: B :+: CNil = self.fold(Coproduct[A :+: B :+: CNil](_), Coproduct[A :+: B :+: CNil](_))
  }

    /**
   * Type class to support creating a lightweight syntax for folding a `Coproduct`.
   * Unlike the `Folder` type class, this one does not require a polymorphic
   * function with a stable identifier.  Additionally, unlike directly folding over 
   * a `Coproduct` with pattern matching over `Inl` and `Inr` injectors, this type 
   * class guarantees that the resulting fold is exhaustive.  It is also possible to
   * partially fold a `Coproduct` (as long as cases are handled in the order specified
   * by the `Coproduct` type signature), which makes it possible to incrementally fold
   * a `Coproduct`.
   * 
   * @author William Harvey
   */
  trait FoldCases[C <: Coproduct, R, D <: Coproduct] extends Serializable {
    type F <: HList
    type Out

    def apply(c: C, fs: F): Out
  }

  type FoldCasesInit[C <: Coproduct, R] = FoldCases[C, R, C] { type F = HNil }

  object FoldCases {
    trait CaseBuilder[A, B, C] {
      def atCase(f: A => B): C
    }

    private[this] object FoldHelper extends Poly1 {
      implicit def default[T, A]: Case.Aux[(T, T => A), A] = at[(T, T => A)] {
        case (t, f) => f(t)
      }
    }

    def apply[C <: Coproduct, F <: HList, R](
      implicit foldCases: FoldCases[C, R, C]): Aux[C, R, C, foldCases.F, foldCases.Out] = foldCases

    type Aux[C <: Coproduct, R, D <: Coproduct, F0 <: HList, Out0] = FoldCases[C, R, D] {
      type F = F0
      type Out = Out0
    }

    implicit def baseFoldCases[C <: Coproduct, R, DH, FIn <: HList, FOut <: HList, Z <: Coproduct](
      implicit prepend: shapeless.ops.hlist.Prepend.Aux[FIn, (DH => R) :: HNil, FOut],
      zipWith: ZipWith.Aux[FOut, C, Z],
      folder: Folder.Aux[FoldHelper.type, Z, R]): Aux[C, R, DH :+: CNil, FIn, CaseBuilder[DH, R, R]] =
      new FoldCases[C, R, DH :+: CNil] {
        type F = FIn
        type Out = CaseBuilder[DH, R, R]

        def apply(c: C, fs: F): Out = new CaseBuilder[DH, R, R] {
          def atCase(f: DH => R): R = c.zipWith(fs :+ f).fold(FoldHelper)
        }
      }

    implicit def inductiveFoldCases[C <: Coproduct, DH1, DH2, DT0 <: Coproduct, FIn <: HList, FOut0 <: HList, R, TOut](
      implicit prepend: shapeless.ops.hlist.Prepend.Aux[FIn, (DH1 => R) :: HNil, FOut0],
      caseTail: Aux[C, R, DH2 :+: DT0, FOut0, TOut]): Aux[C, R, DH1 :+: DH2 :+: DT0, FIn, CaseBuilder[DH1, R, TOut]] =
      new FoldCases[C, R, DH1 :+: DH2 :+: DT0] {
        type F = FIn
        type Out = CaseBuilder[DH1, R, TOut]

        type DH = DH1
        type DT = DH2 :+: DT0

        def apply(c: C, fs: F): Out = new CaseBuilder[DH, R, TOut] {
          def atCase(f: DH => R): TOut = caseTail.apply(c, fs :+ f)
        }
      }
  }
}