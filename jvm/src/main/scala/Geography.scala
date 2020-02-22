package edu.holycross.shot.ptolemy

import edu.holycross.shot.pleiades._
import scala.io.Source


import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


case class Geography (rawData: Vector[PtolemyString]) extends LogSupport {

  /** Number of records in raw data.*/
  def size: Int = rawData.size

  /** Header line for delimited-text output of
  * for collections of [[SimplePoint]]s.
  *
  * @param delimiter Delimiting string for text output
  */
  def simpleHeader(delimiter: String = ",") = {
    Vector("id,lon,lat").mkString(delimiter) + "\n"
  }

  /** Map raw data to [[SimplePoint]]s. */
  def rawPoints : Vector[SimplePoint] = rawData.map(pt => SimplePoint(pt.id, pt.lon, pt.lat))

  /** Create delimited-text representation of
  * [[SimplePoint]]s for this data set.
  *
  * @param delimiter String to use a plain-text delimiter.
  */
  def rawDelimited(delimiter: String = ",") = {
    val data = rawPoints.map(pt => pt.delimited(delimiter)).mkString("\n")
    simpleHeader(delimiter) + data
  }


  def scaledPoints : Vector[SimplePoint] = {
    rawPoints.map( pt => GeographicDatum.scalePoint(pt))
  }
  def scaledDelimited(delimiter: String = ",") = {
    val data = scaledPoints.map(pt => pt.delimited(delimiter)).mkString("\n")
    simpleHeader(delimiter) + data
  }

  def scaledLatShiftedPoints : Vector[SimplePoint] = {
    scaledPoints.map(pt => SimplePoint(pt.id, pt.lon, GeographicDatum.shiftLatitude(pt.lat)))
  }
  def scaledLatShiftedDelimited(delimiter: String = ",") = {
    val data = scaledLatShiftedPoints.map(pt => pt.delimited(delimiter)).mkString("\n")
    simpleHeader(delimiter) + data
  }


  def scaledLonShiftedPoints : Vector[SimplePoint] = {
    val offset : Double = offsetLon
    scaledPoints.map(pt => SimplePoint(pt.id, pt.lon + offset, pt.lat))
  }
  def scaledLonShiftedDelimited(delimiter: String = ",") = {
    val data = scaledLonShiftedPoints.map(pt => pt.delimited(delimiter)).mkString("\n")
    simpleHeader(delimiter) + data
  }


  def adjustedPoints : Vector[SimplePoint] = {
    val offset : Double = offsetLon
    scaledPoints.map(pt => SimplePoint(pt.id, pt.lon + offset, GeographicDatum.shiftLatitude(pt.lat)))
  }
  def adjustedPointsDelimited(delimiter: String = ",") = {
    val data = adjustedPoints.map(pt => pt.delimited(delimiter)).mkString("\n")
    simpleHeader(delimiter) + data
  }

  /**  Map Ptolemy IDs to longitude value in rescaled
  * data set so we can compare them to modern coordinates
  * and approximate a value for Ptolemy's origin of longitude.
  */
  def scaledLonMap: Map[String, Double] = {
    scaledPoints.map( pt => pt.id  -> pt.lon).toMap
  }

  /** Given a set of Pleiades values and a set
  * of Ptolemy values, find the average difference.
  */
  def offsetLon: Double  =
    {
    info("Computing longitude offset: retrieving pleiades data")
    info("Please be patient...")
    val pl = GeographicDatum.pleiadesLonForPtolemy
    debug("Now cycle keyset of " + pl.keySet.size + " entries.")
    val pleiadesDataMap = GeographicDatum.pleiadesLonForPtolemy
    val diffs = for (k <- pl.keySet) yield {
      debug(k)
      val diff = pleiadesDataMap(k)  - scaledLonMap(k)
      diff
    }
    val avgRaw = diffs.sum / diffs.size
    val offset = BigDecimal(avgRaw).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    info("Done retrieving pleiades data.")
    offset
  }


}
