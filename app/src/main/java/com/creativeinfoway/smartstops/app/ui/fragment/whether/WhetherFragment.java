package com.creativeinfoway.smartstops.app.ui.fragment.whether;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.models.GetGeoPositionSearch;
import com.creativeinfoway.smartstops.app.ui.models.WhetherDetails;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.WhetherRetrofitUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class WhetherFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = WhetherFragment.class.getSimpleName();

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60 * 5;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    TextView tvCountryName, tvWhetherName, tvDate, tvTime, tvTemprature;
    ImageView ivWhetherIcon;
    ImageView ivback;
    int[] imgList = {R.drawable._1,
            R.drawable._2,
            R.drawable._3,
            R.drawable._4,
            R.drawable._5,
            R.drawable._6,
            R.drawable._7,
            R.drawable._8,
            R.drawable._8,
            R.drawable._8,
            R.drawable._11,
            R.drawable._12,
            R.drawable._13,
            R.drawable._14,
            R.drawable._15,
            R.drawable._16,
            R.drawable._17,
            R.drawable._18,
            R.drawable._19,
            R.drawable._20,
            R.drawable._21,
            R.drawable._22,
            R.drawable._23,
            R.drawable._24,
            R.drawable._25,
            R.drawable._26,
            R.drawable._26,
            R.drawable._26,
            R.drawable._29,
            R.drawable._30,
            R.drawable._31,
            R.drawable._32,
            R.drawable._33,
            R.drawable._34,
            R.drawable._35,
            R.drawable._36,
            R.drawable._37,
            R.drawable._38,
            R.drawable._39,
            R.drawable._40,
            R.drawable._41,
            R.drawable._42,
            R.drawable._43,
            R.drawable._44};

    private String mLastUpdateTime;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;

    ProgressBar loading;

    Call<ResponseBody> call;

    public WhetherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whether, container, false);
        loading = view.findViewById(R.id.loading);

        initIDs(view);
        init();
        restoreValuesFromBundle(savedInstanceState);
        return view;
    }

    private void init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//                getWhetherDetails(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()));
                getNewWeatherDetail(String.valueOf(mCurrentLocation.getLatitude()), String.valueOf(mCurrentLocation.getLongitude()));
            }
        };

        mRequestingLocationUpdates = true;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void stopLocationUpdates() {
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            stopLocationUpdates();
        }
    }

    private void initIDs(View view) {

        tvCountryName = view.findViewById(R.id.frag_whether_tv_country);
        tvDate = view.findViewById(R.id.frag_whether_tv_date);
        tvTime = view.findViewById(R.id.frag_whether_tv_time);
        tvWhetherName = view.findViewById(R.id.frag_whether_tv_whethername);
        tvTemprature = view.findViewById(R.id.frag_whether_tv_temprature);
        ivWhetherIcon = view.findViewById(R.id.frag_whether_icon);
        ivback = view.findViewById(R.id.farg_whether_iv_back);
        ivback.setOnClickListener(this);

    }

    float Kelvin_to_Fahrenheit(float K) {
        return (float) (K - 273.15) * 9 / 5 + 32;
    }

    private void getWhetherDetails(String lat, String longt) {
        try {
            Call<ResponseBody> responseBodyCall = WhetherRetrofitUtils.callAPI().getWhetherDetails(lat, longt, Constant.APP_ID_WHETHER);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();

                            WhetherDetails whetherDetails = new Gson().fromJson(strResponse, WhetherDetails.class);
                            String country = "";
                            if (whetherDetails.getSys().getCountry() != null && !whetherDetails.getSys().getCountry().isEmpty()) {
                                Locale l = new Locale("en", whetherDetails.getSys().getCountry());
                                country = l.getDisplayCountry();
                            }


                            tvCountryName.setText(String.valueOf(whetherDetails.getName() + " , " + country));

                            Calendar c = Calendar.getInstance();
                            c.setTimeZone(TimeZone.getTimeZone(String.valueOf(whetherDetails.getTimezone())));
                            Date date = c.getTime();
                            SimpleDateFormat datef = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                            String strDate = datef.format(date);
                            tvDate.setText(strDate);


                            SimpleDateFormat time = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            String strTime = time.format(date);
                            tvTime.setText(strTime);


                            float temp_calvin = (float) whetherDetails.getMain().getTemp();
                            String temp = String.valueOf(Kelvin_to_Fahrenheit(temp_calvin));
                            double value = Double.parseDouble(temp);
                            value = Double.parseDouble(new DecimalFormat("##.#").format(value));
                            Log.d("val", String.valueOf(value));


                            tvTemprature.setText(value + " °F");

                            for (int i = 0; i < whetherDetails.getWeather().size(); i++) {
                                tvWhetherName.setText(String.valueOf(whetherDetails.getWeather().get(i).getMain()));
                                String iconCode = whetherDetails.getWeather().get(i).getIcon();
                                setWhetherIcon(iconCode);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    //AccuWeather APi

    private void getNewWeatherDetail(String lat, String lng) {

        loading.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = null;

        String latLng = lat + "," + lng;

        try {
            call = WhetherRetrofitUtils.callAPI().getNewWeatherDetail(latLng, Constant.ACCUWEATHER_API_KEY);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (response.body() != null) {

                            String responseString = response.body().string();

                            GetGeoPositionSearch getGeoPositionSearch = new Gson().fromJson(responseString, GetGeoPositionSearch.class);

                            String locationKey = getGeoPositionSearch.getKey();

                            getCurrentCondition(locationKey, getGeoPositionSearch);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void getCurrentCondition(String locationKey, GetGeoPositionSearch getGeoPositionSearch) {

        try {
            call = WhetherRetrofitUtils.callAPI().getCurrentCondition(locationKey, Constant.ACCUWEATHER_API_KEY);

            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (response.body() != null) {
                            loading.setVisibility(View.GONE);
                            String responseString = response.body().string();

//                            Date date = calendar.getTime();
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//                            String strDate = simpleDateFormat.format(date);
//                            tvDate.setText(strDate);
//
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//                            String strTime = time.format(date);
//                            tvTime.setText(strTime);

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String weatherText = jsonObject.getString("WeatherText");
                                String weatherIcon = jsonObject.getString("WeatherIcon");

                                String LocalObservationDateTime = jsonObject.optString("LocalObservationDateTime");

                                if (getActivity() == null) {
                                    return;
                                }
                                for (int j = 0; j < imgList.length; j++) {


                                    if (j == Integer.parseInt(weatherIcon)) {
                                        if (j !=0 ){
                                            Glide.with(getActivity()).load(imgList[j-1]).placeholder(R.drawable.one_d).into(ivWhetherIcon);
                                        }else{
                                            Glide.with(getActivity()).load(imgList[j]).placeholder(R.drawable.one_d).into(ivWhetherIcon);
                                        }

                                        break;
                                    }
                                }

                                tvCountryName.setText(getGeoPositionSearch.getEnglishName());

                                //2019-09-18T10:50:00+05:30
                                //yyyy-MM-dd'T'HH:mm:ssXXX
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX",Locale.getDefault());
                                try {
                                    Date date  = dateFormat.parse(LocalObservationDateTime);

                                    long timeAtTemperatureMeasured = date.getTime();
                                    long currentTime = Calendar.getInstance().getTimeInMillis();

                                    long lastUpdatedTime =      currentTime-timeAtTemperatureMeasured;

                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(lastUpdatedTime);

                                    tvTime.setText("Last Updated "+ minutes+" minutes ago");


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                tvWhetherName.setText(weatherText);

                                JSONObject tempObj = jsonObject.getJSONObject("Temperature");

                                JSONObject imperialObj = tempObj.getJSONObject("Imperial");
                                String imperialVal = imperialObj.getString("Value");
                                String imperialUnit = imperialObj.getString("Unit");
                                tvTemprature.setText(imperialVal + " °" + imperialUnit);
                            }
                        }

                    } catch (IOException | JSONException e) {
                        loading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            loading.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void setWhetherIcon(String iconCode) {
        switch (iconCode) {
            case "01d":
                Glide.with(getContext()).load(R.drawable.one_d).placeholder(R.drawable.one_d).into(ivWhetherIcon);
                break;
            case "01n":
                Glide.with(getContext()).load(R.drawable.one_n).placeholder(R.drawable.one_n).into(ivWhetherIcon);
                break;
            case "02d":
                Glide.with(getContext()).load(R.drawable.two_d).placeholder(R.drawable.two_d).into(ivWhetherIcon);
                break;
            case "02n":
                Glide.with(getContext()).load(R.drawable.two_n).placeholder(R.drawable.two_n).into(ivWhetherIcon);
                break;
            case "03d":
                Glide.with(getContext()).load(R.drawable.three_d).placeholder(R.drawable.three_d).into(ivWhetherIcon);
                break;
            case "03n":
                Glide.with(getContext()).load(R.drawable.three_d).placeholder(R.drawable.three_d).into(ivWhetherIcon);
                break;
            case "04d":
                Glide.with(getContext()).load(R.drawable.four_d).placeholder(R.drawable.four_d).into(ivWhetherIcon);
                break;
            case "04n":
                Glide.with(getContext()).load(R.drawable.four_d).placeholder(R.drawable.four_d).into(ivWhetherIcon);
                break;
            case "09d":
                Glide.with(getContext()).load(R.drawable.nine_d).placeholder(R.drawable.nine_d).into(ivWhetherIcon);
                break;
            case "09n":
                Glide.with(getContext()).load(R.drawable.nine_d).placeholder(R.drawable.nine_d).into(ivWhetherIcon);
                break;
            case "10d":
                Glide.with(getContext()).load(R.drawable.ten_d).placeholder(R.drawable.ten_d).into(ivWhetherIcon);
                break;
            case "10n":
                Glide.with(getContext()).load(R.drawable.ten_n).placeholder(R.drawable.ten_n).into(ivWhetherIcon);
                break;
            case "11d":
                Glide.with(getContext()).load(R.drawable.eleven_d).placeholder(R.drawable.eleven_d).into(ivWhetherIcon);
                break;
            case "11n":
                Glide.with(getContext()).load(R.drawable.eleven_d).placeholder(R.drawable.eleven_d).into(ivWhetherIcon);
                break;
            case "13d":
                Glide.with(getContext()).load(R.drawable.one_three_d).placeholder(R.drawable.one_three_d).into(ivWhetherIcon);
                break;
            case "13n":
                Glide.with(getContext()).load(R.drawable.one_three_d).placeholder(R.drawable.one_three_d).into(ivWhetherIcon);
                break;
            case "50d":
                Glide.with(getContext()).load(R.drawable.five_zero_d).placeholder(R.drawable.five_zero_d).into(ivWhetherIcon);
                break;
            case "50n":
                Glide.with(getContext()).load(R.drawable.five_zero_d).placeholder(R.drawable.five_zero_d).into(ivWhetherIcon);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(call !=null) {
            call.cancel();
        }
    }
}
