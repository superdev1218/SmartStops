package com.creativeinfoway.smartstops.app.ui.fragment.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;

/**
 * A simple {@link Fragment} subclass.
 */

public class Mapfragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, PermissionsListener,MapView.OnStyleImageMissingListener,Style.OnStyleLoaded{

    LinearLayout ll_call;

    private AutoCompleteTextView textviewSearch;
    private ImageView ivMenu;
    private String knownName;
    private GoogleMap mMap;
    private ProgressBar progress_circular;
    private GpsTracker gpsTracker;

    private int screenWidth;

    private static final String ICON_SOURCE_ID = "ICON_SOURCE_ID";
    private static final String ICON_LAYER_ID = "ICON_LAYER_ID";
    private static final String PROFILE_NAME = "PROFILE_NAME";
    private static final String PROPERTY_CAPITAL = "capital";
    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";

    GetAllSubCategoryWaypoint getAllLatLongPoints;
    private PermissionsManager permissionsManager;

    private String allWaypoint;
    private LatLng mid_lat_lng;

    MapView mapView;
    private MapboxMap mapboxMap1;
    private NavigationMapboxMap map;
    public Mapfragment() {
        // Required empty public constructor
    }
    List<Feature> features = new ArrayList<>();
    private FeatureCollection featureCollection;
    private static final String PROPERTY_SELECTED = "selected";
    private GeoJsonSource source;

    Activity activity;
    Call<ResponseBody> responseBodyCall;

    @Override
    public void onViewCreated(@NotNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);



        activity = getActivity();
        Mapbox.getInstance(activity,activity.getString(R.string.mapbox_access_token));

        mapView =v.findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        gpsTracker = new GpsTracker(getActivity());
        //mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initIds(v);

        if(features.size() !=0){
            features.clear();
        }

        getAllWayPoint(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()),mapboxMap1);

        mapView.addOnStyleImageMissingListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initIds(View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        gpsTracker = new GpsTracker(getActivity());
        ivMenu = view.findViewById(R.id.act_home_img_menu);
        ll_call = view.findViewById(R.id.act_home_call);
        textviewSearch = view.findViewById(R.id.mapfrag_search);
        progress_circular = view.findViewById(R.id.progress_circular);
        textviewSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_DONE) {
                    Fragment fragment = new SearchResultFragment();
                    FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                return false;
            }
        });
        ll_call.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
    }

    private void addImage(String id, Bitmap drawableImage) {

        Style style = mapboxMap1.getStyle();
        if (style != null) {
            style.addImageAsync(id, drawableImage);
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap1 = mapboxMap;
    }

    private void getAllWayPoint(final String latitude, final String longtitude, MapboxMap mapboxMap) {

        if (progress_circular.getVisibility() != View.VISIBLE){
            progress_circular.setVisibility(View.VISIBLE);
        }

        try {
            responseBodyCall = RetrofitUtils.callAPI().getallwaypoint(latitude, longtitude);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                    if (progress_circular.getVisibility() == View.VISIBLE){
                        progress_circular.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {

                            String strResponse = responseBody.string();

                            getAllLatLongPoints = new Gson().fromJson(strResponse, GetAllSubCategoryWaypoint.class);
                            if (getAllLatLongPoints.getStatus().equals("1")) {

                                setMapStyle();
                            } else {
                                Toast.makeText(getActivity(), String.valueOf(getAllLatLongPoints.getMsg()), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e1) {
                            Log.d("HomeAc", "IOException1: ");
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (progress_circular.getVisibility() == View.VISIBLE){
                        progress_circular.setVisibility(View.GONE);
                    }
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e1) {
            Log.d("HomeAc", "IOException2: ");
            e1.printStackTrace();
        }
    }

    private void setMapStyle() {

        Log.d("HomeAc", "status: ");
        if(getActivity() == null){
            return;
        }

        Log.d("HomeAc", "before thread: ");

        if(mapboxMap1 == null){
            Log.d("HomeAc", "mapbox null");
        }else {
            Log.d("HomeAc", "mapbox not null");
        }

        if (mapboxMap1 != null) {

            try {
                setStyle(mapboxMap1);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Log.d("HomeAc", ": ");
        }
    }

    private void setStyle(MapboxMap mapboxMap) {

       try {

           for (GetAllSubCategoryWaypoint.DataBean featureListItem  : getAllLatLongPoints.getData()) {
               Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(featureListItem.getLongitude_decimal()),Double.parseDouble(featureListItem.getLatitude_decimal())));
               carlosFeature.addStringProperty(PROFILE_NAME, featureListItem.getName());
               features.add(carlosFeature);
           }

           Timber.e(MapFragment.class.getSimpleName(), "setStyle: " + features.size() );


           if (mapboxMap.getStyle() == null)
               mapboxMap.setStyle(new Style.Builder().fromUri(Style.LIGHT).withSource(new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(features))),this);

       }catch (Exception e){
           Timber.e(MapFragment.class.getSimpleName(), "Exception: " + e.getMessage() );

           e.printStackTrace();
       }
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if(getActivity() == null){
            return;
        }
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap1.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            //locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap1.queryRenderedFeatures(screenPoint, ICON_LAYER_ID);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROFILE_NAME);
            List<Feature> featureList = features;
            if (featureList != null) {
                for (int i = 0; i < featureList.size(); i++) {
                    if (featureList.get(i).getStringProperty(PROFILE_NAME).equals(name)) {

                        for(int i1 = 0; i1<getAllLatLongPoints.getData().size();i1++){
                            if(name.equals(getAllLatLongPoints.getData().get(i1).getName())){
                                //openDetailsDialog(name,getAllLatLongPoints.getData().get(i1).getWaypoint_id());
                                //Toast.makeText(getActivity(),"ID---"++"Name: -- "+getAllLatLongPoints.getData().get(i1).getName(),Toast.LENGTH_LONG).show();

                                Fragment fragment = new MarkerInfoWindowFragment();
                                FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putString("from", "0");
                                bundle.putParcelable("markerinfo", getAllLatLongPoints.getData().get(i1));
                                bundle.putParcelable("markerinfoAllData",getAllLatLongPoints);
                                fragment.setArguments(bundle);
                                fragmentTransaction.add(R.id.frameContainer, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();

                            }
                        }

                        /*if (featureSelectStatus(i)) {
                            setFeatureSelectState(featureList.get(i), false);
                        } else {
                            setSelected(i);
                        }*/
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {

        if (v == ivMenu) {
            if (((HomeActivity) getActivity()).drawer.isDrawerOpen(GravityCompat.START)) {
                ((HomeActivity) getActivity()).drawer.closeDrawer(GravityCompat.START);
            } else {
                ((HomeActivity) getActivity()).drawer.openDrawer(GravityCompat.START);
                ((HomeActivity) getActivity()).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
        if (v == ll_call) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getActivity());
            }
        }
    }

    @Override
    public void onDestroyView() {

        /*if (handler != null && runnable != null){
            handler.removeCallbacks(runnable);
        }*/


        Log.d("HomeAc", "onDestroyView: ");
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(responseBodyCall !=null){
            responseBodyCall.cancel();
        }
        mapView.removeOnDidFinishRenderingFrameListener(new MapView.OnDidFinishRenderingFrameListener() {
            @Override
            public void onDidFinishRenderingFrame(boolean fully) {
                if (fully){
                    mapView.onDestroy();
                }
            }
        });

        mapView.removeOnDidFinishLoadingStyleListener(new MapView.OnDidFinishLoadingStyleListener() {
            @Override
            public void onDidFinishLoadingStyle() {
                mapView.onDestroy();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted && mapboxMap1 != null) {

            mapboxMap1.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }


    @Override
    public void onStyleImageMissing(@NonNull String id) {

        URL url = null;

        for(int i = 0;i<getAllLatLongPoints.getData().size();i++){
            if(id.equals(getAllLatLongPoints.getData().get(i).getName())){
                try {
                    url = new URL(getAllLatLongPoints.getData().get(i).getWaypoint_icon_image());
                    //Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    //image = getResizedBitmap(image,100,100);

                    /*Glide.with(getActivity())
                            .asBitmap()
                            .load(url)
                            .into(new CustomTarget<Bitmap>(145,145) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    addImage(id, resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });*/

                    if(getAllLatLongPoints.getData().get(i).getCat_name().contains("Pullout Small")){
                        Glide.with(getContext())
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
                        Glide.with(getContext())
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


                    //addImage(id, image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {

        map = new NavigationMapboxMap(mapView, mapboxMap1);
        //map.setOnRouteSelectionChangeListener(this);
        map.updateLocationLayerRenderMode(RenderMode.COMPASS);

        if(getActivity() == null){
            return;
        }

        style.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(get(PROFILE_NAME)),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                /*textField(get(PROFILE_NAME)),*/
                textIgnorePlacement(true),
                textAllowOverlap(true),
                textOffset(new Float[] {0f, 2f})
        ));
        enableLocationComponent(style);

        //setUpData(FeatureCollection.fromJson(json));
        //new GenerateViewIconTask(activity).execute(FeatureCollection.fromJson(json));

        mapboxMap1.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                return handleClickIcon(mapboxMap1.getProjection().toScreenLocation(point));
            }
        });


    }
}
