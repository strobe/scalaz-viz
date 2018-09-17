package scalaz.viz.tests
import argonaut.Argonaut._
import argonaut.ArgonautScalaz._
import scalaz.Scalaz._
import testz.{Harness, assert}

/**
  * Specification correctness tests.
 */
object SpecSuite {
  def tests[T](harness: Harness[T]): T = {
    import harness._

    section(
      test("simple json content diff") { () =>
        val json1 =
          """
            |{
            |  "a": "b",
            |  "b": 1,
            |  "c": false,
            |  "cars":[ "Ford", "BMW", "Fiat" ]
            |}
          """.stripMargin.parseOption.get

        val json2 =
          """
            |{
            |  "b": 1,
            |  "a": "b",
            |  "cars":[ "Ford", "BMW", "Fiat" ],
            |  "c": false
            |}
          """.stripMargin.parseOption.get

        assert(
          json1 === json2
        )
      },
      test("area")(AreaVL.test),
      test("bar")(BarVL.test),
      test("circle")(CircleVL.test),
      test("point_2d")(PointVL.test),
      test("rect_heatmap")(RectHeatmapVL.test),
      test("geo circle")(GeoCircleVL.test),
      test("facet_bullet")(FacetBulletVL.test),
      test("histogram_bin_transform")(HistogramBinTransformVL.test),
      test("circle_github_punchcard")(CircleGithubPunchCardVL.test),
      test("boxplot_2D_horizontal")(BoxPlot2DHorizontalVL.test),
      test("layer_histogram")(LayerHistogramVL.test)
    )

  }
}
