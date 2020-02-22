package edu.holycross.shot.ptolemy

import edu.holycross.shot.pleiades._
import scala.io.Source

object GeographicDatum  {


  /** Searchable data set of geogrpahic data from Pleiades.*/
  lazy val pleiades = PleiadesDataSource.loadFromUrls()

  /** Latitude through Rhodes is Ptolemy's base latitude line.*/
  val rhodes = 36.0

  /** Rescale circumference of earth by ratio of Ptolemy's
  * circumference of 180,000 stades to Eratosthenes' circumference
  * of 250,000 stades.*/
  val scale = 18.0 / 25.0

  /** Difference between Ptolemy's baseline in raw
  * coordinates and baseline latitude in adjusted
  * coordiantes, to two decimal places. */
  def offsetLat: Double = {
    val offsetLatRaw = rhodes - (scale * rhodes)
    BigDecimal(offsetLatRaw).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }


  /** Map of Ptolemy IDs to coordinate values
  * retrieved from Pleiades.
  */
  def pleiadesLonForPtolemy: Map[String, Double] = {
    val url = "https://raw.githubusercontent.com/neelsmith/ptolemy/master/data/ptolemy2pleiades.csv"
    val lines = Source.fromURL(url).getLines.toVector

    val ptOpts = for (ln <- lines.tail) yield {
      val cols = ln.split(",")
      val pl = pleiades.lookup(BigDecimal(cols(2)))
      (cols(0), cols(1), pl.get.pointOption)
    }
    val pts = ptOpts.filter(_._3 != None)

    // Create map of Ptolemy IDs to Pleiades longitude values:
    val pleiadesLonValues = pts.map(p => p._1 -> p._3.get.y.toDouble)
    pleiadesLonValues.toMap
  }



  /** Create a new [[SimplePoint]] scaled to a Eratosthenic
  * value for earth's size.
  *
  * @param pt Point to rescale.
  */
  def scalePoint(pt: SimplePoint): SimplePoint = {
    SimplePoint(pt.id, pt.lon * scale, pt.lat * scale)
  }

  /** Create a new scaled and adjusted latitude
  * value from a raw Ptolemaic value.
  *
  * @rawLat Ptolemy's latitude value for a point.
  */
  def adjustLatitude(rawLat: Double): Double = {
    (rawLat * scale) + offsetLat
  }




}
