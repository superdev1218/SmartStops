package com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;

interface OnFeatureFilteredCallback {
  void onFeatureFiltered(@NonNull Feature feature);
}
