package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import com.mapbox.mapboxsdk.Mapbox;

class MapConnectivityController {

  void assign(Boolean state) {
    Mapbox.setConnected(state);
  }
}
