package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object GeoCircleVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.1.json",
        |  "width": 500,
        |  "height": 300,
        |  "data": {
        |    "url": "data/zipcodes.csv"
        |  },
        |  "transform": [{"calculate": "substring(datum.zip_code, 0, 1)", "as": "digit"}],
        |  "projection": {
        |    "type": "albersUsa"
        |  },
        |  "mark": "circle",
        |  "encoding": {
        |    "longitude": {
        |      "field": "longitude",
        |      "type": "quantitative"
        |    },
        |    "latitude": {
        |      "field": "latitude",
        |      "type": "quantitative"
        |    },
        |    "size": {"value": 1},
        |    "color": {"field": "digit", "type": "nominal"}
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
