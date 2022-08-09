package com.creativeinfoway.smartstops.app.ui.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.helpers.DBHelper;
import com.creativeinfoway.smartstops.app.ui.adapter.GetAllSubCategoriesWaypointAdapter;
import com.creativeinfoway.smartstops.app.ui.adapter.SubCategoriesAdapter;
import com.creativeinfoway.smartstops.app.ui.models.GetAllSubCategories;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.utils.GpsTracker;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_CHARSET;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_FIELD_REGION_NAME;

public class SubCategoryActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivback,act_home_img_menu;
    private TextView txtSubCatName;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchView;
    private String subCatId,catName,catimage;
    private RelativeLayout rootViewSubCategory;
    private boolean isKeyboardShowing;
    private GpsTracker gpsTracker;
    private SubCategoriesAdapter subCategoriesAdapter;
    private GetAllSubCategoriesWaypointAdapter getAllSubCategoriesWaypointAdapter;
    private  boolean isShowSearch;

    private DBHelper dbHelper;
    private int page = 0;
    private List<GetAllSubCategoryWaypoint.DataBean> subCategories = new ArrayList<>();

    private OfflineManager offlineManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        gpsTracker = new GpsTracker(this);

        dbHelper = new DBHelper(this);
        subCatId = getIntent().getStringExtra("subcatId");
        catName = getIntent().getStringExtra("catName");
        catimage = getIntent().getStringExtra("catimage");
        isShowSearch = getIntent().getBooleanExtra("isShowSearch",false);

        ivback = findViewById(R.id.iv_back);
        ivback.setOnClickListener(this);

        txtSubCatName = findViewById(R.id.txtSubCatName);
        recyclerView = findViewById(R.id.frag_cat_recyclerview);
        progressBar = findViewById(R.id.frag_cat_pb);
        searchView = findViewById(R.id.searchView);
        rootViewSubCategory = findViewById(R.id.rootViewSubCategory);

        txtSubCatName.setText(catName+"");

        if (isShowSearch) {
            searchView.setVisibility(View.GONE);
            searchView.onActionViewExpanded();
        } else {
            searchView.setVisibility(View.GONE);
        }

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (getAllSubCategoriesWaypointAdapter != null)
                    getAllSubCategoriesWaypointAdapter.getFilter().filter(newText);

                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
        });

        rootViewSubCategory.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        rootViewSubCategory.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootViewSubCategory.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                //  onKeyboardVisibilityChanged(true);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                // onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });

        initRecyclerView();
       // addSubCategories(subCatId,String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));
        getSubCategoryWayPoint();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareOfflineManager();
    }

    private void initRecyclerView(){
        getAllSubCategoriesWaypointAdapter = new GetAllSubCategoriesWaypointAdapter(SubCategoryActivity.this, subCategories,isShowSearch,searchView,catimage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubCategoryActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(getAllSubCategoriesWaypointAdapter);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void prepareOfflineManager(){
        offlineManager = OfflineManager.getInstance(SubCategoryActivity.this);
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
                getAllSubCategoriesWaypointAdapter.setDownloadedMapName(offlineDownloadMapName);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getSubCategoryWayPoint() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getWayPointByCategories(subCatId, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()),page);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();

                    try {
                        assert responseBody != null;
                        String str_Response = responseBody.string();
                        GetAllSubCategoryWaypoint getAllSubCategoryWaypoint = new Gson().fromJson(str_Response, GetAllSubCategoryWaypoint.class);

                        if(getAllSubCategoryWaypoint.getStatus().equals("1")){
                            subCategories.addAll(getAllSubCategoryWaypoint.getData());
                            if(getAllSubCategoryWaypoint.getData().size() < 15){
                                isLastPage = true;
                            }
                            fillSubcategoryToRecyclerView();
                            dbHelper.saveSubCategory(getAllSubCategoryWaypoint.getData(),subCatId,page);
                        }
                        else{
                            subCategories.addAll(dbHelper.getSubCategory(subCatId));
                            isLastPage = true;
                            fillSubcategoryToRecyclerView();
                            Toast.makeText(SubCategoryActivity.this,getAllSubCategoryWaypoint.getMsg(),Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        subCategories.addAll(dbHelper.getSubCategory(subCatId));
                        isLastPage = true;
                        fillSubcategoryToRecyclerView();
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    subCategories.addAll(dbHelper.getSubCategory(subCatId));
                    isLastPage = true;
                    fillSubcategoryToRecyclerView();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        } catch (KeyManagementException e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
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

    private void fillSubcategoryToRecyclerView(){
        getAllSubCategoriesWaypointAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private void addSubCategories(String subCatId, String latitude, String longitude) {
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getSubCategories(subCatId);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();
                            GetAllSubCategories getAllSubCategories = new Gson().fromJson(strResponse, GetAllSubCategories.class);
                            if (getAllSubCategories.getStatus().equals("1")) {
                                subCategoriesAdapter = new SubCategoriesAdapter(SubCategoryActivity.this, getAllSubCategories.getData(),isShowSearch,searchView,catimage);
                                //GridLayoutManager gridLayoutManager = new GridLayoutManager(SubCategoryActivity.this, 2);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubCategoryActivity.this);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(subCategoriesAdapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

        }
    }

    @Override
    public void onPause() {
        super.onPause();

       // searchView.setQuery("", false);
        //searchView.setIconified(true);
        searchView.clearFocus();
        //searchView.onActionViewCollapsed();
        onKeyboardVisibilityChanged(isKeyboardShowing);
    }
    private void onKeyboardVisibilityChanged(boolean b) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
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
                getSubCategoryWayPoint();
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
