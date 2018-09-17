package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object LayerHistogramVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "data": {"url": "data/flights-2k.json"},
        |  "layer": [
        |    {
        |      "mark": "bar",
        |      "encoding": {
        |        "x": {
        |          "field": "distance",
        |          "type": "quantitative",
        |          "bin": true
        |        },
        |        "y": {
        |          "aggregate": "count",
        |          "type": "quantitative"
        |        }
        |      }
        |    },
        |    {
        |      "transform": [{"filter": "datum.delay < 500"}],
        |      "mark": "bar",
        |      "encoding": {
        |        "x": {
        |          "field": "distance",
        |          "type": "quantitative",
        |          "bin": true
        |        },
        |        "y": {
        |          "aggregate": "count",
        |          "type": "quantitative"
        |        },
        |        "color": {"value": "goldenrod"}
        |      }
        |    }
        |  ]
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
