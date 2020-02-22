package edu.holycross.shot.ptolemy




case class SimplePoint(id: String, lon: Double, lat: Double)  {

  def delimited(delimiter: String = ",") = {
    Vector(id,lon,lat).mkString(delimiter)
  }
}
