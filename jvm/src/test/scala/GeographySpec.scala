package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._


class GeographySpec extends FlatSpec {
  val f = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
  val root = XML.loadFile(f)
  val geo = TeiParser.geography(root)
  val expectedRecords = 6288

  "A Geography object" should "count number of lon-lat points" in {
    assert(geo.size == expectedRecords)
  }

  it should "create a Vector of raw SimplePoints" in {
    val simple = geo.rawPoints
    assert(simple.size == expectedRecords)
  }
  it should "create a Vector of scaled SimplePoints" in {
    assert(geo.scaledPoints.size == expectedRecords)
  }
  it should "create a Vector of scaled SimplePoints with adjusted latitude" in pending
  it should "create a Vector of scaled SimplePoints with adjusted longitude" in pending
  it should "create a Vector of scaled SimplePoints with adjusted longitude and latitude" in pending
  it should "create a CSV representation of full data set" in pending
}
