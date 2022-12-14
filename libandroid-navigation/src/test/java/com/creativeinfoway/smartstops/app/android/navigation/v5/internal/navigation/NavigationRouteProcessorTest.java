package com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation;

import com.mapbox.navigator.NavigationStatus;
import com.mapbox.navigator.RouteState;
import com.creativeinfoway.smartstops.app.android.navigation.v5.BaseTest;
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NavigationRouteProcessorTest extends BaseTest {

  @Test
  public void buildNewRouteProgress_routeProgressReturned() throws IOException {
    MapboxNavigator navigator = mock(MapboxNavigator.class);
    NavigationStatus status = mock(NavigationStatus.class);
    when(status.getRouteState()).thenReturn(mock(RouteState.class));
    NavigationRouteProcessor processor = new NavigationRouteProcessor();

    RouteProgress progress = processor.buildNewRouteProgress(navigator, status, buildTestDirectionsRoute());

    assertNotNull(progress);
  }

  @Test
  public void buildNewRouteProgress_previousStatusIsReturned() throws IOException {
    MapboxNavigator navigator = mock(MapboxNavigator.class);
    NavigationStatus status = mock(NavigationStatus.class);
    when(status.getRouteState()).thenReturn(mock(RouteState.class));
    NavigationRouteProcessor processor = new NavigationRouteProcessor();

    processor.buildNewRouteProgress(navigator, status, buildTestDirectionsRoute());

    assertEquals(status, processor.retrievePreviousStatus());
  }
}
