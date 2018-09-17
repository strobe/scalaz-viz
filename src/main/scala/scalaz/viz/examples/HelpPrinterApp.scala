package scalaz.viz.examples

import scalaz.viz.interpreter.HelpPrinter

object HelpPrinterApp {

  def main(args: Array[String]): Unit = {
    import scalaz.viz.vegalite.grammar.Padding

    val printed = HelpPrinter.print(Padding.schema)
    println(printed)
  }
}
