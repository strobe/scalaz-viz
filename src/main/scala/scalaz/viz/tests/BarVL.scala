package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object BarVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "description": "A simple bar chart with embedded data.",
        |  "data": {
        |    "values": [
        |      {"a": "A","b": 28}, {"a": "B","b": 55}, {"a": "C","b": 43},
        |      {"a": "D","b": 91}, {"a": "E","b": 81}, {"a": "F","b": 53},
        |      {"a": "G","b": 19}, {"a": "H","b": 87}, {"a": "I","b": 52}
        |    ]
        |  },
        |  "mark": "bar",
        |  "encoding": {
        |    "x": {"field": "a", "type": "ordinal"},
        |    "y": {"field": "b", "type": "quantitative"}
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
