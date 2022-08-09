package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import android.location.Location;

import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress;

class NavigationViewModelProgressChangeListener implements ProgressChangeListener {

  private final NavigationViewModel viewModel;

  NavigationViewModelProgressChangeListener(NavigationViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void onProgressChange(Location location, RouteProgress routeProgress) {
    viewModel.updateRouteProgress(routeProgress);
    viewModel.updateLocation(location);
  }
}