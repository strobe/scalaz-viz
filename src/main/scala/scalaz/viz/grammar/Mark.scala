package scalaz.viz.grammar

/**
  * Graphical marks visually encode data using geometric primitives such as
  * rectangles, lines, and plotting symbols. Marks are the basic visual building
  * block of a visualization, providing basic shapes whose properties can be set
  * according to backing data. Mark property definitions may be simple constants
  * or data fields, or scales can be used to map data values to visual values.
  *
  * @param markType The graphical mark type. Must be one of the supported mark
  *                 types.
  * @param clip Indicates if the marks should be clipped to a specified shape.
  *             If boolean-valued, the clipping region is the enclosing group’s
  *             width and height (default false). If object-valued, should
  *             specify either an arbitrary SVG path string or a cartographic
  *             projection with which to clip to the sphere of the Earth.
  * @param description An optional description of this mark. Can be used as a
  *                    comment.
  * @param encode An object containing a set of visual encoding rules for mark
  *               properties.
  * @param from An object describing the data this mark set should visualize.
  *             If undefined, a single element data set containing an empty
  *             object is assumed. The from property can either specify a data
  *             set to use (e.g., {"data": "table"}) or provide a faceting
  *             directive to subdivide a data set across a set of group marks.
  * @param interactive A boolean flag (default true) indicating if the marks can
  *                    serve as input event sources. If false, no mouse or touch
  *                    events corresponding to the marks will be generated. This
  *                    property can also take a Signal value to dynamically
  *                    toggle interactive status.
  * @param key A data field to use as a unique key for data binding. When a
  *            visualization’s data is updated, the key value will be used to
  *            match data elements to existing mark instances. Use a key field
  *            to enable object constancy for transitions over dynamic data.
  * @param name A unique name for the mark. This name can be used to refer to
  *             these marks within an event stream definition. SVG renderers
  *             will add this name value as a CSS class name on the enclosing
  *             SVG group (g) element containing the mark instances.
  * @param on A set of triggers for modifying mark properties in response to
  *           signal changes.
  * @param sort A comparator for sorting mark items. The sort order will
  *             determine the default rendering order. The comparator is defined
  *             over generated scenegraph items and sorting is performed after
  *             encodings are computed, allowing items to be sorted by size or
  *             position. To sort by underlying data properties in addition to
  *             mark item properties, append the prefix datum. to a field name
  *             (e.g., {"field": "datum.field"}).
  * @param transform A set of post-encoding transforms, applied after any encode
  *                  blocks, that operate directly on mark scenegraph items (not
  *                  backing data objects). These can be useful for performing
  *                  layout with transforms that can set x, y, width, height,
  *                  etc. properties. Only data transforms that do not generate
  *                  or filter data objects may be used.
  * @param role A metadata string indicating the role of the mark. SVG renderers
  *             will add this role value (prepended with the prefix role-) as a
  *             CSS class name on the enclosing SVG group (g) element containing
  *             the mark instances. Roles are used internally by Vega to guide
  *             layout. Do not set this property unless you know which layout
  *             effect you are trying to achieve.
  * @param style A string or array of strings indicating the name of custom
  *              styles to apply to the mark. A style is a named collection of
  *              mark property defaults defined within the configuration. These
  *              properties will be applied to the mark’s enter encoding set,
  *              with later styles overriding earlier styles. Any properties
  *              explicitly defined within the mark’s encode block will override
  *              a style default.
  * @param zindex The integer z-index indicating the layering of this mark set
  *               relative to other marks, axes, or legends. The default value
  *               is 0; higher values (1) will cause this mark set to be drawn
  *               on top of other mark, axis, or legend definitions with lower
  *               z-index values. Note that this value applies to the all marks
  *               in a set, not individual mark items. To adjust the ordering of
  *               items within a set, use the zindex encoding channel.
  */
case class Mark(
  markType: MarkType,
  clip: Clip = NoClip,
  description: Option[String] = None,
  encode: Encode,
  from: Option[From],
  interactive: Boolean = true,
  key: Field,
  name: String,
  on: Seq[Trigger] = Seq.empty,
  sort: Compare,
  transform: Seq[Transform] = Seq.empty,
  role: Option[String] = None,
  style: Seq[String] = Seq.empty,
  zindex: Int = 0
)
