package scalaz.viz.examples

import argonaut._
import Argonaut._
import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import scalaz.viz.interpreter.JsonEncoder
import scalaz.viz.interpreter.JsonDecoder
import shapeless._
import shapeless.poly._

/**
 * Created by Evgeniy Tokarev on 2018-09-13.
 */
object JsonEncoderApp {

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
 }""".parseOption.get

    import scalaz.viz.vegalite.grammar.{ Scale, Spec }
    import ArgonautScalaz._
    import scalaz._
    import Scalaz._

    val result = JsonDecoder.getDecoder(Spec.schema).decodeJson(visJson)
    result.result match {
      case Left((errorMessage, history)) =>
        println("Failed decode.")
        println(errorMessage)
        println(history.shows)
      case Right(spec) =>
        println(spec)
        val specEncoded = JsonEncoder.getEncoder(Spec.schema).encode(spec)
        println(specEncoded.spaces2)
    }

    val scaleJson = """
 {
   "name": "x",
   "type": "ordinal",
   "range": "width",
   "domain": {"data": "table"}
 }
                    """.parseOption.get

    val decodeScale = JsonDecoder.getDecoder(Scale.schema).decodeJson(scaleJson)
    println(decodeScale)

    decodeScale.result.foreach { scale =>
      println("Scale:  " + scale)
      val enc = JsonEncoder.getEncoder(Scale.schema)
      println(enc)
      println(enc.encode(scale))
    }
  }
}
