package edu.holycross.shot.ptolemy

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter

import scala.math.BigDecimal.RoundingMode

case class Sexagesimal(int: Int, parts: Vector[Int]) extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.INFO)

  def decimal(scale: Int = 4) = {
    int + decimalFract(scale = scale)
  }

  def decimalFract(remaining: Vector[Int] = parts, total: BigDecimal = 0.0, exponent: Int = 1, scale: Int = 4) : BigDecimal = {
    debug("decimal fract remainging " + remaining)
    debug("total: " + total)
    debug("exponent: " + exponent)
    debug("scale: " + scale)
    if (remaining.isEmpty) {
      val scaled = total.setScale(scale, RoundingMode.HALF_UP)
      debug("Returning FINAL sclaed to " + scale + " :  " + scaled)
      scaled
    } else {
      val divisor = scala.math.pow(60, exponent)
      val quotient = remaining(0) / divisor
      debug("quotient: " + quotient)
      debug ("New total: " + (total + quotient))
      decimalFract(remaining.tail, total + quotient, exponent + 1, scale)
    }
  }
}

object Sexagesimal {
  def apply(s: String): Sexagesimal = {
    Sexagesimal( intPart(s), parts(s))
  }


  def intPart(s: String): Int = {
    def top = s.split(";")
    top(0).toInt
  }

  def parts(s: String) : Vector[Int] = {
    val top = s.split(";")
    top.size match {
      case 1 => Vector.empty[Int]
      case _ =>{
        val divisions = top(1).split(",").map(_.trim).toVector
        divisions.map(_.toInt)
      }
    }

  }
}
