package com.creativeinfoway.smartstops.app.ui.fragment.category;


import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.home.SearchResultFragment;
import com.creativeinfoway.smartstops.app.ui.models.WayPointByCategory;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private LinearLayout ll_call;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private ImageView ivBack;
    private GpsTracker gpsTracker;

     private SearchView searchCategoryMap;

    private WayPointByCategory wayPointByCategory;

    public CategoryMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category_map, container, false);

        initIds(v);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.frag_cat_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build());

        return v;
    }

    private void initIds(View v) {
        gpsTracker = new GpsTracker(getContext());
        ivBack = v.findViewById(R.id.act_cat_map_img_back);
        ll_call = v.findViewById(R.id.act_cat_map_home_call);
        searchCategoryMap = v.findViewById(R.id.searchCategoryMap);
        searchCategoryMap.onActionViewExpanded();
        searchCategoryMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Fragment fragment = new SearchResultFragment();
                FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("searchWord", query);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ll_call.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadMap();


    }



    @Override
    public void onDestroyView() {
            mMap.clear();
        super.onDestroyView();
    }

    private void loadMap() {


        if (gpsTracker.getLocation() != null){

            getWayPointByCategories(getArguments().getString("catid"), String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));

            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions.title(String.valueOf(gpsTracker.getLocation())));

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for (int i = 0; i < wayPointByCategory.getData().size(); i++) {
                        if (wayPointByCategory.getData().get(i).getLatitude_decimal().equals(String.valueOf(marker.getPosition().latitude))) {
                            Fragment fragment = new MarkerInfoWindowFragment();
                            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "1");
                            bundle.putSerializable("markerinfo_from_cat_map", wayPointByCategory.getData().get(i));
                            fragment.setArguments(bundle);
                            fragmentTransaction.replace(R.id.frameContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                    return false;
                }
            });
        }


    }

    private void setLocationEnabledTrue() {
//        View locationButton = ((View) getActivity().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 30);
//        locationButton.setVisibility(View.VISIBLE);
    }









    @Override
    public void onClick(View v) {

        if (v == ivBack) {
            getActivity().onBackPressed();
        }
        if (v == ll_call) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getActivity());
            }
        }
    }

    public void getWayPointByCategories(String catid, String latitude, String longitude) {

        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getWayPointByCategories(catid, latitude, longitude);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    try {
                        String str_Response = responseBody.string();
                        wayPointByCategory = new Gson().fromJson(str_Response, WayPointByCategory.class);
                        if (wayPointByCategory.getStatus().equals("1")) {
                            for (int i = 0; i < wayPointByCategory.getData().size(); i++) {

                                addMarketToLocation(wayPointByCategory.getData().get(i));
                            }


                            updateCamera();
                        } else {
                            Toast.makeText(getActivity(), String.valueOf(wayPointByCategory.getMsg()), Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }

    private void addMarketToLocation(WayPointByCategory.DataBean data) {

        final LatLng latLng = new LatLng(Double.parseDouble(data.getLatitude_decimal()), Double.parseDouble(data.getLongitude_decimal()));

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(data.getWaypoint_icon_image())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(data.getName()).icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap)));

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void updateCamera() {

        LatLng coordinate = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
        mMap.animateCamera(location);

    }

}
