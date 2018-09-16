package scalaz.viz.tests

import scala.concurrent.Future
import testz._
import testz.runner.TestOutput

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.Duration

/**
  * Running set of Testz test suites
  *
  * @note:
  *   as example see testz/tests/src/main/scala/testz/Main.scala
  *   at scalaz/testz
  */
object Main {
  def main(args: Array[String]): Unit = {

    val ec = global

    val printer: (Result, List[String]) => Unit  = (result, name) =>
      println(s"${name.reverse.mkString("[\""," - ", "\"]")} -> $result")

    val harness: Harness[PureHarness.Uses[Unit]] =
      PureHarness.makeFromPrinter(printer)

    def unitTests = TestOutput.combineAll1(
      SpecSuite.tests(harness)((), List("Spec tests")),
    )

    val testOutputs: List[() => Future[TestOutput]] = List(
       Future(unitTests)(ec)
    ).map(s => () => s)
    
    val runSuites = runner(testOutputs, Console.print(_), ec)
    val result = Await.result(runSuites, Duration.Inf)

    if (result.failed) throw new Exception("Some Test Failed!!!")

  }
}
