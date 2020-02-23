package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._

class ItemFormattingSpec extends FlatSpec {

  val noFractionsXml = """<item>
      <name  key='pt_ll_1' type='place'>Βόρειον ἄκρον</name>
      <measure  type='llpair'>
          <num  type='cardinal'>ια</num>
          <num  type='fraction'/>
          <num  type='cardinal'>ξα</num>
          <num  type='fraction'/>
      </measure>
  </item>
"""
  "The GeographyPrinter object" should "parse an <item> element with empty fractional components and include ID value" in {
    val item = XML.loadString(noFractionsXml)
    println(GeographyPrinter.mdForNode(item))
  }

}
