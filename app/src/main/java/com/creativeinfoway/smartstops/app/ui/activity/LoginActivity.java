package com.creativeinfoway.smartstops.app.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnSignUp,btnWithoutLogin;
    String userid, user_name, email, country;
    SmartStopsPref smartStopsPref;
    private EditText etEmail,act_login_et_password;

    private ProgressDialog progressDialog;

    public static void hideSoftKeyboard(Activity context) {
        View v = context.getCurrentFocus();
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        initids();
        smartStopsPref = new SmartStopsPref(getApplicationContext());
    }

    private void initids() {
        btnLogin = findViewById(R.id.act_login_btn_login);
        btnSignUp = findViewById(R.id.act_login_btn_signup);
        btnWithoutLogin = findViewById(R.id.btnWithoutLogin);
        etEmail = findViewById(R.id.act_login_et_email);
        act_login_et_password = findViewById(R.id.act_login_et_password);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnWithoutLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnLogin) {
            hideSoftKeyboard(this);
            if (checkValidation()) {
                String sEmail = etEmail.getText().toString();
                String sPassword = act_login_et_password.getText().toString();

                if(progressDialog !=null && !progressDialog.isShowing()){
                    progressDialog.show();
                }

                userLogin(sEmail,sPassword);
            }
        }
        if (v == btnSignUp) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        if(v == btnWithoutLogin){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void userLogin(String sEmailId,String sPassword) {
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().login(sEmailId,sPassword);
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

                                //  {"status":"1","msg":"success","user_id":"qKpIXavvf","user_name":"hardik","email":"hardikmayani1@gmail.com","country":"india"}

                                userid = jsonObject.getString("user_id");
                                user_name = jsonObject.getString("username");
                                email = jsonObject.getString("email");
                                country = jsonObject.getString("country");

                                signUpPref();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            } else {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    }

    private boolean checkValidation() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;

        }else if (act_login_et_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password can not be empty " , Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }
}
