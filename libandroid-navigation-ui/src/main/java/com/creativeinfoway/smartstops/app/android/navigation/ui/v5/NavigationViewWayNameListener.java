package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;


import androidx.annotation.NonNull;

import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.OnWayNameChangedListener;

public class NavigationViewWayNameListener implements OnWayNameChangedListener {

  private final NavigationPresenter presenter;

  public NavigationViewWayNameListener(NavigationPresenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void onWayNameChanged(@NonNull String wayName) {
    presenter.onWayNameChanged(wayName);
  }
}
