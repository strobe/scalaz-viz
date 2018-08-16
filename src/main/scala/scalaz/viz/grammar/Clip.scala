package scalaz.viz.grammar

/**
  * The clip property limits the area in which a set of marks is visible.
  */
sealed trait Clip

/**
  * The default (boolean false) is to disable clipping. A boolean true value
  * clips the marks to the width and height of the enclosing group mark. The
  * clip property can also accept a signal that should evaluate to a boolean
  * value.
  */
case object NoClip extends Clip
case object EnclosingGroupClip extends Clip

/**
  * Alternatively, an object specification can be used to define more
  * sophisticated clipping regions. An object-valued clipping specification can
  * take either a path (for arbitrary SVG paths) or a sphere property (to clip
  * to the globe, relative to a geographic projection), but not both. Both
  * properties can be signal values to enable dynamic clipping regions.
  */
case class PathClip(path: String) extends Clip // Or Signal

/**
  * @param name - The name of a cartogrpahic projection with which to clip all
  *             marks to the projected sphere of the globe. This option is
  *             useful in conjunction with map projections that otherwise
  *             included projected content (such as graticule lines) outside the
  *             bounds of the globe.
  */
case class SphereClip(name: String) extends Clip // Or Signal

