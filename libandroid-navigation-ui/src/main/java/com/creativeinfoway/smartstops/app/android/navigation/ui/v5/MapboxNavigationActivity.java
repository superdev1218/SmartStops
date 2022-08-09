package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.v5.offroute.OffRoute;
import com.creativeinfoway.smartstops.app.android.navigation.v5.offroute.OffRouteListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.routeprogress.RouteProgress;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.listeners.NavigationListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.NavigationConstants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Serves as a launching point for the custom drop-in UI, {@link NavigationView}.
 * <p>
 * Demonstrates the proper setup and usage of the view, including all lifecycle methods.
 */
public class MapboxNavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback,
        NavigationListener, OnMapReadyCallback {

    private NavigationView navigationView;
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypoint = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        navigationView = findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);

//        allwaypoint = getIntent().getParcelableArrayListExtra("allwaypoint");
        allAPiWaypoint = getIntent().getParcelableArrayListExtra("allAPiWaypoint");

        allwaypoint = new ArrayList<>();
        Constant.allAPIdataBeans = allAPiWaypoint;

        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        NavigationViewOptions.Builder options = NavigationViewOptions.builder();
        options.navigationListener(this);
        extractRoute(options);
        extractConfiguration(options);
        options.navigationOptions(new MapboxNavigationOptions.Builder().build());
        navigationView.startNavigation(options.build(), allwaypoint, allAPiWaypoint, this);
    }

    @Override
    public void onCancelNavigation() {
        finishNavigation();
    }

    @Override
    public void onNavigationFinished() {
        finishNavigation();
    }

    @Override
    public void onNavigationRunning() {
        // Intentionally empty
    }

    private void initialize() {
        Parcelable position = getIntent().getParcelableExtra(NavigationConstants.NAVIGATION_VIEW_INITIAL_MAP_POSITION);
        if (position != null) {
            navigationView.initialize(this, (CameraPosition) position);
        } else {
            navigationView.initialize(this);
        }
    }

    private void extractRoute(NavigationViewOptions.Builder options) {
        DirectionsRoute route = NavigationLauncher.extractRoute(this);
        options.directionsRoute(route);
    }

    private void extractConfiguration(NavigationViewOptions.Builder options) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        options.shouldSimulateRoute(preferences.getBoolean(NavigationConstants.NAVIGATION_VIEW_SIMULATE_ROUTE, false));
        String offlinePath = preferences.getString(NavigationConstants.OFFLINE_PATH_KEY, "");
        if (!offlinePath.isEmpty()) {
            options.offlineRoutingTilesPath(offlinePath);
        }
        String offlineVersion = preferences.getString(NavigationConstants.OFFLINE_VERSION_KEY, "");
        if (!offlineVersion.isEmpty()) {
            options.offlineRoutingTilesVersion(offlineVersion);
        }
        String offlineMapDatabasePath = preferences.getString(NavigationConstants.MAP_DATABASE_PATH_KEY, "");
        String offlineMapStyleUrl = preferences.getString(NavigationConstants.MAP_STYLE_URL_KEY, "");
        if (!offlineMapDatabasePath.isEmpty() && !offlineMapStyleUrl.isEmpty()) {
            MapOfflineOptions mapOfflineOptions = new MapOfflineOptions(offlineMapDatabasePath, offlineMapStyleUrl);
            options.offlineMapOptions(mapOfflineOptions);
        }
    }

    private void finishNavigation() {
        NavigationLauncher.cleanUpPreferences(this);
        finish();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
    }
}
