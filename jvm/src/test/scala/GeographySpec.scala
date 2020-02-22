package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._


class GeographySpec extends FlatSpec {
  val f = "tei/tlg0363.tlg009.epist03-p5-u8.xml"

  "A Geography object" should "do a lot" in {
    val root = XML.loadFile(f)
    val parsed = TeiParser.parseTEI(root)

  }
}
