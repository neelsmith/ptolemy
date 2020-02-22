package edu.holycross.shot.ptolemy

import org.scalatest.FlatSpec
import scala.xml._


class GeographicDatumSpec extends FlatSpec {


  "The GeographicDatum object" should "compute an offset in latitude to two decimal places for a rescaled value" in {
    val expectedOffset = 10.08
    assert(GeographicDatum.offsetLat == expectedOffset)
  }
  it should "compute a shifted latitude value" in {
    val shifted = GeographicDatum.shiftLatitude(0.0)
    assert(shifted == GeographicDatum.offsetLat)
  }

  it should "scale raw Ptolemy values to a larger earth circdumference" in pending
  it should "adjust scaled latitude values to account for scaling of Ptolemy's baseline" in pending
  it should "retreive a Pleiades data set" in pending
  it should "create a map of Ptolemy IDs to Pleiades coordiantes" in pending

}
