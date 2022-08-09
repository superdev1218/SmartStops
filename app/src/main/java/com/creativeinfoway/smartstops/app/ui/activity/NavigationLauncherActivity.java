package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.Constant;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.NavigationLauncher;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.NavigationLauncherOptions;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.camera.CameraUpdateMode;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.camera.NavigationCameraUpdate;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.route.OnRouteSelectionChangeListener;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.NavigationRoute;
import com.creativeinfoway.smartstops.app.android.navigation.v5.utils.extensions.LocaleEx;
import com.creativeinfoway.smartstops.app.helpers.DBHelper;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.utils.StoreSharePreference;
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
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_CHARSET;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_FIELD_REGION_NAME;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.WAYPOINT_OBJECT;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;

//49.125,-122.85
public class NavigationLauncherActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapLongClickListener, OnRouteSelectionChangeListener, MapboxMap.OnMapClickListener {

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
    private final List<Point> wayPoints = new ArrayList<>();
    ProgressBar loading;
    MapView mapView;
    List<Feature> features = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypoint = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypointMapProcessTemp = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypointMapProcess = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> singleAPiWaypoint = new ArrayList<>();
    private static LocationEngine locationEngine;
    private static NavigationMapboxMap map;
    private DirectionsRoute route;
    private Point currentLocation;
    private boolean locationFound;
    private LinearLayout launchRouteBtn, frag_get_dir_ll_getdifflayout, frag_get_dir_window_call,linOffline;
    private TextView lblOfflineMap ;
    private RelativeLayout relDownloadingMap ;
    private TextView lblDownloadingProgress ;
    private ImageView frag_get_dir_iv_back;
    private static MapboxMap mapboxMap;
    private FeatureCollection featureCollection;
    private GeoJsonSource source;
    private GpsTracker gpsTracker;
    private String lati, longi, activityType, waypointimage;
    private GetAllSubCategoryWaypoint getAllSubCategoryWaypoint;
    private GetAllSubCategoryWaypoint getAllApiSubCategoryWaypoint;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private String waypointPosition = "0";
    private StoreSharePreference storeSharePreference;
    private ArrayList<Object> arrayList = new ArrayList<>();
    private String visibleIcon = "false";
    private String waypointOnly = "false";
    private String wayPointIdenty;
    private String downloadedMapName = null;
    private String subCategoryWayPointString = null;

    private OfflineManager offlineManager;
    private OfflineRegion currentOfflineRegion = null;

    private List<Point> currentRoutePoints = new ArrayList<>();
    private DBHelper dbHelper = null;

    private String catId;
    public static ArrayList<GetAllSubCategoryWaypoint.DataBean> categoryWayPoints = new ArrayList<>();

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
        setContentView(R.layout.activity_navigation_launcher);
        gpsTracker = new GpsTracker(this);
        storeSharePreference = new StoreSharePreference(NavigationLauncherActivity.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dbHelper = new DBHelper(this);
        arrayList.clear();
        activityType = getIntent().getStringExtra("from");
        lati = getIntent().getStringExtra("latitude");
        longi = getIntent().getStringExtra("longtitude");
        downloadedMapName = getIntent().getStringExtra("mapName");
        subCategoryWayPointString = getIntent().getStringExtra("dataBeanString");
        catId = getIntent().getStringExtra("catId");

        linOffline = findViewById(R.id.lin_offline);
        lblOfflineMap = findViewById(R.id.lbl_offline_map);
        relDownloadingMap = findViewById(R.id.rel_downloading_map);
        lblDownloadingProgress = findViewById(R.id.lbl_downloading_progress);
        frag_get_dir_iv_back = findViewById(R.id.frag_get_dir_iv_back);
        frag_get_dir_window_call = findViewById(R.id.frag_get_dir_window_call);
        frag_get_dir_ll_getdifflayout = findViewById(R.id.frag_get_dir_ll_getdifflayout);
        launchRouteBtn = findViewById(R.id.frag_get_dir_ll_proceed);

        loading = findViewById(R.id.loading);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.addOnDidFailLoadingMapListener(new MapView.OnDidFailLoadingMapListener() {
            @Override
            public void onDidFailLoadingMap(String errorMessage) {
                System.out.println(errorMessage);
            }
        });

        if (activityType.equals("SubCategoryActivity")) {
            visibleIcon = getIntent().getStringExtra("waypointiconVisible");
            allwaypoint = getIntent().getParcelableArrayListExtra("allwaypoint");
            waypointPosition = getIntent().getStringExtra("wayPointPosition");
            waypointOnly = getIntent().getStringExtra("WaypointOnlyDisplay");
            wayPointIdenty = getIntent().getStringExtra("WaypointIdentify");
            if (!visibleIcon.equals("true")) {
                singleAPiWaypoint.add(allwaypoint.get(Integer.parseInt(waypointPosition)));
            }

        } else if (activityType.equals("MarkerInfoWindowFragment")) {
            allwaypoint = getIntent().getParcelableArrayListExtra("markerinfoAllData");
        }

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
                if (checkSinglePermission(NavigationLauncherActivity.this, Manifest.permission.CALL_PHONE)) {
                    startCall911(NavigationLauncherActivity.this);
                }
            }
        });

        launchRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRouteLaunchClick();
            }
        });
        linOffline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(linOffline.getTag().toString().equals("true")){
                    confirmDelete();
                } else {
                    prepareForDownload();
                }
            }
        });


       /* mapView.addOnStyleImageMissingListener(new MapView.OnStyleImageMissingListener() {
            @Override
            public void onStyleImageMissing(@NonNull String id) {
                    addImages(id);
            }
        });*/
        launchRouteBtn.setClickable(false);


    }

    private void prepareDownloadManager(MapboxMap mapboxMap){
        offlineManager = OfflineManager.getInstance(NavigationLauncherActivity.this);

        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                if(offlineRegions.length == 0 || offlineRegions == null){
                    return;
                }

                for(OfflineRegion offlineRegion1 : offlineRegions){
                    if(getRegionName(offlineRegion1).equals(downloadedMapName)){
                        lblOfflineMap.setText("Remove Download");
                        linOffline.setTag("true");
                        currentOfflineRegion = offlineRegion1;
                    }
                }
                if(currentOfflineRegion != null && !hasInternetConnection()){
                    LatLngBounds bounds = ((OfflineTilePyramidRegionDefinition) currentOfflineRegion.getDefinition()).getBounds();
                    double regionZoom = ((OfflineTilePyramidRegionDefinition) currentOfflineRegion.getDefinition()).getMinZoom();
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(bounds.getCenter())
                            .zoom(regionZoom)
                            .build();
                    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    List<Point> points = dbHelper.getCoordinates(downloadedMapName);
                    if(!points.isEmpty()){
                        mapboxMap.getStyle().addSource(new GeoJsonSource("line-source",FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(LineString.fromLngLats(points))})));
                        mapboxMap.getStyle().addLayer(new LineLayer("line-layer","line-source").withProperties(
                                PropertyFactory.lineColor(Color.parseColor("#58A9FB")),
                                PropertyFactory.lineWidth(10F),
                                PropertyFactory.fillOutlineColor(Color.BLACK),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                        ));

                        List<LatLng> bboxPoints = new ArrayList<>();
                        for (Point point : points) {
                            bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
                        }
                        if (bboxPoints.size() > 1) {
                            try {
                                LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                                animateCameraBbox(latLngBounds, CAMERA_ANIMATION_DURATION, new int[]{50, 50, 50, 100});
                                linOffline.setVisibility(View.VISIBLE);
                            } catch (InvalidLatLngBoundsException exception) {
                                exception.printStackTrace();
                                linOffline.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });
    }

    private void confirmDelete(){
        AlertDialog dialog = new AlertDialog.Builder(NavigationLauncherActivity.this)
                .setTitle("Delete?")
                .setMessage("Are you sure want to delete this downloaded map?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        loading.setVisibility(View.VISIBLE);
                        currentOfflineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                            @Override
                            public void onDelete() {
                                dbHelper.deleteCoordinate(downloadedMapName);
                                hideLoading();
                                linOffline.setTag("false");
                                lblOfflineMap.setText("Download offline");
                                currentOfflineRegion = null;
                                Toast.makeText(NavigationLauncherActivity.this,"Offline map deleted.",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {
                                hideLoading();
                                Toast.makeText(NavigationLauncherActivity.this,"Could not delete offline map.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create();
        dialog.show();
    }

    private void prepareForDownload(){
        startProgress();
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                String styleUrl = style.getUri();
                LatLngBounds bounds = mapboxMap.getProjection().getVisibleRegion().latLngBounds;
                double minZoom = mapboxMap.getCameraPosition().zoom;
                double maxZoom = mapboxMap.getMaxZoomLevel();
                float pixelRatio = NavigationLauncherActivity.this.getResources().getDisplayMetrics().density;
                OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                        styleUrl, bounds, 5, 5, pixelRatio);

                // Build a JSONObject using the user-defined offline region title,
                // convert it into string, and use it to create a metadata variable.
                // The metadata variable will later be passed to createOfflineRegion()
                byte[] metadata;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(JSON_FIELD_REGION_NAME, downloadedMapName);
                    jsonObject.put(WAYPOINT_OBJECT,subCategoryWayPointString);
                    String json = jsonObject.toString();
                    metadata = json.getBytes(JSON_CHARSET);
                } catch (Exception exception) {
                    Timber.e("Failed to encode metadata: %s", exception.getMessage());
                    metadata = null;
                }

                offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        NavigationLauncherActivity.this.currentOfflineRegion = offlineRegion;
                        startDownload();
                    }

                    @Override
                    public void onError(String error) {
                        endProgress("Could not start download.");
                    }
                });
            }
        });
    }

    private void startDownload(){
        currentOfflineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                        0.0;

                if (status.isComplete()) {
                    // Download complete
                    endProgress("Map download successfully.");
                    dbHelper.saveCoordinates(downloadedMapName,currentRoutePoints);
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // Switch to determinate state
                    System.out.println(percentage);
                    setPercentage((int) Math.round(percentage));
                }
            }

            @Override
            public void onError(OfflineRegionError error) {
                endProgress("Could not start download.");
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                System.out.println("LIMIT EXCEED"+ limit);
                endProgressAndDeleteMap("Map is too large for offline saving");
            }
        });
        currentOfflineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

    }

    private String getRegionName(OfflineRegion offlineRegion) {
        // Get the region name from the offline region metadata
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            regionName = "";
        }
        return regionName;
    }

    private void startProgress() {
        // Disable buttons
        linOffline.setEnabled(false);

        // Start and show the progress bar
        relDownloadingMap.setVisibility(View.VISIBLE);
        lblDownloadingProgress.setText("0%");
    }

    private void setPercentage(final int percentage) {
        lblDownloadingProgress.setText(percentage + "%");
    }

    private void endProgress(final String message){
        // Enable buttons
        linOffline.setEnabled(true);
        lblOfflineMap.setText("Remove Download");
        linOffline.setTag("true");
        relDownloadingMap.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(NavigationLauncherActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void endProgressAndDeleteMap(final String message){
        linOffline.setEnabled(true);
        lblOfflineMap.setText("Download offline");
        linOffline.setTag("false");
        relDownloadingMap.setVisibility(View.GONE);

        currentOfflineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
            @Override
            public void onDelete() {
            }

            @Override
            public void onError(String error) {
            }
        });

        Toast.makeText(NavigationLauncherActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private boolean hasInternetConnection(){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            } else {
                int networkType = connectivityManager.getActiveNetworkInfo().getType();
                return networkType == ConnectivityManager.TYPE_WIFI || networkType == ConnectivityManager.TYPE_MOBILE || networkType == ConnectivityManager.TYPE_ETHERNET;
            }
        } catch (Exception e){
            return false;
        }
    }

    private void addImages(String id) {
        //stringArrayList.clear();
        String url = null;

        if (activityType.equals("SubCategoryActivity")) {

            for (GetAllSubCategoryWaypoint.DataBean dataBean : allAPiWaypoint) {
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

        } else if (activityType.equals("MarkerInfoWindowFragment") && allAPiWaypoint != null) {
            for (GetAllSubCategoryWaypoint.DataBean dataBean : allAPiWaypoint) {
                //  if (id.equals(dataBean.getWaypoint_id())) {
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
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .load(url)
                                .into(new CustomTarget<Bitmap>(180, 180) {
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
                                .into(new CustomTarget<Bitmap>(135, 135) {
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

                // }
            }
        } else {

            waypointimage = getIntent().getStringExtra("waypointimage");

            if (waypointimage.contains("www")) {
                url = waypointimage;
            } else {
                url = waypointimage;
                url = url.replace("http://", "http://www.");
            }

            Glide.with(NavigationLauncherActivity.this)
                    .asBitmap()
                    .load(url)
                    .into(new CustomTarget<Bitmap>(110, 110) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            //imageView.setImageBitmap(resource);
                            addImage(id, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    private void getAllWayPoint(final String latitude, final String longtitude, @NotNull MapboxMap mapboxMap,boolean check) {
//        allAPiWaypoint.clear();
//        allAPiWaypoint.addAll(categoryWayPoints);
//        allAPiWaypointMapProcess.addAll(categoryWayPoints);
//        setStyle(mapboxMap);
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getWayPointByCategoriesByKM(catId,latitude, longtitude,0);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{

                                    String strResponse = responseBody.string();
                                    getAllApiSubCategoryWaypoint = new Gson().fromJson(strResponse, GetAllSubCategoryWaypoint.class);

                                    if (getAllApiSubCategoryWaypoint.getStatus().equals("1")) {
                                        if (getAllApiSubCategoryWaypoint.getData().size() > 0) {
                                            if(!check) {
                                                allAPiWaypoint.clear();
                                                allAPiWaypoint.addAll(getAllApiSubCategoryWaypoint.getData());
                                                for(GetAllSubCategoryWaypoint.DataBean data : allAPiWaypoint){
                                                    if(data.getCat_name() != null) {
                                                        if(data.getMapbox_icon_visibility().equals("true")) {
                                                            if (data.getCat_name().contains("Rest Area")) {
                                                                allAPiWaypointMapProcess.add(data);
                                                            }else if(data.getCat_name().contains("SmartStop")){
                                                                allAPiWaypointMapProcess.add(data);
                                                            }else if(data.getCat_name().contains("Highway Pullout")){
                                                                allAPiWaypointMapProcess.add(data);
                                                            }
                                                        }
                                                    }

                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        setStyle(mapboxMap);
                                                    }
                                                });
                                            }else {
                                                allAPiWaypoint.addAll(getAllApiSubCategoryWaypoint.getData());
                                                allAPiWaypointMapProcessTemp.addAll(getAllApiSubCategoryWaypoint.getData());
                                                for(GetAllSubCategoryWaypoint.DataBean data : allAPiWaypointMapProcessTemp){
                                                    if(data.getCat_name() != null) {
                                                        if(data.getMapbox_icon_visibility().equals("true")) {
                                                            if (data.getCat_name().equalsIgnoreCase("Rest Area")) {
                                                                allAPiWaypointMapProcess.add(data);
                                                            } else if(data.getCat_name().equalsIgnoreCase("Large Pullout")){
                                                                allAPiWaypointMapProcess.add(data);
                                                            } else if(data.getCat_name().equalsIgnoreCase("Small Pullout")){
                                                                allAPiWaypointMapProcess.add(data);
                                                            } else if(data.getCat_name().equalsIgnoreCase("Smartstop")){
                                                                allAPiWaypointMapProcess.add(data);
                                                            }
                                                        }
                                                    }

                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        setStyle(mapboxMap);
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setStyle(mapboxMap);
                                            }
                                        });
                                    }
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideLoading();
                    if(currentOfflineRegion != null){
                        LatLngBounds bounds = (currentOfflineRegion.getDefinition()).getBounds();
                        double regionZoom = (currentOfflineRegion.getDefinition()).getMinZoom();

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(bounds.getCenter())
                                .zoom(regionZoom)
                                .build();
                        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    System.out.println("No internet" + currentOfflineRegion == null);
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e1) {
            Log.d("HomeAc", "IOException2: ");
            e1.printStackTrace();
        }
    }

    private void setStyle(MapboxMap mapboxMap) {

        if ("true".equals("true")) {
            if (allAPiWaypoint != null && allAPiWaypoint.size() > 0) {
                for (GetAllSubCategoryWaypoint.DataBean allWayPointItem : allAPiWaypoint) {

                    Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(allWayPointItem.getLongitude_decimal()), Double.parseDouble(allWayPointItem.getLatitude_decimal())));
                    carlosFeature.addStringProperty(PROFILE_NAME, allWayPointItem.getWaypoint_id());

                    features.add(carlosFeature);
                }
            } else {
                Feature antonyFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(longi), Double.parseDouble(lati)));
                antonyFeature.addStringProperty(PROFILE_NAME, Constant.wayPointID);
                features.add(antonyFeature);
            }
        } else {
            for (GetAllSubCategoryWaypoint.DataBean allWayPointItem : singleAPiWaypoint) {


                Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(allWayPointItem.getLongitude_decimal()), Double.parseDouble(allWayPointItem.getLatitude_decimal())));
                carlosFeature.addStringProperty(PROFILE_NAME, allWayPointItem.getWaypoint_id());
                features.add(carlosFeature);
            }
        }

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS).withImage(ICON_LAYER_ID, BitmapFactory.decodeResource(
                this.getResources(), R.drawable.mapbox_marker_icon_default))
                .withSource(new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(features))), style -> {

            mapboxMap.addOnMapLongClickListener(this);
            map = new NavigationMapboxMap(mapView, mapboxMap);
            map.setOnRouteSelectionChangeListener(this);
            map.updateLocationLayerRenderMode(RenderMode.NORMAL);
            initializeLocationEngine();

            wayPoints.add(Point.fromLngLat(Double.parseDouble(longi), Double.parseDouble(lati)));
            launchRouteBtn.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
            setCurrentMarkerPosition(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
            if (locationFound) {
                Log.d("NavigationAc", "locationFound: ");
                fetchRoute();
            }


            NavigationLauncherActivity.this.mapboxMap = mapboxMap;

            style.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                    iconImage(get(PROFILE_NAME)),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true),
                    // textField(get(PROFILE_NAME)),
                    textIgnorePlacement(true),
                    textAllowOverlap(true),
                    textOffset(new Float[]{0f, 2f})
            ));


            addImages("");
            //setUpData(FeatureCollection.fromJson(json));
            //new GenerateViewIconTask(NavigationLauncherActivity.this).execute(FeatureCollection.fromJson(json));
            mapboxMap.addOnMapClickListener(this);

        });

    }

    private void addImage(String id, Bitmap drawableImage) {

    /*    if(!stringArrayList.contains(id)){
            stringArrayList.add(id);*/



        Style style = mapboxMap.getStyle();
        if (style != null) {

            if (style.getImage(id) == null) {
                style.addImageAsync(id, drawableImage);
            }
        }
        //  }


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
            if (!wayPoints.isEmpty() && shouldRefetch) {
                Log.d("NavigationAc", "onActivityResult: ");
                fetchRoute();
            }
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

        /*if (locationEngine != null) {
            locationEngine.requestLocationUpdates(buildEngineRequest(), callback, null);
        }*/
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
        if (map != null) {
            map.onStop();
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }

        if (mapView != null) {
            mapView.removeOnStyleImageMissingListener(this);
            mapView.onDestroy();
        }
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void onRouteLaunchClick() {
        launchNavigationWithRoute();
    }

    @Override
    public void onMapReady(@NotNull MapboxMap mapboxMap) {

        allAPiWaypointMapProcess.clear();
        allAPiWaypointMapProcessTemp.clear();
        String checkName = "";

        if(waypointOnly.equals("true")){

            allAPiWaypoint.add(allwaypoint.get(Integer.parseInt(waypointPosition)));
            for(GetAllSubCategoryWaypoint.DataBean data : allwaypoint){
                if(!wayPointIdenty.equals(String.valueOf(data.getWaypoint_id()))) {
                    if(data.getMapbox_icon_visibility() != null){
                        if (data.getMapbox_icon_visibility().equals("true")) {
                            if(data.getCat_name() != null) {
                                checkName = data.getCat_name();
                            }
                            allAPiWaypoint.add(data);
                        }
                    }
                }

            }

            if (!checkName.contains("Rest Area") && !checkName.contains("SmartStop") && !checkName.contains("Highway Pullout")) {
                allAPiWaypointMapProcess.addAll(allAPiWaypoint);
            }


//            allAPiWaypoint.addAll(categoryWayPoints);
//            setStyle(mapboxMap);
            prepareDownloadManager(mapboxMap);
            getAllWayPoint(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), mapboxMap,true);
        }else {
            getAllWayPoint(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), mapboxMap,false);
        }
    }

    @Override
    public boolean onMapLongClick(@NonNull LatLng point) {
        if (wayPoints.size() == 2) {
            Snackbar.make(mapView, "Max way points exceeded. Clearing route...", Snackbar.LENGTH_SHORT).show();
            wayPoints.clear();
            map.clearMarkers();
            map.removeRoute();
            return false;
        }
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
            Log.d("NavigationAc", "onLocationFound: ");
            fetchRoute();
        }

        if (locationFound) {
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

        NavigationRoute.Builder builder = NavigationRoute.builder(NavigationLauncherActivity.this)
                .accessToken(Mapbox.getAccessToken())
                .origin(currentLocation)
                .profile(getRouteProfileFromSharedPreferences())
                .alternatives(true);

        for (Point wayPoint : wayPoints) {
            builder.addWaypoint(wayPoint);
        }

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

    }

    private void setFieldsFromSharedPreferences(NavigationRoute.Builder builder) {
        builder.language(getLanguageFromSharedPreferences()).voiceUnits(getUnitTypeFromSharedPreferences());
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
                .shouldSimulateRoute(false); // OLD ORIGINAL
//                .shouldSimulateRoute(true);

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

        if (allAPiWaypointMapProcess != null) {
            Constant.dataBeans = allAPiWaypointMapProcess;
        } else {
            Constant.lati = lati;
            Constant.longi = longi;
            Constant.waypointimage = waypointimage;
        }

//        mapboxMap.addPolyline(new PolylineOptions()
//        .color(Color.BLACK)
//        .width(5));


        NavigationLauncher.startNavigation(this, optionsBuilder.build(), allAPiWaypointMapProcess, allAPiWaypointMapProcess);
    }

    private boolean validRouteResponse(Response<DirectionsResponse> response) {
        return response.body() != null && !response.body().routes().isEmpty();
    }

    private void hideLoading() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private static List<Point> currentRoute = new ArrayList<Point>();
    public void boundCameraToRoute() {

        if (route != null) {
            List<Point> routeCoords = LineString.fromPolyline(route.geometry(),
                    Constants.PRECISION_6).coordinates();
            List<LatLng> bboxPoints = new ArrayList<>();
            for (Point point : routeCoords) {
                bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
            }
            currentRoutePoints.addAll(routeCoords);
            currentRoute.addAll(routeCoords);
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                    // left, top, right, bottom
                    //int topPadding = launchBtnFrame.getHeight() * 2;
                    animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, new int[]{50, 50, 50, 100});
                    linOffline.setVisibility(View.VISIBLE);
                } catch (InvalidLatLngBoundsException exception) {
                    linOffline.setVisibility(View.GONE);
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

                    if (allwaypoint != null) {

                        for (int i1 = 0; i1 < allwaypoint.size(); i1++) {
                            if (name.equals(allwaypoint.get(i1).getWaypoint_id())) {

                                com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans = allwaypoint.get(i1);

                                    com.creativeinfoway.smartstops.app.ui.utils.Constant.SINGLE_WAYPOINT = true;
                                    Intent intent = new Intent(NavigationLauncherActivity.this, SingalWaypointActivity.class);
                                    intent.putExtra("from", "0");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("wayPointPosition", "" + i1);
                                    intent.putParcelableArrayListExtra("markerinfoAllData", (ArrayList<GetAllSubCategoryWaypoint.DataBean>) allwaypoint);
                                    startActivity(intent);
                                break;
                               // openDetailsDialog(allAPiWaypoint.get(i1).getName(), allAPiWaypoint.get(i1).getWaypoint_id());
                                //Toast.makeText(getActivity(),"ID---"++"Name: -- "+getAllLatLongPoints.getData().get(i1).getName(),Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        openDetailsDialog(Constant.waypointName, Constant.wayPointID);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void openDetailsDialog(String name, String waypoint_id) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.waypointdetails_dialog_layout, null);

        TextView txtId, txtName;

        txtId = view.findViewById(R.id.txtId);
        txtName = view.findViewById(R.id.txtName);

        txtName.setText("WAY POINT NAME : " + name);
        txtId.setText("WAY POINT ID : " + waypoint_id);

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        dialog.show();
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    public void onClickChooseLanguage(View view) {
        LanguageUtils languageUtils=new LanguageUtils(this);
        languageUtils.openBottomSheet();
    }


    private static class NavigationLauncherLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<NavigationLauncherActivity> activityWeakReference;
        int i = 0;
        NavigationLauncherLocationCallback(NavigationLauncherActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {

            //Log.e(NavigationLauncherActivity.class.getSimpleName(), "onSuccess: " + result.getLastLocation().getLatitude() +"");
            NavigationLauncherActivity activity = activityWeakReference.get();
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

        @Override
        public void onFailure(@NonNull Exception exception) {
            Timber.e(exception);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
