package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.route;

import com.mapbox.geojson.FeatureCollection;

import java.util.List;

interface OnPrimaryRouteUpdatedCallback {
  void onPrimaryRouteUpdated(List<FeatureCollection> updatedRouteCollections);
}
