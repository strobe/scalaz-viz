package scalaz.viz.grammar

trait AutoSizeType
case object Pad extends AutoSizeType
case object Fit extends AutoSizeType
case object FitX extends AutoSizeType
case object FitY extends AutoSizeType
case object None extends AutoSizeType

trait Contains
object Contains {
  case object Content extends Contains
  case object Padding extends Contains  
}

case class AutoSize(
  autoSizeType: AutoSizeType,
  resize: Boolean = false,
  contains: Contains = Contains.Content
)
