package com.creativeinfoway.smartstops.app.ui.interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api_Interface {

    /*@FormUrlEncoded
    @POST("registration.php")
    Call<ResponseBody> registration(
            @Field("user_name") String username,
            @Field("country") String country,
            @Field("email") String email,
            @Field("password") String password

    );
*/


    @FormUrlEncoded
    @POST("new_registration.php")
    Call<ResponseBody> signUp(
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("country") String country,
            @Field("email") String email,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("registration.php")
    Call<ResponseBody> newRegistration(
            @Field("device_id") String deviceId
    );

    @FormUrlEncoded
    @POST("device_registration.php")
    Call<ResponseBody> newDeviceRegistration(
            @Field("device_id") String deviceId
    );


    @POST("get_user_count.php")
    Call<ResponseBody> getActiveUser(
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("get_all_waypoint.php")
    Call<ResponseBody> getallwaypoint(@Field("latitude_decimal") String latitude,
                                      @Field("longitude_decimal") String longitude);

    @GET("get_category.php")
    Call<ResponseBody> getcategory();

    @GET("get_category.php")
    Call<ResponseBody> getcategory(@Query("page") int page);

    @GET("get_setting.php")
    Call<ResponseBody> getsetting();

    @FormUrlEncoded
    @POST("get_waypoint_by_sub_cat.php")
    Call<ResponseBody> getsubcategory(@Field("sub_cat_id") String cat_id,
                                      @Field("latitude_decimal") String latitude,
                                      @Field("longitude_decimal") String longitude);

    @FormUrlEncoded
    @POST("get_waypoint_by_cat.php")
    Call<ResponseBody> getWayPointByCategories(
            @Field("cat_id") String cat_id,
            @Field("latitude_decimal") String latitude,
            @Field("longitude_decimal") String longitude);

    @FormUrlEncoded
    @POST("get_waypoint_by_cat.php")
    Call<ResponseBody> getWayPointByCategories(
            @Field("cat_id") String cat_id,
            @Field("latitude_decimal") String latitude,
            @Field("longitude_decimal") String longitude,
            @Field("page") int page);

    @FormUrlEncoded
    @POST("get_all_waypoint_km_and_cat.php")
    Call<ResponseBody> getWayPointByCategoriesByKM(
            @Field("cat_id") String cat_id,
            @Field("latitude_decimal") String latitude,
            @Field("longitude_decimal") String longitude,
            @Field("page") int page);


    @GET("/data/2.5/weather")
    Call<ResponseBody> getWhetherDetails(@Query("lat") String latitude,
                                         @Query("lon") String longitude,
                                         @Query("appid") String appid);


    //AccuWeather API

    @GET("locations/v1/cities/geoposition/search.json")
    Call<ResponseBody> getNewWeatherDetail(
            @Query("q") String latlong,
            @Query("apikey") String apiKey
    );

    @GET("currentconditions/v1/{locationKey}")
    Call<ResponseBody> getCurrentCondition(
            @Path("locationKey") String locationKey,
            @Query("apikey") String apikey
    );

    @FormUrlEncoded
    @POST("search_waypoint_by_name.php")
    Call<ResponseBody> searchWayPointByName(
            @Field("name") String name,
            @Field("latitude_decimal") String latitude,
            @Field("longitude_decimal") String longitude);


    @GET("get_postal_code.php")
    Call<ResponseBody> getPostalCode();


    @GET("get_legend_waypoint.php")
    Call<ResponseBody> getLegendWayPoint();

    //http://canadasmartstops.com/smartstop/api/get_legend_waypoint.php

    @GET("get_province.php")
    Call<ResponseBody> getProvince();

    @FormUrlEncoded
    @POST("get_sub_category.php")
    Call<ResponseBody> getSubCategories(
            @Field("cat_id") String cat_id);

    //http://canadasmartstops.com/smartstop/api/get_sub_category.php?cat_id=20


    @Multipart
    @POST("post_favourite_waypoint.php")
    Call<ResponseBody> favouriteWayPoint(
            @Part("cat_id") RequestBody cat_id,
            @Part("sub_cat_id") RequestBody sub_cat_id,
            @Part("latitude_decimal") RequestBody latitude,
            @Part("longitude_decimal") RequestBody longitude,
            @Part("waypoint_name") RequestBody waypoint_name,
            @Part("address") RequestBody address,
            @Part("postal_code") RequestBody postalcode,
            @Part("country") RequestBody country,
            @Part("province") RequestBody province,
            @Part("email") RequestBody email,
            @Part("phone_number") RequestBody phone_number,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part[] images);

    @FormUrlEncoded
    @POST("get_favourite_waypoint.php")
    Call<ResponseBody> getFavouriteWaypoint(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("user_report.php")
    Call<ResponseBody> userReport(
            @Field("name") String name,
            @Field("city") String city,
            @Field("email") String email,
            @Field("description") String description,
            @Field("feedback") String feedback,
            @Field("waypoint_id") String waypoint_id

    );



    // http://canadasmartstops.com/smartstop/api/user_report.php?name=Piyush&city=Rajkot&email=piyush@gmail.com&description=test%20dis&feedback=test%20fid


//http://canadasmartstops.com/smartstop/api/get_favourite_waypoint.php?user_id=xyz
    //  http://canadasmartstops.com/smartstop/api/post_favourite_waypoint.php?cat_id=109&sub_cat_id=401&latitude_decimal=22.264616908227158&longitude_decimal=70.79331915825605&waypoint_name=namefromtextfield&address=addressfromtextfield&postal_code=E7E 1X4&country=Canada&province=British Columbia&email=emailfromtextfield&phone_number=fromtextfield&user_id=xyz
    //http://canadasmartstops.com/smartstop/api/get_sub_category.php?cat_id=20
    //http://canadasmartstops.com/smartstop/api/get_province.php
    //http://canadasmartstops.com/smartstop/api/get_postal_code.php
    //http://canadasmartstops.com/smartstop/api/get_category.php
    //http://canadasmartstops.com/smartstop/api/search_waypoint_by_name.php?name=Krishnanagar&latitude_decimal=22.264617&longitude_decimal=70.793319

    // http://canadasmartstops.com/smartstop/api/registration.php?device_id=

}