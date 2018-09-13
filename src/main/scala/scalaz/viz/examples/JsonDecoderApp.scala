package scalaz.viz.examples

import argonaut._
import Argonaut._
import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import scalaz.viz.interpreter.JsonDecoder
import shapeless._
import shapeless.poly._

/**
 * Created by Evgeniy Tokarev on 2018-09-13.
 */
object JsonDecoderApp {

  def main(args: Array[String]): Unit = {
    val visJson = """
   {
     "$schema": "https://vega.github.io/schema/vega-lite/v2.json",
     "description": "A simple bar chart with embedded data.",
     "data": {
       "values": [
         {"a": "A","b": 28}, {"a": "B","b": 55}, {"a": "C","b": 43},
         {"a": "D","b": 91}, {"a": "E","b": 81}, {"a": "F","b": 53},
         {"a": "G","b": 19}, {"a": "H","b": 87}, {"a": "I","b": 52}
       ]
     },
     "mark": "bar",
     "encoding": {
       "x": {"field": "a", "type": "ordinal"},
       "y": {"field": "b", "type": "quantitative"}
     }
   }""".parseOption.get // TODO obviously naughty

    import scalaz.viz.vegalite.grammar.Spec
    import ArgonautScalaz._
    import scalaz._
    import Scalaz._
    val result = JsonDecoder.getDecoder(Spec.schema).decodeJson(visJson)
    result.result match {
      case Left((errorMessage, history)) =>
        println("Failed.")
        println(errorMessage)
        println(history.shows)
      case Right(result) =>
        println("Success")
        println(result)
    }
  }
}
