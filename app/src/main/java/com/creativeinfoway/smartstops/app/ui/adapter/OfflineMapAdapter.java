package com.creativeinfoway.smartstops.app.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.activity.NavigationLauncherActivity;
import com.creativeinfoway.smartstops.app.ui.activity.SingalWaypointActivity;
import com.creativeinfoway.smartstops.app.ui.interfaces.AdapterToActivityCallback;
import com.creativeinfoway.smartstops.app.ui.models.GetAllSubCategories;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.offline.OfflineRegion;

import java.util.ArrayList;
import java.util.List;

public class OfflineMapAdapter extends RecyclerView.Adapter<OfflineMapAdapter.OfflineMapViewHolder> {

    private Activity c;
    private ArrayList<GetAllSubCategoryWaypoint.DataBean> dataBeans = new ArrayList<>();
    private AdapterToActivityCallback adapterToActivityCallback;
    private long mLastClickTime = 0;

    private ArrayList<String> offlineDownloadedMapNames = new ArrayList<>();

    public boolean isMultiSelectEnable = false;
    private ArrayList<OfflineRegion> offlineRegionArrayList = new ArrayList<>();
    public ArrayList<OfflineRegion> selectedOfflineRegion = new ArrayList<>();

    public OfflineMapAdapter(Activity c, List<GetAllSubCategoryWaypoint.DataBean> data, AdapterToActivityCallback adapterToActivityCallback) {
        this.c = c;
        this.dataBeans = (ArrayList<GetAllSubCategoryWaypoint.DataBean>) data;
        this.adapterToActivityCallback = adapterToActivityCallback;
    }

    @NonNull
    @Override
    public OfflineMapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_offline_map, viewGroup, false);
        return new OfflineMapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineMapViewHolder offlineMapViewHolder, final int i) {
        offlineMapViewHolder.textView_cat_name.setText(String.valueOf(dataBeans.get(i).getName()));

        String catName = null;

        for(int j = 0; j<dataBeans.get(i).getSub_cat_array().size();j++){
            if(j == 0){
                catName = dataBeans.get(i).getSub_cat_array().get(j).getName();
            }
            else{
                catName = catName + ", " +dataBeans.get(i).getSub_cat_array().get(j).getName();
            }
        }

        offlineMapViewHolder.tvSubCategory.setText(catName);

        if(dataBeans.get(i).getWaypoint_icon_image().contains("www")){
            Glide.with(c).load(dataBeans.get(i).getWaypoint_icon_image()).into(offlineMapViewHolder.imageView_cat_img);
        }else{
            String url;
            //http://canadasmartstops.com/smartstop/admin/image/ef0686db19622501d9327a6329294ce4.png

            url = dataBeans.get(i).getWaypoint_icon_image();
            url = url.replace("http://","http://www.");

            Glide.with(c).load(url).into(offlineMapViewHolder.imageView_cat_img);
        }

        if(isMapDownloaded(i)){
            offlineMapViewHolder.imgMapDownloaded.setVisibility(View.VISIBLE);
        } else {
            offlineMapViewHolder.imgMapDownloaded.setVisibility(View.GONE);
        }

        //Glide.with(c).load(dataBeans.get(i).getWaypoint_icon_image()).into(categoryViewHolder.imageView_cat_img);

        if(isMultiSelectEnable){
            offlineMapViewHolder.checkBoxSelectItem.setVisibility(View.VISIBLE);
        } else {
            offlineMapViewHolder.checkBoxSelectItem.setVisibility(View.GONE);
        }
        offlineMapViewHolder.checkBoxSelectItem.setChecked(false);
        offlineMapViewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isMultiSelectEnable){
                    adapterToActivityCallback.onSelectionStart();
                    isMultiSelectEnable = true;
                    notifyDataSetChanged();
                }
                return true;
            }
        });
        offlineMapViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMultiSelectEnable){
                    if(offlineMapViewHolder.checkBoxSelectItem.isChecked()){
                        offlineMapViewHolder.checkBoxSelectItem.setChecked(false);
                        selectedOfflineRegion.remove(offlineRegionArrayList.get(offlineMapViewHolder.getAdapterPosition()));
                    } else {
                        offlineMapViewHolder.checkBoxSelectItem.setChecked(true);
                        selectedOfflineRegion.add(offlineRegionArrayList.get(offlineMapViewHolder.getAdapterPosition()));
                    }
                    return;
                }
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    Log.d("=====1"," on Click category");
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans = dataBeans.get(offlineMapViewHolder.getAdapterPosition());

                ArrayList<String> imageList = new ArrayList<>();
                String ii1 = com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans.getOption_image_1();
                String ii2 = com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans.getOption_image_2();
                String ii3 = com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans.getOption_image_3();

                if(ii1 != null) {
                    if (!ii1.equals("")) {
                        imageList.add(ii1);
                    }
                }if(ii2 != null){
                    if(!ii2.equals("")){
                        imageList.add(ii2);
                    }
                }if(ii3 != null){
                    if(!ii3.equals("")){
                        imageList.add(ii3);
                    }
                }

                if(imageList.size() > 0) {
                    Constant.SINGLE_WAYPOINT = true;
                    Intent intent = new Intent(c, SingalWaypointActivity.class);
                    intent.putExtra("from", "0");
                    intent.putExtra("wayPointPosition", "" + offlineMapViewHolder.getAdapterPosition());
                    intent.putParcelableArrayListExtra("markerinfoAllData", (ArrayList<GetAllSubCategoryWaypoint.DataBean>) dataBeans);
                    c.startActivity(intent);
                }else {
                    Constant.SINGLE_WAYPOINT = false;
                    Intent intent = new Intent(c, NavigationLauncherActivity.class);
                    intent.putExtra("from", "SubCategoryActivity");
                    intent.putExtra("latitude", String.valueOf(dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getLatitude_decimal()));
                    intent.putExtra("longtitude", String.valueOf(dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getLongitude_decimal()));
                    intent.putExtra("waypointiconVisible", dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getMapbox_icon_visibility());
                    intent.putParcelableArrayListExtra("allwaypoint", dataBeans);
                    intent.putExtra("WaypointIdentify", String.valueOf(dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getWaypoint_id()));
                    intent.putExtra("WaypointOnlyDisplay", "true");
                    intent.putExtra("wayPointPosition", "" + i);
                    intent.putExtra("catId",dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getCat_id());
                    intent.putExtra("mapName",dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getCat_id()+"_"+dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getCat_name()+"_"+dataBeans.get(offlineMapViewHolder.getAdapterPosition()).getId());
                    intent.putExtra("dataBeanString",new Gson().toJson(dataBeans.get(offlineMapViewHolder.getAdapterPosition())));
                    NavigationLauncherActivity.categoryWayPoints = dataBeans;
                    c.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    public void setOfflineRegions(ArrayList<OfflineRegion> offlineRegionArrayList){
        this.offlineRegionArrayList = offlineRegionArrayList;
    }

    private boolean isMapDownloaded(int position){
        String mapName = dataBeans.get(position).getCat_id()+"_"+dataBeans.get(position).getCat_name()+"_"+dataBeans.get(position).getId();
        return offlineDownloadedMapNames.contains(mapName);
    }

    class OfflineMapViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_cat_img,imgMapDownloaded;
        TextView textView_cat_name,tvSubCategory;
        RelativeLayout linearLayout;
        CheckBox checkBoxSelectItem;

        public OfflineMapViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMapDownloaded = (ImageView) itemView.findViewById(R.id.img_map_downloaded);
            imageView_cat_img = (ImageView) itemView.findViewById(R.id.iv_categories_img);
            textView_cat_name = (TextView) itemView.findViewById(R.id.tv_cate_name);
            tvSubCategory = (TextView)itemView.findViewById(R.id.tvSubCategory);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.custom_cat_ll);
            checkBoxSelectItem = itemView.findViewById(R.id.checkboxSelectItem);
        }
    }
}
