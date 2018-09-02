package scalaz.viz.interpreter

import argonaut._
import Argonaut._
import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import shapeless._
import shapeless.poly._

object JsonDecoder {
  type Result[A, B] = DecodeJson[B]

  def getDecoder[X, Y](dsl: GenDsl[X, Y]): DecodeJson[Y] = dsl.apply(new Schema[Result] {
    override def vString: DecodeJson[String] = DecodeJson.of[String]
    override def vNumber: DecodeJson[Double] = DecodeJson.of[Double]
    override def vBoolean: DecodeJson[Boolean] = DecodeJson.of[Boolean]
    override def vJson: DecodeJson[Json] = DecodeJson.of[Json]
    
    override def vConst(value: String): DecodeJson[String] = DecodeJson{json =>
      json.as[String].flatMap{s =>
        if (s == value)
          DecodeResult.ok(s)
        else
          DecodeResult.fail(s"Expected '$value' but found '$s'.", json.history)
      }
    }

    override def vRep[A](item: Dsl[A]): DecodeJson[Vector[A]] = DecodeJson { json =>
      implicit val itemDecoder = getDecoder(item)
      json.as[List[A]].map(_.toVector)
    }

    override def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): DecodeJson[B] = DecodeJson { json =>
      for {
        subJson <- (json --\ key).as[Json]
        result <- getDecoder(value).decodeJson(subJson)
      } yield result
    }

    override def vDimap[A, B, C, D](fbc: GenDsl[B, C])(f: A => B)(g: C => D): DecodeJson[D] = getDecoder(fbc).map(g)

    override def vPair[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): DecodeJson[(B, D)] =
      for {
        b <- getDecoder(ab)
        d <- getDecoder(cd)
      } yield (b, d)

    override def vSum[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): DecodeJson[B Either D] = DecodeJson { json =>
      val result1 = getDecoder(ab).decode(json)
      val result2 = getDecoder(cd).decode(json)
      
      result1.map(Left(_)) ||| result2.map(Right(_))
    }

    override def vAllOf[H <: HList, R <: HList, S <: HList](dsls: H)(implicit cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]): DecodeJson[S] = {
      getDecoder(ChangeAllOf(dsls))
    }

    override def vOneOf[H <: HList, R <: Coproduct, S <: Coproduct](dsls: H)(implicit cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]): DecodeJson[S] = {
      getDecoder(ChangeOneOf(dsls))
    }

    override def vTagged[A, B](tag: String)(dsl: => GenDsl[A, B]): DecodeJson[B] = getDecoder(dsl)

    override def vOpt[A, B](dsl: GenDsl[A, B]): DecodeJson[Option[B]] = DecodeJson { json =>
      DecodeResult.ok(
          getDecoder(dsl)
            .decode(json)
            .fold((_, _) => None, Some(_))
      )
    }

    override def vOpt[A, B](dsl: GenDsl[A, B], defaultValue: B): DecodeJson[B] = DecodeJson { json =>
      val a = getDecoder(dsl).decode(json).getOr(defaultValue)
      DecodeResult.ok(a)
    }

    override def vGetKey[A, B](value: GenDsl[A, B]): DecodeJson[(String, B)] = DecodeJson { json =>
      // TODO: Need to get the key
      getDecoder(value).decode(json).map(a => ("TODO", a))
    }
    
    override def vDescription[A, B](desc: String)(dsl: GenDsl[A, B]): DecodeJson[B] = getDecoder(dsl) 
    
    override def vBoundedInt(min: Option[Int], max: Option[Int]): DecodeJson[Int] = DecodeJson[Int]{ json =>
      DecodeJson.of[Int].decode(json).flatMap{i =>
        if (min.forall(i >= _) && max.forall(i <= _))
          DecodeResult.ok(i)
        else {
          val lower = min.map(_.toString).getOrElse("-∞")
          val upper = max.map(_.toString).getOrElse("+∞")
          DecodeResult.fail(s"Int value '$i' must be in the range [$lower, $upper]", json.history)
        }
      }
    }
      
    override def vBoundedNumber(min: Option[Double], max: Option[Double]): DecodeJson[Double] = DecodeJson[Double]{ json =>
      DecodeJson.of[Double].decode(json).flatMap{i =>
        if (min.forall(i >= _) && max.forall(i <= _))
          DecodeResult.ok(i)
        else {
          val lower = min.map(_.toString).getOrElse("-∞")
          val upper = max.map(_.toString).getOrElse("+∞")
          DecodeResult.fail(s"Number value '$i' must be in the range [$lower, $upper]", json.history)
        }
      }
    }
      
    override def vInt: DecodeJson[Int] = DecodeJson.of[Int]
    
    override def vRep[A](item: Dsl[A], minItems: Option[Int], maxItems: Option[Int]): DecodeJson[Vector[A]] = DecodeJson { json =>
      implicit val itemDecoder = getDecoder(item)
      json.as[List[A]].map(_.toVector).flatMap{vec =>
        if (minItems.forall(vec.length >= _) && maxItems.forall(vec.length <= _))
          DecodeResult.ok(vec)
        else {
          val lower = minItems.map(_.toString).getOrElse("-∞")
          val upper = maxItems.map(_.toString).getOrElse("+∞")
          DecodeResult.fail(s"Number of items '${vec.length}' must be in the range [$lower, $upper]", json.history)
        }
      }
    }
  })

// TODO: Get this out of here and into an "examples" folder
def main(args: Array[String]): Unit = {
  import scalaz.viz.vegalite.grammar.Padding

  val json = """
    {
      "top":    1.1,
      "right":  2.2,
      "bottom": 3.3,
      "left":   4.4
    }
  """.parseOption.get // Yeah I know

  val result = JsonDecoder.getDecoder(Padding.schema).decodeJson(json)
  result.result match {
    case Left((errorMessage, history)) => 
      println("Failed.")
      println(errorMessage)
      println(history)
    case Right(padding) => 
      println("Success")
      println(padding)
    }

}
  
//  def main(args: Array[String]): Unit = {
//        val visJson = """
//        {
//          "width": 400,
//          "height": 200,
//          "padding": {"top": 10, "left": 30, "bottom": 30, "right": 10},
//          "data": [
//            {
//              "name": "table",
//              "values": [
//                {"x": 1,  "y": 28}, {"x": 2,  "y": 55},
//                {"x": 3,  "y": 43}, {"x": 4,  "y": 91},
//                {"x": 5,  "y": 81}, {"x": 6,  "y": 53},
//                {"x": 7,  "y": 19}, {"x": 8,  "y": 87},
//                {"x": 9,  "y": 52}, {"x": 10, "y": 48},
//                {"x": 11, "y": 24}, {"x": 12, "y": 49},
//                {"x": 13, "y": 87}, {"x": 14, "y": 66},
//                {"x": 15, "y": 17}, {"x": 16, "y": 27},
//                {"x": 17, "y": 68}, {"x": 18, "y": 16},
//                {"x": 19, "y": 49}, {"x": 20, "y": 15}
//              ]
//            }
//          ],
//          "scales": [
//            {
//              "name": "x",
//              "type": "ordinal",
//              "range": "width",
//              "domain": {"data": "table", "field": "x"}
//            },
//            {
//              "name": "y",
//              "type": "linear",
//              "range": "height",
//              "domain": {"data": "table", "field": "y"},
//              "nice": true
//            }
//          ],
//          "axes": [
//            {"type": "x", "scale": "x"},
//            {"type": "y", "scale": "y"}
//          ],
//          "marks": [
//            {
//              "type": "rect",
//              "from": {"data": "table"},
//              "properties": {
//                "enter": {
//                  "x": {"scale": "x", "field": "x"},
//                  "width": {"scale": "x", "band": true, "offset": -1},
//                  "y": {"scale": "y", "field": "y"},
//                  "y2": {"scale": "y", "value": 0}
//                },
//                "update": {
//                  "fill": {"value": "steelblue"}
//                },
//                "hover": {
//                  "fill": {"value": "red"}
//                }
//              }
//            }
//          ]
//        }""".parseOption.get
//        
//    import org.oclc.vega.grammar.Spec
//    val result = JsonDecoder.getDecoder(Spec.prog).decodeJson(visJson)
//    result.result match {
//      case scalaz.-\/((errorMessage, history)) => 
//        println("Failed.")
//        println(errorMessage)
//        import scalaz._, Scalaz._
//        println(history.shows)
//      case scalaz.\/-(blah) => 
//        println("Success")
//        println(blah)
//    }
//  }
}