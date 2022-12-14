package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigation;

import timber.log.Timber;

class NavigationOfflineDatabaseCallback implements OfflineDatabaseLoadedCallback {

  private final MapboxNavigation navigation;
  private final MapOfflineManager mapOfflineManager;

  NavigationOfflineDatabaseCallback(MapboxNavigation navigation, MapOfflineManager mapOfflineManager) {
    this.navigation = navigation;
    this.mapOfflineManager = mapOfflineManager;
  }

  @Override
  public void onComplete() {
    navigation.addProgressChangeListener(mapOfflineManager);
  }

  @Override
  public void onError(String error) {
    Timber.e(error);
  }

  void onDestroy() {
    mapOfflineManager.onDestroy();
  }
}