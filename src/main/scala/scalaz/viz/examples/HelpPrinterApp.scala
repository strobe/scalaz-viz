package scalaz.viz.examples

import scalaz.viz.interpreter.HelpPrinter

/**
 * Created by Evgeniy Tokarev on 2018-09-13.
 */
object HelpPrinterApp {

  def main(args: Array[String]): Unit = {
    import scalaz.viz.vegalite.grammar.Padding

    val printed = HelpPrinter.print(Padding.schema)
    println(printed)
  }
}
