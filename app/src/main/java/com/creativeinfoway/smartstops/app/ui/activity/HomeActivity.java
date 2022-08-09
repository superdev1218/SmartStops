package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.activity.OfflineMapListActivity;
import com.creativeinfoway.smartstops.app.activity.OfflineRegionDownloadActivity;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.adapter.HomeMenuAdapter;
import com.creativeinfoway.smartstops.app.ui.fragment.GetDirectionMapFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.category.CategoryFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.report.ReportFragmentt;
import com.creativeinfoway.smartstops.app.ui.fragment.whether.WhetherFragment;
import com.creativeinfoway.smartstops.app.ui.interfaces.ItemClickListner;
import com.creativeinfoway.smartstops.app.ui.models.SettingModel;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;
import com.creativeinfoway.smartstops.app.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.navigation.utils.SmartStopsPrefLanguage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    private final String[] gridViewString = {"WAYPOINT", "WEATHER", "OFFLINE",
            "REPORT", "FAQ", "SHARE",
            "RATE US", "UPDATE","THE LAW", "LANGUAGE",
            "LOGOUT"};
    private final int[] gridViewImageId = {R.drawable.icon_add_forward, R.drawable.icon_weather, R.drawable.download,
            R.drawable.icon_report, R.drawable.icon_faq, R.drawable.icon_share,
            R.drawable.icon_rate_us, R.drawable.icon_update, R.drawable.law,R.drawable.lang,
            R.drawable.icon_logout};


    private final String[] gridViewStringNew2 = {"WAYPOINT", "WEATHER", "OFFLINE", "REPORT", "FAQ", "SHARE", "RATE US", "UPDATE", "THE LAW", "LANGUAGE"};
       private final int[] gridViewImageIdNew2 = {R.drawable.icon_add_forward, R.drawable.icon_weather, R.drawable.download,
            R.drawable.icon_report, R.drawable.icon_faq, R.drawable.icon_share, R.drawable.icon_rate_us, R.drawable.icon_update, R.drawable.law,  R.drawable.lang};

    public DrawerLayout drawer;
    boolean isallpermission = false;
    Toolbar toolbar;
    private RecyclerView androidGridView;
    private SmartStopsPref smartStopsPref;

    private LinearLayout linearLayoutView;
    //  RecyclerView recLegendList;

    private boolean showSearchViewInCategory;
    private String deviceId;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private GpsTracker gpsTracker;
    private ProgressBar progress_circular;
    private Activity activity;

    public static Intent startCall911(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:911"));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return callIntent;
        }
        context.startActivity(callIntent);
        return callIntent;
    }

    public static boolean checkSinglePermission(final Activity activity, String permission) {

        final boolean[] isallpermission1 = new boolean[1];

        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isallpermission1[0] = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog(activity);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
        return isallpermission1[0];
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.need_permission);
        builder.setMessage(R.string.this_app_needs);
        builder.setPositiveButton(R.string.goto_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton(R.string._cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // navigating user to app settings
    public static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = HomeActivity.this;
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_home);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        gpsTracker = new GpsTracker(this);
        progress_circular = findViewById(R.id.progress_circular);

        Log.e("ACTIVITY :::", HomeActivity.class.getCanonicalName());
        initIds();

        requestPermission();

        try {
            smartStopsPref = new SmartStopsPref(getApplicationContext());

            smartStopsPref.putString(Constant.ISOPEN, "open");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        setUpNavigationDrawer();
        initNavigationHeader(savedInstanceState);

        Bundle bundle = new Bundle();
        Fragment fragment = new CategoryFragment();
        bundle.putBoolean("isShowSearch", true);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();

        if (checkSinglePermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
        }

      //  getUniqueDeviceID();
    }

    private void getUniqueDeviceID() {

       /* TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        deviceId = telephonyManager.getDeviceId();*/


        deviceId = Utils.getDeviceInfo(this);

        Log.d("deviceId", "getUniqueDeviceID: " + deviceId);

        newUserRegistration();
        // 860889034417217

    }

    private void initIds() {
        //  toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ivMenu = findViewById(R.id.act_home_img_menu);
//        ll_call = findViewById(R.id.act_home_call);
//        ll_call.setOnClickListener(this);
//        ivMenu.setOnClickListener(this);
    }

    private void setUpNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                drawer.openDrawer(drawerView);
//                //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                drawer.closeDrawer(drawerView);
//                //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            }
//        };
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        //drawer.openDrawer(Gravity.START);
        //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        //  addCategories();
    }

    private void initNavigationHeader(Bundle savedInstanceState) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        androidGridView = navigationView.getHeaderView(0).findViewById(R.id.grid_view_image_text);
        TextView tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tv_nav_username);

        LinearLayout linWhereTo = navigationView.getHeaderView(0).findViewById(R.id.linWhereTo);
        LinearLayout linViewMap = navigationView.getHeaderView(0).findViewById(R.id.linViewMap);
        LinearLayout map_box_icon = navigationView.getHeaderView(0).findViewById(R.id.mapbox_icon);
        LinearLayout linWayPoint = navigationView.getHeaderView(0).findViewById(R.id.linWayPoint);
        Switch map_switch = navigationView.getHeaderView(0).findViewById(R.id.map_switch);

       /* linWayPoint.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,FavouriteWayPointActivity.class);
            startActivity(intent);
        });

        map_switch.setChecked(false);
        map_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Constant.MAP_SWITCH = false;
                }else {
                    Constant.MAP_SWITCH = true;
                }
            }
        });*/

        //callSetting();


        linViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (savedInstanceState == null) {
                   /* Bundle bundle = new Bundle();
                    bundle.putBoolean("pleaseLoadView", true);
                    Fragment fragment1 = new Mapfragment();
                    fragment1.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer, fragment1)*//*.addToBackStack(null)*//*;
                    fragmentTransaction.commit();*/
                    Intent intent = new Intent(HomeActivity.this, MapLauncherActivity.class);
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        linWhereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShowSearch", true);
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.commit();

                drawer.closeDrawer(GravityCompat.START);

            }
        });


        tvUsername.setText(smartStopsPref.getString(Constant.USERNAME));

        HomeMenuAdapter adapterViewAndroid;
        if (smartStopsPref.getString(Constant.USERNAME).equals("")) {
            adapterViewAndroid = new HomeMenuAdapter(this, gridViewStringNew2, gridViewImageIdNew2, new ItemClickListner() {

                @Override
                public void onItemClick(int pos) {
                    onClickHomeMenuItem(pos);
                }
            });
        } else {
            adapterViewAndroid = new HomeMenuAdapter(this, gridViewString, gridViewImageId, new ItemClickListner() {

                @Override
                public void onItemClick(int pos) {
                    onClickHomeMenuItem(pos);
                }
            });
        }


        androidGridView.setLayoutManager(new GridLayoutManager(this, 2));
        androidGridView.setAdapter(adapterViewAndroid);
    }

    private void onClickHomeMenuItem(int position) {
        Fragment fragment = null;
        switch (position) {
           /* case 0: {

                Bundle bundle = new Bundle();
                bundle.putBoolean("pleaseLoadView", true);
                Fragment fragment1 = new Mapfragment();
                fragment1.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_home, fragment1);
                fragmentTransaction.commit();

                break;
            }*/

           /* case 2: {
                fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShowSearch", false);
                fragment.setArguments(bundle);
                //Categories
                break;
            }*/
            case 0:
                //WAYPOINT
                Intent intents = new Intent(HomeActivity.this, FavouriteWayPointActivity.class);
                startActivity(intents);
                break;
            case 1: {
                //Whether
                fragment = new WhetherFragment();
                break;
            }
            case 2: {
                //Offline
                Intent intent = new Intent(HomeActivity.this, OfflineMapListActivity.class);
                startActivity(intent);
                break;
            }
            case 3: {
                //Report
                fragment = new ReportFragmentt();

                break;
            }
            case 4: {
                // faq

                Intent intent = new Intent(HomeActivity.this, LawActivity.class);
                intent.putExtra("txtSName", "FAQ");
                startActivity(intent);

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.FAQ)));
                break;
            }
            case 5: {
                // Share
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(shareIntent, "Share With"));
                break;
            }
            case 6: {
                // rate us

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                break;
            }
            case 7: {
                // update
                forceUpdate();
                break;

            }
            case 8: {

                Intent intent = new Intent(HomeActivity.this, LawActivity.class);
                intent.putExtra("txtSName", "LAW");
                startActivity(intent);

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.LAW)));
                break;
            }

            case 9: {
                LanguageUtils languageUtils=new LanguageUtils(this);
                languageUtils.openBottomSheet();
               /* SmartStopsPrefLanguage smartStopsPrefLanguage = new SmartStopsPrefLanguage(this);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(R.layout.custom_language_bottom_shit_dailog);

                ImageView close = bottomSheetDialog.findViewById(R.id.iv_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                    }
                });

                RadioGroup radioGroup = bottomSheetDialog.findViewById(R.id.radio_group);
                RadioButton btnGerman= (RadioButton)radioGroup.findViewById(R.id.rb_german);
                RadioButton btnFrench= (RadioButton)radioGroup.findViewById(R.id.rb_french);
                RadioButton btnMandarin= (RadioButton)radioGroup.findViewById(R.id.rb_Mandarin);
                RadioButton btnContonoese= (RadioButton)radioGroup.findViewById(R.id.rb_contonese);
                RadioButton btnEnglish= (RadioButton)radioGroup.findViewById(R.id.rb_English);
                Button btnDone=bottomSheetDialog.findViewById(R.id.btn_done);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId=radioGroup.getCheckedRadioButtonId();
                        if (selectedId == R.id.rb_german) {
                            smartStopsPrefLanguage.putString("lang", "german");
                        } else if (selectedId == R.id.rb_french) {
                            smartStopsPrefLanguage.putString("lang", "french");
                        } else if (selectedId == R.id.rb_Mandarin || selectedId == 4) {
                            smartStopsPrefLanguage.putString("lang", "mandarin");
                        }else if(selectedId == R.id.rb_contonese){
                            smartStopsPrefLanguage.putString("lang", "cantonoese");
                        } else if(selectedId == R.id.rb_English){
                            smartStopsPrefLanguage.putString("lang", "english");
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                switch (smartStopsPrefLanguage.getString("lang")) {
                    case "german":
                        btnGerman.setChecked(true);
                        break;
                    case "french":
                        btnFrench.setChecked(true);
                        break;
                    case "mandarin":
                        btnMandarin.setChecked(true);
                        break;
                    case "cantonoese":
                        btnContonoese.setChecked(true);
                        break;
                    case "english":
                        btnEnglish.setChecked(true);
                        break;
                    default:
                        btnEnglish.setChecked(true);
                        break;
                }

                bottomSheetDialog.show();

*/
                break;
            }


            case 10: {
                // logout
//                smartStopsPref.clear();
                smartStopsPref.putString(Constant.USERID,"");
                smartStopsPref.putString(Constant.USERNAME,"");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        //   drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

  /*  private void callSetting(){
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getsetting();
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {

                            String strResponse = responseBody.string();
                            SettingModel model = new Gson().fromJson(strResponse,SettingModel.class);
                            if(model.getStatus().equals("1")){
                                if(model.getValue().equals("No")){
                                    Constant.MAP_SWITCH = true;
                                }else {
                                    Constant.MAP_SWITCH = false;
                                }
                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e1) {
            Log.d("HomeAc", "IOException2: ");
            e1.printStackTrace();
        }
    }*/

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameContainer);

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count > 0) {
                if (fragment instanceof MarkerInfoWindowFragment) {
                    getSupportFragmentManager().popBackStack();
                } else if (fragment instanceof GetDirectionMapFragment) {
                    String from = fragment.getArguments().getString("from");
                    if (from.equals("ShowWayPointCategory")) {
                        //getSupportFragmentManager().popBackStack();
                        super.onBackPressed();
                    }
                } else {
                    //getSupportFragmentManager().popBackStack();
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        Log.d("on pause", "pause");
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String currentVersion = packageInfo.versionName;
            new ForceUpdateAsync(currentVersion, this).execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showForceUpdateDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.Theme_AppCompat_Light_Dialog_Alert));

        alertDialogBuilder.setTitle(this.getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(this.getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    private void newUserRegistration() {
        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().newDeviceRegistration(deviceId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        //{"status":"1","msg":"success","user_id":"knpiICfKC"}

                        String responseString = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.getString("status").equals("1")) {
                            smartStopsPref.putString(Constant.NEWUSERID, jsonObject.getString("user_id"));
                            Log.e("userID is : ", smartStopsPref.getString(Constant.NEWUSERID).trim());
                        } else {
                            //Toast.makeText(HomeActivity.this, "" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Register", "onFailure: " + t.getMessage());
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            getUniqueDeviceID();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getUniqueDeviceID();


            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("StaticFieldLeak")
    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;

        ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "Update not available", Toast.LENGTH_LONG).show();
                    }
                });

                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if (!(context instanceof SplashActivity)) {
                        if (!((Activity) context).isFinishing()) {
                            showForceUpdateDialog();
                        }
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Update not available", Toast.LENGTH_LONG).show();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }


}



