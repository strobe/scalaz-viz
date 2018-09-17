package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object CircleGithubPunchCardVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "description": "Punchcard Visualization like on Github. The day on y-axis uses a custom order from Monday to Sunday.  The sort property supports both full day names (e.g., 'Monday') and their three letter initials (e.g., 'mon') -- both of which are case insensitive.",
        |  "data": { "url": "data/github.csv"},
        |  "mark": "circle",
        |  "encoding": {
        |    "y": {
        |      "field": "time",
        |      "type": "ordinal",
        |      "timeUnit": "day",
        |      "sort": ["mon", "tue", "wed", "thu", "fri", "sat", "sun"]
        |    },
        |    "x": {
        |      "field": "time",
        |      "type": "ordinal",
        |      "timeUnit": "hours"
        |    },
        |    "size": {
        |      "field": "count",
        |      "type": "quantitative",
        |      "aggregate": "sum"
        |    }
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty
    
    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
