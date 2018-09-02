package scalaz.viz.schema



object Doodling {

  }

// import argonaut._
// import Argonaut._

// import scalaz._
// import Scalaz._

// object Doodling {

//   // sealed trait LogicalConnective
//   // case object And extends LogicalConnective
//   // case object Or extends LogicalConnective
//   // case object Xor extends LogicalConnective

//   // sealed trait LogicHList[O <: LogicalConnective]
//   // case class LogicHCons[O <: LogicalConnective, H, T <: And](head: H, tail: T) extends LogicHList[O]
//   // case class LogicHNil[O <: LogicalConnective]() extends LogicHList[O]

//   trait Schema[F[_]] {
//     def string: F[String]
//     def number: F[Double]
//     def boolean: F[Boolean]

//     def oneOf[A](x: F[A], xs: F[A]*): F[A]
//     def keyValue[A](key: String, value: F[A]): F[A]
//   }

//   object Schema {
//     def apply[F[_]](implicit F: Schema[F]): Schema[F] = F
//   }

//   implicit class SchemaSyntax[F[_], A](fa: F[A]) {
//     def string(implicit F: Schema[F]): F[String] = F.string
//     def number(implicit F: Schema[F]): F[Double] = F.number
//     def boolean(implicit F: Schema[F]): F[Boolean] = F.boolean
//     def oneOf[A](x: F[A], xs: F[A]*)(implicit F: Schema[F]): F[A] = F.oneOf(x, xs: _*)
//     def keyValue[A](key: String, value: F[A])(implicit F: Schema[F]): F[A] = F.keyValue(key, value)
//   }

//   /*
//    * Interpreter that converts the schema to human-readable string.
//    */
//   type Str[A] = Const[String, A]
//   implicit def schemaPrinterInterpreter: Schema[Str] = new Schema[Str] {
//     def indent(s: String): String = "  " + s

//     override def string: Str[String] = Const("A string")
//     override def number: Str[Double] = Const("A number")
//     override def boolean: Str[Boolean] = Const("A boolean")
//     override def oneOf[A](x: Str[A], xs: Str[A]*): Str[A] = {
//       val xIndented = x.getConst.lines.map(indent(_)).toList
//       val xsIndented = xs.flatMap(_.getConst.lines.map(indent))
//       Const(
//         (
//           List("Exactly one of the following:") ++
//           xIndented ++
//           xsIndented
//         ).mkString("", "\n", "\n")
//       )
//     }

//     override def keyValue[A](key: String, value: Str[A]): Str[A] = {
//       val substrings = value.getConst.lines.map(indent(_)).toList
//       Const(
//         (
//           List(s"Key: $key") ++
//           List("Value:") ++
//           substrings
//         ).mkString("", "\n", "\n")
//       )
//     }
//   }

//   /*
//    * Interpreter that produces a JSON decoder.
//    */
//   implicit def jsonDecoderInterpreter: Schema[DecodeJson] = new Schema[DecodeJson] {
//     override def string: DecodeJson[String] = DecodeJson(_.as[String])
//     override def number: DecodeJson[Double] = DecodeJson(_.as[Double])
//     override def boolean: DecodeJson[Boolean] = DecodeJson(_.as[Boolean])

//     override def oneOf[A](x: DecodeJson[A], xs: DecodeJson[A]*): DecodeJson[A] = DecodeJson { json =>
//       val succeeded = for {
//         decoder <- x +: xs
//         y = decoder.decode(json) if y.result.isRight
//       } yield y

//       succeeded match {
//         case Seq(result) => result
//         case _ => DecodeResult.fail("'oneOf' constraint failed.", json.history)
//       }
//     }

//     override def keyValue[A](key: String, value: DecodeJson[A]): DecodeJson[A] = DecodeJson( json => 
//       for {
//         subJson <- (json --\ key).as[Json]
//         result <- value.decodeJson(subJson)
//       } yield result
//     )
//   }

//   /*
//    * An `Apply` instance for `DecodeJson`
//    */
//   implicit def decodeJsonApply: Apply[DecodeJson] = new Apply[DecodeJson] {
//     def map[A, B](fa: DecodeJson[A])(f: A => B): DecodeJson[B] = fa.map(f)
//     def ap[A, B](fa: => DecodeJson[A])(f: => DecodeJson[A => B]): DecodeJson[B] = f.flatMap(fa.map)
//   }

//   /*
//    * An interpreter that produces a JSON encoder.
//    */
//   implicit def jsonEncoderInterpreter: Schema[EncodeJson] = new Schema[EncodeJson] {
//     override def string: EncodeJson[String] = EncodeJson(jString)
//     override def number: EncodeJson[Double] = EncodeJson(jNumber(_))
//     override def boolean: EncodeJson[Boolean] = EncodeJson(jBool)

//     override def oneOf[A](x: EncodeJson[A], xs: EncodeJson[A]*): EncodeJson[A] =
//       EncodeJson(_ => jEmptyObject)

//     override def keyValue[A](key: String, value: EncodeJson[A]): EncodeJson[A] = 
//       EncodeJson{a => 
//         val encoded = value.encode(a)
//         (key := encoded) ->: jEmptyObject
//       }
//   }

//   // /*
//   //  * An `Apply` instance for `EncodeJson`
//   //  */
//   // def encodeJsonApply: Apply[EncodeJson] = new Apply[EncodeJson] {
//   //   def map[A, B](fa: EncodeJson[A])(f: A => B): EncodeJson[B] = fa.map(f)
//   //   def ap[A, B](fa: => EncodeJson[A])(f: => EncodeJson[A => B]): EncodeJson[B] = 
//   //   {
//   //     // We have: F[A]
//   //     //          F[A => B]

//   //     // We need: F[B]

//   //     // We can:  F[A].contramap[X](f: X => A): F[X]
//   //     //          F[A].encode(a: A): Json
//   //     //         

//   //     type F[A] = EncodeJson[A]

//   //     val blah = fa.map(identity)
//   //     println(blah)

//   //     val x: F[Int] = null
//   //     println(x)
//   //     ???
//   //     //f.flatMap(fa.map(_))
//   //   }
//   // }

//   def main(args: Array[String]): Unit = {
//     println("asdf")

//     import scalaz.viz.vega.Testing
//     import scalaz.viz.vega.Testing.Padding

//     val helpMessage: String = Testing.schema[Str].getConst
//     println(helpMessage)

//     val jsonDecoder: DecodeJson[Padding] = Testing.schema[DecodeJson]
//     // val jsonEncoder: EncodeJson[Padding] = Testing.schema[EncodeJson]

//     val someJson = 
//       """
//       {
//         "top": 1.1,
//         "bottom": 2.2,
//         "right": 3.3,
//         "left": 4.4
//       }
//       """.parseOption.get

//     jsonDecoder.decodeJson(someJson).result.toOption match {
//       case Some(result) =>
//         println("Successfully parsed result!")
//         println(result)

//         println("Re-encoding:")
//         // val reencoded = jsonEncoder.encode(result)
//         // println(reencoded)


//       case None => println("Json parse failed.")
//     }
//   }
// }