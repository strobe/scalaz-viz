package scalaz.viz.api

import scalaz._, Scalaz._
import monocle.Lens
import monocle.macros.GenLens
import monocle.macros.syntax.lens._
import monocle.Prism
import monocle.macros.GenPrism
import monocle.macros.GenIso
import shapeless.syntax.singleton._
import shapeless.syntax.std.tuple._
import shapeless.record._
import shapeless.HNil
import shapeless.poly.{~> => ~~>, _}
import shapeless.Poly1
import shapeless.labelled._
import shapeless.labelled.field
import scala.math._

import InterpreterHelpers._


// TODO: MeanFn and ReverseFn should be polymorphic
// - seems like Data should has something like :Foldable
// TODO: layerBuilder
// TODO: Chart ~> Image
object Main extends App {

  case class ColumnId(idx: Int)

  case class RawData(t: Int, v: Double)
  object RawData {
    val _t: ColumnId = ColumnId(0)
    val _v: ColumnId = ColumnId(1)
    val _s: ColumnId = ColumnId(3)
  }

  type Source = Vector[RawData]

  type TFunction = Source => Source

  sealed trait Mappable
  val Mappable = Tag.of[Mappable]

  object vis {
    /// Axis ///
    sealed trait Coordinates
    case class Cartesian(x: Double, y: Double) extends Coordinates
    case class Polar(radial: Double, angle: Float) extends Coordinates

    /// Aes ///
    sealed trait FontWeight
    case class FontWeightNormal() extends FontWeight
    case class FontWeightBold() extends FontWeight

    case class FontStyle(
      name: String,
      weight: FontWeight,
      size: Double,
      color: Color
    ) extends MarkStyle

    case class Color(r: Int, g: Int, b: Int, a: Int)
    object Color {
      def red   = Color(255,0,0,255)
      def green = Color(0,255,0,255)
      def blue  = Color(0,0,255,255)
      def white = Color(255,255,255,255)
      def black = Color(0,0,0,0)
      def gray  = Color(128,128,128,255)
    }

    case class LineStyle(
      width: Int,
      color: Color
    )

    sealed trait PointShape
    case class Circle() extends PointShape
    case class Star() extends PointShape
    case class Cross() extends PointShape
    case class Square() extends PointShape
    case class Arrow() extends PointShape

    sealed trait MarkStyle
    case class PointStyle(
      color: Color,
      borderColor: Color,
      bolderWidth: Double,
      radius: Double,
      shape: PointShape
    ) extends MarkStyle

    case class GridStyle(lineStyle: LineStyle) extends MarkStyle

    case class AxisStyle(
      lineStyle: LineStyle,
      labelStyle: FontStyle,
      gridStyle: LineStyle,
      labelGap: Double
    )

    case class Canvas()
    object Canvas {
      val min = 0.1
      val max = 100
    }

    // marks types
    sealed trait Mark
    case class Point() extends Mark
    case class Text() extends Mark

    // marks properties
    sealed trait MarkProperties
    case class X() extends MarkProperties
    case class Y() extends MarkProperties
    case class Size() extends MarkProperties
    case class Opacity() extends MarkProperties
    case class Shape() extends MarkProperties

    // relation between mark properties and data columns
    case class MarkDataRel(m: Mark, property: MarkProperties, columnId: ColumnId)

    // aes description
    sealed trait Aes
    case class Aes2D(axis: AxisStyle, grid: GridStyle, markStyle: MarkStyle) extends Aes

    /// ///
  }

  object aesDefaults {
    import vis._

    // defaults
    val defaultPointStyle = PointStyle(Color.red, Color.black, 1, 1, Circle())

    val defaultAxisStyle = AxisStyle(LineStyle(1, Color.gray),
      FontStyle("Arial", FontWeightNormal(), 12, Color.black),
      LineStyle(1, Color.gray),
      5)

    val defaultGridStyle = GridStyle(LineStyle(1, Color.gray))

    val defaultAes = Aes2D(defaultAxisStyle, defaultGridStyle, defaultPointStyle)

    // lens for Point mark
    // val xLens: Lens[Point, Double] =
      // Lens[Point, Double](_.x)(v => a => a.copy(x = v))
    // val yLens: Lens[Point, Double] =
      // Lens[Point, Double](_.x)(v => a => a.copy(x = v))
    // val styleLens: Lens[Point, PointStyle] = Lens[Point, PointStyle](_.style)(v => a => a.copy(style = v))
    // val radiusLens: Lens[PointStyle, Double] =
      // Lens[PointStyle, Double](_.radius)(v => a => a.copy(radius = v))
    // val sizeLens = styleLens composeLens radiusLens
  }

  object algebra {
    import vis._

    // data loading operations
    sealed trait DataF[A]
    case class LiftSource(s: Source) extends DataF[Source]

    // scales data transformations operations
    sealed trait ScalesF[A]
    case class ReverseFn() extends ScalesF[TFunction]
    case class LogFn() extends ScalesF[TFunction]

    // stats/views data transformations operations
    sealed trait StatsF[A]
    case class MeanFn() extends StatsF[TFunction]
    case class RangeFn(from: Int, to: Int) extends StatsF[TFunction]

    // point mark mappings
    sealed trait MarksF[A]
    case class SetMapping(m: Mark, property: MarkProperties, columnId: ColumnId) extends MarksF[MarkDataRel]
    case class SetAes(aes: Aes) extends MarksF[Aes2D]

    // coordinates
    abstract class CoordinatesF[A]
    case class SetCoordinates(cs: Coordinates) extends CoordinatesF[Coordinates]

    // coproducts
    type TransformsInsF[A] = Coproduct[ScalesF, StatsF, A]
    type DataInsF[A] = Coproduct[DataF, TransformsInsF, A]
    type VisInsF[A] = Coproduct[MarksF, DataInsF, A]
    type CoordInsF[A] = Coproduct[CoordinatesF, VisInsF, A]
    type ChartF[A] = Coproduct[VisInsF, CoordInsF, A]
  }


  object dsl {

    import algebra._
    import vis._

    /// Helpers ///

    case class ExecStrategy[Op[_], F[_], A](op: Op[A])(implicit inject: Inject[Op, F]) {
      val seq: Free[F, A] = Free.liftF(inject.inj(op))
      val par: FreeAp[F, A] = FreeAp.lift(inject.inj(op))
    }

    def combineInterpreters[F[_], G[_], H[_]](f: F ~> H, g: G ~> H): Coproduct[F, G, ?] ~> H =
      new (Coproduct[F, G, ?] ~> H) {
        override def apply[A](fa: Coproduct[F, G, A]): H[A] = fa.run match {
          case -\/(ff) => f(ff)
          case \/-(gg) => g(gg)
        }
      }

    implicit class RichNaturalTransformation[F[_], H[_]](val f: F ~> H) {
      def or[G[_]](g: G ~> H): Coproduct[F, G, ?] ~> H = combineInterpreters[F, G, H](f, g)
    }

    def lift[Op[_], F[_], A](op: Op[A])(implicit inject: Inject[Op, F]) =
      FreeAp.lift(inject.inj(op))

    def logScale(v: Double) =  Canvas.max * log10(v/Canvas.min) / log10(Canvas.max/Canvas.min)

    /// Smart Constructors ///

    class DataLifted[F[_]](implicit I: Inject[DataF, F]) {
      object data {
        def liftSource(s: Source): FreeAp[F, Source] =
          lift(LiftSource(s))
      }
    }

    object DataLifted {
      implicit def apply[F[_]](implicit I: Inject[DataF, F]): DataLifted[F] = new DataLifted[F]
    }

    class ScalesLifted[F[_]](implicit I: Inject[ScalesF, F]) {
      object scales {
        def reverseFn(): FreeAp[F, TFunction] = lift(ReverseFn())
        def logFn(): FreeAp[F, TFunction]     = lift(LogFn())
      }
    }

    object ScalesLifted {
      implicit def apply[F[_]](implicit I: Inject[ScalesF, F]): ScalesLifted[F] = new ScalesLifted[F]
    }

    class StatsLifted[F[_]](implicit I: Inject[StatsF, F]) {
      object stats {
        def meanFn(): FreeAp[F, TFunction]                    = lift(MeanFn())
        def rangeFn(from: Int, to: Int): FreeAp[F, TFunction] = lift(RangeFn(from, to))
      }
    }

    object StatsLifted {
      implicit def apply[F[_]](implicit I: Inject[StatsF, F]): StatsLifted[F] = new StatsLifted[F]
    }

    class MarksLifted[F[_]](implicit I: Inject[MarksF, F]) {
      object m {
        def setMapping(m: Mark, property: MarkProperties, columnId: ColumnId): FreeAp[F, MarkDataRel] =
          lift(SetMapping(m, property, columnId))
      }
      object aes {
        def setAes(aes: Aes): FreeAp[F, Aes2D] = lift(SetAes(aes))
      }
    }

    object MarksLifted {
      implicit def apply[F[_]](implicit I: Inject[MarksF, F]): MarksLifted[F] = new MarksLifted[F]
    }

    class CoordinatesLifted[F[_]](implicit I: Inject[CoordinatesF, F]) {
      implicit def apply[F[_]](implicit I: Inject[CoordinatesF, F]): CoordinatesLifted[F] =
        new CoordinatesLifted[F]
    }

    object CoordinatesLifted {
      implicit def apply[F[_]](implicit I: Inject[CoordinatesF, F]): CoordinatesLifted[F] =
        new CoordinatesLifted[F]
    }

    /// Natural Transformations ///

    // test printer

    type Printer[A] = String

    def toDataPrinter[A](): DataF ~> Printer = new (DataF ~> Printer) {
      def apply[A](fa: DataF[A]) = fa match {
        case LiftSource(s) => s"source ${s.mkString(", ")} \n"
      }
    }

    def toScalesPrinter[A](): ScalesF ~> Printer = new (ScalesF ~> Printer) {
      def apply[A](fa: ScalesF[A]) = fa match {
        case ReverseFn() => "reverse fn\n"
        case LogFn() => "log fn\n"
      }
    }

    def toStatsPrinter[A](): StatsF ~> Printer = new (StatsF ~> Printer) {
      def apply[A](fa: StatsF[A]) = fa match {
        case MeanFn() => "mean fn 2\n"
        case RangeFn(from, to) => "range fn\n"
      }
    }

    def toMarksPrinter[A](): MarksF ~> Printer = new (MarksF ~> Printer) {
      def apply[A](fa: MarksF[A]) = fa match {
        case SetMapping(m, p, cId) => s"mark: $m, prop: $p, columnId: $cId"
        case SetAes(aes) => aes match {
          case Aes2D(a, g, m) => s"axis: $a, grid: $g, mark: $m"
        }
      }
    }

    def toCoordsPrinter[A](): CoordinatesF ~> Printer = new (CoordinatesF ~> Printer) {
        def apply[A](fa: CoordinatesF[A]) = fa match {
            case SetCoordinates(cs) => s"SetCoordinates $cs"
        }
    }

    def toTransfInsPrinter[A](): TransformsInsF ~> Printer = toScalesPrinter or toStatsPrinter
    def toDataInsPrinter[A](): DataInsF ~> Printer = toDataPrinter or toTransfInsPrinter
    def toVisInsPrinter[A](): VisInsF ~> Printer = toMarksPrinter or toDataInsPrinter
    def toCoordInsPrinter[A](): CoordInsF ~> Printer = toCoordsPrinter or toVisInsPrinter
    def toChartPrinter[A](): ChartF ~> Printer = toVisInsPrinter or toCoordInsPrinter

    // evaluation

    type Evaluator[A] = A

    def toDataEvaluator[A](): DataF ~> Evaluator = new (DataF ~> Evaluator) {
      def apply[A](fa: DataF[A]) = fa match {
        case LiftSource(s) => s
      }
    }

    object mean extends Poly1 {
      implicit def caseInt    = at[Int](x => x + 1)
      implicit def caseDouble = at[Double](x => x + 1)
      implicit def caseString = at[String](x => x)
    }

    // i.fold(0)(_+_) / i.size
    def mean(s: Source, lens: Lens[RawData, Double]): Double = {
      val v = s.map { x => lens.get(x) }
      val m = v.sum / v.size
      m
    }

    def toStatsEvaluator[A](): StatsF ~> Evaluator = new (StatsF ~> Evaluator) {
      def apply[A](fa: StatsF[A]) = fa match {
        case MeanFn() => (i: Source) => Vector(i.fold(RawData(0,0))((x,y) => RawData(0,x.v + y.v)))
        case RangeFn(from, to) => (i: Source) => i.slice(from, to)
      }
    }

    def toScalesEvaluator[A](): ScalesF ~> Evaluator = new (ScalesF ~> Evaluator) {
      def apply[A](fa: ScalesF[A]) = fa match {
        case ReverseFn() => (i: Source) => i.map(x => RawData(x.t, -x.v))
        case LogFn() => (i: Source) =>
          i.map(x => RawData(x.t, logScale(x.v)))
      }
    }

    /**
      apply X for Data.x and Data.y return Data

      def mean(s: Source, ids: Seq[ColumnId]) = {
        def transf = ???
        s.map(v =>
          RawData.unapply(v).map(x =>
            val tuple = x
            for(a <- 0 to ids.size) {
              // def get = tuple._<a>
              // tuple._<a> = transf(get)
              //
            }
            tuple
          )
        )
      }

      */


    def toMarksEvaluator[A](): MarksF ~> Evaluator = new (MarksF ~> Evaluator) {
      def apply[A](fa: MarksF[A]) = fa match {
        case SetMapping(m, p, cId) => MarkDataRel(m, p, cId)
        case SetAes(aes) => aes match {
          case Aes2D(a, g, m) => Aes2D(a, g, m)
        }
      }
    }

    def toCoordsEvaluator[A](): CoordinatesF ~> Evaluator = new (CoordinatesF ~> Evaluator) {
        def apply[A](fa: CoordinatesF[A]) = fa match {
            case SetCoordinates(cs) => cs
        }
    }

    def toTransfInsEvaluator[A](): TransformsInsF ~> Evaluator = toScalesEvaluator or toStatsEvaluator
    def toDataInsEvaluator[A](): DataInsF ~> Evaluator = toDataEvaluator or toTransfInsEvaluator
    def toVisInsEvaluator[A](): VisInsF ~> Evaluator = toMarksEvaluator or toDataInsEvaluator
    def toCoordInsEvaluator[A](): CoordInsF ~> Evaluator = toCoordsEvaluator or toVisInsEvaluator
    def toChartEvaluator[A](): ChartF ~> Evaluator = toVisInsEvaluator or toCoordInsEvaluator

  }

  object example {
    import dsl._
    import algebra._

    def program(implicit A: DataLifted[ChartF],
      B: ScalesLifted[ChartF],
      C: StatsLifted[ChartF],
      D: MarksLifted[ChartF],
      E: CoordinatesLifted[ChartF]
      ) = {
      // ): FreeAp[ChartF, Source] = {

      import vis._
      import aesDefaults._
      import A._, B._, C._, D._, E._

      // the data source
      val in = Vector(
        RawData(1,2.1),
        RawData(2,11.5),
        RawData(3,33.1)
      )

      // lift data
      val dataB: FreeAp[ChartF, Vector[RawData]] = data.liftSource(in)

      // apply scales
      val scalesB = dataB |@| scales.logFn |@| scales.reverseFn

      // create views to scaled data by applying series of transformations functions
      val stats1: FreeAp[ChartF, Vector[RawData]] = (scalesB |@| stats.rangeFn(0,2))(seq3)
      val stats2: FreeAp[ChartF, Vector[RawData]] = (scalesB |@| stats.meanFn)(seq3)

      // customizing default aesthetic styles
      val lStyle = GenLens[LineStyle](_.width).set(2)(defaultAxisStyle.lineStyle)

      val pStyle = defaultPointStyle
        .lens(_.color).set(vis.Color.red)
        .lens(_.bolderWidth).modify(_ + 1)
        .lens(_.shape).set(Square())

      val pStyle2 = pStyle.lens(_.color).set(vis.Color.blue)

      val aStyle = defaultAxisStyle
        .lens(_.lineStyle).set(lStyle)

      val gStyle = defaultGridStyle
        .lens(_.lineStyle).set(lStyle)

      // defining mark type
      val mark = Point()
      // define set of mappable properties
      val mProps = (X(), Y(), Size())

      // layer1 definition
      val layer1 = (stats1 |@|
        m.setMapping(mark, mProps._1, RawData._t) |@|
        m.setMapping(mark, mProps._2, RawData._v) |@|
        m.setMapping(mark, mProps._3, RawData._s) |@|
        aes.setAes(defaultAes))((x,y,z,w, a) => (x,y,z,w, a))

      // layer2 definition
      val layer2 = (stats2 |@|
        m.setMapping(mark, mProps._1, RawData._t) |@|
        m.setMapping(mark, mProps._2, RawData._v) |@|
        aes.setAes(defaultAes))((x,y,z,w) => (x,y,z,w))

      // combing layers to chart
      val chart = (layer1 |@| layer2)((x,y) => (x,y))

      // val chart = (layerOneB |@| layerTwoB)(renderToFile("the-chart.png"))

      chart
    }

    def run() = {
      println(program.foldMap(toChartPrinter()))
      println(program.foldMap(toChartEvaluator()))
    }
  }

  example.run
}

//  LocalWords:  SourceFn LocalWords coproducts MarkProps mappable
