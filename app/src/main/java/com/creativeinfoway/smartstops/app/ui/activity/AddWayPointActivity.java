package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;

public class AddWayPointActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, PermissionsListener {

    private TextView tvAddress;
    private LinearLayout btnAdd;
    private LatLng mid_lat_lng;
    private LinearLayout llcall;
    private ImageView ivback;
    private  long mLastClickTime = 0;


    private GpsTracker gpsTracker;

    private PermissionsManager permissionsManager;

    MapView mapView;
    private MapboxMap mapboxMap1;
    private NavigationMapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_add_way_point);

        gpsTracker = new GpsTracker(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mapView =findViewById(R.id.mapView);
        tvAddress = findViewById(R.id.frag_add_waypoint_tv_address);
        btnAdd = findViewById(R.id.frag_add_waypoint_btnadd);
        llcall = findViewById(R.id.frag_add_fav_way_call);
        ivback = findViewById(R.id.frag_add_fav_way_iv_back);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnAdd.setOnClickListener(this);
        llcall.setOnClickListener(this);
        ivback.setOnClickListener(this);

    }

    private void setLocationEnabledTrue() {
        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
        locationButton.setVisibility(View.GONE);
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onClick(View v) {

        if (v == btnAdd) {
            {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    Log.d("=====1"," on Add Button");
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent intent = new Intent(AddWayPointActivity.this,AddFavouriteWaypointActivity.class);
                intent.putExtra("address",tvAddress.getText().toString());
                intent.putExtra("latitude",String.valueOf(mid_lat_lng.getLatitude()));
                intent.putExtra("longitude",String.valueOf(mid_lat_lng.getLongitude()));
                startActivity(intent);


                /*Fragment fragment = new AddFavouriteWaypointfragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("address", tvAddress.getText().toString());
                bundle.putString("latitude", String.valueOf(mid_lat_lng.getLatitude()*//*.latitude*//*));
                bundle.putString("longitude", String.valueOf(mid_lat_lng.getLongitude()*//*longitude*//*));
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_contain, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        }
        if (v == llcall) {
            if (checkSinglePermission(this, Manifest.permission.CALL_PHONE)) {
                startCall911(this);
            }
        }
        if (v == ivback) {
            onBackPressed();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        mapboxMap1 = mapboxMap;

       /* mapboxMap.setStyle(new Style.Builder().fromUri(Style.LIGHT),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });*/

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });

        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                com.mapbox.mapboxsdk.geometry.LatLng midLatLng = mapboxMap.getCameraPosition().target;
                mid_lat_lng = midLatLng;

                Geocoder geocoder = new Geocoder(AddWayPointActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(midLatLng.getLatitude(), midLatLng.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        String addressLine = addresses.get(0).getAddressLine(0);

                        Timber.d(String.valueOf(addressLine));
                        tvAddress.setVisibility(View.VISIBLE);
                        if (addressLine.equals("null")) {
                            tvAddress.setText("");
                        } else {
                            tvAddress.setText(addressLine);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        if (gpsTracker.getLocation() != null){
            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mid_lat_lng = latLng;
            Geocoder geocoder = new Geocoder(AddWayPointActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    String addressLine = addresses.get(0).getAddressLine(0);

                    Log.d("loca", String.valueOf(addressLine));
                    tvAddress.setVisibility(View.VISIBLE);
                    if (addressLine.equals("null")) {
                        tvAddress.setText("");
                    } else {
                        tvAddress.setText(addressLine);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setLocationEnabledTrue();
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap1.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(AddWayPointActivity.this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap1.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(AddWayPointActivity.this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
