package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.camera.NavigationCamera;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.instruction.ImageCreator;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.instruction.InstructionView;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.instruction.NavigationAlertView;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMapInstanceState;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.WayNameView;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.summary.SummaryBottomSheet;
import com.creativeinfoway.smartstops.app.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigation;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.NavigationRoute;
import com.creativeinfoway.smartstops.app.android.navigation.v5.navigation.TimeFormatType;
import com.creativeinfoway.smartstops.app.android.navigation.v5.utils.DistanceFormatter;
import com.creativeinfoway.smartstops.app.android.navigation.v5.utils.extensions.LocaleEx;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.navigation.utils.extensions.ContextEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;

/**
 * View that creates the drop-in UI.
 * <p>
 * Once started, this view will check if the {@link Activity} that inflated
 * it was launched with a {@link DirectionsRoute}.
 * <p>
 * Or, if not found, this view will look for a set of {@link Point} coordinates.
 * In the latter case, a new {@link DirectionsRoute} will be retrieved from {@link NavigationRoute}.
 * <p>
 * Once valid data is obtained, this activity will immediately begin navigation
 * with {@link MapboxNavigation}.
 * <p>
 * If launched with the simulation boolean set to true, a {@link ReplayRouteLocationEngine}
 * will be initialized and begin pushing updates.
 * <p>
 * This activity requires user permissions ACCESS_FINE_LOCATION
 * and ACCESS_COARSE_LOCATION have already been granted.
 * <p>
 * A Mapbox access token must also be set by the developer (to initialize navigation).
 *
 * @since 0.7.0
 */
public class NavigationView extends CoordinatorLayout implements LifecycleOwner, OnMapReadyCallback,
        NavigationContract.View {

    private static final String MAP_INSTANCE_STATE_KEY = "navgation_mapbox_map_instance_state";
    private static final int INVALID_STATE = 0;
    private MapView mapView;
    private InstructionView instructionView;
    private SummaryBottomSheet summaryBottomSheet;
    private BottomSheetBehavior summaryBehavior;
    private ImageButton cancelBtn;
    private RecenterButton recenterBtn;
    private WayNameView wayNameView;
    private ImageButton routeOverviewBtn;

    private NavigationPresenter navigationPresenter;
    private NavigationViewEventDispatcher navigationViewEventDispatcher;
    private NavigationViewModel navigationViewModel;
    private NavigationMapboxMap navigationMap;
    private OnNavigationReadyCallback onNavigationReadyCallback;
    private NavigationOnCameraTrackingChangedListener onTrackingChangedListener;
    private NavigationMapboxMapInstanceState mapInstanceState;
    private CameraPosition initialMapCameraPosition;
    private boolean isMapInitialized;
    private boolean isSubscribed;
    private LifecycleRegistry lifecycleRegistry;

    private static final String PROFILE_NAME = "PROFILE_NAME";
    private static final String PROFILE_NAME2 = "PROFILE_NAME_2";
    private static final String ICON_SOURCE_ID = "ICON_SOURCE_ID";
    private static final String ICON_LAYER_ID = "ICON_LAYER_ID";
    List<Feature> features = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint = new ArrayList<>();
    ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypoint = new ArrayList<>();

    Context activity;
    private MapboxMap mapboxMap1;

    public NavigationView(Context context) {
        this(context, null);
        activity=context;
       // (activity).setTheme(R.style.customdark);
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
        activity=context;
       // (activity).setTheme(R.style.customdark);
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity=context;
        //(activity).setTheme(R.style.customdark);
        ThemeSwitcher.setTheme(context, attrs);
        initializeView();
    }

    /**
     * Uses savedInstanceState as a cue to restore state (if not null).
     *
     * @param savedInstanceState to restore state if not null
     */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        updatePresenterState(savedInstanceState);
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        /*mapView.addOnStyleImageMissingListener(new MapView.OnStyleImageMissingListener() {
            @Override
            public void onStyleImageMissing(@NonNull final String id) {

                String url = null;

                if (allAPiWaypoint != null) {

                    for (int i = 0; i < allAPiWaypoint.size(); i++) {
                        if (id.equals(allAPiWaypoint.get(i).getWaypoint_id())) {

                            if (allAPiWaypoint.get(i).getWaypoint_icon_image().contains("www")) {
                                url = allAPiWaypoint.get(i).getWaypoint_icon_image();
                            } else {
                                url = allAPiWaypoint.get(i).getWaypoint_icon_image();
                                url = url.replace("http://", "http://www.");
                            }

                            if (allAPiWaypoint.get(i).getCat_name() != null && allAPiWaypoint.get(i).getCat_name().contains("Pullout Small")) {
                                Glide.with(getContext())
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>(200, 200) {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                addImage(id, resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
                            } else {
                                Glide.with(getContext())
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>(145, 145) {
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
                } else {
                    if (Constant.waypointimage.contains("www")) {
                        url = Constant.waypointimage;
                    } else {
                        url = Constant.waypointimage;
                        url = url.replace("http://", "http://www.");
                    }

                    Glide.with(getContext())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .load(url)
                            .into(new CustomTarget<Bitmap>(145, 145) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    addImage(id, resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }

       *//* if(Constant.dataBeans !=null) {

          for (int i = 0; i < Constant.dataBeans.size(); i++) {
            if (id.equals(Constant.dataBeans.get(i).getName())) {

              if(allwaypoint.get(i).getWaypoint_icon_image().contains("www")){
                url = allwaypoint.get(i).getWaypoint_icon_image();
              }else{

                url = allwaypoint.get(i).getWaypoint_icon_image();
                url = url.replace("http://","http://www.");

              }

              Glide.with(getContext())
                      .asBitmap()
                      .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                      .load(url)
                      .into(new CustomTarget<Bitmap>(100, 100) {
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
        }
        else{
          if(Constant.waypointimage.contains("www")){
            url = Constant.waypointimage;
          }else{
            url = Constant.waypointimage;
            url = url.replace("http://","http://www.");
          }

          Glide.with(getContext())
                  .asBitmap()
                  .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                  .load(url)
                  .into(new CustomTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                      //imageView.setImageBitmap(resource);
                      addImage(id, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                  });
        }*//*
            }
        });*/
    }

    private void addImages(final String id) {

      String url = null;

      if (allAPiWaypoint != null) {

        for (final GetAllSubCategoryWaypoint.DataBean dataBean : allAPiWaypoint) {

            if (dataBean.getWaypoint_icon_image().contains("www")) {
              url = dataBean.getWaypoint_icon_image();
            } else {
              url = dataBean.getWaypoint_icon_image();
              url = url.replace("http://", "http://www.");
            }

            if (dataBean.getCat_name() != null && dataBean.getCat_name().contains("Pullout Small")) {
              Glide.with(getContext())
                      .asBitmap()
                      .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                      .load(url)
                      .into(new CustomTarget<Bitmap>(160, 160) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                          addImage(dataBean.getWaypoint_id(), resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                      });
            } else {
              Glide.with(getContext())
                      .asBitmap()
                      .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                      .load(url)
                      .into(new CustomTarget<Bitmap>(115, 115) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                          addImage(dataBean.getWaypoint_id(), resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                      });
            }


          }
      } else {
        if (Constant.waypointimage.contains("www")) {
          url = Constant.waypointimage;
        } else {
          url = Constant.waypointimage;
          url = url.replace("http://", "http://www.");
        }

        Glide.with(getContext())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .load(url)
                .into(new CustomTarget<Bitmap>(115, 115) {
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

    /**
     * Low memory must be reported so the {@link MapView}
     * can react appropriately.
     */
    public void onLowMemory() {
        mapView.onLowMemory();
    }

    /**
     * If the instruction list is showing and onBackPressed is called,
     * hide the instruction list and do not hide the activity or fragment.
     *
     * @return true if back press handled, false if not
     */
    public boolean onBackPressed() {
        return instructionView.handleBackPressed();
    }

    /**
     * Used to store the bottomsheet state and re-center
     * button visibility.  As well as anything the {@link MapView}
     * needs to store in the bundle.
     *
     * @param outState to store state variables
     */
    public void onSaveInstanceState(Bundle outState) {
        int bottomSheetBehaviorState = summaryBehavior == null ? INVALID_STATE : summaryBehavior.getState();
        boolean isWayNameVisible = wayNameView.getVisibility() == VISIBLE;
        NavigationViewInstanceState navigationViewInstanceState = new NavigationViewInstanceState(
                bottomSheetBehaviorState, recenterBtn.getVisibility(), instructionView.isShowingInstructionList(),
                isWayNameVisible, wayNameView.retrieveWayNameText(), navigationViewModel.isMuted());
        String instanceKey = getContext().getString(R.string.navigation_view_instance_state);
        outState.putParcelable(instanceKey, navigationViewInstanceState);
        outState.putBoolean(getContext().getString(R.string.navigation_running), navigationViewModel.isRunning());
        mapView.onSaveInstanceState(outState);
        saveNavigationMapInstanceState(outState);
    }

    /**
     * Used to restore the bottomsheet state and re-center
     * button visibility.  As well as the {@link MapView}
     * position prior to rotation.
     *
     * @param savedInstanceState to extract state variables
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String instanceKey = getContext().getString(R.string.navigation_view_instance_state);
        NavigationViewInstanceState navigationViewInstanceState = savedInstanceState.getParcelable(instanceKey);
        recenterBtn.setVisibility(navigationViewInstanceState.getRecenterButtonVisibility());
        wayNameView.setVisibility(navigationViewInstanceState.isWayNameVisible() ? VISIBLE : INVISIBLE);
        wayNameView.updateWayNameText(navigationViewInstanceState.getWayNameText());
        resetBottomSheetState(navigationViewInstanceState.getBottomSheetBehaviorState());
        updateInstructionListState(navigationViewInstanceState.isInstructionViewVisible());
        updateInstructionMutedState(navigationViewInstanceState.isMuted());
        mapInstanceState = savedInstanceState.getParcelable(MAP_INSTANCE_STATE_KEY);
    }

    /**
     * Called to ensure the {@link MapView} is destroyed
     * properly.
     * <p>
     * In an {@link Activity} this should be in {@link Activity#onDestroy()}.
     * <p>
     * In a {@link androidx.fragment.app.Fragment}, this should
     * be in {@link androidx.fragment.app.Fragment#onDestroyView()}.
     */
    public void onDestroy() {
        shutdown();
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }

    public void onStart() {
        mapView.onStart();
        if (navigationMap != null) {
            navigationMap.onStart();
        }
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    public void onResume() {
        mapView.onResume();
        lifecycleRegistry.markState(Lifecycle.State.RESUMED);
    }

    public void onPause() {
        mapView.onPause();
    }

    public void onStop() {
        mapView.onStop();
        if (navigationMap != null) {
            navigationMap.onStop();
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    /**
     * Fired after the map is ready, this is our cue to finish
     * setting up the rest of the plugins / location engine.
     * <p>
     * Also, we check for launch data (coordinates or route).
     *
     * @param mapboxMap used for route, camera, and location UI
     * @since 0.6.0
     */
    @Override
    public void onMapReady(final MapboxMap mapboxMap) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        allwaypoint = Constant.dataBeans;
        allAPiWaypoint = Constant.allAPIdataBeans;

    /*if(allwaypoint !=null){
      for(int i= 0 ; i<allwaypoint.size();i++){

        Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(allwaypoint.get(i).getLongitude_decimal()),Double.parseDouble(allwaypoint.get(i).getLatitude_decimal())));
        carlosFeature.addStringProperty(PROFILE_NAME, allwaypoint.get(i).getName());
        features.add(carlosFeature);

      }
    }else{
      Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(Constant.longi),Double.parseDouble(Constant.lati)));
      carlosFeature.addStringProperty(PROFILE_NAME, Constant.waypointName);
      features.add(carlosFeature);
    }*/

        if (allAPiWaypoint != null) {
            for (int i = 0; i < allAPiWaypoint.size(); i++) {

                Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(allAPiWaypoint.get(i).getLongitude_decimal()), Double.parseDouble(allAPiWaypoint.get(i).getLatitude_decimal())));
                carlosFeature.addStringProperty(PROFILE_NAME, allAPiWaypoint.get(i).getWaypoint_id());
                carlosFeature.addStringProperty(PROFILE_NAME2, allAPiWaypoint.get(i).getName());
                features.add(carlosFeature);

            }
        } else {
            Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(Constant.longi), Double.parseDouble(Constant.lati)));
            carlosFeature.addStringProperty(PROFILE_NAME, Constant.waypointName);
            features.add(carlosFeature);
        }

        mapboxMap.setStyle(
                new Style.Builder().fromUrl(Style.LIGHT)
                        .withSource(new GeoJsonSource(ICON_SOURCE_ID,
                                FeatureCollection.fromFeatures(features))),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap1 = mapboxMap;

                        style.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                                iconImage(get(PROFILE_NAME)),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                               // textField(get(PROFILE_NAME2)),
                                textIgnorePlacement(true),
                                textAllowOverlap(true),
                                textOffset(new Float[]{0f, 2f})
                        ));

                        initializeNavigationMap(mapView, mapboxMap);
                        initializeWayNameListener();
                        onNavigationReadyCallback.onNavigationReady(navigationViewModel.isRunning());
                        isMapInitialized = true;


                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                return handleClickIcon(mapboxMap1.getProjection().toScreenLocation(point));
                            }
                        });

                        addImages("");
                    }
                });


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void addImage(String id, Bitmap drawableImage) {

        Style style = mapboxMap1.getStyle();
        if (style != null) {
            style.addImageAsync(id, drawableImage);
        }
    }

    @Override
    public void setSummaryBehaviorState(int state) {
        summaryBehavior.setState(state);
    }

    @Override
    public void setSummaryBehaviorHideable(boolean isHideable) {
        summaryBehavior.setHideable(isHideable);
    }

    @Override
    public boolean isSummaryBottomSheetHidden() {
        return summaryBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN;
    }

    @Override
    public void resetCameraPosition() {
        if (navigationMap != null) {
            navigationMap.resetPadding();
            navigationMap.resetCameraPositionWith(NavigationCamera.NAVIGATION_TRACKING_MODE_GPS);
        }
    }

    @Override
    public void showRecenterBtn() {
        recenterBtn.show();
    }

    @Override
    public void hideRecenterBtn() {
        recenterBtn.hide();
    }

    @Override
    public boolean isRecenterButtonVisible() {
        return recenterBtn.getVisibility() == View.VISIBLE;
    }

    @Override
    public void drawRoute(DirectionsRoute directionsRoute) {
            if (navigationMap != null) {
                navigationMap.drawRoute(directionsRoute);
        }
    }

    @Override
    public void addMarker(Point position) {
        if (navigationMap != null) {
            navigationMap.addDestinationMarker(position);
        }
    }

    /**
     * Provides the current visibility of the way name view.
     *
     * @return true if visible, false if not visible
     */
    public boolean isWayNameVisible() {
        return wayNameView.getVisibility() == VISIBLE;
    }

    /**
     * Updates the text of the way name view below the
     * navigation icon.
     * <p>
     * If you'd like to use this method without being overridden by the default way names
     * values we provide, please disabled auto-query with
     * {@link NavigationMapboxMap#updateWaynameQueryMap(boolean)}.
     *
     * @param wayName to update the view
     */
    @Override
    public void updateWayNameView(@NonNull String wayName) {
        wayNameView.updateWayNameText(wayName);
    }

    /**
     * Updates the visibility of the way name view that is show below
     * the navigation icon.
     * <p>
     * If you'd like to use this method without being overridden by the default visibility values
     * values we provide, please disabled auto-query with
     * {@link NavigationMapboxMap#updateWaynameQueryMap(boolean)}.
     *
     * @param isVisible true to show, false to hide
     */
    @Override
    public void updateWayNameVisibility(boolean isVisible) {
        wayNameView.updateVisibility(isVisible);
        if (navigationMap != null) {
            navigationMap.updateWaynameQueryMap(isVisible);
        }
    }

    @Override
    public void takeScreenshot() {
        if (navigationMap != null) {
            navigationMap.takeScreenshot(new NavigationSnapshotReadyCallback(this, navigationViewModel));
        }
    }

    /**
     * Used when starting this {@link android.app.Activity}
     * for the first time.
     * <p>
     * Zooms to the beginning of the {@link DirectionsRoute}.
     *
     * @param directionsRoute where camera should move to
     */
    @Override
    public void startCamera(DirectionsRoute directionsRoute) {
        if (navigationMap != null) {
            navigationMap.startCamera(directionsRoute);
        }
    }

    /**
     * Used after configuration changes to resume the camera
     * to the last location update from the Navigation SDK.
     *
     * @param location where the camera should move to
     */
    @Override
    public void resumeCamera(Location location) {
        if (navigationMap != null) {
            navigationMap.resumeCamera(location);
        }
    }

    @Override
    public void updateNavigationMap(Location location) {
            if (navigationMap != null) {
                navigationMap.updateLocation(location);
        }
    }


    @Override
    public void updateCameraRouteOverview() {
        if (navigationMap != null) {
            int[] padding = buildRouteOverviewPadding(getContext());
            navigationMap.showRouteOverview(padding);
        }
    }

    /**
     * Should be called when this view is completely initialized.
     *
     * @param options        with containing route / coordinate data
     * @param allwaypoint
     * @param allAPiWaypoint
     */
    public void startNavigation(NavigationViewOptions options, ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint, ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypoint, Activity activity) {
        initializeNavigation(options, allwaypoint, allAPiWaypoint, activity);
    }

    /**
     * Call this when the navigation session needs to end navigation without finishing the whole view
     *
     * @since 0.16.0
     */
    public void stopNavigation() {
        navigationPresenter.onNavigationStopped();
        navigationViewModel.stopNavigation();
    }

    /**
     * Should be called after {@link NavigationView#onCreate(Bundle)}.
     * <p>
     * This method adds the {@link OnNavigationReadyCallback},
     * which will fire the ready events for this view.
     *
     * @param onNavigationReadyCallback to be set to this view
     */
    public void initialize(OnNavigationReadyCallback onNavigationReadyCallback) {
        this.onNavigationReadyCallback = onNavigationReadyCallback;
        if (!isMapInitialized) {
            mapView.getMapAsync(this);
        } else {
            onNavigationReadyCallback.onNavigationReady(navigationViewModel.isRunning());
        }
    }

    /**
     * Should be called after {@link NavigationView#onCreate(Bundle)}.
     * <p>
     * This method adds the {@link OnNavigationReadyCallback},
     * which will fire the ready events for this view.
     * <p>
     * This method also accepts a {@link CameraPosition} that will be set as soon as the map is
     * ready.  Note, this position is ignored during rotation in favor of the last known map position.
     *
     * @param onNavigationReadyCallback to be set to this view
     * @param initialMapCameraPosition  to be shown once the map is ready
     */
    public void initialize(OnNavigationReadyCallback onNavigationReadyCallback,
                           @NonNull CameraPosition initialMapCameraPosition) {
        this.onNavigationReadyCallback = onNavigationReadyCallback;
        this.initialMapCameraPosition = initialMapCameraPosition;
        if (!isMapInitialized) {
            mapView.getMapAsync(this);
        } else {
            onNavigationReadyCallback.onNavigationReady(navigationViewModel.isRunning());
        }
    }

    /**
     * Gives the ability to manipulate the map directly for anything that might not currently be
     * supported. This returns null until the view is initialized.
     * <p>
     * The {@link NavigationMapboxMap} gives direct access to the map UI (location marker, route, etc.).
     *
     * @return navigation mapbox map object, or null if view has not been initialized
     */
    @Nullable
    public NavigationMapboxMap retrieveNavigationMapboxMap() {
        return navigationMap;
    }

    /**
     * Returns the instance of {@link MapboxNavigation} powering the {@link NavigationView}
     * once navigation has started.  Will return null if navigation has not been started with
     * {@link NavigationView#startNavigation(NavigationViewOptions, ArrayList, MapboxNavigationActivity)}.
     *
     * @return mapbox navigation, or null if navigation has not started
     */
    @Nullable
    public MapboxNavigation retrieveMapboxNavigation() {
        return navigationViewModel.retrieveNavigation();
    }

    /**
     * Returns the sound button used for muting instructions
     *
     * @return sound button
     */
    public NavigationButton retrieveSoundButton() {
        return instructionView.retrieveSoundButton();
    }

    /**
     * Returns the feedback button for sending feedback about navigation
     *
     * @return feedback button
     */
    public NavigationButton retrieveFeedbackButton() {
        return instructionView.retrieveFeedbackButton();
    }

    /**
     * Returns the re-center button for recentering on current location
     *
     * @return recenter button
     */
    public NavigationButton retrieveRecenterButton() {
        return recenterBtn;
    }

    /**
     * Returns the {@link NavigationAlertView} that is shown during off-route events with
     * "Report a Problem" text.
     *
     * @return alert view that is used in the instruction view
     */
    public NavigationAlertView retrieveAlertView() {
        return instructionView.retrieveAlertView();
    }

    private void initializeView() {
        inflate(getContext(), R.layout.navigation_view_layout, this);
        bind();
        initializeNavigationViewModel();
        initializeNavigationEventDispatcher();
        initializeNavigationPresenter();
        initializeInstructionListListener();
        initializeSummaryBottomSheet();
    }

    private void bind() {
        mapView = findViewById(R.id.navigationMapView);
        instructionView = findViewById(R.id.instructionView);
        ViewCompat.setElevation(instructionView, 10);
        summaryBottomSheet = findViewById(R.id.summaryBottomSheet);
        cancelBtn = findViewById(R.id.cancelBtn);
        recenterBtn = findViewById(R.id.recenterBtn);
        wayNameView = findViewById(R.id.wayNameView);
        routeOverviewBtn = findViewById(R.id.routeOverviewBtn);
    }

    private void initializeNavigationViewModel() {
        try {
            navigationViewModel = ViewModelProviders.of((FragmentActivity) getContext()).get(NavigationViewModel.class);
        } catch (ClassCastException exception) {
            throw new ClassCastException("Please ensure that the provided Context is a valid FragmentActivity");
        }
    }

    private void initializeSummaryBottomSheet() {
        summaryBehavior = BottomSheetBehavior.from(summaryBottomSheet);
        summaryBehavior.setHideable(false);
        summaryBehavior.setBottomSheetCallback(new SummaryBottomSheetCallback(navigationPresenter,
                navigationViewEventDispatcher));
    }

    private void initializeNavigationEventDispatcher() {
        navigationViewEventDispatcher = new NavigationViewEventDispatcher();
        navigationViewModel.initializeEventDispatcher(navigationViewEventDispatcher);
    }

    private void initializeInstructionListListener() {
        instructionView.setInstructionListListener(new NavigationInstructionListListener(navigationPresenter,
                navigationViewEventDispatcher));
    }

    private void initializeNavigationMap(MapView mapView, MapboxMap map) {
        if (initialMapCameraPosition != null) {
            map.setCameraPosition(initialMapCameraPosition);
        }
        navigationMap = new NavigationMapboxMap(mapView, map);
        navigationMap.updateLocationLayerRenderMode(RenderMode.GPS);
        if (mapInstanceState != null) {
            navigationMap.restoreFrom(mapInstanceState);
            return;
        }
    }

    private void initializeWayNameListener() {
        NavigationViewWayNameListener wayNameListener = new NavigationViewWayNameListener(navigationPresenter);
        navigationMap.addOnWayNameChangedListener(wayNameListener);
    }

    private void saveNavigationMapInstanceState(Bundle outState) {
        if (navigationMap != null) {
            navigationMap.saveStateWith(MAP_INSTANCE_STATE_KEY, outState);
        }
    }

    private void resetBottomSheetState(int bottomSheetState) {
        if (bottomSheetState > INVALID_STATE) {
            boolean isShowing = bottomSheetState == BottomSheetBehavior.STATE_EXPANDED;
            summaryBehavior.setHideable(!isShowing);
            summaryBehavior.setState(bottomSheetState);
        }
    }

    private void updateInstructionListState(boolean visible) {
        if (visible) {
            instructionView.showInstructionList();
        } else {
            instructionView.hideInstructionList();
        }
    }

    private void updateInstructionMutedState(boolean isMuted) {
        if (isMuted) {
            ((SoundButton) instructionView.retrieveSoundButton()).soundFabOff();
        }
    }

    private int[] buildRouteOverviewPadding(Context context) {
        Resources resources = context.getResources();
        int leftRightPadding = (int) resources.getDimension(R.dimen.route_overview_left_right_padding);
        int paddingBuffer = (int) resources.getDimension(R.dimen.route_overview_buffer_padding);
        int instructionHeight = (int) (resources.getDimension(R.dimen.instruction_layout_height) + paddingBuffer);
        int summaryHeight = (int) resources.getDimension(R.dimen.summary_bottomsheet_height);
        return new int[]{leftRightPadding, instructionHeight, leftRightPadding, summaryHeight};
    }

    private boolean isChangingConfigurations() {
        try {
            return ((FragmentActivity) getContext()).isChangingConfigurations();
        } catch (ClassCastException exception) {
            throw new ClassCastException("Please ensure that the provided Context is a valid FragmentActivity");
        }
    }

    private void initializeNavigationPresenter() {
        navigationPresenter = new NavigationPresenter(this);
    }

    private void updatePresenterState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String navigationRunningKey = getContext().getString(R.string.navigation_running);
            boolean resumeState = savedInstanceState.getBoolean(navigationRunningKey);
            navigationPresenter.updateResumeState(resumeState);
        }
    }

    private void initializeNavigation(NavigationViewOptions options, ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint, ArrayList<GetAllSubCategoryWaypoint.DataBean> allAPiWaypoint, Activity activity) {

        this.activity = activity;
        this.allwaypoint = allwaypoint;
        this.allAPiWaypoint = allAPiWaypoint;
        establish(options);
        navigationViewModel.initialize(options);
        initializeNavigationListeners(options, navigationViewModel);
        setupNavigationMapboxMap(options);

        if (!isSubscribed) {
            initializeClickListeners();
            initializeOnCameraTrackingChangedListener();
            subscribeViewModels();
        }
    }

    private void initializeClickListeners() {
        cancelBtn.setOnClickListener(new CancelBtnClickListener(navigationViewEventDispatcher));
        recenterBtn.addOnClickListener(new RecenterBtnClickListener(navigationPresenter));
        routeOverviewBtn.setOnClickListener(new RouteOverviewBtnClickListener(navigationPresenter));
    }

    private void initializeOnCameraTrackingChangedListener() {
        onTrackingChangedListener = new NavigationOnCameraTrackingChangedListener(navigationPresenter, summaryBehavior);
        navigationMap.addOnCameraTrackingChangedListener(onTrackingChangedListener);
    }

    private void establish(NavigationViewOptions options) {
        establishDistanceFormatter(options);
        establishTimeFormat(options);
    }

    private void establishDistanceFormatter(NavigationViewOptions options) {
        String unitType = establishUnitType(options);
        String language = establishLanguage(options);
        int roundingIncrement = establishRoundingIncrement(options);
        DistanceFormatter distanceFormatter = new DistanceFormatter(getContext(), language, unitType, roundingIncrement);

        instructionView.setDistanceFormatter(distanceFormatter);
        summaryBottomSheet.setDistanceFormatter(distanceFormatter);
    }

    private int establishRoundingIncrement(NavigationViewOptions navigationViewOptions) {
        MapboxNavigationOptions mapboxNavigationOptions = navigationViewOptions.navigationOptions();
        return mapboxNavigationOptions.roundingIncrement();
    }

    private String establishLanguage(NavigationViewOptions options) {
        String voiceLanguage = options.directionsRoute().voiceLanguage();
        return voiceLanguage != null ? voiceLanguage : ContextEx.inferDeviceLanguage(getContext());
//        return voiceLanguage = ContextEx.inferDeviceLanguage(getContext());
    }

    private String establishUnitType(NavigationViewOptions options) {
        RouteOptions routeOptions = options.directionsRoute().routeOptions();
        String voiceUnits = routeOptions == null ? null : routeOptions.voiceUnits();
        return voiceUnits != null ? voiceUnits : LocaleEx.getUnitTypeForLocale(ContextEx.inferDeviceLocale(getContext()));
    }

    private void establishTimeFormat(NavigationViewOptions options) {
        @TimeFormatType
        int timeFormatType = options.navigationOptions().timeFormatType();
        summaryBottomSheet.setTimeFormat(timeFormatType);
    }

    private void initializeNavigationListeners(NavigationViewOptions options, NavigationViewModel navigationViewModel) {
        navigationMap.addProgressChangeListener(navigationViewModel.retrieveNavigation());
        navigationViewEventDispatcher.initializeListeners(options, navigationViewModel);
    }

    private void setupNavigationMapboxMap(NavigationViewOptions options) {
        navigationMap.updateWaynameQueryMap(options.waynameChipEnabled());
    }

    /**
     * Subscribes the {@link InstructionView} and {@link SummaryBottomSheet} to the {@link NavigationViewModel}.
     * <p>
     * Then, creates an instance of {@link NavigationViewSubscriber}, which takes a presenter.
     * <p>
     * The subscriber then subscribes to the view models, setting up the appropriate presenter / listener
     * method calls based on the {@link androidx.lifecycle.LiveData} updates.
     */
    private void subscribeViewModels() {
        instructionView.subscribe(this, navigationViewModel);
        summaryBottomSheet.subscribe(this, navigationViewModel);

        new NavigationViewSubscriber(this, navigationViewModel, navigationPresenter).subscribe();
        isSubscribed = true;
    }

    private void shutdown() {
        if (navigationMap != null) {
            navigationMap.removeOnCameraTrackingChangedListener(onTrackingChangedListener);
        }
        navigationViewEventDispatcher.onDestroy(navigationViewModel.retrieveNavigation());
        mapView.onDestroy();
        navigationViewModel.onDestroy(isChangingConfigurations());
        ImageCreator.getInstance().shutdown();
        navigationMap = null;
    }


    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap1.queryRenderedFeatures(screenPoint, ICON_LAYER_ID);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROFILE_NAME);
            for (int i = 0; i < features.size(); i++) {
                if (features.get(i).getStringProperty(PROFILE_NAME).equals(name)) {

                    if (allAPiWaypoint != null) {

                        for (int i1 = 0; i1 < allAPiWaypoint.size(); i1++) {
                            if (name.equals(allAPiWaypoint.get(i1).getWaypoint_id())) {

                                ConstantsLib.dataBeans = allAPiWaypoint.get(i1);
                                openDetailsDialog(allAPiWaypoint.get(i1).getName(), allAPiWaypoint.get(i1).getWaypoint_id());
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

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.driving_waypointdetails_dialog_layout, null);

        TextView txtId, txtName;

        txtId = view.findViewById(R.id.txtId);
        txtName = view.findViewById(R.id.txtName);

        txtName.setText("WAY POINT NAME : " + name);
        txtId.setText("WAY POINT ID : " + waypoint_id);

        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        if(!dialog.isShowing()) {
            dialog.show();
        }
    }
}