package org.open.geo;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by shashank on 03/06/15.
 */
public class LatLonTest {
  @Test
  public void testToString() {
    LatLon bangalore = new LatLon(12.9719400,77.5936900);
    assertEquals("12.0°58.0′19.0″N,077.0°35.0′37.0″E", bangalore.toString());
  }

  @Test
  public void testToStringDegOnly() {
    LatLon bangalore = new LatLon(12.9719400,77.5936900);
    assertEquals("12.9719°N,077.5937°E", bangalore.toString("d",4));
  }

  @Test
  public void testToStringDegMinOnly() {
    LatLon bangalore = new LatLon(12.9719400,77.5936900);
    assertEquals("12.97°N,077.59°E", bangalore.toString("d",2));
  }

  @Test
  public void testToStringDegMinSec() {
    LatLon bangalore = new LatLon(12.9719400,77.5936900);
    assertEquals("13.0°N,078.0°E", bangalore.toString("d",0));
  }
}
