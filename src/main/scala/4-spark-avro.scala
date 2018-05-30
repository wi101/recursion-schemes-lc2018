package lc2018

import lc2018.solutions._
import matryoshka._
import matryoshka.data.Fix
import matryoshka.implicits._
import matryoshka.patterns.EnvT
import org.apache.avro.Schema
import scalaz._
import scalaz.Scalaz._

import scala.language.higherKinds
import org.apache.avro.generic.{GenericContainer, GenericData, GenericRecordBuilder}
import org.apache.spark.sql.Row

import scala.collection.immutable.ListMap
import scala.language.higherKinds

/**
  * It's time to confront ourselves to the real world of manipulating data with Spark & Avro
  * Two specific pain points we have to tackle are :
  *
  * - Spark's org.apache.spark.sql.Row is basically a wrapper of Array[Any]
  * but we need to handle two specifically different behaviour according to the level of the data :
  * When we're handling Arrays and Structs, no worry we need to output a Row
  * but when we're handling "simple" types, then if it's a top-level value we need to output a Row
  * but if it's not, then the value itself must be written.
  *
  * Exemple :
  *   - Value("b") will be Row("b")
  * but
  *   - Struct(a -> Value("b")) will be Row("b") as well (the Row now representing the outer struct)
  *
  * - For Apache Avro, it's a new kind of pain you'll need to overcome, Avro basically represents all of its data
  * as if, it will be at one point or another generated into Java classes.
  * So every "record" or Struct needs to have a qualified name "unique" otherwise the Avro engine will consider
  * the struct as being the same class.
  * But as it will obviously have different fields - you'll most likely end up with an error.
  *
  * Good hunting.
  */
object SparkConverter {

  def fromGDataToSparkRow(row: Fix[GData]): Row = ???
}

object AvroConverter extends SchemaToAvroAlgebras {

  import scala.collection.JavaConverters._

  /**
    * Generic piece of data "decorated" with the corresponding Avro Schema
    */
  type DataWithSchema[A] = EnvT[Schema, GData, A]

  case class Incompatibility(schema: Schema, data: Fix[GData])

  def fromGDataToAvro(schema: Fix[SchemaF], data: Fix[GData]): \/[Incompatibility, GenericContainer] = ???

}

trait GDataInstances {}