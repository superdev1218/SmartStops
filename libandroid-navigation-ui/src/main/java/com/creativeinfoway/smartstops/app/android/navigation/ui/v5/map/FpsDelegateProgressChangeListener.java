package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map;

import android.location.Location;

import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress;

class FpsDelegateProgressChangeListener implements ProgressChangeListener {

  private final MapFpsDelegate fpsDelegate;

  FpsDelegateProgressChangeListener(MapFpsDelegate fpsDelegate) {
    this.fpsDelegate = fpsDelegate;
  }

  @Override
  public void onProgressChange(Location location, RouteProgress routeProgress) {
    fpsDelegate.adjustFpsFor(routeProgress);
  }
}
