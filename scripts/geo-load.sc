import scala.xml._
//import edu.holycross.shot.greek._
import edu.holycross.shot.ptolemy._

val xml = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
val root = XML.loadFile(xml)

val delimited = TeiParser.parseTEI(root)
val ptolStrings = delimited.map(ln => PtolemyString(ln)).filter(_.id.nonEmpty)
