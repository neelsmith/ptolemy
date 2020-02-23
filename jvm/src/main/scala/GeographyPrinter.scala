package edu.holycross.shot.ptolemy

import edu.holycross.shot.greek._
import edu.holycross.shot.cite._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


object GeographyPrinter extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.DEBUG)
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
    val pages = sections.zipWithIndex.map {  case ((u,n),i) =>
      val yaml = "---\ntitle: Ptolemy, Geography " + u.passageComponent + "\nlayout:  page\n---\n\n"



      val h1 = s"# Ptolemy, *Geography*, ${u.passageComponent}\n\n"
      val childText = for (ch <- n.child) yield {
        mdForNode(ch,"")
      }

      val prev = if (i > 0) {
        val prevRef = (sections(i - 1)._1).passageComponent
        "previous: [" + prevRef + "](../geography-" + prevRef + "/)"
      } else { "prev: "}

      val next = if (i < sections.size - 2) {
        val nextRef = (sections(i + 1)._1).passageComponent
        "next: [" + nextRef + "](../geography-" + nextRef + "/)"
      } else { "next: "}
      val pn = "---\n\n" + prev + " | " + next + "\n\n"
      yaml + h1 + childText.mkString("\n\n") + pn
    }
    pages
  }

  def formatItem(item: scala.xml.Node): String = {
    val lead = "| "
    val itemBody = {
      val childText = for (ch <- item.child) yield {
        debug("Formatting a node...")
        mdForNode(ch)
      }
      childText.mkString("")
    }
    val trail = " |"

    lead + itemBody + trail
  }



  def formatMeasure(e: scala.xml.Elem): String = {
    val listTypeOpt = e.attribute("type")
    listTypeOpt match {
      case None => {
        error("No type attribute on " + e)

        ""
      }
      case _ => {
        listTypeOpt.get.text match {
          case "llpair" => {
            // verified ahead of time to contain 4 num elements
            val nums = (e \ "num").toVector
            if (nums.size != 4) {
              error("WRONG NUMBER OF NUMS IN " + e)
              "| error | error | error | error"
            } else {
              println(nums.size + " nums from " + nums)

              val lonDeg = if (nums(0).text.isEmpty) {""} else {
                nums(0).text + "ʹ"
              }
              val lonFract = if (nums(1).text.isEmpty) {""} else {
                nums(1).text + "\""
              }
              val latDeg = if (nums(2).text.isEmpty) {""} else {
                nums(2).text + "ʹ"
              }
              val latFract = if (nums(3).text.isEmpty) {""} else {
                nums(3).text + "\""
              }
              s"| ${lonDeg}| ${lonFract} | ${latDeg} | ${latFract}"
            }

          }
          case _ => {
            val childText = for (ch <- e.child) yield {
              mdForNode(ch)
            }
            childText.mkString("")
          }
        }
      }
    }
  }

  def formatList(e: scala.xml.Elem): String = {
    val listTypeOpt = e.attribute("type")
    listTypeOpt match {
      case None => {
        error("No type attribute on " + e)
        ""
      }
      case _ => {
        listTypeOpt.get.text match {
          case "simple" => {
            val simpleHeader = "| "
            val items = (e \ "item").toVector
            val msg = "processing lon-lat list with " + items.size + " items."
            info(msg)

            val md = items.map (i => formatItem(i))

            lonlatListHeader + md.mkString("\n") + "\n\n"

          }

          case "episemos" => {
            info("processing list of episemoi poleis")
            val childText = for (ch <- e.child) yield {
              mdForNode(ch)
            }
            childText.mkString("")
          }
          case _ => {
            error("Unrecognized list type FOR \n" + e )
            ""
          }
        }
      }
    }
  }
  // recursively parse
  def mdForNode(n : scala.xml.Node, accum: String = "") : String = {
    val txt = n match {
      case t: xml.Text => {
        accum + t.text.replaceAll("[\\s]+", " ")
      }
      case e: xml.Elem => {
        // handle table structures differently than
        // other text content.
        // match e.label
        val leader = e.label match {
          case _ => ""
        }
        val closer = e.label match {
          case "p" => "\n\n"
          case "name" => {
            debug("Formatting a NAME node...")
            val keyOpt = e.attribute("key")
            debug("Key attribute: " + keyOpt)
            val urn = keyOpt match {
              case None => ""
              case _ => {
                " <span style=\"color:silver\">(" + keyOpt.get.text + ")</span> "
              }
            }
            debug("So formatting as " + urn)
            "**" + urn
          }
          case _ => ""
        }

        e.label match {
          case "list" => {
            formatList(e)
          }

          case "name" => {
            val childText = for (ch <- e.child) yield {
              mdForNode(ch, accum)
            }
            "**" + childText.mkString("").trim + closer
          }
          case "measure" => {
            formatMeasure(e)
          }
          case _ => {
            val childText = for (ch <- e.child) yield {
              mdForNode(ch, accum + leader)
            }
            childText.mkString("") + closer
          }
        }
      }
    }
    txt
  }

  val lonlatListHeader = "\n\n" + """| <span style="color:silver">site</span> | <span style="color:silver">lon. degree</span> | <span style="color:silver">lon. fraction</span> | <span style="color:silver">lat. degree</span> | <span style="color:silver">lat. fraction</span> |""" +"\n| :------------- | :------------- | :------------- | :------------- | :------------- |\n"

}
