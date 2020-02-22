// write delimited-text output for principal data sets
// to analyze in GIS.
//
import java.io.PrintWriter
import scala.xml._
val url = "https://raw.githubusercontent.com/neelsmith/ptolemy/master/tei/tlg0363.tlg009.epist03-p5-u8.xml"
val root = XML.load(url)

import edu.holycross.shot.ptolemy._
val geo = TeiParser.geography(root)

new PrintWriter("gis-raw.csv"){write(geo.rawDelimited()); close;}
new PrintWriter("gis-scaled.csv"){write(geo.scaledDelimited()); close;}
new PrintWriter("gis-scaled-latshifted.csv"){write(geo.scaledLatShiftedDelimited()); close;}
new PrintWriter("gis-scaled-lonshifted.csv"){write(geo.scaledLonShiftedDelimited()); close;}


new PrintWriter("gis-adjusted.csv"){write(geo.adjustedPointsDelimited()); close;}

new PrintWriter("gis-full.tsv"){write(geo.ptolemyWithAdjustedPointDelimited("\t")); close;}
