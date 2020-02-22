package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._


class TeiParserSpec extends FlatSpec {
  val f = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
  val root = XML.loadFile(f)

  "The TeiParser object" should "parse a whole TEI document" in {
    val parsed = TeiParser.parseTEI(root)
    val expectedRecords = 6289
    assert(parsed.size == expectedRecords)
  }
  it should "allow omission of a header line" in {
    val parsed = TeiParser.parseTEI(root, false)
    val expectedRecords = 6288
    assert(parsed.size == expectedRecords)
  }

  it should "create a Geography instance" in {
    val geo = TeiParser.geography(root)
    val expectedRecords = 6288
    assert(geo.size == expectedRecords)
  }
}
