package scalaz.viz.api

object InterpreterHelpers {
    def seq1[F[_], A](v: F[A], fn: F[A] => F[A]) = fn(v)
    def seq2[F[_], A](v: F[A], fn: F[A] => F[A], fn2: F[A] => F[A]) = fn2(fn(v))
    def seq3[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A]
    ) = fn3(fn2(fn(v)))
    def seq4[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A]
    ) = fn4(fn3(fn2(fn(v))))
    def seq5[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A]
    ) = fn5(fn4(fn3(fn2(fn(v)))))
    def seq6[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A]
    ) = fn6(fn5(fn4(fn3(fn2(fn(v))))))
    def seq7[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A]
    ) = fn7(fn6(fn5(fn4(fn3(fn2(fn(v)))))))
    def seq8[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A],
      fn8: F[A] => F[A]
    ) = fn8(fn7(fn6(fn5(fn4(fn3(fn2(fn(v))))))))
    def seq9[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A],
      fn8: F[A] => F[A],
      fn9: F[A] => F[A]
    ) = fn9(fn8(fn7(fn6(fn5(fn4(fn3(fn2(fn(v)))))))))
    def seq10[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A],
      fn8: F[A] => F[A],
      fn9: F[A] => F[A],
      fn10: F[A] => F[A]
    ) = fn10(fn9(fn8(fn7(fn6(fn5(fn4(fn3(fn2(fn(v))))))))))
    def seq11[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A],
      fn8: F[A] => F[A],
      fn9: F[A] => F[A],
      fn10: F[A] => F[A],
      fn11: F[A] => F[A]
    ) = fn11(fn10(fn9(fn8(fn7(fn6(fn5(fn4(fn3(fn2(fn(v)))))))))))
    def seq12[F[_], A](v: F[A],
      fn: F[A] => F[A],
      fn2: F[A] => F[A],
      fn3: F[A] => F[A],
      fn4: F[A] => F[A],
      fn5: F[A] => F[A],
      fn6: F[A] => F[A],
      fn7: F[A] => F[A],
      fn8: F[A] => F[A],
      fn9: F[A] => F[A],
      fn10: F[A] => F[A],
      fn11: F[A] => F[A],
      fn12: F[A] => F[A]
    ) = fn12(fn11(fn10(fn9(fn8(fn7(fn6(fn5(fn4(fn3(fn2(fn(v))))))))))))
  }
