package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._

class SouthItemParserSpec extends FlatSpec {

  val equator = """    <item > καθ᾽ ὃ συμβάλλει <name  key='pt_ll_5669' type='place'>τῷ Σαίνῳ ποταμῷ</name>
          <measure  type='llpair'><num  type='cardinal'>ρπ</num>
              <num  type='fraction'/>
              <num  type='cardinal' value='0'> ἰσημερινός</num>
              <num  type='fraction'/>
          </measure>
      </item>
"""

  val south = """<item >
          <name  key='pt_ll_5773' type='place'>Καττίγαρα, ὅρμος Σινῶν</name>
          <measure  type='llpair'>
              <num  type='cardinal'>ροζ</num>
              <num  type='fraction'/>
              <num  n='NO/T' type='cardinal'>η</num>
              <num  n='NO/T' type='fraction'>𐅵 </num>
          </measure>. </item>
"""



  "The TeiParser object" should "recognize latitudes on the equator" in  {
    val item = XML.loadString(equator)
    val delimited = TeiParser.parseItem(item)
    val ptString = PtolemyString("7.3.3#Asia#sinae#paralios#" + delimited)
    assert(ptString.lat == 0.0)
  }


  it should "recognize latitudes south of the equator" in {
    val item = XML.loadString(south)
    val delimited = TeiParser.parseItem(item)
    val ptString = PtolemyString("7.3.3#Asia#sinae#paralios#" + delimited)
    assert(ptString.latDeg == "-8")
  }

}
