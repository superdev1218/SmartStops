package com.creativeinfoway.smartstops.app.ui.fragment.home;


import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.adapter.SearchResultAdapter;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
import com.creativeinfoway.smartstops.app.ui.models.GetAllLatLongPoints;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SimpleDividerItemDecoration;
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
public class SearchResultFragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout llCall;
    ImageView ivBack;
    private GpsTracker gpsTracker;

    public SearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_result_, container, false);


        initIds(v);

        String searchWord = getArguments().getString("searchWord");
        searchWayPointByName(searchWord);
        return v;
    }

    private void initIds(View view) {

        gpsTracker = new GpsTracker(getContext());
         progressBar=view.findViewById(R.id.frag_searchresult_pb);
        recyclerView=view.findViewById(R.id.frag_search_result_recyclerview);
        llCall=view.findViewById(R.id.act_search_result_call);
        ivBack=view.findViewById(R.id.act_search_result_iv_back);
        ivBack.setOnClickListener(this);
        llCall.setOnClickListener(this);


    }

    ///http://canadasmartstops.com/smartstop/api/search_waypoint_by_name.php?name=Krishnanagar&latitude_decimal=22.264617&longitude_decimal=70.793319
    private void searchWayPointByName(String searchWord) {
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().searchWayPointByName(searchWord,String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.body() != null && response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        try {
                            String strResponse = responseBody.string();
                            final GetAllLatLongPoints getAllLatLongPoints = new Gson().fromJson(strResponse, GetAllLatLongPoints.class);
                            if (getAllLatLongPoints.getStatus().equals("1")) {
                                {
                                    for (int i = 0; i < getAllLatLongPoints.getData().size(); i++) {
                                        SearchResultAdapter favouriteWaypointAdapter = new SearchResultAdapter(getActivity(), getAllLatLongPoints.getData()){
                                        @Override
                                        public void onFavWayPointClick(int position) {

                                            Fragment fragment = new MarkerInfoWindowFragment();
                                            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("from", "0");
                                            bundle.putSerializable("markerinfo", getAllLatLongPoints.getData().get(position));
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
                                }
                            }else
                            {
                                Toast.makeText(getActivity(), getAllLatLongPoints.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack) {
            getActivity().onBackPressed();
        }
        if(v==llCall)
        {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getActivity());
            }
        }
    }
}
