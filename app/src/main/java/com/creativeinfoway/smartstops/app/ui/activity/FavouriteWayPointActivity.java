package com.creativeinfoway.smartstops.app.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.adapter.FavouriteWaypointAdapter;
import com.creativeinfoway.smartstops.app.ui.models.GetFavouriteWaypoint;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SimpleDividerItemDecoration;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;

public class FavouriteWayPointActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView ivback;
    Button btnAddWaypint;
    SmartStopsPref smartStopsPref;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout ll_call;
    TextView txtError;
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_way_point);

        smartStopsPref = new SmartStopsPref(this);

        txtError = findViewById(R.id.txtError);
        ivback = findViewById(R.id.frag_fav_iv_back);
        ivback = findViewById(R.id.frag_fav_iv_back);
        btnAddWaypint = findViewById(R.id.frag_fav_btn_add_waypoint);
        recyclerView = findViewById(R.id.frag_fav_waypoint_recyclerview);
        progressBar = findViewById(R.id.frag_fav_waypoint_pb);
        btnAddWaypint.setOnClickListener(this);
        ivback.setOnClickListener(this);
        ll_call = findViewById(R.id.frag_fav_waypoint_call);
        ll_call.setOnClickListener(this);

        initGetFavouriteWaypointAPi();
    }

    private void initGetFavouriteWaypointAPi() {

        try {
//            Call<ResponseBody> call = RetrofitUtils.callAPI().getFavouriteWaypoint(smartStopsPref.getString(Constant.USERID));
            Call<ResponseBody> call = RetrofitUtils.callAPI().getFavouriteWaypoint(smartStopsPref.getString(Constant.NEWUSERID));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    ResponseBody responseBody = response.body();
                    try {
                        String strResponse = responseBody.string();

                        GetFavouriteWaypoint getFavouriteWaypoint = new Gson().fromJson(strResponse, GetFavouriteWaypoint.class);
                        if (getFavouriteWaypoint.getStatus().equals("1")) {

                            txtError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            for (int i = 0; i < getFavouriteWaypoint.getData().size(); i++) {

                                final int finalI = i;
                                FavouriteWaypointAdapter favouriteWaypointAdapter = new FavouriteWaypointAdapter(FavouriteWayPointActivity.this, getFavouriteWaypoint.getData()) {
                                    @Override
                                    public void onFavWayPointClick(int position) {

                                        Intent intent = new Intent(FavouriteWayPointActivity.this,SubFavouriteWayPointActivity.class);
                                        intent.putExtra("type","MarkerInfoWindowFragment");
                                        intent.putExtra("position",position);
                                        intent.putExtra("result",strResponse);
                                        startActivity(intent);

                                       /* Fragment fragment = new MarkerInfoWindowFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("from", "2");
                                        bundle.putSerializable("markerinfo_from_fav_waypoint_map", getFavouriteWaypoint.getData().get(position));
                                        fragment.setArguments(bundle);
                                        fragmentTransaction.replace(R.id.frameContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();*/
                                    }
                                };
                                LinearLayoutManager linearLayout = new LinearLayoutManager(FavouriteWayPointActivity.this);
                                recyclerView.setLayoutManager(linearLayout);
                                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(recyclerView.getContext()));
                                recyclerView.setAdapter(favouriteWaypointAdapter);
                                progressBar.setVisibility(View.GONE);

                            }
                        } else {

                            progressBar.setVisibility(View.GONE);
                            txtError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toast.makeText(FavouriteWayPointActivity.this, "" + getFavouriteWaypoint.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivback) {
            onBackPressed();
        }
        if (v == btnAddWaypint) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                Log.d("=====1"," on Add Waypoint");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            Intent intent = new Intent(FavouriteWayPointActivity.this,AddWayPointActivity.class);
            /*intent.putExtra("type","AddWayPointFrgment");
            intent.putExtra("position",0);
            intent.putExtra("result","");*/
            startActivity(intent);

           /* Fragment fragment = new AddWayPointFrgment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/


           /* Intent intent = new Intent(getActivity(), AddWayPointActivity.class);
            startActivity(intent);*/
        }
        if (v == ll_call) {
            if (checkSinglePermission(this, Manifest.permission.CALL_PHONE)) {
                startCall911(this);
            }
        }
    }

}
