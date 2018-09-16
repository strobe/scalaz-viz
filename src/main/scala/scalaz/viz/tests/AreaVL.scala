package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object AreaVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "width": 300,
        |  "height": 200,
        |  "data": {"url": "data/unemployment-across-industries.json"},
        |  "mark": "area",
        |  "encoding": {
        |    "x": {
        |      "timeUnit": "yearmonth", "field": "date", "type": "temporal",
        |      "axis": {"format": "%Y"}
        |    },
        |    "y": {
        |      "aggregate": "sum", "field": "count", "type": "quantitative",
        |      "axis": {"title": "count"}
        |    }
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty
    
    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
