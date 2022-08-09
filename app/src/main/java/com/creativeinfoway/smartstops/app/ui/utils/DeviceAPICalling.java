package com.creativeinfoway.smartstops.app.ui.utils;

import android.content.Context;
import android.util.Log;

import com.creativeinfoway.smartstops.app.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAPICalling {

    public static void callRegistrationDeviceIdApi(Context context) {
        try {

            Call<ResponseBody> call = RetrofitUtils.callAPI().newDeviceRegistration(Utils.getDeviceInfo(context));
            Log.d("deviceId", "getUniqueDeviceID: " + Utils.getDeviceInfo(context));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Register Device Id", "onFailure: " + t.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void getTotalActiveUserAPI(Context context,UserCountListener userCountListener){

        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().getActiveUser();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("1")) {
                        //    tvTotalActiveUser.setText("12512");
                            new SmartStopsPref(context).putString("user_count",jsonObject.getString("total_user"));
                            if(userCountListener!=null){
                                userCountListener.getliveCount(jsonObject.getString("total_user"));
                            }
                        } else {
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d("Active User Count", "onFailure: " + t.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public interface UserCountListener{
        void getliveCount(String count);
    }
}
