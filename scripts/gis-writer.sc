// write delimited-text output for principal data sets
// to analyze in GIS.
//
import java.io.PrintWriter
import scala.xml._
val url = "https://raw.githubusercontent.com/neelsmith/ptolemy/master/tei/tlg0363.tlg009.epist03-p5-u8.xml"
//val root = XML.load(url)
val fileName = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
val root = XML.loadFile(fileName)

import edu.holycross.shot.ptolemy._
val geo = TeiParser.geography(root)


new PrintWriter("data/geography-min-adjusted.csv"){write(geo.adjustedPointsDelimited()); close;}
new PrintWriter("data/geography-min-raw.csv"){write(geo.rawDelimited()); close;}
new PrintWriter("data/geography-min-scaled-latshifted.csv"){write(geo.scaledLatShiftedDelimited()); close;}
new PrintWriter("data/geography-min-scaled-lonshifted.csv"){write(geo.scaledLonShiftedDelimited()); close;}
new PrintWriter("data/geography-min-scaled.csv"){write(geo.scaledDelimited()); close;}


// For QGIS, avoid the two columns with Greek numerals including
// codepoints BMP.  E.g., on this output:
//
// cut -f1-6,9-16 geography-full.tsv > usable-in-qgis.tsv
new PrintWriter("data/geography-full.tsv"){write(geo.ptolemyWithAdjustedPointDelimited("\t")); close;}
