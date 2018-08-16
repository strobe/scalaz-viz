package scalaz.viz.grammar

/**
  * A Vega specification defines an interactive visualization.
  */
case class Specification(
  schema: Schema,
  description: String,
  background: Color,
  width: Int, // TODO: Numeric[T] ?
  height: Int, // TODO: Numeric[T] ?
  padding: Padding,
  autoSize: AutoSize,
  config: Config,
  signals: Seq[Signal],
  data: Seq[Data],
  scales: Seq[Scales],
  projections: Seq[Projection],
  axes: Seq[Axis],
  legends: Seq[Legend],
  title: Title,
  marks: Seq[Mark],
  encode: Encode
)
