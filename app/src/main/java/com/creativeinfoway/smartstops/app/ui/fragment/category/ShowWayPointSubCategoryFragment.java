package com.creativeinfoway.smartstops.app.ui.fragment.category;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.adapter.WayPointAdapter;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.gson.Gson;

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
public class ShowWayPointSubCategoryFragment extends Fragment {

    WayPointAdapter adapter;
    private RecyclerView recWayPointList;
    private GpsTracker gpsTracker;
    private ProgressBar frag_cat_pb;
    private SearchView searchView;
    private ImageView iv_back;
    private TextView txtWayPointOfPlace,txtLocation;
    private String type;

    public ShowWayPointSubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_way_point_category, container, false);

        setDataToView(view);

        return view;
    }

    private void setDataToView(View view) {
        frag_cat_pb = view.findViewById(R.id.frag_cat_pb);
        searchView = view.findViewById(R.id.searchViewWayPoint);
        recWayPointList = view.findViewById(R.id.recWayPointList);
        recWayPointList.setLayoutManager(new LinearLayoutManager(getActivity()));
        gpsTracker = new GpsTracker(getActivity());
        searchView.onActionViewExpanded();
        txtWayPointOfPlace = view.findViewById(R.id.txtWayPointOfPlace);
        txtWayPointOfPlace.setText(getArguments().getString("searchName") + "");

        type = getArguments().getString("type");

        txtLocation = view.findViewById(R.id.txtLocation);

        String catID = getArguments().getString("catid");
        getWayPointByCategories(catID, gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "");

        iv_back = view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (adapter != null)
                    adapter.getFilter().filter(newText);

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
        });
    }

    public void getWayPointByCategories(String catid, String latitude, String longitude) {

        if (frag_cat_pb.getVisibility() != View.VISIBLE) {
            frag_cat_pb.setVisibility(View.VISIBLE);
        }

        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getsubcategory(catid, latitude, longitude);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    if (frag_cat_pb.getVisibility() == View.VISIBLE) {
                        frag_cat_pb.setVisibility(View.GONE);
                    }

                    try {
                        String str_Response = responseBody.string();
                        GetAllSubCategoryWaypoint wayPointByCategory = new Gson().fromJson(str_Response, GetAllSubCategoryWaypoint.class);
                        if (wayPointByCategory.getStatus().equals("1")) {
                            setDataToAdapter(wayPointByCategory);
                        } else {
                            if(type.equals("SubCategory")){
                                txtLocation.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(getActivity(), String.valueOf(wayPointByCategory.getMsg()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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

    private void setDataToAdapter(GetAllSubCategoryWaypoint wayPointByCategory) {

        adapter = new WayPointAdapter(getActivity(), wayPointByCategory.getData(),searchView,"SubCategory");
    //    recWayPointList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recWayPointList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchView != null)
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
            }

        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
