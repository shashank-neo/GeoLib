package org.open.geo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shashank on 31/05/15.
 */
public class Dms {

  /**
   * Parses string representing degrees/minutes/seconds into numeric degrees.
   *
   * This is very flexible on formats, allowing signed decimal degrees, or deg-min-sec optionally
   * suffixed by compass direction (NSEW). A variety of separators are accepted (eg 3° 37′ 09″W).
   * Seconds and minutes may be omitted.
   *
   * @param   {string} dmsStr - Degrees or deg/min/sec in variety of formats.
   * @returns {Double} Degrees as decimal number.
   */

  public static Double parseDms(String dmsStr) {
    // check for signed decimal degrees without NSEW, if so return it directly
    try {
      Double result = Double.parseDouble(dmsStr);
      if(Double.isFinite(result)) return result;
    } catch(NumberFormatException nfe) {
      // continue in case there is nfe.
    }

    // strip off any sign or compass dir'n & split out separate d/m/s
    String[] dms = dmsStr.trim().replaceAll("^-", "").replaceAll("[NSEW]$", "").split("[^0-9.,]+");
    int dmsLength = dms.length;
    //if (dms[dms.length-1]=="") dms.splice(dms.length-1);  // from trailing symbol
    if("".equalsIgnoreCase(dms[dmsLength - 1])) dmsLength-=1;
    if (dmsLength == 0) return null;

    // and convert to decimal degrees...
    Double deg;
    switch (dmsLength) {
      case 3:  // interpret 3-part result as d/m/s
        deg = Double.parseDouble(dms[0])/1 + Double.parseDouble(dms[1])/60 + Double.parseDouble(dms[2])/3600;
        break;
      case 2:  // interpret 2-part result as d/m
        deg = Double.parseDouble(dms[0])/1 + Double.parseDouble(dms[1])/60;
        break;
      case 1:  // just d (possibly decimal) or non-separated dddmmss
        deg = Double.parseDouble(dms[0]);
        // check for fixed-width unseparated format eg 0033709W
        //if (/[NS]/i.test(dmsStr)) deg = '0' + deg;  // - normalise N/S to 3-digit degrees
        //if (/[0-9]{7}/.test(deg)) deg = deg.slice(0,3)/1 + deg.slice(3,5)/60 + deg.slice(5)/3600;
        break;
      default:
        return null;
    }
    Pattern wsPattern = Pattern.compile("^-|[WS]$");
    Matcher m = wsPattern.matcher(dmsStr);
    if (m.find()) deg = -deg; // take '-', west and south as -ve
    return deg;
  }


/**
 * Converts decimal degrees to deg/min/sec format
 *  - degree, prime, double-prime symbols are added, but sign is discarded, though no compass
 *    direction is added.
 *
 * @private
 * @param   {Double} deg - Degrees to be formatted as specified.
 * @param   {string} [format=dms] - Return value as 'd', 'dm', 'dms' for deg, deg+min, deg+min+sec.
 * @param   {Integer} [precision=0|2|4] - Number of decimal places to use – default 0 for dms, 2 for dm, 4 for d.
 * @returns {string} Degrees formatted as deg/min/secs according to specified format.
 */
  public static String toDms(Double deg, String format, Integer precision) {
    if(deg == null) return null;

    // default values
    if (format == null || "".equals(format)) {
      format = "dms";
    }
    if (precision == null ) {
      if("d".equalsIgnoreCase(format) || "deg".equalsIgnoreCase(format)) {
        precision = 4;
      } else if("dm".equalsIgnoreCase(format) || "deg+min".equalsIgnoreCase(format)) {
        precision = 2;
      } else if("dms".equalsIgnoreCase(format) || "deg+min+sec".equalsIgnoreCase(format)) {
        precision = 0;
      } else {
        precision = 0;
      }
    }
    deg = Math.abs(deg);

    String dms = null;
    Double degree, min, sec;
    String tempDegree = null, tempMin = null, tempSec = null;
    if("dms".equalsIgnoreCase(format) || "deg+min+sec".equalsIgnoreCase(format)) {
      sec = Math.round((deg * 3600 * Math.pow(10.0, precision))) / Math.pow(10.0, precision); // convert degrees to seconds & round
      degree = Math.floor(sec / 3600);       // get component deg/min/sec
      min = Math.floor(sec/60) % 60;
      sec = Math.round(((sec % 60) * Math.pow(10.0, precision))) / Math.pow(10.0, precision);       // pad with trailing zeros
      tempDegree = "" + degree;
      tempMin = "" + min;
      tempSec = "" + sec;
      if (degree<100) tempDegree = "0" + degree;           // pad with leading zeros
      if (degree<10) tempDegree = "0" + degree;
      if (min<10) tempMin = "0" + min;
      if (sec<10) tempSec = "0" + sec;
      dms = tempDegree + "°" + tempMin + "′" + tempSec + "″";
    } else if("dm".equalsIgnoreCase(format) || "deg+min".equalsIgnoreCase(format)) {
      min = Math.round((deg * 60 * Math.pow(10.0, precision))) / Math.pow(10.0, precision); // convert degrees to minutes & round
      degree = Math.floor(min / 60);       // get component deg/min
      min = Math.round(((min % 60) * Math.pow(10.0, precision))) / Math.pow(10.0, precision);     // pad with trailing zeros
      tempDegree = "" + degree;
      if (degree<100) tempDegree = "0" + degree;         // pad with leading zeros
      if (degree<10) tempDegree = "0" + tempDegree;
      tempMin = "" + min;
      if (min<10) tempMin = "0" + min;
      dms = tempDegree + "°" + tempMin + "′";
    } else if("d".equalsIgnoreCase(format) || "deg".equalsIgnoreCase(format)) {
      degree = Math.round((deg * Math.pow(10.0,precision)))/Math.pow(10.0,precision);    // round degrees
      if (degree<100) tempDegree = "0" + degree; // pad with leading zeros
      if (degree<10) tempDegree = "0" + tempDegree;
      dms = tempDegree + "°";
    }
    return dms;
  }

/**
 * Converts numeric degrees to deg/min/sec latitude (2-digit degrees, suffixed with N/S).
 *
 * @param   {Double} deg - Degrees to be formatted as specified.
 * @param   {string} [format=dms] - Return value as 'd', 'dm', 'dms' for deg, deg+min, deg+min+sec.
 * @param   {Integer} [precision=0|2|4] - Number of decimal places to use – default 0 for dms, 2 for dm, 4 for d.
 * @returns {string} Degrees formatted as deg/min/secs according to specified format.
 */
  public static String toLat(Double deg, String format, Integer precision) {
    String lat = toDms(deg, format, precision);
    return lat == null ? "–" : lat.substring(1, lat.length()) + (deg<0.0 ? "S" : "N");  // knock off initial '0' for lat!
  }


/**
 * Convert numeric degrees to deg/min/sec longitude (3-digit degrees, suffixed with E/W)
 *
 * @param   {number} deg - Degrees to be formatted as specified.
 * @param   {string} [format=dms] - Return value as 'd', 'dm', 'dms' for deg, deg+min, deg+min+sec.
 * @param   {number} [precision=0|2|4] - Number of decimal places to use – default 0 for dms, 2 for dm, 4 for d.
 * @returns {string} Degrees formatted as deg/min/secs according to specified format.
 */

  public static String toLon(Double deg, String format, Integer precision){
    String lon = toDms(deg, format, precision);
    return lon == null ? "–" : lon + (deg<0 ? "W" : "E");
  }


/**
 * Converts numeric degrees to deg/min/sec as a bearing (0°..360°)
 *
 * @param   {Double} deg - Degrees to be formatted as specified.
 * @param   {string} [format=dms] - Return value as 'd', 'dm', 'dms' for deg, deg+min, deg+min+sec.
 * @param   {Integer} [precision=0|2|4] - Number of decimal places to use – default 0 for dms, 2 for dm, 4 for d.
 * @returns {string} Degrees formatted as deg/min/secs according to specified format.
 */
  public static String toBrng(Double deg, String format, Integer precision) {
    deg = (deg+360) % 360;  // normalise -ve values to 180°..360°
    String brng = toDms(deg, format, precision);
    return brng==null ? "–" : brng.replace("360", "0");  // just in case rounding took us up to 360°!
  }


/**
 * Returns compass point (to given precision) for supplied bearing.
 *
 * @param   {Double} bearing - Bearing in degrees from north.
 * @param   {Integer} [precision=3] - Precision (cardinal / intercardinal / secondary-intercardinal).
 * @returns {string} Compass point for supplied bearing.
 *
 * @example
 *   String point = Dms.compassPoint(24);    // point = 'NNE'
 *   String point = Dms.compassPoint(24, 1); // point = 'N'
 */
  public static String compassPoint(Double bearing, Integer precision) {
    if (precision == null) precision = 3;
    if(precision > 3 ) precision = 3;

    bearing = ((bearing%360)+360)%360;

    String point = null;

    switch (precision) {
      case 1: // 4 compass points
        switch ((int)Math.round(bearing*4/360)%4) {
          case 0: point = "N"; break;
          case 1: point = "E"; break;
          case 2: point = "S"; break;
          case 3: point = "W"; break;
        }
        break;
      case 2: // 8 compass points
        switch ((int)Math.round(bearing*8/360)%8) {
          case 0: point = "N";  break;
          case 1: point = "NE"; break;
          case 2: point = "E";  break;
          case 3: point = "SE"; break;
          case 4: point = "S";  break;
          case 5: point = "SW"; break;
          case 6: point = "W";  break;
          case 7: point = "NW"; break;
        }
        break;
      case 3: // 16 compass points
        switch ((int)Math.round(bearing*16/360)%16) {
          case  0: point = "N";   break;
          case  1: point = "NNE"; break;
          case  2: point = "NE";  break;
          case  3: point = "ENE"; break;
          case  4: point = "E";   break;
          case  5: point = "ESE"; break;
          case  6: point = "SE";  break;
          case  7: point = "SSE"; break;
          case  8: point = "S";   break;
          case  9: point = "SSW"; break;
          case 10: point = "SW";  break;
          case 11: point = "WSW"; break;
          case 12: point = "W";   break;
          case 13: point = "WNW"; break;
          case 14: point = "NW";  break;
          case 15: point = "NNW"; break;
        }
        break;
    }
    return point;
  }

}
