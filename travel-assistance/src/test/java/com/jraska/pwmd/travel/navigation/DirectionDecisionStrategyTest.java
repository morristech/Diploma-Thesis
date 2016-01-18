package com.jraska.pwmd.travel.navigation;

import com.jraska.BaseTest;
import com.jraska.pwmd.core.gps.LatLng;
import org.assertj.core.data.Offset;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class DirectionDecisionStrategyTest extends BaseTest {

  //region Constants

  public static final List<LatLng> TEST_DATA = Collections.unmodifiableList(Lists.newArrayList(
      new LatLng(95, 85),
      new LatLng(85, 95),
      new LatLng(80, 70),
      new LatLng(70, 65),
      new LatLng(60, 70)));

  public static final int TEST_DATA_DIRECTION = 180 + 33;

  //endregion

  //region Test Methods

  @Test
  public void testGetCoefficient() throws Exception {
    DirectionDecisionStrategy decisionStrategy = new DirectionDecisionStrategy(5);
    for (LatLng latLng : TEST_DATA) {
      decisionStrategy.addPoint(latLng);
    }

    double coefficient = decisionStrategy.computeDirectionCoefficient();

    assertThat(coefficient).isCloseTo(0.644, Offset.offset(0.001));
  }

  @Test
  public void testGetDirectionTestData() throws Exception {
    DirectionDecisionStrategy decisionStrategy = new DirectionDecisionStrategy(5);
    for (LatLng latLng : TEST_DATA) {
      decisionStrategy.addPoint(latLng);
    }

    int direction = decisionStrategy.getDirection();
    assertThat(direction).isEqualTo(TEST_DATA_DIRECTION);
  }

  @Test
  public void testGetDirectionReverseData() throws Exception {
    DirectionDecisionStrategy decisionStrategy = new DirectionDecisionStrategy(5);
    ArrayList<LatLng> reverseData = new ArrayList<>(TEST_DATA);
    Collections.reverse(reverseData);
    for (LatLng latLng : reverseData) {
      decisionStrategy.addPoint(latLng);
    }

    int direction = decisionStrategy.getDirection();
    assertThat(direction).isEqualTo(33);
  }

  @Test
  public void testCoefficientToAngle() throws Exception {
    double[][] expected = {{1, 45}, {-1, -45}, {0.644, 33}};

    for (double[] testCase : expected) {
      int angle = DirectionDecisionStrategy.coefficientToAngle(testCase[0]);
      assertThat(angle).isEqualTo((int) testCase[1]);
    }
  }

  @Test
  public void testKeepsOnlyLastResults() throws Exception {
    DirectionDecisionStrategy decisionStrategy = new DirectionDecisionStrategy(5);
    Random random = new Random();

    decisionStrategy.addPoint(new LatLng(random.nextDouble(), random.nextDouble()));
    decisionStrategy.addPoint(new LatLng(356, 117));

    for (LatLng pos : TEST_DATA) {
      decisionStrategy.addPoint(pos);
    }

    int direction = decisionStrategy.getDirection();
    assertThat(direction).isEqualTo(TEST_DATA_DIRECTION);
  }

  //endregion
}