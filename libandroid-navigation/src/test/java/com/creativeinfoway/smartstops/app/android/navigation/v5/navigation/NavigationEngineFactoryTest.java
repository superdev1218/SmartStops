package com.creativeinfoway.smartstops.app.android.navigation.v5.navigation;

import com.creativeinfoway.smartstops.app.android.navigation.v5.internal.navigation.NavigationEngineFactory;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class NavigationEngineFactoryTest {

  @Test
  public void onInitialization_defaultCameraEngineIsCreated() {
    NavigationEngineFactory provider = new NavigationEngineFactory();

    assertNotNull(provider.retrieveCameraEngine());
  }

  @Test
  public void onInitialization_defaultOffRouteEngineIsCreated() {
    NavigationEngineFactory provider = new NavigationEngineFactory();

    assertNotNull(provider.retrieveOffRouteEngine());
  }

  @Test
  public void onInitialization_defaultSnapEngineIsCreated() {
    NavigationEngineFactory provider = new NavigationEngineFactory();

    assertNotNull(provider.retrieveSnapEngine());
  }

  @Test
  public void onInitialization_defaultFasterRouteEngineIsCreated() {
    NavigationEngineFactory provider = new NavigationEngineFactory();

    assertNotNull(provider.retrieveFasterRouteEngine());
  }
}