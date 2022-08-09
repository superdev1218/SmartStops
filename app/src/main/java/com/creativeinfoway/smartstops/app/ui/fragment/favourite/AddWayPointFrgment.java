package com.creativeinfoway.smartstops.app.ui.fragment.favourite;


import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.map.NavigationMapboxMap;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.google.android.gms.maps.GoogleMap;
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

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddWayPointFrgment extends Fragment implements OnMapReadyCallback, View.OnClickListener, PermissionsListener {

    private TextView tvAddress;
    private LinearLayout btnAdd;
    private LatLng mid_lat_lng;
    private LinearLayout llcall;
    private ImageView ivback;
    private GoogleMap mMap;

    private GpsTracker gpsTracker;

    private PermissionsManager permissionsManager;

    public AddWayPointFrgment() {
        // Required empty public constructor
    }

    MapView mapView;
    private MapboxMap mapboxMap1;
    private NavigationMapboxMap map;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_way_point_frgment, container, false);

        gpsTracker = new GpsTracker(getContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mapView =v.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        initIDs(v);

        return v;
    }

    private void initIDs(View v) {

        tvAddress = v.findViewById(R.id.frag_add_waypoint_tv_address);
        btnAdd = v.findViewById(R.id.frag_add_waypoint_btnadd);
        btnAdd.setOnClickListener(this);
        llcall = v.findViewById(R.id.frag_add_fav_way_call);
        llcall.setOnClickListener(this);
        ivback = v.findViewById(R.id.frag_add_fav_way_iv_back);
        ivback.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        //mMap.clear();

        super.onDestroyView();
    }



    private void setLocationEnabledTrue() {
        View locationButton = ((View) getActivity().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
        locationButton.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        if (v == btnAdd) {
            {
                Fragment fragment = new AddFavouriteWaypointfragment();
                FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("address", tvAddress.getText().toString());
                bundle.putString("latitude", String.valueOf(mid_lat_lng.getLatitude()/*.latitude*/));
                bundle.putString("longitude", String.valueOf(mid_lat_lng.getLongitude()/*longitude*/));
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        if (v == llcall) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getActivity());
            }
        }
        if (v == ivback) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        mapboxMap1 = mapboxMap;

        /*mapboxMap.setStyle(new Style.Builder().fromUri(Style.LIGHT),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //enableLocationComponent(style);
                    }
                });*/

        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                com.mapbox.mapboxsdk.geometry.LatLng midLatLng = mapboxMap.getCameraPosition().target;
                mid_lat_lng = midLatLng;

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(midLatLng.getLatitude(), midLatLng.getLongitude(), 1);
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
        });

        if (gpsTracker.getLocation() != null){
            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            mid_lat_lng = latLng;
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
