package com.creativeinfoway.smartstops.app.ui.fragment.favourite;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.adapter.FavouriteWaypointAdapter;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteWayPointFragment extends Fragment implements View.OnClickListener {
    ImageView ivback;
    Button btnAddWaypint;
    SmartStopsPref smartStopsPref;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout ll_call;
    TextView txtError;

    public FavouriteWayPointFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourite_way_point, container, false);
        smartStopsPref = new SmartStopsPref(getContext());
        initIDs(v);

        initGetFavouriteWaypointAPi();
        return v;
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
                                FavouriteWaypointAdapter favouriteWaypointAdapter = new FavouriteWaypointAdapter(getActivity(), getFavouriteWaypoint.getData()) {
                                    @Override
                                    public void onFavWayPointClick(int position) {

                                        Fragment fragment = new MarkerInfoWindowFragment();
                                        FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("from", "2");
                                        bundle.putSerializable("markerinfo_from_fav_waypoint_map", getFavouriteWaypoint.getData().get(position));
                                        fragment.setArguments(bundle);
                                        fragmentTransaction.replace(R.id.frameContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                };
                                LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(linearLayout);
                                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(recyclerView.getContext()));
                                recyclerView.setAdapter(favouriteWaypointAdapter);
                                progressBar.setVisibility(View.GONE);

                            }
                        } else {
                            assert getActivity() != null;
                            progressBar.setVisibility(View.GONE);
                            txtError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "" + getFavouriteWaypoint.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void initIDs(View v) {
        txtError = v.findViewById(R.id.txtError);
        ivback = v.findViewById(R.id.frag_fav_iv_back);
        ivback = v.findViewById(R.id.frag_fav_iv_back);
        btnAddWaypint = v.findViewById(R.id.frag_fav_btn_add_waypoint);
        recyclerView = v.findViewById(R.id.frag_fav_waypoint_recyclerview);
        progressBar = v.findViewById(R.id.frag_fav_waypoint_pb);
        btnAddWaypint.setOnClickListener(this);
        ivback.setOnClickListener(this);
        ll_call = v.findViewById(R.id.frag_fav_waypoint_call);
        ll_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivback) {
            getActivity().onBackPressed();
        }
        if (v == btnAddWaypint) {
            Fragment fragment = new AddWayPointFrgment();
            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
           /* Intent intent = new Intent(getActivity(), AddWayPointActivity.class);
            startActivity(intent);*/
        }
        if (v == ll_call) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getActivity());
            }
        }
    }


}
