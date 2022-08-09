package com.creativeinfoway.smartstops.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.adapter.OfflineMapAdapter;
import com.creativeinfoway.smartstops.app.ui.interfaces.AdapterToActivityCallback;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.creativeinfoway.smartstops.app.ui.utils.Constant.JSON_CHARSET;
import static com.creativeinfoway.smartstops.app.ui.utils.Constant.WAYPOINT_OBJECT;

public class OfflineMapListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivback,imgDelete,imgSelect;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private OfflineMapAdapter offlineMapAdapter;
    private  boolean isShowSearch = false;
    private List<GetAllSubCategoryWaypoint.DataBean> subCategories = new ArrayList<>();
    private ArrayList<OfflineRegion> offlineRegionsList = new ArrayList<>();

    private OfflineManager offlineManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map_list);

        recyclerView = findViewById(R.id.frag_cat_recyclerview);
        searchView = findViewById(R.id.searchView);

        if (isShowSearch) {
            searchView.setVisibility(View.GONE);
            searchView.onActionViewExpanded();
        } else {
            searchView.setVisibility(View.GONE);
        }

        ivback = findViewById(R.id.iv_back);
        imgDelete = findViewById(R.id.imgDelete);
        imgSelect = findViewById(R.id.imgSelect);

        ivback.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        imgSelect.setOnClickListener(this);

        initRecyclerView();
    }

    @Override
    public void onBackPressed() {
        if(offlineMapAdapter.isMultiSelectEnable){
            stopMultiSelect();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDownloadedMap();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.imgDelete:
                deleteSelectedMaps();
                break;
            case R.id.imgSelect :
                startSelection();
                break;
        }
    }

    private void initRecyclerView(){
        offlineMapAdapter = new OfflineMapAdapter(OfflineMapListActivity.this, subCategories,adapterToActivityCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OfflineMapListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(offlineMapAdapter);
    }

    private void getDownloadedMap(){
        subCategories.clear();
        offlineRegionsList.clear();
        offlineManager = OfflineManager.getInstance(OfflineMapListActivity.this);
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(OfflineRegion[] offlineRegions) {
                for (OfflineRegion offlineRegion : offlineRegions){
                    GetAllSubCategoryWaypoint.DataBean dataBean = getDataBeanFromOfflineRegion(offlineRegion);
                    if(dataBean != null){
                        subCategories.add(dataBean);
                        offlineRegionsList.add(offlineRegion);
                    }
                }
                fillSubcategoryToRecyclerView();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void fillSubcategoryToRecyclerView(){
        offlineMapAdapter.setOfflineRegions(offlineRegionsList);
        offlineMapAdapter.notifyDataSetChanged();
    }

    private GetAllSubCategoryWaypoint.DataBean getDataBeanFromOfflineRegion(OfflineRegion offlineRegion){
        GetAllSubCategoryWaypoint.DataBean dataBean ;

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            String dataBeanString = jsonObject.getString(WAYPOINT_OBJECT);
            dataBean = new Gson().fromJson(dataBeanString,GetAllSubCategoryWaypoint.DataBean.class);
        } catch (Exception exception) {
            dataBean = null;
        }
        return dataBean;
    }

    private void startSelection(){
        offlineMapAdapter.isMultiSelectEnable = true;
        offlineMapAdapter.selectedOfflineRegion.clear();
        offlineMapAdapter.notifyDataSetChanged();
        imgDelete.setVisibility(View.VISIBLE);
        imgSelect.setVisibility(View.GONE);
    }

    private void stopMultiSelect(){
        offlineMapAdapter.isMultiSelectEnable = false;
        offlineMapAdapter.selectedOfflineRegion.clear();
        offlineMapAdapter.notifyDataSetChanged();
        imgDelete.setVisibility(View.GONE);
        imgSelect.setVisibility(View.VISIBLE);
    }

    private void deleteSelectedMaps(){
        for (int i = 0; i < offlineMapAdapter.selectedOfflineRegion.size();i++){
            OfflineRegion offlineRegion = offlineMapAdapter.selectedOfflineRegion.get(i);
            offlineRegion.delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                @Override
                public void onDelete() {
                    int index = offlineRegionsList.indexOf(offlineRegion);
                    offlineRegionsList.remove(offlineRegion);
                    subCategories.remove(index);
                    offlineMapAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    private AdapterToActivityCallback adapterToActivityCallback = new AdapterToActivityCallback() {
        @Override
        public void onSelectionStart() {
            imgDelete.setVisibility(View.VISIBLE);
            imgSelect.setVisibility(View.GONE);
        }
    };
}
