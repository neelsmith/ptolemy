package edu.holycross.shot.ptolemy

import edu.holycross.shot.greek._
import edu.holycross.shot.cite._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


object GeographyPrinter extends LogSupport {

  val urlBase = CtsUrn("urn:cts:greekLit:tlg0363.tlg009.episteme:")

  def sectionRoots(docRoot: scala.xml.Node) = {
    val bookNodes = docRoot \ "text" \ "body" \ "div"
    val leafNodes = bookNodes.toVector.map( bk => {
      val bkRef: String = bk.attribute("n").get.text
      val chaps = bk \ "div"

      val cVals = chaps.toVector.map (ch => {
        val bkChap: String = bkRef + "." + ch.attribute("n").get.text
        val bkChapUrl = urlBase.addPassage(bkChap)
        val sects  = ch \ "div"
        sects.toVector.map(sect => {
          val psgRef = bkChap + "." +  sect.attribute("n").get.text
          val url = urlBase.addPassage(psgRef)
          (url, sect)
        })
      })
      cVals
    })
    leafNodes.flatten.flatten
  }

  def sectionUrns(docRoot: scala.xml.Node): Vector[CtsUrn] = {
    sectionRoots(docRoot).map{ case (u, n) => u }
  }

  def cexLinesForDocument(docRoot: scala.xml.Node): Vector[String] = {
    val sections = sectionRoots(docRoot: scala.xml.Node)
    val cex = sections.map{ case (u,n) =>
      u + "#" + n.text
    }
    cex
  }

  def mdPagesForDocument(docRoot: scala.xml.Node): Vector[String]  = {
    val sections = sectionRoots(docRoot: scala.xml.Node)
    val pages = sections.map{  case (u,n) =>
      val h1 = s"# Ptolemy, *Geography*, ${u.passageComponent}\n\n"
      val childText = for (ch <- n.child) yield {
        mdForNode(ch,"")
      }
      h1 + childText.mkString("\n\n")
    }
    pages
  }

  // recursively parse
  def mdForNode(n : scala.xml.Node, accum: String = "") : String = {
    val txt = n match {
      case t: xml.Text => {
        accum + t.text.replaceAll("[\\s]+", " ").trim
      }
      case e: xml.Elem => {
        // handle table structures differently than
        // other text content.
        // match e.label
        val leader = e.label match {
          case "list" => {
            //println("ADD LIST HEADER")
            "\n\n| name | lon1 | lon2 | lat1 | lat2 |\n"
          }
          case _ => ""
        }
        val closer = e.label match {
          case "item" => ""
          case _ => ""
        }
        val childText = for (ch <- e.child) yield {
          mdForNode(ch, accum + leader) + closer
        }
        childText.mkString("")
      }
    }
    txt
  }

}
