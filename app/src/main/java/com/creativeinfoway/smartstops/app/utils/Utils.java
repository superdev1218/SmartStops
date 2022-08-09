package com.creativeinfoway.smartstops.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Random;

import timber.log.Timber;

public class Utils {

  /**
   * <p>
   * Returns the Mapbox access token set in the app resources.
   * </p>
   * It will first search for a token in the Mapbox object. If not found it
   * will then attempt to load the access token from the
   * {@code res/values/dev.xml} development file.
   *
   * @param context The {@link Context} of the {@link android.app.Activity} or {@link android.app.Fragment}.
   * @return The Mapbox access token or null if not found.
   */
  public static String getMapboxAccessToken(@NonNull Context context) {
    try {
      // Read out AndroidManifest
      String token = Mapbox.getAccessToken();
      if (token == null || token.isEmpty()) {
        throw new IllegalArgumentException();
      }
      return token;
    } catch (Exception exception) {
      // Use fallback on string resource, used for development
      int tokenResId = context.getResources()
        .getIdentifier("mapbox_access_token", "string", context.getPackageName());
      return tokenResId != 0 ? context.getString(tokenResId) : null;
    }
  }

  /**
   * Demonstrates converting any Drawable to an Icon, for use as a marker icon.
   */
  public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
    Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
    Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
      vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    vectorDrawable.draw(canvas);
    return IconFactory.getInstance(context).fromBitmap(bitmap);
  }

  public static LatLng getRandomLatLng(double[] bbox) {
    Random random = new Random();

    double randomLat = bbox[1] + (bbox[3] - bbox[1]) * random.nextDouble();
    double randomLon = bbox[0] + (bbox[2] - bbox[0]) * random.nextDouble();

    LatLng latLng = new LatLng(randomLat, randomLon);
    Timber.d("getRandomLatLng: %s", latLng.toString());
    return latLng;
  }

  public static String getDeviceInfo(Context context) {

    String androidId = "";
    if (androidId.equals("")) {
      androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    String imei = "3481156846581195";
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    String serial = Build.SERIAL;
    try {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            imei = telephonyManager.getDeviceId();
      imei = "3481156846581195";
    } catch (Exception e) {
      e.getMessage();
      imei = "3481156846581195";
    }
    String versionName = "";
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      versionName = info.versionName;
    } catch (Exception e) {
      versionName = "2.4.4";
    }
    if (manufacturer == null || manufacturer.equals("")) {
      manufacturer = "unknown";
    }
    if (model == null || model.equals("")) {
      model = "unknown";
    }
    if (versionName == null || versionName.equals("")) {
      versionName = "unknown";
    }
    if (serial == null || serial.equals("")) {
      serial = "unknown";
    }

    String dhating = "name=" + manufacturer
            + "||model=" + model +
            "||imei=" + imei +
//                "||serial_number=" + serial +
            "||version=" + versionName +
            "||udid=" + androidId;
    return dhating;
  }
}
