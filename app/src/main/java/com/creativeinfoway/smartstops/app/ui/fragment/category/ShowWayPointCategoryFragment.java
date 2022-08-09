package com.creativeinfoway.smartstops.app.ui.fragment.category;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.helpers.DBHelper;
import com.creativeinfoway.smartstops.app.ui.activity.SubCategoryActivity;
import com.creativeinfoway.smartstops.app.ui.adapter.WayPointAdapter;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_CHARSET;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_FIELD_REGION_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowWayPointCategoryFragment extends Fragment {

    WayPointAdapter adapter;
    private RecyclerView recWayPointList;
    private GpsTracker gpsTracker;
    private ProgressBar frag_cat_pb;
    private SearchView searchView;
    private ImageView iv_back;
    private TextView txtWayPointOfPlace,txtLocation;
    private String type;
    private Activity activity;

    private DBHelper dbHelper;
    private List<GetAllSubCategoryWaypoint.DataBean> wayPointByCategory = new ArrayList<>();
    private int page = 0;
    private String catID = "";
    public ShowWayPointCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_show_way_point_category, container, false);

        setDataToView(view);
        initRecyclerView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareOfflineManager();
    }

    private void setDataToView(View view) {
        dbHelper = new DBHelper(requireContext());
        frag_cat_pb = view.findViewById(R.id.frag_cat_pb);
        searchView = view.findViewById(R.id.searchViewWayPoint);
        recWayPointList = view.findViewById(R.id.recWayPointList);
        gpsTracker = new GpsTracker(getActivity());
        searchView.onActionViewExpanded();
        txtWayPointOfPlace = view.findViewById(R.id.txtWayPointOfPlace);
        txtWayPointOfPlace.setText(getArguments().getString("searchName") + "");

        type = getArguments().getString("type");

        txtLocation = view.findViewById(R.id.txtLocation);

        catID = getArguments().getString("catid");
        getWayPointByCategories();

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

    private void initRecyclerView(){
        adapter = new WayPointAdapter(activity, wayPointByCategory, searchView, "Category");
        recWayPointList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recWayPointList.addOnScrollListener(scrollListener);
        recWayPointList.setAdapter(adapter);
    }

    private void prepareOfflineManager(){
        OfflineManager offlineManager = OfflineManager.getInstance(requireContext());
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                ArrayList<String> offlineDownloadMapName = new ArrayList<>();
                for (OfflineRegion offlineRegion : offlineRegions){
                    String downloadRegionName = getRegionName(offlineRegion);
                    if(downloadRegionName != null){
                        offlineDownloadMapName.add(downloadRegionName);
                    }
                }
                adapter.setDownloadedMapName(offlineDownloadMapName);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private String getRegionName(OfflineRegion offlineRegion) {
        // Get the region name from the offline region metadata
        String regionName;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            regionName = "";
        }
        return regionName;
    }

    public void getWayPointByCategories() {

        if (frag_cat_pb.getVisibility() != View.VISIBLE) {
            frag_cat_pb.setVisibility(View.VISIBLE);
        }

        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getWayPointByCategories(catID, gpsTracker.getLatitude() + "", gpsTracker.getLongitude() + "",page);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    if (frag_cat_pb.getVisibility() == View.VISIBLE) {
                        frag_cat_pb.setVisibility(View.GONE);
                    }

                    try {
                        String str_Response = responseBody.string();
                        Log.d("Waypoint", "getWayPointByCategories: "+str_Response);
                        GetAllSubCategoryWaypoint getWayPointByCategory = new Gson().fromJson(str_Response, GetAllSubCategoryWaypoint.class);
                        if (getWayPointByCategory.getStatus().equals("1")) {
                            wayPointByCategory.addAll(getWayPointByCategory.getData());
                            if(getWayPointByCategory.getData().size() < 15){
                                isLastPage = true;
                            }
                            setDataToAdapter();
                            dbHelper.saveSubCategory(getWayPointByCategory.getData(),catID,page);
                        } else {
                            if(type.equals("SubCategory")){
                                txtLocation.setVisibility(View.VISIBLE);
                            } else {
                                if(getActivity() !=null) {
                                    Toast.makeText(getActivity(), String.valueOf(getWayPointByCategory.getMsg()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        isLastPage = true;
                        wayPointByCategory.addAll(dbHelper.getSubCategory(catID));
                        setDataToAdapter();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isLastPage = true;
                    wayPointByCategory.addAll(dbHelper.getSubCategory(catID));
                    setDataToAdapter();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void setDataToAdapter() {
        if (frag_cat_pb.getVisibility() == View.VISIBLE) {
            frag_cat_pb.setVisibility(View.GONE);
        }
        if(recWayPointList != null) {
           // recWayPointList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
            adapter.notifyDataSetChanged();
            isLoading = false;
        }
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

    private boolean isLoading = false;
    private boolean isScrolling = false;
    private boolean isLastPage = false;
    private RecyclerView.OnScrollListener scrollListener= new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
            boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
            boolean isTotalMoreThanVisible = totalItemCount >= 15;
            boolean shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling;
            if(shouldPaginate){
                page += 1;
                isScrolling = false;
                isLoading = true;
                getWayPointByCategories();
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }
    };
}
