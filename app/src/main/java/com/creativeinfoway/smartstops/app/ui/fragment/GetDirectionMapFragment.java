package com.creativeinfoway.smartstops.app.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.SubCategoryActivity;
import com.creativeinfoway.smartstops.app.ui.directionhelpers.DataParser;
import com.creativeinfoway.smartstops.app.ui.models.GetAllLatLongPoints;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class GetDirectionMapFragment extends Fragment implements LocationListener,View.OnClickListener, OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    LinearLayout llProceed, llGetDiffWaypoint, llCall;
    ImageView ivBack;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    ArrayList<LatLng> MarkerPoints;
    LatLng destination;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline mPolyline;
    private String from;
    private RelativeLayout rootViewCategory;
    boolean isKeyboardShowing;

    private ProgressBar progress_circular;
    private GetAllLatLongPoints getAllLatLongPoints;
    private GpsTracker gpsTracker;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean isFirst = false;
    Handler handler = new Handler();

    Call<ResponseBody> responseBodyCall;

    String s_latitude,s_longtitude;

    public GetDirectionMapFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_get_direction_map, container, false);

        initIds(v);

        initlatlng();
        MarkerPoints = new ArrayList<>();
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_get_dir_map);
        if (fragment != null) {
            fragment.getMapAsync(this);
        }

        getLocation();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeath()
                .build());
        return v;
    }

    void getLocation() {
        try {
            locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void initlatlng() {

        s_latitude = getArguments().getString("latitude");
        s_longtitude = getArguments().getString("longtitude");

        destination = new LatLng(Double.parseDouble(s_latitude), Double.parseDouble(s_longtitude));

    }

    private void initIds(View view) {

        from =getArguments().getString("from");

        gpsTracker = new GpsTracker(getActivity());

        progress_circular = view.findViewById(R.id.progress_circular);

        llProceed = view.findViewById(R.id.frag_get_dir_ll_proceed);
        llCall = view.findViewById(R.id.frag_get_dir_window_call);
        llGetDiffWaypoint = view.findViewById(R.id.frag_get_dir_ll_getdifflayout);
        ivBack = view.findViewById(R.id.frag_get_dir_iv_back);
        llGetDiffWaypoint.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llProceed.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rootViewCategory = view.findViewById(R.id.rootViewCategory);

        rootViewCategory.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        rootViewCategory.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootViewCategory.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                  onKeyboardVisibilityChanged(true);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                // onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });

        isFirst = true;

        if(from.equals("SubCategoryActivity")){
        }

        if (progress_circular.getVisibility() != View.VISIBLE){
            progress_circular.setVisibility(View.VISIBLE);
        }
    }

    private void onKeyboardVisibilityChanged(boolean b) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            imm.hideSoftInputFromWindow(rootViewCategory.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    @Override
    public void onDestroyView() {
      //  mMap.clear();
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle_nodetail));

        loadMap();

    }

    private void loadMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                setLocationEnabledTrue();
            }
        } else {
            buildGoogleApiClient();
            setLocationEnabledTrue();
        }
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this::onLocationChanged);
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this::onLocationChanged);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View v) {

        if (v == llCall) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getContext());
            }
        }
        if (v == llProceed) {
//            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?daddr=28.605989,77.372970"));
//            intent.setPackage("com.google.android.apps.maps");
//            startActivity(intent);
           /* Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + destination.latitude + "," + destination.longitude + ""));
            intent.setPackage("com.google.android.apps.maps");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

            Fragment fragment = new DirectionMapFragment();
            FragmentManager fragmentManager = ((SubCategoryActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("from","SubCategoryActivity");
            bundle.putString("latitude",s_latitude);
            bundle.putString("longtitude",s_longtitude);
            fragment.setArguments(bundle);
            fragmentTransaction.add(R.id.frame_map, fragment,"Waypoint");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        if (v == llGetDiffWaypoint) {

            if (from.equals("ShowWayPointCategory")){
                FragmentManager fm = getFragmentManager();
                for (int i = fm.getBackStackEntryCount(); i > 1; i--) {
                    fm.popBackStack();
                }
            }else if(from.equals("SubCategoryActivity")){
                /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();*/
                //getActivity().finish();

                getActivity().onBackPressed();
            }else{
                FragmentManager fm = getFragmentManager();
                for (int i = fm.getBackStackEntryCount(); i > 0; i--) {
                    fm.popBackStack();
                }
            }

//
//            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + destination.latitude + "," + destination.longitude + ""));
//            intent.setPackage("com.google.android.apps.maps");
//            startActivity(intent);

        }
        if (v == ivBack) {
            Log.d("on back press", "main");
          //  getActivity().moveTaskToBack(false);
             getActivity().onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(Location location) {
        //mMap.clear();

        //Toast.makeText(getActivity(),"Location Changed",Toast.LENGTH_LONG).show();

        // LatLng origin = new LatLng(12.922294704121231, 77.61939525604248);
        //  LatLng dest = new LatLng(12.933065305628435, 77.62390136718749);
        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
//        LatLng dest = new LatLng(22.264617, 70.793319);
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(origin).radius(1000);
//        circleOptions.strokeWidth(2)
//                .strokeColor(Color.rgb(0, 136, 255))
//                .fillColor(Color.argb(20, 0, 136, 255));
//        mMap.addCircle(circleOptions);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(origin);
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 8));
        //  mMap.addMarker(markerOptions.title(String.valueOf(location)));


        //locManager.removeUpdates(this); <-- REMOVE THIS LINE

        //stop location updates

        //Toast.makeText(getActivity(),"Source - Location Changed"+origin,Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(),"Destination - Location Changed"+destination,Toast.LENGTH_LONG).show();

        place1 = new MarkerOptions().position(origin).title("Source").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        place2 = new MarkerOptions().position(destination).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //the include method will calculate the min and max bound.
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        //int width = getResources().getDisplayMetrics().widthPixels;
        //int height = getResources().getDisplayMetrics().heightPixels;
        //int padding = (int) (width * 0.25); // offset from edges of the map 10% of screen
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        //mMap.animateCamera(cu);

        CameraUpdate location1 = CameraUpdateFactory.newLatLngZoom(origin,18);
        mMap.animateCamera(location1);

        //new FetchURL(getActivity()).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        if(isFirst) {

           /* CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(origin).radius(60);
            circleOptions.strokeWidth(2)
                    .strokeColor(Color.rgb(0, 136, 255))
                    .fillColor(Color.argb(20, 0, 136, 255));
            mMap.addCircle(circleOptions);*/

            //mMap.addMarker(place1);
            mMap.addMarker(place2);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, destination);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

            isFirst = false;

        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllWayPoint(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));
            }
        },15000);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            } else
                Toast.makeText(getActivity(), "No route is found", Toast.LENGTH_LONG).show();
        }
    }

    private void getAllWayPoint(final String latitude, final String longtitude) {

        //Toast.makeText(getActivity(),"API CALLED",Toast.LENGTH_LONG).show();

    /*    if (progress_circular.getVisibility() == View.VISIBLE){
            progress_circular.setVisibility(View.GONE);
        }


        if (progress_circular.getVisibility() != View.VISIBLE){
            progress_circular.setVisibility(View.VISIBLE);
        }*/

        try {
            responseBodyCall = RetrofitUtils.callAPI().getallwaypoint(latitude, longtitude);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (progress_circular.getVisibility() == View.VISIBLE){
                        progress_circular.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {

                            String strResponse = responseBody.string();

                            getAllLatLongPoints = new Gson().fromJson(strResponse, GetAllLatLongPoints.class);
                            if (getAllLatLongPoints.getStatus().equals("1")) {

                                for (int i = 0; i < getAllLatLongPoints.getData().size(); i++) {
                                    LatLng latLng = new LatLng(Double.parseDouble(getAllLatLongPoints.getData().get(i).getLatitude_decimal()), Double.parseDouble(getAllLatLongPoints.getData().get(i).getLongitude_decimal()));

                                    if(getActivity() !=null){
                                        addMarketToLocation(latLng,getAllLatLongPoints.getData().get(i));

                                        updateCamera(Double.parseDouble(latitude), Double.parseDouble(longtitude));
                                    }
                                }

                            } else {
                                Toast.makeText(getActivity(), String.valueOf(getAllLatLongPoints.getMsg()), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e1) {
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
            e1.printStackTrace();
        }
    }
    private void addMarketToLocation(LatLng latLng, GetAllLatLongPoints.DataBean dataBean) {

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(dataBean.getWaypoint_icon_image())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(dataBean.getName()).icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap)));

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void updateCamera(double latitude, double longtitude) {

        LatLng coordinate = new LatLng(latitude, longtitude); //Store these lat lng values somewhere. These should be constant.
        //(float) calculateZoomLevel(25000, screenWidth)

        //CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate,18);
        //mMap.animateCamera(location);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMap.clear();
        if(handler !=null){
            handler.removeCallbacksAndMessages(null);
        }

        if(responseBodyCall !=null){
            responseBodyCall.cancel();
        }
    }
}


