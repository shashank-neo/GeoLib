package org.open.geo;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by shashank on 01/06/15.
 */
public class DmsTest {

  @Test
  public void parseDmsTestValidString() {
    String dmsString = "40° 44′ 55″ N";
    Double degrees = Dms.parseDms(dmsString);
    assertEquals(40.74861111111111,degrees);
  }

  @Test
  public void parseDmsTestBlankString() {
    String dmsString = "";
    Double degrees = Dms.parseDms(dmsString);
    assertEquals(null, degrees);
  }

  @Test
  public void parseDmsTestOnlyDegreeString() {
    String dmsString = "3°";
    Double degrees = Dms.parseDms(dmsString);
    assertEquals(3.0, degrees);
  }

  @Test
  public void parseDmsTestDegreeAndMinString() {
    String dmsString = "40° 44′W";
    Double degrees = Dms.parseDms(dmsString);
    assertEquals(-40.733333333333334, degrees);
  }

  @Test
  public void parseDmsTestNegativeString() {
    String dmsString = "-40° 44′";
    Double degrees = Dms.parseDms(dmsString);
    assertEquals(-40.733333333333334, degrees);
  }

  @Test
  public void toDmsTestOnlyDeg() {
    Double degrees = 40.7486;
    String dmsString = Dms.toDms(degrees, "d", 4);
    assertEquals("040.7486°", dmsString);
  }

  @Test
  public void toDmsTestDegMin() {
    Double degrees = 40.74861111111111;
    String dmsString = Dms.toDms(degrees, "dm", 4);
    assertEquals("040.0°44.9167′", dmsString);
  }

  @Test
  public void toDmsTestDegMinSec() {
    Double degrees = 40.74861111111111;
    String dmsString = Dms.toDms(degrees, "dms", 4);
    assertEquals("040.0°44.0′55.0″", dmsString);
  }

  @Test
  public void toLatTest() {
    Double degrees = 40.74861111111111;
    String lat = Dms.toLat(degrees, "dms", 2);
    assertEquals("40.0°44.0′55.0″N",lat);
  }

  @Test
  public void toLonTest() {
    Double degrees = 40.74861111111111;
    String lat = Dms.toLon(degrees, "dms", 2);
    assertEquals("040.0°44.0′55.0″E",lat);
  }

  @Test
  public void toBearngTest() {
    Double degrees = 40.74861111111111;
    String lat = Dms.toBrng(degrees, "dms", 2);
    assertEquals("040.0°44.0′55.0″",lat);
  }

  @Test
  public void compassPointTest() {
    String compassPoint = Dms.compassPoint(24.0, 3);
    assertEquals("NNE",compassPoint);
  }
}
