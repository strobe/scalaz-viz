package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object CircleVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "data": {"url": "data/cars.json"},
        |  "mark": "circle",
        |  "encoding": {
        |    "x": {"field": "Horsepower", "type": "quantitative"},
        |    "y": {"field": "Miles_per_Gallon", "type": "quantitative"}
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
