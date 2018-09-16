package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object BoxPlot2DHorizontalVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "description": "A horizontal box plot showing median, min, and max in the US population distribution of age groups in 2000.",
        |  "data": {"url": "data/population.json"},
        |  "mark": {
        |    "type": "boxplot",
        |    "extent": 1.5
        |  },
        |  "encoding": {
        |    "y": {"field": "age","type": "ordinal"},
        |    "x": {
        |      "field": "people",
        |      "type": "quantitative",
        |      "axis": {"title": "population"}
        |    }
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
