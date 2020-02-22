package edu.holycross.shot.ptolemy





case class SimplePoint(id: String, lon: Double, lat: Double)  {


  def pointDelimited(delimiter: String = ",") = {
    Vector(lon,lat).mkString(delimiter)
  }

  def delimited(delimiter: String = ",") = {
    Vector(id,lon,lat).mkString(delimiter)
  }
}
