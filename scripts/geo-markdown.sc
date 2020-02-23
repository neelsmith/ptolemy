import scala.xml._
import edu.holycross.shot.ptolemy._

val xmlFile = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
val root = XML.loadFile(xmlFile)

val pages = GeographyPrinter.mdPagesForDocument(root)
val urns = GeographyPrinter.sectionUrns(root)

import java.io.PrintWriter

val fileBase = "md/geography-"
for ( (u,i) <- urns.zipWithIndex) {
  val fName = fileBase + u.passageComponent + ".md"
  println( fName + ": " + pages(i))
  new PrintWriter(fName){write(pages(i)); close;}
}
