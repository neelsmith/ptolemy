package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._
import edu.holycross.shot.cite._


class GeographyPrinterSpec extends FlatSpec {
  val f = "tei/tlg0363.tlg009.epist03-p5-u8.xml"
  val docRoot = XML.loadFile(f)
  val expectedSections = 1940

  val mixedKids = """  <div n='3'><p > Πόλεις δὲ εἰσὶν ἐν αὐτῇ ὑπὸ μὲν τὸν Δανούβιον
      ποταμὸν</p>
  <list  type='simple'>
      <item >
          <name  key='pt_ll_976' type='place'>Ἀρελάπη</name>
          <measure  type='llpair'>
              <num  type='cardinal'>λε</num>
              <num  type='fraction'/>
              <num  type='cardinal'>μζ</num>
              <num  type='fraction'/>
          </measure>
      </item>
      <item >
          <name  key='pt_ll_977' type='place'>Κλαυδιούιον</name>
          <measure  type='llpair'>
              <num  type='cardinal'>λϚ</num>
              <num  type='fraction'/>
              <num  type='cardinal'>μϚ</num>
              <num  type='fraction'>𐅷 </num>
          </measure>
      </item>
      <item > Ὑπὸ δὲ ταύτας <name  key='pt_ll_978' type='place'>Γαμαυόδουρον</name>
          <measure  type='llpair'>
              <num  type='cardinal'>λδ</num>
              <num  type='fraction'>𐅷 </num>
              <num  type='cardinal'>μϚ</num>
              <num  type='fraction'>𐅷 </num>
          </measure>
      </item>
      </list></div>
"""
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
    println("para : \n" + pg)
  }
  it should "format a page with <p> content" in {
    val page = GeographyPrinter.mdPagesForDocument(XML.loadString(paraDoc))
    println("PAGE: \n\n" + page(0))
  }

  it should "format a section mixing paras and lists" in {

    val  mixedDiv = XML.loadString(mixedKids)
    val md =  GeographyPrinter.mdForNode(mixedDiv)
    println("MIXED CONTENT : \n" + md)

  }


}
