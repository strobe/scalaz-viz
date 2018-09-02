package scalaz.viz.schema

import scalaz.viz.schema.Algebra.Schema

trait GenDsl[A, B] {
  def apply[F[_, _]](schema: Schema[F]): F[A, B]
}