package scalaz.viz.vegalite.grammar

import scalaz.viz.schema._
import shapeless._

sealed trait Spec extends Product with Serializable
object Spec {
  case class GenericFacetSpec(
    config: Option[Config],
    data: Option[UrlData Either InlineData],
    description: Option[String],
    facet: Facet,
    name: Option[String],
    padding: Option[Padding],
    // NOTE: anyOf replaced with oneOf
    spec: GenericUnitSpec Either GenericLayerSpec, 
  //  selection: // TODO selection
    transform: Option[Transform]
  ) extends Spec

  object GenericFacetSpec {
    def schema: Dsl[GenericFacetSpec] = allOf(
      opt("config" -> Config.schema) ::
      opt("data" -> oneOf(UrlData.schema :: InlineData.schema :: HNil).toEither) ::
      opt("description" -> str) ::
      "facet" -> Facet.schema ::
      opt("name" -> str) ::
      opt("padding" -> Padding.schema) ::
      "spec" -> oneOf(GenericUnitSpec.schema :: GenericLayerSpec.schema :: HNil).toEither ::
      opt("transform" -> Transform.schema) ::
      HNil
    ).to[GenericFacetSpec]
  }
  
  case class GenericLayerSpec(
    config: Option[Config],
    data: Option[UrlData Either InlineData],
    description: Option[String],
    height: Option[Double],
    // TODO: This should allow self-recursion.  Taken out for now.
//    layer: Vector[GenericUnitSpec Either GenericLayerSpec],
    layer: Vector[GenericUnitSpec],
    name: Option[String],
    padding: Option[Padding],
    transform: Option[Transform],
    width: Option[Double]
  ) extends Spec

  object GenericLayerSpec {
    def schema: Dsl[GenericLayerSpec] = allOf(
      opt("config" -> Config.schema) ::
      opt("data" -> oneOf(UrlData.schema :: InlineData.schema :: HNil).toEither) ::
      opt("description" -> str) ::
      opt("height" -> number) ::
      // TODO: This should allow a recursive call to GenericLayerSpecGrammar
  //    "layer" -> rep(oneOf(GenericUnitSpec.schema :: GenericLayerSpecG
      "layer" -> rep(GenericUnitSpec.schema) ::
      opt("name" -> str) ::
      opt("padding" -> Padding.schema) ::
      opt("transform" -> Transform.schema) ::
      opt("width" -> number) ::
      HNil
    ).to[GenericLayerSpec]
  }
  
  case class GenericUnitSpec(
    config: Option[Config],
    data: Option[UrlData Either InlineData],
    description: Option[String],
    encoding: Option[EncodingWithFacet],
    height: Option[Double],
    mark: MarkDef Either String,
    name: Option[String],
    padding: Option[Padding],
  //  selection: // TODO selection
    transform: Option[Transform],
    width: Option[Double]
  ) extends Spec

  object GenericUnitSpec {
    def schema: Dsl[GenericUnitSpec] = allOf(
      opt("config" -> Config.schema) ::
      opt("data" -> oneOf(UrlData.schema :: InlineData.schema :: HNil).toEither) ::
      opt("description" -> str) ::
      opt("encoding" -> EncodingWithFacet.schema) ::
      opt("height" -> number) ::
      "mark" -> oneOf(MarkDef.schema :: str :: HNil).toEither ::
      opt("name" -> str) ::
      opt("padding" -> Padding.schema) ::
      // selection
      opt("transform" -> Transform.schema) ::
      opt("width" -> number) ::
      HNil
    ).to[GenericUnitSpec]
  }

  // NOTE:  oneOf used instead of anyOf.
  def schema: Dsl[Spec] = oneOf(
    GenericFacetSpec.schema ::
    GenericLayerSpec.schema ::
    GenericUnitSpec.schema ::
    HNil
  ).to[Spec]
}