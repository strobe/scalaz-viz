package scalaz.viz.interpreter

import argonaut._
import Argonaut._
import scalaz.viz.schema._
import scalaz.viz.schema.Algebra._
import scalaz.viz.schema.GenDsl
import shapeless._
import shapeless.poly._

object JsonEncoder {
  type Result[A, B] = EncodeJson[A]
  
  def getEncoder[X, Y](dsl: GenDsl[X, Y]): EncodeJson[X] = dsl.apply(new Schema[Result] {
    override def vString: EncodeJson[String] = EncodeJson(_.asJson)
    override def vNumber: EncodeJson[Double] = EncodeJson(_.asJson)
    override def vBoolean: EncodeJson[Boolean] = EncodeJson(_.asJson)
    override def vJson: EncodeJson[Json] = EncodeJson(identity)

    override def vRep[A](item: Dsl[A]): EncodeJson[Vector[A]] = {
      val c = getEncoder(item)
      EncodeJson[Vector[A]] { vec =>
        val jsons = vec.map(c.encode)
        Json.array(jsons: _*)
      }
    }

    override def vKeyValue[A, B](key: String)(value: GenDsl[A, B]): EncodeJson[A] =
      EncodeJson(a => (key := getEncoder(value).encode(a)) ->: jEmptyObject)

    override def vDimap[A, B, C, D](fbc: GenDsl[B, C])(f: A => B)(g: C => D): EncodeJson[A] = getEncoder(fbc).contramap(f)

    override def vPair[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): EncodeJson[(A, C)] = {
      val c1 = getEncoder(ab)
      val c2 = getEncoder(cd)

      EncodeJson[(A, C)] {
        case (a, c) =>
          c1(a).deepmerge(c2(c))
      }
    }

    override def vSum[A, B, C, D](ab: GenDsl[A, B])(cd: GenDsl[C, D]): EncodeJson[A Either C] = {
      val c1 = getEncoder(ab)
      val c2 = getEncoder(cd)

      EncodeJson[A Either C] {
        case Left(a)  => c1(a)
        case Right(c) => c2(c)
      }
    }

    override def vAllOf[H <: HList, R <: HList, S <: HList](dsls: H)(implicit cse: Case.Aux[ChangeAllOf.type, H :: HNil, GenDsl[R, S]]): EncodeJson[R] = getEncoder(ChangeAllOf(dsls))

    override def vOneOf[H <: HList, R <: Coproduct, S <: Coproduct](dsls: H)(implicit cse: Case.Aux[ChangeOneOf.type, H :: HNil, GenDsl[R, S]]): EncodeJson[R] = {
      getEncoder(ChangeOneOf(dsls))
    }

    override def vTagged[A, B](tag: String)(dsl: => GenDsl[A, B]): EncodeJson[A] = JsonEncoder.getEncoder(dsl)

    // TODO:  What happens here when the const does not match the input string?
    override def vConst(const: String): EncodeJson[String] = EncodeJson { _ => const.asJson }

    override def vOpt[A, B](dsl: GenDsl[A, B]): EncodeJson[Option[A]] = EncodeJson{ aOpt =>
      aOpt match {
        case Some(a) => getEncoder(dsl).encode(a)
        case None => jEmptyObject
      }
    } 

    override def vOpt[A, B](dsl: GenDsl[A, B], defaultValue: B): EncodeJson[A] = getEncoder(dsl)

    override def vGetKey[A, B](value: GenDsl[A, B]): EncodeJson[(String, A)] = EncodeJson[(String, A)] { pair =>
      implicit val aEnc = getEncoder(value)
      val ret = pair.asJson
      ret
    }
    
    override def vDescription[A, B](desc: String)(dsl: GenDsl[A, B]): EncodeJson[A] = getEncoder(dsl)
    
    override def vBoundedInt(min: Option[Int], max: Option[Int]): EncodeJson[Int] = EncodeJson(_.asJson)
      
    override def vBoundedNumber(min: Option[Double], max: Option[Double]): EncodeJson[Double] = EncodeJson(_.asJson)
      
    override def vInt: EncodeJson[Int] = EncodeJson(_.asJson)
    
    override def vRep[A](item: Dsl[A], minItems: Option[Int], maxItems: Option[Int]): EncodeJson[Vector[A]] = {
      val c = getEncoder(item)
      EncodeJson[Vector[A]] { vec =>
        val jsons = vec.map(c.encode)
        Json.array(jsons: _*)
      }
    }
  })

  // TODO: Get this out of here and into "examples" folder
  def main(args: Array[String]): Unit = {
    import scalaz.viz.vegalite.grammar.Padding

    val padding = Padding.NonUniform(1.1, 2.2, 3.3, 4.4)
    val encoder = JsonEncoder.getEncoder(Padding.schema)
    val json = encoder.encode(padding)
    println(json.spaces2)
  }
  
//  def main(args: Array[String]): Unit = {
//    val visJson = """
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
//        println("Failed decode.")
//        println(errorMessage)
//        import scalaz._, Scalaz._
//        println(history.shows)
//      case scalaz.\/-(spec) => 
//        println(spec)
//        val specEncoded = JsonEncoder.getEncoder(Spec.prog).encode(spec)
//        println(specEncoded.spaces2)
//    }
//    
//    val scaleJson = """
//      {
//              "name": "x",
//              "type": "ordinal",
//              "range": "width",
//              "domain": {"data": "table"}
//            }
//      """.parseOption.get
//    val decodeScale = JsonDecoder.getDecoder(org.oclc.vega.grammar.Scale.prog).decodeJson(scaleJson) 
//    println(decodeScale)
//    
//    decodeScale.result.foreach { scale =>  
//      println("Scale!  " + scale)
//      val enc = JsonEncoder.getEncoder(org.oclc.vega.grammar.Scale.prog)
//      println(enc)
//      println(enc.encode(scale))  
//    }
//    
//  }
}