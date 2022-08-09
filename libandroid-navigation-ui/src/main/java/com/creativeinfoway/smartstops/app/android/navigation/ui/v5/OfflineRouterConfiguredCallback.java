package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;


import androidx.annotation.NonNull;

import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OfflineError;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.OnOfflineTilesConfiguredCallback;

import timber.log.Timber;

class OfflineRouterConfiguredCallback implements OnOfflineTilesConfiguredCallback {

  private final NavigationViewOfflineRouter offlineRouter;

  OfflineRouterConfiguredCallback(NavigationViewOfflineRouter offlineRouter) {
    this.offlineRouter = offlineRouter;
  }

  @Override
  public void onConfigured(int numberOfTiles) {
    offlineRouter.setIsConfigured(true);
  }

  @Override
  public void onConfigurationError(@NonNull OfflineError error) {
    Timber.e(error.getMessage());
  }
}