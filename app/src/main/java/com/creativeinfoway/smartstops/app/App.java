package com.creativeinfoway.smartstops.app;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.bugsee.library.Bugsee;
import com.creativeinfoway.smartstops.app.ui.utils.DeviceAPICalling;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends MultiDexApplication {
    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Bugsee.launch(this, "c16a3c80-2ae4-494f-af1b-0f2f49127782");
        DeviceAPICalling.callRegistrationDeviceIdApi(getApplicationContext());

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("OnLow memory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.out.println(level);
    }
}
