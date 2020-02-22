package edu.holycross.shot.ptolemy

import edu.holycross.shot.pleiades._
import scala.io.Source

case class Geography (rawData: Vector[PtolemyString]) {

/*
  def simplePoints : Vector[SimplePoint] = rawData.map(pt => SimplePoint(pt))

  def scaledGeo : Vector[SimplePoint] = {
    simplePoints.map( pt => GeographicDatum.scalePoint(pt))
  }


  def ptolemyLonValues: Map[String, Double] = {
    val idPlusLonVals = scaledGeo.map{ case (pt, geo) => pt.id  -> geo.y.toDouble }
    idPlusLonVals.toMap
  }
*/
  def lonMap : Map[String, Double] = {
    Map.empty[String, Double]
  }
  
  /** Given a set of Pleiades values and a set
  * of Ptolemy values, find the average difference.
  */
  def offsetLon: Double  =
    {
    val diffs = for (k <- GeographicDatum.pleiadesLonForPtolemy.keySet) yield {
      val diff = GeographicDatum.pleiadesLonForPtolemy(k)  - lonMap(k)
      diff
    }
    val avgRaw = diffs.sum / diffs.size
    val offset = BigDecimal(avgRaw).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    offset
  }


}
