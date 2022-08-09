package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;
import com.creativeinfoway.smartstops.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creativeinfoway.smartstops.app.ui.activity.LoginActivity.hideSoftKeyboard;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp, btnSignIn;
    EditText etUsername, etEmailId, etCountry,act_signup_et_password;
    SmartStopsPref smartStopsPref;
    String userid, user_name, email, country,deviceId;
    private ProgressDialog progressDialog;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        initids();
        requestPermission();
        getUniqueDeviceID();
        smartStopsPref = new SmartStopsPref(getApplicationContext());
    }

    private void initids() {

        btnSignIn = findViewById(R.id.act_signup_btn_signin);
        btnSignUp = findViewById(R.id.act_signup_btn_signup);

        etUsername = findViewById(R.id.act_signup_et_username);
        etCountry = findViewById(R.id.act_signup_et_country);
        etEmailId = findViewById(R.id.act_signup_et_emailid);
        act_signup_et_password = findViewById(R.id.act_signup_et_password);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnSignIn) {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (v == btnSignUp) {
            hideSoftKeyboard(this);
            if (checkValidation()) {
                String sUsername = etUsername.getText().toString();
                String sCountry = etCountry.getText().toString();
                String sEmailId = etEmailId.getText().toString();
                String password = act_signup_et_password.getText().toString();

                if(progressDialog !=null && !progressDialog.isShowing()){
                    progressDialog.show();
                }

                userRegistration(sUsername, sCountry, sEmailId,password);
            }
        }
    }

    private void getUniqueDeviceID() {

        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
        }
        else{
            deviceId = telephonyManager.getDeviceId();
        }*/


        deviceId = Utils.getDeviceInfo(this);

        Log.d("deviceId", "getUniqueDeviceID: "+deviceId);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SignUpActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
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
                //permission not granted
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void userRegistration(String sUsername, String sCountry, String sEmailId,String sPassword) {
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().signUp(deviceId,sUsername, sCountry, sEmailId,sPassword);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(progressDialog !=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();

                            JSONObject jsonObject = new JSONObject(strResponse);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                //{"status":"1","msg":"success","user_id":"MGYtuw6UN","user_name":"kajal","email":"kaju147@gmail.com","country":"india"}
                                userid = jsonObject.getString("user_id");
                                user_name = jsonObject.getString("username");
                                email = jsonObject.getString("email");
                                country = jsonObject.getString("country");

                                signUpPref();

                                /*Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();*/

                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                                // {"status":"0","msg":"email already exitst"}
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(progressDialog !=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            e.printStackTrace();
        } catch (KeyManagementException e) {
            if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }
    }

    private void signUpPref() {
        smartStopsPref.putString(Constant.USERNAME, user_name);
        smartStopsPref.putString(Constant.COUNTRY, country);
        smartStopsPref.putString(Constant.EMAIL, email);
        smartStopsPref.putString(Constant.USERID, userid);
        smartStopsPref.putBoolean(Constant.ISSIGNUP,true);
    }

    private boolean checkValidation() {
        if (etUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCountry.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Country", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etEmailId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (act_signup_et_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
