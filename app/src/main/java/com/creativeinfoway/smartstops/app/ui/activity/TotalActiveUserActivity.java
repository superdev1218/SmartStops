package com.creativeinfoway.smartstops.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.DeviceAPICalling;
import com.creativeinfoway.smartstops.app.ui.utils.Pref;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;
import com.creativeinfoway.smartstops.app.utils.Utils;

public class TotalActiveUserActivity extends AppCompatActivity {

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    Pref pref;
    SmartStopsPref smartStopsPref;
    RelativeLayout rootView;
    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean sentToSettings = false;
    TextView tvTotalActiveUser;

    private SharedPreferences permissionStatus;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_active_user);
        rootView = findViewById(R.id.rootView);
        tvTotalActiveUser=findViewById(R.id.tv_total_active_users);
        pref = new Pref(getApplicationContext());
        smartStopsPref = new SmartStopsPref(getApplicationContext());
        setWelcomtext();
        userid = smartStopsPref.getString(Constant.USERID);
        checkPermission();


    }

    private boolean checkPermission(){
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[3])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Call and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(TotalActiveUserActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.apply();
        }
        return true;
    }

    private void welcomeText(String liveUserCount) {
        String text2 = "Welcome,\nThere are currently\n" + liveUserCount + "\nSmartStop users in Canada.";
        Spannable spannable = new SpannableString(text2);
        int tempCount = 0;
        String splited[] = text2.split("\n");
        for (int i = 0; i < splited.length; i++) {
            Log.d("===count", String.valueOf(tempCount));
            if (i == 0) {
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new RelativeSizeSpan(1.4f), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if(i==1){
                spannable.setSpan(new RelativeSizeSpan(1.1f), 9, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (i == 2) {
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 29, 29+splited[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new RelativeSizeSpan(2.8f), 29, 29+splited[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tempCount=29+splited[i].length();
            }else{
                spannable.setSpan(new RelativeSizeSpan(1.1f), tempCount, tempCount+splited[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        tvTotalActiveUser.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
               // openNewActivity(100);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[3])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Grant Permissions");
                builder.setMessage("This app needs Call and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(TotalActiveUserActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Without this permission App may not work properly.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
            //    openNewActivity(100);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
              //  openNewActivity(100);
            }
        }
    }

    private void setWelcomtext() {
        welcomeText(smartStopsPref.getString("user_count").equals("") ? "0":smartStopsPref.getString("user_count"));
        DeviceAPICalling.getTotalActiveUserAPI(getApplicationContext(), new DeviceAPICalling.UserCountListener() {
            @Override
            public void getliveCount(String count) {
                welcomeText(count);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void openNewActivity(int i) {

        Handler mHandler = new Handler();
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (!Pref.getBoolean("isFirstOpen")) {
                            Pref.putBoolean("isFirstOpen", true);
                            Intent intent = new Intent(TotalActiveUserActivity.this, LearnMoreActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {


                            if(smartStopsPref.getBoolean(Constant.ISSIGNUP)){
                                if (userid.equals("")) {
                                    Intent intent = new Intent(TotalActiveUserActivity.this, SignUpActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(TotalActiveUserActivity.this, HomeActivity.class);
//                                     intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                if(smartStopsPref.getString(Constant.ISOPEN).equals("open")){
                                    Intent intent = new Intent(TotalActiveUserActivity.this, HomeActivity.class);
//                                     intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(TotalActiveUserActivity.this, LearnMoreActivity.class);
//                                     intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                }, i);
    }

    public void onClickPleaseContinue(View view) {
       if(checkPermission()) {
           openNewActivity(0);
       }
    }
}