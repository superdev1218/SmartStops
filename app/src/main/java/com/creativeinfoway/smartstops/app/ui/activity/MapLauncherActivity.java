package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.creativeinfoway.smartstops.app.activity.navigationui.SimplifiedCallback;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.NavigationLauncherOptions;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.camera.CameraUpdateMode;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.camera.NavigationCameraUpdate;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.route.OnRouteSelectionChangeListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.NavigationRoute;
import com.creativeinfoway.smartstops.app.android.navigation.v5.utils.extensions.LocaleEx;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.navigation.utils.extensions.ContextEx;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;

public class MapLauncherActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapLongClickListener, OnRouteSelectionChangeListener,MapboxMap.OnMapClickListener  {

    public static final String UNIT_TYPE_CHANGED = "unit_type_changed";
    public static final String LANGUAGE_CHANGED = "language_changed";
    public static final String OFFLINE_CHANGED = "offline_changed";
    private static final int CAMERA_ANIMATION_DURATION = 1000;
    private static final int DEFAULT_CAMERA_ZOOM = 16;
    private static final int CHANGE_SETTING_REQUEST_CODE = 1;
    private static final int INITIAL_ZOOM = 16;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String ICON_SOURCE_ID = "ICON_SOURCE_ID";
    private static final String ICON_LAYER_ID = "ICON_LAYER_ID";
    private static final String PROFILE_NAME = "PROFILE_NAME";
    private static final String WAYPOINT_ID = "PROFILE_NAME";
    private static final String PROPERTY_CAPITAL = "capital";
    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private static final String CARLOS = "Carlos";
    private static final String ANTONY = "Antony";
    private static final String MARIA = "Maria";
    private static final String LUCIANA = "Luciana";
    private static final String PROPERTY_SELECTED = "selected";
    private static String ICON_ID = "ICON_ID";
    private final NavigationLauncherLocationCallback callback = new NavigationLauncherLocationCallback(this);
    //private final List<Point> wayPoints = new ArrayList<>();
    ProgressBar loading;
    MapView mapView;
    List<Feature> features = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint = new ArrayList<>();
    private LocationEngine locationEngine;
    private NavigationMapboxMap map;
    private DirectionsRoute route;
    private Point currentLocation;
    private boolean locationFound;
    private LinearLayout launchRouteBtn, frag_get_dir_ll_getdifflayout, frag_get_dir_window_call;
    private ImageView frag_get_dir_iv_back;
    private MapboxMap mapboxMap;
    private FeatureCollection featureCollection;
    private GeoJsonSource source;
    private GpsTracker gpsTracker;
    private String activityType, waypointimage;
    private GetAllSubCategoryWaypoint getAllSubCategoryWaypoint;
    private String lati, longi;

    private OfflineManager offlineManager = null;
    private OfflineRegion offlineRegion = null;

    public static Intent startCall911(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:911"));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return callIntent;
        }
        context.startActivity(callIntent);
        return callIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_launcher);
        gpsTracker = new GpsTracker(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //activityType = getIntent().getStringExtra("from");
       //lati = getIntent().getStringExtra("latitude");
       //longi = getIntent().getStringExtra("longtitude");

        frag_get_dir_iv_back = findViewById(R.id.frag_get_dir_iv_back);
        frag_get_dir_window_call = findViewById(R.id.frag_get_dir_window_call);
        frag_get_dir_ll_getdifflayout = findViewById(R.id.frag_get_dir_ll_getdifflayout);
        launchRouteBtn = findViewById(R.id.frag_get_dir_ll_proceed);

        loading = findViewById(R.id.loading);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

       /* if (activityType.equals("SubCategoryActivity")) {
            allwaypoint = getIntent().getParcelableArrayListExtra("allwaypoint");
        } else if (activityType.equals("MarkerInfoWindowFragment")) {
            allwaypoint = getIntent().getParcelableArrayListExtra("markerinfoAllData");
        }*/

        frag_get_dir_ll_getdifflayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frag_get_dir_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frag_get_dir_window_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSinglePermission(MapLauncherActivity.this, Manifest.permission.CALL_PHONE)) {
                    startCall911(MapLauncherActivity.this);
                }
            }
        });

        launchRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRouteLaunchClick();
            }
        });

       // mapView.addOnStyleImageMissingListener(this);
        launchRouteBtn.setClickable(false);
    }








    private void getAllWayPoint(final String latitude, final String longtitude, @NotNull MapboxMap mapboxMap) {

        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getallwaypoint(latitude, longtitude);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {

                            String strResponse = responseBody.string();

                            Log.d("Waypoint", "getAllWayPoint: "+strResponse);

                            getAllSubCategoryWaypoint = new Gson().fromJson(strResponse, GetAllSubCategoryWaypoint.class);

                            if (getAllSubCategoryWaypoint.getStatus().equals("1")) {

                                if(getAllSubCategoryWaypoint.getData().size()>0){
                                    allwaypoint.addAll(getAllSubCategoryWaypoint.getData());
                                }else{
                                    Toast.makeText(MapLauncherActivity.this,"No waypoint found in your location",Toast.LENGTH_LONG).show();
                                }

                                setStyle(mapboxMap);

                            } else {
                                Toast.makeText(MapLauncherActivity.this,"No waypoint found in your location",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e1) {
            Log.d("HomeAc", "IOException2: ");
            e1.printStackTrace();
        }
    }

    private void setStyle(MapboxMap mapboxMap) {

        if (allwaypoint.size() > 0) {

            for (GetAllSubCategoryWaypoint.DataBean allWayPointItem: allwaypoint) {
                Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(allWayPointItem.getLongitude_decimal()), Double.parseDouble(allWayPointItem.getLatitude_decimal())));
                carlosFeature.addStringProperty(PROFILE_NAME, allWayPointItem.getWaypoint_id());
                //carlosFeature.addStringProperty(PROFILE_NAME,allWayPointItem.getWaypoint_id());
                features.add(carlosFeature);
            }
        } else {
            //Feature antonyFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(longi), Double.parseDouble(lati)));
            //antonyFeature.addStringProperty(PROFILE_NAME, ANTONY);
            //features.add(antonyFeature);
        }

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.LIGHT).withSource(new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(features))), style -> {

            mapboxMap.addOnMapLongClickListener(this);
            map = new NavigationMapboxMap(mapView, mapboxMap);
            map.setOnRouteSelectionChangeListener(this);
            map.updateLocationLayerRenderMode(RenderMode.COMPASS);
            initializeLocationEngine();

            //wayPoints.add(Point.fromLngLat(Double.parseDouble(longi), Double.parseDouble(lati)));
            launchRouteBtn.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
            setCurrentMarkerPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
            if (locationFound) {
                //fetchRoute();
            }

            MapLauncherActivity.this.mapboxMap = mapboxMap;
            offlineManager = OfflineManager.getInstance(MapLauncherActivity.this);

            style.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                    iconImage(get(PROFILE_NAME)),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true),
                    /*textField(get(PROFILE_NAME)),*/
                    textIgnorePlacement(true),
                    textAllowOverlap(true),
                    textOffset(new Float[]{0f, 2f})
            ));

            //setUpData(FeatureCollection.fromJson(json));
            //new GenerateViewIconTask(NavigationLauncherActivity.this).execute(FeatureCollection.fromJson(json));
            addImages("");
            mapboxMap.addOnMapClickListener(this);

        });
    }

    private void addImage(String id, Bitmap drawableImage) {

        Style style = mapboxMap.getStyle();
        if (style != null) {
            style.addImageAsync(id, drawableImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_view_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_SETTING_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean shouldRefetch = data.getBooleanExtra(UNIT_TYPE_CHANGED, false)
                    || data.getBooleanExtra(LANGUAGE_CHANGED, false);
            /*if (!wayPoints.isEmpty() && shouldRefetch) {
                //fetchRoute();
            }*/
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @SuppressWarnings({"MissingPermission"})

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates(buildEngineRequest(), callback, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        //map.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }

        if (mapView != null) {
          //  mapView.removeOnStyleImageMissingListener(this);
            mapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void onRouteLaunchClick() {
        //launchNavigationWithRoute();
    }

    @Override
    public void onMapReady(@NotNull MapboxMap mapboxMap) {
            getAllWayPoint(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), mapboxMap);
            mapboxMap.getStyle();
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {

        /*if (wayPoints.size() == 2) {
            Snackbar.make(mapView, "Max way points exceeded. Clearing route...", Snackbar.LENGTH_SHORT).show();
            wayPoints.clear();
            map.clearMarkers();
            map.removeRoute();
            return false;
        }*/

        return false;
    }

    @Override
    public void onNewPrimaryRouteSelected(DirectionsRoute directionsRoute) {
        route = directionsRoute;
    }

    void updateCurrentLocation(Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    void onLocationFound(Location location) {
        map.updateLocation(location);
        if (!locationFound) {
            animateCamera(new LatLng(location.getLatitude(), location.getLongitude()));
            locationFound = true;
            hideLoading();
        }

        if (locationFound) {
            //fetchRoute();
        }
    }

    private void showSettings() {
        //startActivityForResult(new Intent(this, NavigationSettingsActivity.class), CHANGE_SETTING_REQUEST_CODE);
    }

    @SuppressWarnings({"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(getApplicationContext());
        LocationEngineRequest request = buildEngineRequest();
        locationEngine.requestLocationUpdates(request, callback, null);
        locationEngine.getLastLocation(callback);
    }

    private void fetchRoute() {
        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(currentLocation)
                .profile(getRouteProfileFromSharedPreferences())
                .alternatives(true);

       /* for (Point wayPoint : wayPoints) {
            builder.addWaypoint(wayPoint);
        }*/

        setFieldsFromSharedPreferences(builder);
        builder.build().getRoute(new SimplifiedCallback() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (validRouteResponse(response)) {
                    hideLoading();
                    launchRouteBtn.setClickable(true);
                    route = response.body().routes().get(0);
                    if (route.distance() > 25d) {
                        launchRouteBtn.setEnabled(true);
                        map.drawRoutes(response.body().routes());
                        boundCameraToRoute();
                    } else {
                        //Snackbar.make(mapView, R.string.error_select_longer_route, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loading.setVisibility(View.VISIBLE);
    }

    private void setFieldsFromSharedPreferences(NavigationRoute.Builder builder) {
        builder
                .language(getLanguageFromSharedPreferences())
                .voiceUnits(getUnitTypeFromSharedPreferences());
    }

    private String getUnitTypeFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultUnitType = getString(R.string.default_unit_type);
        String unitType = sharedPreferences.getString(getString(R.string.unit_type_key), defaultUnitType);
        if (unitType.equals(defaultUnitType)) {
            unitType = LocaleEx.getUnitTypeForLocale(ContextEx.inferDeviceLocale(this));
        }

        return unitType;
    }

    private Locale getLanguageFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultLanguage = getString(R.string.default_locale);
        String language = sharedPreferences.getString(getString(R.string.language_key), defaultLanguage);
        if (language.equals(defaultLanguage)) {
            return ContextEx.inferDeviceLocale(this);
        } else {
            return new Locale(language);
        }
    }

    private boolean getShouldSimulateRouteFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.simulate_route_key), false);
    }

    private String getRouteProfileFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(
                getString(R.string.route_profile_key), DirectionsCriteria.PROFILE_DRIVING_TRAFFIC
        );
    }

    private String obtainOfflinePath() {
        File offline = getExternalStoragePublicDirectory("Offline");
        return offline.getAbsolutePath();
    }

    private String retrieveOfflineVersionFromPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(getString(R.string.offline_version_key), "");
    }

    private void launchNavigationWithRoute() {
        if (route == null) {
            Snackbar.make(mapView, R.string.error_route_not_available, Snackbar.LENGTH_SHORT).show();
            return;
        }

        NavigationLauncherOptions.Builder optionsBuilder = NavigationLauncherOptions.builder()
                //.shouldSimulateRoute(getShouldSimulateRouteFromSharedPreferences());
                .shouldSimulateRoute(true); //Chnage
        CameraPosition initialPosition = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.latitude(), currentLocation.longitude()))
                .zoom(INITIAL_ZOOM)
                .build();
        optionsBuilder.initialMapCameraPosition(initialPosition);
        optionsBuilder.directionsRoute(route);
        String offlinePath = obtainOfflinePath();
        if (!TextUtils.isEmpty(offlinePath)) {
            optionsBuilder.offlineRoutingTilesPath(offlinePath);
        }
        String offlineVersion = retrieveOfflineVersionFromPreferences();
        if (!offlineVersion.isEmpty()) {
            optionsBuilder.offlineRoutingTilesVersion(offlineVersion);
        }
        // TODO Testing dynamic offline
        /**
         * File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
         * String databaseFilePath = downloadDirectory + "/" + "kingfarm.db";
         * String offlineStyleUrl = "mapbox://styles/mapbox/navigation-guidance-day-v4";
         * optionsBuilder.offlineMapOptions(new MapOfflineOptions(databaseFilePath, offlineStyleUrl));
         */

        //NavigationLauncher.startNavigation(this, optionsBuilder.build(), allwaypoint, allwaypoint);

    }

    private boolean validRouteResponse(Response<DirectionsResponse> response) {
        return response.body() != null && !response.body().routes().isEmpty();
    }

    private void hideLoading() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    public void boundCameraToRoute() {
        if (route != null) {
            List<Point> routeCoords = LineString.fromPolyline(route.geometry(),
                    Constants.PRECISION_6).coordinates();
            List<LatLng> bboxPoints = new ArrayList<>();
            for (Point point : routeCoords) {
                bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
            }
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                    // left, top, right, bottom
                    //int topPadding = launchBtnFrame.getHeight() * 2;
                    animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, new int[]{50, 50, 50, 100});
                } catch (InvalidLatLngBoundsException exception) {
                    Toast.makeText(this, R.string.error_valid_route_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
        CameraPosition position = map.retrieveMap().getCameraForLatLngBounds(bounds, padding);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
        NavigationCameraUpdate navigationCameraUpdate = new NavigationCameraUpdate(cameraUpdate);
        navigationCameraUpdate.setMode(CameraUpdateMode.OVERRIDE);
        map.retrieveCamera().update(navigationCameraUpdate, animationTime);
    }

    private void animateCamera(LatLng point) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, DEFAULT_CAMERA_ZOOM);
        NavigationCameraUpdate navigationCameraUpdate = new NavigationCameraUpdate(cameraUpdate);
        navigationCameraUpdate.setMode(CameraUpdateMode.OVERRIDE);
        map.retrieveCamera().update(navigationCameraUpdate, CAMERA_ANIMATION_DURATION);
    }

    private void setCurrentMarkerPosition(LatLng position) {
        if (position != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    map.addDestinationMarker(Point.fromLngLat(position.getLongitude(), position.getLatitude()));
                }
            });
        }
    }

    @NonNull
    private LocationEngineRequest buildEngineRequest() {
        return new LocationEngineRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .build();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, ICON_LAYER_ID);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROFILE_NAME);
            for (int i = 0; i < features.size(); i++) {
                if (features.get(i).getStringProperty(PROFILE_NAME).equals(name)) {

                    for(int i1 = 0; i1<allwaypoint.size();i1++){
                        if(name.equals(allwaypoint.get(i1).getWaypoint_id())){
                            //openDetailsDialog(name,getAllLatLongPoints.getData().get(i1).getWaypoint_id());
                            //Toast.makeText(getActivity(),"ID---"++"Name: -- "+getAllLatLongPoints.getData().get(i1).getName(),Toast.LENGTH_LONG).show();

                            com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans = allwaypoint.get(i1);
                            Constant.SINGLE_WAYPOINT = true;
                            Intent intent = new Intent(MapLauncherActivity.this,SingalWaypointActivity.class);
                            intent.putExtra("from","0");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("wayPointPosition", "" +i1);
                            intent.putParcelableArrayListExtra("markerinfoAllData", (ArrayList<GetAllSubCategoryWaypoint.DataBean>)getAllSubCategoryWaypoint.getData());
                            startActivity(intent);

                          /*  Fragment fragment = new MarkerInfoWindowFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "0");
                            bundle.putParcelable("markerinfo", allwaypoint.get(i1));
                            bundle.putParcelable("markerinfoAllData",getAllSubCategoryWaypoint);
                            fragment.setArguments(bundle);
                            fragmentTransaction.add(R.id.frame_container, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commitAllowingStateLoss();*/

                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    private void addImages(String id) {
        //stringArrayList.clear();
        String url = null;


            for (GetAllSubCategoryWaypoint.DataBean dataBean : allwaypoint) {
                // if(id.equals(dataBean.getWaypoint_id())){
                if (dataBean.getWaypoint_icon_image().contains("www")) {
                    url = dataBean.getWaypoint_icon_image();
                } else {
                    url = dataBean.getWaypoint_icon_image();
                    url = url.replace("http://", "http://www.");
                }

                if (dataBean.getCat_name() != null && dataBean.getCat_name().contains("Pullout Small")) {

                    if(dataBean.getBitmap() != null){
                        addImage(dataBean.getWaypoint_id(), dataBean.getBitmap());
                    }else {
                        Glide.with(this)
                                .asBitmap()
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(new CustomTarget<Bitmap>(160, 160) {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        dataBean.setBitmap(resource);
                                        addImage(dataBean.getWaypoint_id(), resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }


                } else {
                    if(dataBean.getBitmap() != null){
                        addImage(dataBean.getWaypoint_id(), dataBean.getBitmap());
                    }else {
                        Glide.with(this)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .load(url)
                                .into(new CustomTarget<Bitmap>(115, 115) {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        dataBean.setBitmap(resource);
                                        addImage(dataBean.getWaypoint_id(), resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }
                }
                //  }
            }

    }

   /* @Override
    public void onStyleImageMissing(@NonNull String id) {
        Log.d("NavigationViewwww", "onStyleImageMissing: " + id);

        String url = null;

        for (int i = 0; i < allwaypoint.size(); i++) {
            if (id.equals(allwaypoint.get(i).getWaypoint_id())) {
                //url = new URL(allwaypoint.get(i).getWaypoint_icon_image());
                //Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                if(allwaypoint.get(i).getWaypoint_icon_image().contains("www")){
                    url = allwaypoint.get(i).getWaypoint_icon_image();
                }else{

                    url = allwaypoint.get(i).getWaypoint_icon_image();
                    url = url.replace("http://","http://www.");

                }

                *//*Glide.with(MapLauncherActivity.this)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(new CustomTarget<Bitmap>(145,145) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                addImage(id, resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });*//*

                if(allwaypoint.get(i).getCat_name() !=null  && allwaypoint.get(i).getCat_name().contains("Pullout Small")){
                    Glide.with(this)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .load(url)
                            .into(new CustomTarget<Bitmap>(200,200) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    addImage(id, resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }else{
                    Glide.with(this)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .load(url)
                            .into(new CustomTarget<Bitmap>(145,145) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    addImage(id, resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }

            }
        }
    }*/

    private static class NavigationLauncherLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapLauncherActivity> activityWeakReference;

        NavigationLauncherLocationCallback(MapLauncherActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {

            if(result.getLastLocation() != null) {
                Log.e(MapLauncherActivity.class.getSimpleName(), "onSuccess: " + result.getLastLocation().getLatitude() + "");
                MapLauncherActivity activity = activityWeakReference.get();
                if (activity != null) {
                    Location location = result.getLastLocation();
                    if (location == null) {
                        return;
                    }
                    activity.updateCurrentLocation(Point.fromLngLat(location.getLongitude(), location.getLatitude()));
                    activity.onLocationFound(location);
                    activity.addImages("");
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Timber.e(exception);
        }
    }


}
