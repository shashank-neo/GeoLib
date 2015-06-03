package org.open.geo;

/**
 * Created by shashank on 28/05/15.
 */
public class GeoUtil {

  /**
   * Return the distance from source to destination point (using havershine algorithm).
   *
   * @param {LatLon} source point latitude/longitude of source point.
   * @param {LatLon} destination point latitude/longitude of destination point.
   * @param {Double} radius [radius=6371e3] - (Mean) radius of earth (defaults to radius in metres).
   * @return {Double} distance between this point and the destination point, in units same as radius.
   *
   * @example
   *        LatLon p1 = new LatLon(52.205, 0.119), LatLon p2 = new LatLon(48.857, 2.351);
   *        Double distance = GeoUtil.distanceBetween(p1, p2, null); // Distance in meters
   *
   * Formula Used
   *
   *      R = 6371000 // metres
   *      φ1 = lat1 //radians
   *      φ2 = lat2 //radians
   *      Δφ = (lat2-lat1) // radians;
   *      Δλ = (lon2-lon1) // radians;
   *
   *     Haversine formula:	 a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
   *                         c = 2 ⋅ atan2( √a, √(1−a) )
   *                         d = R ⋅ c
   */
  public static Double distanceBetween(LatLon source, LatLon destination, Double radius) {
    if(radius == null ) {
      radius = 6371000.00;
    }
    Double r = radius;
    Double lat1ToRad = Math.toRadians(source.getLatitude());
    Double lon1ToRad = Math.toRadians(source.getLongitude());
    Double lat2ToRad = Math.toRadians(destination.getLatitude());
    Double lon2ToRad = Math.toRadians(destination.getLongitude());
    Double deltaLat = lat2ToRad - lat1ToRad;
    Double deltaLon = lon2ToRad - lon1ToRad;
    Double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
        Math.cos(lat1ToRad) * Math.cos(lat2ToRad) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    Double d = r * c;
    return  d;
  }

  /**
   * Returns the (initial) bearing from source point to destination point.
   *
   * @param   {LatLon} source - Latitude/longitude of source point.
   * @param   {LatLon} destination - Latitude/longitude of destination point.
   * @returns {Double} Initial bearing in degrees from north.
   *
   * @example
   *     LatLon p1 = new LatLon(52.205, 0.119); LatLon p2 = new LatLon(48.857, 2.351);
   *     Double bearing = GeoUtil.bearingBetween(p1,p2); // 156.2 fixed to 2 digit precision
   *
   * Formula Used
   *     θ = atan2( sin Δλ ⋅ cos φ2 , cos φ1 ⋅ sin φ2 − sin φ1 ⋅ cos φ2 ⋅ cos Δλ )
   *
   *     Δλ  (lon2-lon1) //radians
   *     φ2  lat2 //radians
   *     φ1  lat1 //radians
   *
   */
  public static Double bearingBetween(LatLon source, LatLon destination) {
    Double lat1ToRad = Math.toRadians(source.getLatitude());
    Double lat2ToRad = Math.toRadians(destination.getLatitude());
    Double deltaLonToRad = Math.toRadians(destination.getLongitude() - source.getLongitude());
    Double y = Math.sin(deltaLonToRad) * Math.cos(lat2ToRad);
    Double x = Math.cos(lat1ToRad) *  Math.sin(lat2ToRad) - Math.sin(lat1ToRad) * Math.cos(lat2ToRad) * Math.cos(deltaLonToRad);
    Double theta = Math.atan2(y,x);
    return (Math.toDegrees(theta) + 360) % 360;
  }

  /**
   * Returns final bearing arriving at destination destination point from source point; the final bearing
   * will differ from the initial bearing by varying degrees according to distance and latitude.
   *
   * @param   {LatLon} source - Latitude/longitude of source point.
   * @param   {LatLon} destination - Latitude/longitude of destination point.
   * @returns {Double} Final bearing in degrees from north.
   *
   * @example
   *     LatLon p1 = new LatLon(52.205, 0.119); LatLon p2 = new LatLon(48.857, 2.351);
   *     Double bearing = GeoUtil.finalBearingTo(p1,p2);
   */
  public static Double finalBearingTo(LatLon source, LatLon destination) {
    return (bearingBetween(destination, source) + 180) % 360;
  }

  /**
   * Returns the midpoint between source point and the destination point.
   *
   * @param   {LatLon} source - Latitude/longitude of source point.
   * @param   {LatLon} destination - Latitude/longitude of destination point.
   * @returns {LatLon} Midpoint between source point and the destination point.
   *
   * @example
   *     LatLon p1 = new LatLon(52.205, 0.119), p2 = new LatLon(48.857, 2.351);
   *     LatLon pMid = GeoUtil.midpointBetween(p1,p2); // pMid.toString(): 50.5363°N, 001.2746°E
   *
   * Formula Used
   *     Bx = cos φ2 ⋅ cos Δλ
   *     By = cos φ2 ⋅ sin Δλ
   *     φm = atan2( sin φ1 + sin φ2, √(cos φ1 + Bx)² + By² )
   *     λm = λ1 + atan2(By, cos(φ1)+Bx)
   *
   *     φ1  lat1 //radians
   *     φ2  lat2 //radians
   *     Δλ  (lon2-lon1) //radians
   *
   */
  public static LatLon midPointBetween(LatLon source, LatLon destination) {
    Double lat1ToRad = Math.toRadians(source.getLatitude()), lon1ToRad = Math.toRadians(source.getLongitude());
    Double lat2ToRad = Math.toRadians(destination.getLatitude());
    Double deltaLon = Math.toRadians((destination.getLongitude() - source.getLongitude()));

    Double Bx = Math.cos(lat2ToRad) * Math.cos(deltaLon);
    Double By = Math.cos(lat2ToRad) * Math.sin(deltaLon);

    Double lat = Math.atan2(Math.sin(lat1ToRad)+Math.sin(lat2ToRad),
        Math.sqrt( (Math.cos(lat1ToRad)+Bx)*(Math.cos(lat1ToRad)+Bx) + By*By) );
    Double lon = lon1ToRad + Math.atan2(By, Math.cos(lat1ToRad) + Bx);
    lon = (lon+3*Math.PI) % (2*Math.PI) - Math.PI;

    return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
  }

  /**
   * Returns the destination point from source point having travelled the given distance on the
   * given initial bearing (bearing normally varies around path followed).
   *
   * @param   {LatLon} source - Latitude/longitude of source point.
   * @param   {Double} distance - Distance travelled, in same units as earth radius (default: metres).
   * @param   {Double} bearing - Initial bearing in degrees from north.
   * @param   {Double} [radius=6371e3] - (Mean) radius of earth (defaults to radius in metres).
   * @returns {LatLon} Destination point.
   *
   * @example
   *     LatLon p1 = new LatLon(51.4778, -0.0015);
   *     LatLon p2 = GeoUtil.destinationPoint(p1,7794, 300.7);
   *
   * Formula Used
   *     φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
   *     λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
   *
   *     φ1  lat1 //radians
   *     δ   distance //radians
   *     θ   bearing //radians
   */
  public static LatLon destinationPoint(LatLon source, Double distance, Double bearing, Double radius) {
    if(radius == null) {
      radius = 6371000.00;
    }
    Double distanceInRads = distance / radius;
    Double bearingInRads = Math.toRadians(bearing);

    Double lat1ToRad = Math.toRadians(source.getLatitude());
    Double lon1ToRad = Math.toRadians(source.getLongitude());

    Double lat = Math.asin(Math.sin(lat1ToRad) * Math.cos(distanceInRads) +
        Math.cos(lat1ToRad) * Math.sin(distanceInRads) * Math.cos(bearingInRads));
    Double lon = lon1ToRad + Math.atan2(Math.sin(bearingInRads) * Math.sin(distanceInRads) * Math.cos(lat1ToRad),
        Math.cos(distanceInRads) - Math.sin(lat1ToRad) * Math.sin(lat));
    lon = (lon + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

    return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
  }


  /**
   * Returns the point of intersection of two paths defined by point and bearing.
   *
   * @param   {LatLon} sourcePoint - First point.
   * @param   {Double} bearingSource - Initial bearing from first point.
   * @param   {LatLon} destinationPoint - Second point.
   * @param   {Double} bearingDestination - Initial bearing from second point.
   * @returns {LatLon} Destination point (null if no unique intersection defined).
   *
   * @example
   *     LatLon p1 = LatLon(51.8853, 0.2545);
   *     Double bearingSource = 108.547;
   *     LatLon p2 = LatLon(49.0034, 2.5735);
   *     Double bearingDestination =  32.435;
   *     LatLon pInt = GeoUtil.intersection(p1, bearingSource, p2, bearingDestination); // pInt.toString(): 50.9078°N, 004.5084°E
   *
   * Formula Used
   *     δ12 = 2⋅asin( √(sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)) )
   *      θa = acos( sin φ2 − sin φ1 ⋅ cos δ12 / sin δ12 ⋅ cos φ1 )
   *      θb = acos( sin φ1 − sin φ2 ⋅ cos δ12 / sin δ12 ⋅ cos φ2 )
   *
   *      if sin(λ2−λ1) > 0
   *         θ12 = θa
   *         θ21 = 2π − θb
   *      else
   *        θ12 = 2π − θa
   *        θ21 = θb
   *
   *      α1 = (θ13 − θ12 + π) % 2π − π
   *      α2 = (θ21 − θ23 + π) % 2π − π
   *
   *      α3 = acos( −cos α1 ⋅ cos α2 + sin α1 ⋅ sin α2 ⋅ cos δ12 )
   *      δ13 = atan2( sin δ12 ⋅ sin α1 ⋅ sin α2 , cos α2 + cos α1 ⋅ cos α3 )
   *      φ3 = asin( sin φ1 ⋅ cos δ13 + cos φ1 ⋅ sin δ13 ⋅ cos θ13 )
   *      Δλ13 = atan2( sin θ13 ⋅ sin δ13 ⋅ cos φ1 , cos δ13 − sin φ1 ⋅ sin φ3 )
   *      λ3 = (λ1+Δλ13+π) % 2π − π
   *
   *
   *      φ1  lat1 //radians
   *      λ1  lon1 //radians
   *      θ1  bearing1  //double
   *      φ2  lat2 //radians
   *      λ2  lon2 //radians
   *      θ2  bearing2 //double
   *
   *
   */
  public static LatLon intersection(LatLon sourcePoint, Double bearingSource, LatLon destinationPoint,
                                    Double bearingDestination) {

    Double lat1ToRad = Math.toRadians(sourcePoint.getLatitude()),
        lon1ToRad = Math.toRadians(sourcePoint.getLongitude());
    Double lat2ToRad = Math.toRadians(destinationPoint.getLatitude()),
        lon2ToRad = Math.toRadians(destinationPoint.getLongitude());
    Double bearSrcToRad = Math.toRadians(bearingSource), bearDestToRad = Math.toRadians(bearingDestination);
    Double deltaLatToRad = lat2ToRad-lat1ToRad, deltaLonToRad = lon2ToRad-lon1ToRad;

    Double delta12 = 2 * Math.asin(Math.sqrt(Math.sin(deltaLatToRad/2) * Math.sin(deltaLatToRad/2) +
        Math.cos(lat1ToRad) * Math.cos(lat2ToRad) * Math.sin(deltaLonToRad/2) * Math.sin(deltaLonToRad/2)));
    if (delta12 == 0.0) return null;


    Double theta1 = Math.acos(( Math.sin(lat2ToRad) - Math.sin(lat1ToRad)*Math.cos(delta12)) /
        (Math.sin(delta12)*Math.cos(lat1ToRad)));

    Double theta2 = Math.acos( ( Math.sin(lat1ToRad) - Math.sin(lat2ToRad)*Math.cos(delta12) ) /
        ( Math.sin(delta12)*Math.cos(lat2ToRad) ) );

    Double theta12, theta21;
    if (Math.sin(lon2ToRad-lon1ToRad) > 0) {
      theta12 = theta1;
      theta21 = 2*Math.PI - theta2;
    } else {
      theta12 = 2*Math.PI - theta1;
      theta21 = theta2;
    }

    Double alpha1 = (bearSrcToRad - theta12 + Math.PI) % (2*Math.PI) - Math.PI; // angle 2-1-3
    Double alpha2 = (theta21 - bearDestToRad + Math.PI) % (2*Math.PI) - Math.PI; // angle 1-2-3

    if (Math.sin(alpha1)==0.0 && Math.sin(alpha2)==0.0) return null; // infinite intersections
    if (Math.sin(alpha1)*Math.sin(alpha2) < 0) return null;      // ambiguous intersection


    Double alpha3 = Math.acos( -Math.cos(alpha1)*Math.cos(alpha2) +
        Math.sin(alpha1)*Math.sin(alpha2)*Math.cos(delta12) );
    Double delta13 = Math.atan2( Math.sin(delta12)*Math.sin(alpha1)*Math.sin(alpha2),
        Math.cos(alpha2)+Math.cos(alpha1)*Math.cos(alpha3) );
    Double lat = Math.asin( Math.sin(lat1ToRad)*Math.cos(delta13) +
        Math.cos(lat1ToRad)*Math.sin(delta13)*Math.cos(bearSrcToRad) );
    Double deltaLat13 = Math.atan2( Math.sin(bearSrcToRad)*Math.sin(delta13)*Math.cos(lat1ToRad),
        Math.cos(delta13)-Math.sin(lat1ToRad)*Math.sin(lat) );
    Double lon = lon1ToRad + deltaLat13;
    lon = (lon+3*Math.PI) % (2*Math.PI) - Math.PI; // normalise to -180..+180°

    return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
  }

  /**
   * Returns (signed) distance from source point to great circle defined by start-point and end-point.
   *
   * @param   {LatLon} pathStart - Start point of great circle path.
   * @param   {LatLon} pathEnd - End point of great circle path.
   * @param   {Double} [radius=6371e3] - (Mean) radius of earth (defaults to radius in metres).
   * @returns {Double} Distance to great circle (-ve if to left, +ve if to right of path).
   *
   * @example
   *   LatLon pCurrent = new LatLon(53.2611, -0.7972);
   *   LatLon p1 = new LatLon(53.3206, -1.7297), p2 = new LatLon(53.1887, 0.1334);
   *   Double d = GeoUtil.crossTrackDistanceTo(pCurrent, p1, p2);
   *
   * Formula Used
   *   dxt = asin( sin(δ13) ⋅ sin(θ13−θ12) ) ⋅ R
   *
   *   where	δ13 is (angular) distance from start point to third point
   *   θ13 is (initial) bearing from start point to third point
   *   θ12 is (initial) bearing from start point to end point
   *   R is the earth’s radius
   */
  public static Double crossTrackDistanceTo(LatLon source, LatLon pathStart, LatLon pathEnd, Double radius) {
    if(radius == null) {
      radius = 6371000.00;
    }
    Double delta13 = distanceBetween(pathStart, source, radius)/radius;
    Double theta13 = Math.toRadians(bearingBetween(pathStart, source));
    Double  theta12 = Math.toRadians(bearingBetween(pathStart, pathEnd));
    Double dxt = Math.asin( Math.sin(delta13) * Math.sin(theta13-theta12) ) * radius;
    return dxt;
  }


  /**
   * Returns the distance travelling from source point to destination point along a rhumb line.
   *
   * @param   {LatLon} source - Latitude/longitude of destination point.
   * @param   {LatLon} destination - Latitude/longitude of destination point.
   * @param   {Double} [radius=6371e3] - (Mean) radius of earth (defaults to radius in metres).
   * @returns {Double} Distance in km between this point and destination point (same units as radius).
   *
   * @example
   *     LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
   *     Double d = GeoUtil.rhumbDistanceBetween(p1,p2); // Number(d.toPrecision(4)): 40310
   *
   * Formula Used
   *     Δψ = ln( tan(π/4 + φ2/2) / tan(π/4 + φ1/2) )
   *     q = Δφ/Δψ (or cosφ for E-W line)
   *     d = √(Δφ² + q²⋅Δλ²) ⋅ R	(Pythagoras)
   *
   *     φ is latitude,
   *     λ is longitude,
   *     Δλ is taking shortest route (<180°),
   *     R is the earth’s radius,
   *     ln is natural log
   */
  public static  Double rhumbDistanceBetween(LatLon source, LatLon destination, Double radius) {
    if(radius == null) {
      radius = 6371000.00;
    }

    Double R = radius;
    Double lat1ToRad = Math.toRadians(source.getLatitude()), lat2ToRad = Math.toRadians(destination.getLatitude());
    Double deltaLat = lat2ToRad - lat1ToRad;
    Double deltaLon = Math.toRadians(Math.abs(destination.getLongitude() - source.getLongitude()));
    // if dLon over 180° take shorter rhumb line across the anti-meridian:
    if (Math.abs(deltaLon) > Math.PI) deltaLon = deltaLon>0 ? -(2*Math.PI-deltaLon) : (2*Math.PI+deltaLon);

    // on Mercator projection, longitude distances shrink by latitude; q is the 'stretch factor'
    // q becomes ill-conditioned along E-W line (0/0); use empirical tolerance to avoid it
    Double deltaLog = Math.log(Math.tan(lat2ToRad/2+Math.PI/4)/Math.tan(lat1ToRad/2+Math.PI/4));
    Double q = Math.abs(deltaLog) > 10e-12 ? deltaLat/deltaLog : Math.cos(lat1ToRad);

    // distance is pythagoras on 'stretched' Mercator projection
    Double angularDist = Math.sqrt(deltaLat*deltaLat + q*q*deltaLon*deltaLon); // angular distance in radians
    Double dist = angularDist * R;

    return dist;
  }


/**
 * Returns the bearing from source point to destination point along a rhumb line.
 *
 * @param   {LatLon} source - Latitude/longitude of source point.
 * @param   {LatLon} point - Latitude/longitude of destination point.
 * @returns {Double} Bearing in degrees from north.
 *
 * @example
 *     LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
 *     double d = GeoUtil.rhumbBearingBetween(p1,p2); // d.toFixed(1): 116.7
 *
 * Formula Used
 *    Δψ = ln( tan(π/4 + φ2/2) / tan(π/4 + φ1/2) )
 *    θ = atan2(Δλ, Δψ)
 *
 *   	φ is latitude,
 *   	λ is longitude,
 *   	Δλ is taking shortest route (<180°)
 *    R is the earth’s radius,
 *    ln is natural log
 */
  public static Double rhumbBearingBetween(LatLon source, LatLon destination) {

    Double lat1ToRad = Math.toRadians(source.getLatitude()), lat2ToRad = Math.toRadians(destination.getLatitude());
    Double deltaLon = Math.toRadians(destination.getLongitude() - source.getLongitude());
    // if dLon over 180° take shorter rhumb line across the anti-meridian:
    if (Math.abs(deltaLon) > Math.PI) deltaLon = deltaLon>0 ? -(2*Math.PI-deltaLon) : (2*Math.PI+deltaLon);

    Double deltaLog = Math.log(Math.tan(lat2ToRad/2+Math.PI/4)/Math.tan(lat1ToRad/2+Math.PI/4));

    Double theta = Math.atan2(deltaLon, deltaLog);

    return (Math.toDegrees(theta)+360) % 360;
  }


/**
 * Returns the destination point having travelled along a rhumb line from source point the given
 * distance on the  given bearing.
 *
 * @param   {LatLon} source - Latitude/longitude of source point.
 * @param   {Double} distance - Distance travelled, in same units as earth radius (default: metres).
 * @param   {Double} bearing - Bearing in degrees from north.
 * @param   {Double} [radius=6371e3] - (Mean) radius of earth (defaults to radius in metres).
 * @returns {LatLon} Destination point.
 *
 * @example
 *     LatLon p1 = new LatLon(51.127, 1.338);
 *     LatLon p2 = GeoUtil.rhumbDestinationPoint(p1, 40300, 116.7); // p2.toString(): 50.9642°N, 001.8530°E
 *
 * Formula Used
 *     δ = d/R	(angular distance)
 *     Δψ = ln( tan(π/4 + φ2/2) / tan(π/4 + φ1/2) )
 *     q = Δφ/Δψ (or cosφ for E-W line)
 *     Δλ = δ ⋅ sin θ / q
 *     φ2 = φ1 + δ ⋅ cos θ
 *     λ2 = λ1 + Δλ
 *
 *     φ is latitude,
 *     λ is longitude,
 *     Δλ is taking shortest route (<180°),
 *     ln is natural log,
 *     R is the earth’s radius
 */
  public static LatLon rhumbDestinationPoint(LatLon source, Double distance, Double bearing, Double radius) {
    if(radius == null) {
      radius = 6371000.00;
    }
    Double angularDist = distance / radius; // angular distance in radians
    Double lat1ToRad = Math.toRadians(source.getLatitude()), lon1ToRad = Math.toRadians(source.getLongitude());
    Double bearingToRad = Math.toRadians(bearing);

    Double deltaLat = angularDist * Math.cos(bearingToRad);

    Double lat = lat1ToRad + deltaLat;
    // check for some daft bugger going past the pole, normalise latitude if so
    if (Math.abs(lat) > Math.PI/2) lat = lat>0 ? Math.PI-lat : -Math.PI-lat;

    Double deltaLog = Math.log(Math.tan(lat/2+Math.PI/4)/Math.tan(lat1ToRad/2+Math.PI/4));
    Double q = Math.abs(deltaLog) > 10e-12 ? deltaLat / deltaLog : Math.cos(lat1ToRad); // E-W course becomes ill-conditioned with 0/0

    Double deltaLon = angularDist*Math.sin(bearingToRad)/q;

    Double lon = lon1ToRad + deltaLon;

    lon = (lon + 3*Math.PI) % (2*Math.PI) - Math.PI; // normalise to -180..+180°

    return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
  }


/**
 * Returns the loxodromic midpoint (along a rhumb line) between source point and destination point.
 *
 * @param   {LatLon} source - Latitude/longitude of first point.
 * @param   {LatLon} destination - Latitude/longitude of second point.
 * @returns {LatLon} Midpoint between first point and second point.
 *
 * @example
 *     LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
 *     LatLon p3 = GeoUtil.rhumbMidpointBetween(p1,p2); // p3.toString(): 51.0455°N, 001.5957°E
 *
 *     φm = (φ1+φ2) / 2
 *     f1 = tan(π/4 + φ1/2)
 *     f2 = tan(π/4 + φ2/2)
 *     fm = tan(π/4+φm/2)
 *     λm = [ (λ2−λ1) ⋅ ln(fm) + λ1 ⋅ ln(f2) − λ2 ⋅ ln(f1) ] / ln(f2/f1)
 *
 *     φ is latitude,
 *     λ is longitude,
 *     ln is natural log
 */
  public static LatLon rhumbMidpointBetween(LatLon source, LatLon destination) {

    Double lat1ToRad = Math.toRadians(source.getLatitude()), lon1ToRad = Math.toRadians(source.getLongitude());
    Double lat2ToRad = Math.toRadians(destination.getLatitude()), lon2ToRad = Math.toRadians(destination.getLongitude());

    if (Math.abs(lon2ToRad-lon1ToRad) > Math.PI) lon1ToRad += 2*Math.PI;

    Double lat = (lat1ToRad+lat2ToRad)/2;
    Double f1 = Math.tan(Math.PI/4 + lat1ToRad/2);
    Double f2 = Math.tan(Math.PI/4 + lat2ToRad/2);
    Double f3 = Math.tan(Math.PI/4 + lat/2);
    Double lon = ((lon2ToRad-lon1ToRad)*Math.log(f3) + lon1ToRad*Math.log(f2) - lon2ToRad*Math.log(f1) ) / Math.log(f2/f1);

    if (!Double.isFinite(lon)) lon = (lon1ToRad+lon2ToRad)/2;

    lon = (lon + 3*Math.PI) % (2*Math.PI) - Math.PI;

    return new LatLon(Math.toDegrees(lat), Math.toDegrees(lon));
  }

}
