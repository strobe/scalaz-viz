package scalaz.viz.grammar

case class Padding(left: Int, top: Int, right: Int, bottom: Int)

object Padding {
  def apply(p: Int) = Padding(p, p, p, p)
}
