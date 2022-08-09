package com.creativeinfoway.smartstops.app.ui.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;

public class Constant {

    public static final String BASE_URL = "https://www.canadasmartstops.com/smartstop/api/";

    //https://www.canadasmartstops.com/smartstop/api/get_waypoint_by_cat.php

    //    public static final String BASE_URL_WHETHER = "http://api.openweathermap.org/";
//    public static final String BASE_URL_WHETHER = "https://www.accuweather.com/en/world-weather";
    public static final String BASE_URL_WHETHER = "https://dataservice.accuweather.com/";

    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String COUNTRY = "country";
    public static final String ISSIGNUP = "isSignUp";
    public static final String ISOPEN = "isOpen";
   // public static boolean MAP_SWITCH = true;


    public static final String APP_ID_WHETHER = "7c22663f5fb36678674fb9d24d55c831";

    public static final String FAQ = "http://www.canadasmartstops.com/smartstop/web/faq.php";
    public static final String LAW = "http://www.canadasmartstops.com/smartstop/web/law.html";
    public static final String PRIVACY_POLICY = "http://www.canadasmartstops.com/smartstop/web/privacy_policy.html";

    public static final String NEWUSERID = "newUserId";

    public static boolean SINGLE_WAYPOINT = false;

//    public static final String ACCUWEATHER_API_KEY = "CRjDUFo4tA7OjFa3hMO2EoANYCnwK66Y";

    public static final String ACCUWEATHER_API_KEY = "LYcRriqXfeGYexytQ5R4ipQejbcCJiyk";

    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    public static final String WAYPOINT_OBJECT = "waypoint_object";

    public static GetAllSubCategoryWaypoint.DataBean dataBeans = null;

    public static void showDialog(Activity activity, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}

// https://dataservice.accuweather.com/locations/v1/cities/geoposition/search.json?q=lat,long&apikey=
// https://dataservice.accuweather.com/currentconditions/v1/locationKey?apikey=

// https://www.accuweather.com/en/world-weather/data/2.5/weather?lat=&lon=&appid=7c22663f5fb36678674fb9d24d55c831




