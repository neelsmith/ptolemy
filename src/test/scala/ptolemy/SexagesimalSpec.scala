package edu.holycross.shot.ptolemy
import org.scalatest.FlatSpec

class SexagesimalSpec extends FlatSpec {


  // The obliquity of the ecliptic:
  val obliquity = "23;51,20"

  "The Sexagesimal object" should "isolate fractional part values from a conventionally formatted string" in {
    val expected = Vector(51,20)
    assert (Sexagesimal.parts(obliquity) == expected)
  }

  it should "isolate the integer part from a conventionally formatted string" in {
    val expected = 23
    assert(Sexagesimal.intPart(obliquity) == expected)
  }

  "A Sexagesimal" should "convert fraction parts to decimal values" in {
    val half = Sexagesimal("0;30")
    val expected: BigDecimal = 0.50
    assert(half.decimalFract() == expected)

    val halfASecMore = Sexagesimal("0;30,30")
    val expected2 : BigDecimal = 0.5083
    assert(halfASecMore.decimalFract() == expected2)
  }

  it should "support specifying a scale for rounding" in {
    val halfASecMore = Sexagesimal("0;30,30")
    val expected : BigDecimal = 0.51
    assert(halfASecMore.decimalFract(scale = 2) == expected)
  }

  it should "convert to decimal" in {
    val oblique  = Sexagesimal(obliquity)
    val expected = 23.8556
    assert(oblique.decimal() == expected)
  }

  it should "permit specifying a scale" in {
    val oblique  = Sexagesimal(obliquity)
    val expected = 23.86
    val rounded = oblique.decimal(scale = 2)
    assert(rounded == expected)
  }
}
