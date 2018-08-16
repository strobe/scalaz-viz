package scalaz.viz.grammar

/**
  * All visual mark property definitions are specified as name-value pairs in a
  * property set (such as update, enter, or exit). The name is simply the name
  * of the visual property: individual mark types support standardized encoding
  * channel names, but arbitrary names are also allowed, resulting in new named
  * properties on output scenegraph items. The value of a property definition
  * should be a value reference or production rule, as defined below.
  *
  * The enter set is invoked when a mark item is first instantiated and also
  * when a visualization is resized. Unless otherwise indicated, the update set
  * is invoked whenever data or display properties update. The exit set is
  * invoked when the data value backing a mark item is removed. If hover
  * processing is requested on the Vega View instance, the hover set will be
  * invoked upon mouse hover.
  *
  * Custom encoding sets with arbitrary names are also allowed. To invoke a
  * custom encoding set (e.g., instead of the update set), either pass the
  * encoding set name to the Vega View run method or define a signal event
  * handler with an "encode" directive.
  */
case class Encode(propertySets: Map[String, EncodingSet])

trait EncodingSet extends Map[String, PropertyDefinition]

trait PropertyDefinition
/**
  * A value reference specifies the value for a given mark property. The value
  * may be a constant or drawn from a data object. In addition, the value may be
  * run through a scale transform and further modified. Examples include:
  * - {"value": "left"} - Literal value
  * - {"field": "amount"} - Data field value
  * - {"scale": "yscale", "field": "amount"} - Scale-transformed data field value
  * {"signal": "sqrt(pow(datum.a, 2) + pow(datum.b, 2))" - Signal expression value
  *
  * For more, see the Value type documentation, including the specialized Color
  * Value and Field Value types.
  */
trait ValueReference extends PropertyDefinition
trait ColorValue extends ValueReference
trait FieldValue extends ValueReference

/**
  * Visual properties can also be set by evaluating an “if-then-else” style
  * chain of production rules. Rules consist of an array of value reference
  * objects, each of which must contain an additional test property. The value
  * of this property should be a predicate expression, that evaluates to true or
  * false. The visual property is set to the value reference corresponding to
  * the first predicate that evaluates to true within the rule.
  *
  * A single value reference, without a test property, can be specified as the
  * final element within the rule to serve as the “else” condition. If no test
  * properties evaluate to true, the property is set to this final
  * (predicate-less) value reference. If no “else” condition is specified, the
  * property value defaults to null.
  */
trait ProductionRule extends PropertyDefinition
