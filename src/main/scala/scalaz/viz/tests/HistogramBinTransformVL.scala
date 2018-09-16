package scalaz.viz.tests

import argonaut.Argonaut._
import argonaut._
import scalaz.viz.interpreter.JsonDecoder
import testz._

object HistogramBinTransformVL {

  /**
    * Comparing content of encoded json with parsed json.
    */
  def test: () => Result = () => {
    import scalaz.viz.vegalite.grammar.Spec

    val spec =
      """
        |{
        |  "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
        |  "data": {"url": "data/movies.json"},
        |  "transform": [{
        |    "bin": true,
        |    "field": "IMDB_Rating",
        |    "as": "bin_IMDB_Rating"
        |  }],
        |  "mark": "bar",
        |  "encoding": {
        |    "x": {
        |      "field": "bin_IMDB_Rating",
        |      "title": "IMDB_Rating (binned)",
        |      "bin": "binned",
        |      "type": "quantitative",
        |      "axis": {
        |        "tickStep": 1
        |      }
        |    },
        |    "x2": {
        |      "field": "bin_IMDB_Rating_end",
        |      "type": "quantitative"
        |    },
        |    "y": {
        |      "aggregate": "count",
        |      "type": "quantitative"
        |    }
        |  }
        |}
      """.stripMargin

    val json = spec.parseOption.get // TODO obviously naughty

    val decoded: DecodeResult[Spec] = JsonDecoder.getDecoder(Spec.schema).decodeJson(json)

    compareWDecoded(this.getClass.getSimpleName, json, decoded)
  }

}
