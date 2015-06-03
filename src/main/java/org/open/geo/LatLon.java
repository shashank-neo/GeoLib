package org.open.geo;


/**
 * Created by shashank on 28/05/15.
 */
public class LatLon {
  private Double latitude;
  private Double longitude;


  /**
   * Default @Constructor
   * @param latitude Latitude in degrees.
   * @param longitude Longitude in degrees.
   */
  public LatLon(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public String toString(String format, Integer dp) {
    if(format == null || "".equalsIgnoreCase(format)) {
      format = "dms";
    }
    return Dms.toLat(this.latitude, format, dp) + "," + Dms.toLon(this.longitude, format, dp);
  }

  public String toString() {
    return Dms.toLat(this.latitude, "dms", null) + "," + Dms.toLon(this.longitude, "dms", null);
  }

}
