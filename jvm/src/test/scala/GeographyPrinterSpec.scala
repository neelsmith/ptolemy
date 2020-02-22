package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._
import edu.holycross.shot.cite._


class GeographyPrinterSpec extends FlatSpec {
  val f = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
  val docRoot = XML.loadFile(f)
  val expectedSections = 1940

  val childP = """<div><p> Ἡ γεωγραφία μίμησίς ἐστι διὰ γραφῆς τοῦ κατειλημμένου τῆς
        γῆς μέρους ὅλου μετὰ τῶν ὡς ἐπίπαν αὐτῷ συνημμένων: καὶ
        διαφέρει τῆς χωρογραφίας, ἐπειδήπερ αὕτη μὲν ἀποτεμνομένη
        τοὺς κατὰ μέρος τόπους χωρὶς ἕκαστον καὶ καθ᾽ αὑτὸν
        ἐκτίθεται, συναπογραφομένη πάντα σχεδὸν καὶ τὰ σμικρότατα τῶν
        ἐμπεριλαμβανομένων, οἷον λιμένας καὶ κώμας καὶ δήμους καὶ τὰς
        ἀπὸ τῶν πρώτων ποταμῶν ἐκτροπὰς καὶ τὰ παραπλήσια: τῆς δὲ
        γεωγραφίας ἴδιόν ἐστι τὸ μίαν τε καὶ συνεχῆ δεικνύναι τὴν
        ἐγνωσμένην γῆν, ὡς ἔχει φύσεώς τε καὶ θέσεως <add >καὶ</add>
        μέχρι μόνων τῶν ἐν ὅλαις καὶ περιεκτικωτέραις περιγραφαῖς
        αὐτῇ συνημμένων, οἷον κόλπων καὶ πόλεων μεγάλων ἐθνῶν τε
        καὶ ποταμῶν τῶν ἀξιολογωτέρων καὶ τῶν καθ᾽ ἕκαστον εἶδος
        ἐπισημοτέρων.</p></div>"""

  val paraDoc =   """<TEI><text><body><div n='1'><div n='1'><div n='1'><p> Ἡ γεωγραφία μίμησίς ἐστι διὰ γραφῆς τοῦ κατειλημμένου τῆς
        γῆς μέρους ὅλου μετὰ τῶν ὡς ἐπίπαν αὐτῷ συνημμένων: καὶ
        διαφέρει τῆς χωρογραφίας, ἐπειδήπερ αὕτη μὲν ἀποτεμνομένη
        τοὺς κατὰ μέρος τόπους χωρὶς ἕκαστον καὶ καθ᾽ αὑτὸν
        ἐκτίθεται, συναπογραφομένη πάντα σχεδὸν καὶ τὰ σμικρότατα τῶν
        ἐμπεριλαμβανομένων, οἷον λιμένας καὶ κώμας καὶ δήμους καὶ τὰς
        ἀπὸ τῶν πρώτων ποταμῶν ἐκτροπὰς καὶ τὰ παραπλήσια: τῆς δὲ
        γεωγραφίας ἴδιόν ἐστι τὸ μίαν τε καὶ συνεχῆ δεικνύναι τὴν
        ἐγνωσμένην γῆν, ὡς ἔχει φύσεώς τε καὶ θέσεως <add >καὶ</add>
        μέχρι μόνων τῶν ἐν ὅλαις καὶ περιεκτικωτέραις περιγραφαῖς
        αὐτῇ συνημμένων, οἷον κόλπων καὶ πόλεων μεγάλων ἐθνῶν τε
        καὶ ποταμῶν τῶν ἀξιολογωτέρων καὶ τῶν καθ᾽ ἕκαστον εἶδος
        ἐπισημοτέρων.</p></div></div></div></body></text></TEI>"""

  "The Geography object" should "parse a whole TEI document of the Geography by gathering citable books, chapters and sections" in {
    val sects = GeographyPrinter.sectionRoots(docRoot)
    assert(sects.size == expectedSections)
  }

  it should "create a Vector of CEX lines from an XML root" in pending/* {
    val cex = GeographyPrinter.cexLinesForDocument(docRoot)
    println(cex(0))
  }*/

  it should "create a Vector of leaf-node URNs from nan XML root" in {
    val urns = GeographyPrinter.sectionUrns(docRoot)
    assert(urns.size == expectedSections )
    val firstUrn = CtsUrn("urn:cts:greekLit:tlg0363.tlg009.episteme:1.1.1")
    assert(urns(0) == firstUrn)
  }


  it should "create a Vector of markdown pages from an XML root" in {
    val pages = GeographyPrinter.mdPagesForDocument(docRoot)
    assert(pages.size == expectedSections )
    println(pages(0))
  }

  it should "extract TEI <p> content" in {
    val  pElem = XML.loadString(childP)
    val pg =  GeographyPrinter.mdForNode(pElem)
    println("PAGE IS : " + pg)
  }
  it should "format a page with <p> content" in {
    val page = GeographyPrinter.mdPagesForDocument(XML.loadString(paraDoc))
    println("PAGE: \n\n" + page(0))
  }


}
