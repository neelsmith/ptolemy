package edu.holycross.shot.ptolemy

import edu.holycross.shot.greek._
import edu.unc.epidoc.transcoder.TransCoder

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter



/** Factory object for generating objects from TEI
* XML source.
*/
object TeiParser extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.DEBUG)

  val transliterator = new TransCoder("Unicode", "GreekXLit")



  /** Parse a Vector of four TEI <num> elements,
  * and format the resulting lon-lat data as
  * a delimited-text String.
  *
  * @param nums A Vector containing 4 TEI <num> elements.
  */
  def parseLonLat(nums: Vector[scala.xml.Node]): String = {
    debug("Parse nums "  + nums)
    val lonDeg = if (nums(0).text.trim.isEmpty) {""} else {
      nums(0).text.toLowerCase + "' "
    }
    val lonMin = if (nums(1).text.trim.isEmpty) {""} else {
      nums(1).text.toLowerCase + "\""
    }


    val latDeg = if (nums(2).text.trim.isEmpty) {""} else {
      nums(2).text.toLowerCase + "' "
    }
    val latMin = if (nums(3).text.trim.isEmpty) {""} else {
      nums(3).text.toLowerCase + "\""
    }


    val numSign : Int =
      nums(2).attribute("n") match {
        case None => 1
        case Some(att) => if (att.text == "NO/T") { -1 } else { 1 }
      }

    debug("Text values for lat: " + latDeg + latMin + " with numSign " + numSign)
    val lon = MilesianWithFraction((lonDeg + lonMin).trim)
    val lat = if (latDeg.contains("σημ")) {
      MilesianWithFraction(MilesianNumeric.oudenString)
    } else {
      MilesianWithFraction((latDeg + latMin).trim)
    }
    debug("lon/lat " + lon + "/" + lat)
    val lonStr = lon.ucode

    val latStr = lat.ucode

    debug("Created lon/lat pair " + lon + " : " + lat)
    //val lonStr = "X" // if (lon.ucode.isEmpty) { " X "} else { lon.ucode }
    //val latStr = "X"  //if (lat.ucode.isEmpty) { " X "} else { lat.ucode }
    //debug("dispaly strings " + lonStr + " : " + latStr)
    val delimited = Vector(lonStr,latStr,lon.toDouble,lon.toInt, lon.partialDouble, numSign * lat.toDouble, numSign * lat.toInt, numSign * lat.partialDouble).mkString("#")
    debug("DELIMITED: " + delimited)
    delimited
  }


  /** Parse a TEI <item> element, and format
  * a delimited-text String for its id, name and coordinates.
  *
  * @param n A TEI <item> element.
  */
  def parseItem(n: scala.xml.Node) : String = {

    val nameSeq = n \ "name"
    debug("item with nameSeq " + nameSeq)

    if(nameSeq.toVector.nonEmpty) {
      val nd = nameSeq.head

      val processed = nd.attribute("type").get.text match {
        case "episemos" => {
          debug("episemos: " + nd)
          ""
        }

        case "place" => {
          val measures = (n \ "measure" \ "num").toVector
          val id = nd.attribute("key").get.text
          debug("Parse a place with id " + id ) //+ " from measures " + measures)

          val ucodeName = nd.text.replaceAll("[\\s]+", " ")
          val xlitName = try {
            transliterator.getString(ucodeName).replaceAll("#","?")
          } catch {
            case t: Throwable => {
              error("Epidoc transcoder failed on " + ucodeName)
              ""
            }
          }
          val res = id + "#" + ucodeName + s" (${xlitName})"
          val lldata = if (measures.size != 4) {
            error(s"ERROR: ${measures.size} nums in  " + res)
            ""
          } else {
            val ll = parseLonLat(measures)
            debug("Parsed ll as " + ll)
            ll
          }
          res + "#" + lldata
        }
      }
      processed


    } else {
      error("No name element in " + nameSeq)
      ""
    }
  }


  /** Groups together a Vector al list elements for a given section to go
  * along with higher-level data applying to all items in the lists.
  *
  * @param sect A parsed TEI <div> representing a citable section, the third
  * tier in the citation hierarchy for the Geography.
  * @param continent Value for continent  containing this section.
  * @param province Value for province containing this section.
  * @param bkChap Book.chapter value for province containing this section.
  */
    def groupSectionLists(sect: scala.xml.Node,
      continent: String,
      province: String,
      bkChap: String ) :  (String, Vector[scala.xml.Node]) = {
      //debug("Get groups for " + sect)
      val sectNum = sect.attribute("n").get
      val geoType = sect.attribute("type").getOrElse("")

      //val props = if (province.isEmpty) { "" } else {Vector(continent, province, geoType).mkString("#") }
      val summary = Vector(bkChap + "." + sectNum, continent, province, geoType).mkString("#")
      val lists =  sect \ "list"
      (summary, lists.toVector)
    }

  /** Given contextual informatoin, create a complete delimited-text record for
  * a TEI <div> representing a section, the third tier in Ptolemy's citaiton hierarchy.
  *
  * @param sect A parsed TEI <div> representing a citable section, the third
  * tier in the citation hierarchy for the Geography.
  * @param continent Value for continent  containing this section.
  * @param province Value for province containing this section.
  * @param bkChap Book.chapter value for province containing this section.
  */
  def parseSection(sect: scala.xml.Node,
    continent: String,
    province: String,
    bkChap: String ) : Vector[String] = {

    val groupings = groupSectionLists(sect, continent, province, bkChap)
    debug(groupings._1 + " has " + groupings._2.size + " lists.")

    val processedItems = for (l <- groupings._2) yield {
      val items = (l \ "item").toVector
      val processed = for (i <- items) yield {
        val myItem =  TeiParser.parseItem(i)  //itemProcess(i)
        if (myItem.isEmpty) {
          ""
        } else {
          groupings._1 + "#" + myItem
        }
      }
      processed
    }
    //println("Processed to \n" + processedItems.flatten.mkString("\n"))
    processedItems.flatten.map(_.trim).filter(_.nonEmpty)
  }

  def parseChapter(ch: scala.xml.Node,
    continent: String,
    bk: String) : Vector[String]= {
      val chNum = ch.attribute("n").get
      val provinceOpt =   ch.attribute("ana")
      val province = provinceOpt match {
        case None => ""
        case _ => provinceOpt.get.text.replaceAll("#", "")
      }
      val sects = ch \ "div"
      val sectionData = for (s <- sects) yield {
        parseSection(s, continent, province, bk + "." + chNum)
      }
      sectionData.toVector.flatten.map(_.trim).filter(_.nonEmpty)
  }

  def parseBook(bk: scala.xml.Node) : Vector[String] = {
    val bookRef = bk.attribute("n").get.text
    val continentOpt =   bk.attribute("ana")
    val continent = continentOpt match {
      case None => ""
      case _ => continentOpt.get.text.replaceAll("#", "")
    }
    val chaps = bk \ "div"
    val chapData = for (ch <- chaps) yield {
      parseChapter(ch, continent, bookRef)
    }
    chapData.flatten.toVector.map(_.trim).filter(_.nonEmpty)
  }

  /** Parse a full TEI text of the Geography, and
  * extract lon-lat data points to a simple
  * delimited-text representation.
  *
  * @param root Parsed root of TEI document.
  */
  def parseTEI(root: scala.xml.Node, includeHeader : Boolean = true) : Vector[String] = {

    // Books are in top-level TEI <div> elements.
    val books = root \ "text" \ "body" \ "div"
    // parse contents of each book to a Vector of delimited text lines:
    val bookData = for (book <- books) yield {
      parseBook(book)
    }
    // flatten the results, tidy up strings by removing empty lines:
    val lines = bookData.toVector.flatten.map(_.trim).filter(_.nonEmpty)
    debug(lines.size + " non-empty lines.")

    if (includeHeader) {
      PtolemyString.header +: lines
    } else {
      lines
    }
  }

  /** Create a [[Geography]] instance from the parsed
  * root of an XML edition of the Geography.
  *
  * @param root Root element of a TEI edition of the Geography.
  */
  def geography(root: scala.xml.Node): Geography = {
    val dataLines = parseTEI(root, includeHeader = false)
    val rawData = dataLines.map(ln => PtolemyString(ln))
    Geography(rawData)
  }

}
