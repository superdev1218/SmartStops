package com.creativeinfoway.smartstops.app.ui.fragment.report;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.fragment.home.Mapfragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragmentt extends Fragment implements View.OnClickListener {

    EditText etName, etEmail, etDescription, etFeedback, etCity, etWaypointID;
    Button btnSubmit;

    public ReportFragmentt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report_fragmentt, container, false);


        initIDs(v);
        return v;

    }

    private void initIDs(View view) {

        etName = view.findViewById(R.id.frag_report_et_name);
        etEmail = view.findViewById(R.id.frag_report_et_email);
        etDescription = view.findViewById(R.id.frag_report_et_description);
        etFeedback = view.findViewById(R.id.frag_report_et_feedback);
        etCity = view.findViewById(R.id.frag_report_et_city);
        etWaypointID = view.findViewById(R.id.frag_report_et_waypoint_id);

        btnSubmit = view.findViewById(R.id.frag_report_btn_submit);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnSubmit) {
            if (checkValidation()) {
                sendUserReport();
            }
        }
    }

    private void sendUserReport() {

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String description = etDescription.getText().toString();
        String city = etCity.getText().toString();
        String feedback = etFeedback.getText().toString();
        String wayPointId = etWaypointID.getText().toString();

        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().userReport(name, city, email, description, feedback,wayPointId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //{"status":"1","msg":"success"}
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String str_response = responseBody.string();

                            JSONObject jsonObject = new JSONObject(str_response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }

                            Fragment fragment = new Mapfragment();
                            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameContainer, fragment);
                            //  fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
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

    private boolean checkValidation() {
        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etCity.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter City", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etDescription.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etFeedback.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Feedback", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
