package scalaz.viz

import argonaut.{ArgonautScalaz, DecodeResult, Json}
import scalaz.viz.vegalite.grammar.Spec

package object tests {

  def compareWDecoded(className: String, json: Json, decodeResult: DecodeResult[Spec]) = {

    import testz._
    import scalaz.viz.interpreter.JsonEncoder
    import ArgonautScalaz._
    import scalaz._
    import Scalaz._
    import scalaz.viz.vegalite.grammar.Spec

    decodeResult.result match {
      case Left((errorMessage, history)) =>
        println(s"error at $className - $errorMessage, $history")
        Fail()
      case Right(result) =>
        val encodedJson = JsonEncoder.getEncoder(Spec.schema).encode(result)
        assert(json === encodedJson)
    }
  }

}
