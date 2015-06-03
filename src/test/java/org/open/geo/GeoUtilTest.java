package org.open.geo;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by shashank on 03/06/15.
 */
public class GeoUtilTest {
  @Test
  public void distanceBetweenTestNullRadius() {
    LatLon p1 = new LatLon(12.9719400, 77.5936900);
    LatLon p2 = new LatLon(13.0878400, 80.2784700);
    Double distance = GeoUtil.distanceBetween(p1, p2, null);
    assertEquals(291131.41, distance, 0.5);
  }

  @Test
  public void distanceBetweenTestRadiusInMiles() {
    LatLon p1 = new LatLon(12.9719400, 77.5936900);
    LatLon p2 = new LatLon(13.0878400, 80.2784700);
    Double distance = GeoUtil.distanceBetween(p1, p2, 3959.00);
    assertEquals(180.91, distance, 0.5);
  }

  @Test
  public void bearingBetweenTest() {
    LatLon p1 = new LatLon(52.205, 0.119);
    LatLon p2 = new LatLon(48.857, 2.351);
    Double bearing = GeoUtil.bearingBetween(p1,p2);
    assertEquals(156.16, bearing, 0.5);
  }

  @Test
  public void finalBearingToTest() {
    LatLon p1 = new LatLon(52.205, 0.119);
    LatLon p2 = new LatLon(48.857, 2.351);
    Double bearing = GeoUtil.finalBearingTo(p1, p2);
    assertEquals(157.89, bearing, 0.5);
  }

  @Test
  public void midPointBetweenTest() {
    LatLon p1 = new LatLon(52.205, 0.119);
    LatLon p2 = new LatLon(48.857, 2.351);
    LatLon pMid = GeoUtil.midPointBetween(p1, p2);
    assertEquals("50.5363°N,001.2746°E", pMid.toString("d",null));
  }

  @Test
  public void destinationPointTest() {
    LatLon p1 = new LatLon(51.4778, -0.0015);
    LatLon p2 = GeoUtil.destinationPoint(p1,7794.00, 300.7, null);
    assertEquals("51.5135°N,000.0983°W", p2.toString("d", null));
  }

  @Test
  public void intersectionTest() {
    LatLon p1 = new LatLon(51.8853, 0.2545);
    Double bearingSource = 108.547;
    LatLon p2 = new LatLon(49.0034, 2.5735);
    Double bearingDestination =  32.435;
    LatLon pInt = GeoUtil.intersection(p1, bearingSource, p2, bearingDestination);
    assertEquals("50.9078°N,004.5084°E", pInt.toString("d", null));
  }

  @Test
  public void crossTrackDistanceTo() {
    LatLon pCurrent = new LatLon(53.2611, -0.7972);
    LatLon source = new LatLon(53.3206, -1.7297), destination = new LatLon(53.1887, 0.1334);
    Double d = GeoUtil.crossTrackDistanceTo(pCurrent, source, destination, null);
    assertEquals(-307.54, d, 0.5);
  }

  @Test
  public void rhumbDistanceBetweenTest() {
    LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
    Double d = GeoUtil.rhumbDistanceBetween(p1, p2, null);
    assertEquals(40307.74, d, 0.5);
  }

  @Test
  public void rhumbBearingBetweenTest() {
    LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
    double d = GeoUtil.rhumbBearingBetween(p1,p2);
    assertEquals(116.72, d, 0.5);
  }

  @Test
  public void rhumbDestinationPointTest() {
    LatLon p1 = new LatLon(51.127, 1.338);
    LatLon p2 = GeoUtil.rhumbDestinationPoint(p1, 40300.00, 116.7, null);
    assertEquals("50.9642°N,001.853°E", p2.toString("d", null));
  }

  @Test
  public void rhumbMidpointBetweenTest() {
    LatLon p1 = new LatLon(51.127, 1.338), p2 = new LatLon(50.964, 1.853);
    LatLon p3 = GeoUtil.rhumbMidpointBetween(p1,p2);
    assertEquals("51.0455°N,001.5957°E", p3.toString("d", null));
  }
}
