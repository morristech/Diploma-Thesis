package com.jraska.pwmd.travel.navigation;

import com.jraska.BaseTest;
import com.jraska.pwmd.core.gps.LatLng;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteCursorTest extends BaseTest {
  //region Constants

  private static final LatLng ZERO = pos(0, 0);
  private static final LatLng NORTH_WEST = pos(-1, 1);
  private static final LatLng SOUTH_WEST = pos(-1, -1);
  private static final LatLng NORTH_EAST = pos(1, 1);
  private static final LatLng SOUTH_EAST = pos(1, -1);

  //endregion

  @Test
  public void testFindClosestDistance() throws Exception {
    ArrayList<LatLng> positions = Lists.newArrayList(ZERO, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST);

    RouteCursor routeCursor = new RouteCursor(positions);

    LatLng closestPosition = routeCursor.findClosestPosition(pos(0.5, 0.8));
    assertThat(closestPosition).isEqualTo(NORTH_EAST);

    closestPosition = routeCursor.findClosestPosition(pos(-0.5, 0.4));
    assertThat(closestPosition).isEqualTo(ZERO);

    closestPosition = routeCursor.findClosestPosition(pos(-0.05, 0.7));
    assertThat(closestPosition).isEqualTo(ZERO);

    closestPosition = routeCursor.findClosestPosition(pos(-0.5, 0.51));
    assertThat(closestPosition).isEqualTo(NORTH_WEST);

    closestPosition = routeCursor.findClosestPosition(pos(0.7, -0.4));
    assertThat(closestPosition).isEqualTo(SOUTH_EAST);

    closestPosition = routeCursor.findClosestPosition(pos(-0.5, -0.6));
    assertThat(closestPosition).isEqualTo(SOUTH_WEST);
  }

  private static LatLng pos(double lat, double lon) {
    return new LatLng(lat, lon);
  }
}