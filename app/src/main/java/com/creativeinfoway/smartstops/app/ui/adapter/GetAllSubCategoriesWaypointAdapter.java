package com.creativeinfoway.smartstops.app.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.NavigationLauncherActivity;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.activity.SingalWaypointActivity;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GetAllSubCategoriesWaypointAdapter extends RecyclerView.Adapter<GetAllSubCategoriesWaypointAdapter.CategoryViewHolder>  implements Filterable {

    private Activity c;
    private ArrayList<GetAllSubCategoryWaypoint.DataBean> dataBeans = new ArrayList<>();
    private ArrayList<GetAllSubCategoryWaypoint.DataBean> filterDataBeans = new ArrayList<>();
    private boolean isShowSearch;
    private SearchView searchView;
    private String catimage;
    private long mLastClickTime = 0;

    private ArrayList<String> offlineDownloadedMapNames = new ArrayList<>();

    public GetAllSubCategoriesWaypointAdapter(Activity c, List<GetAllSubCategoryWaypoint.DataBean> data, boolean isShowSearch, SearchView searchView, String catimage) {
        this.c = c;
        this.dataBeans = (ArrayList<GetAllSubCategoryWaypoint.DataBean>) data;
        filterDataBeans = new ArrayList<>();
        this.filterDataBeans.addAll(data);
        this.isShowSearch= isShowSearch;
        this.searchView = searchView;
        this.catimage = catimage;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.custom_getallsubcategories_waypoint, viewGroup, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        categoryViewHolder.textView_cat_name.setText(String.valueOf(dataBeans.get(i).getName()));

        String catName = null;

        if(dataBeans.get(i).getSub_cat_array() != null){
            for(int j = 0; j<dataBeans.get(i).getSub_cat_array().size();j++){
                if(j == 0){
                    catName = dataBeans.get(i).getSub_cat_array().get(j).getName();
                }
                else{
                    catName = catName + ", " +dataBeans.get(i).getSub_cat_array().get(j).getName();
                }
            }
        } else {
            catName = "";
        }

        categoryViewHolder.tvSubCategory.setText(catName);

        if(dataBeans.get(i).getWaypoint_icon_image().contains("www")){
            Glide.with(c).load(dataBeans.get(i).getWaypoint_icon_image()).into(categoryViewHolder.imageView_cat_img);
        }else{
            String url;
            //http://canadasmartstops.com/smartstop/admin/image/ef0686db19622501d9327a6329294ce4.png

            url = dataBeans.get(i).getWaypoint_icon_image();
            url = url.replace("http://","http://www.");

            Glide.with(c).load(url).into(categoryViewHolder.imageView_cat_img);
        }

        if(isMapDownloaded(i)){
            categoryViewHolder.imgMapDownloaded.setVisibility(View.VISIBLE);
        } else {
            categoryViewHolder.imgMapDownloaded.setVisibility(View.GONE);
        }

        //Glide.with(c).load(dataBeans.get(i).getWaypoint_icon_image()).into(categoryViewHolder.imageView_cat_img);

        categoryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                    Log.d("=====1"," on Click category");
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                searchView.setQuery("", false);

                //searchView.setIconified(true);
                //searchView.clearFocus();

                /*if (isShowSearch){
                    Fragment fragment = new ShowWayPointSubCategoryFragment();
                    FragmentManager fragmentManager = ((SubCategoryActivity) c).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("catid", dataBeans.get(i).getId());
                    bundle.putString("searchName", dataBeans.get(i).getName());
                    bundle.putBoolean("isShowSearch", isShowSearch);
                    bundle.putString("type", "SubCategory");
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.frame_sub, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    Fragment fragment = new CategoryMapFragment();
                    FragmentManager fragmentManager = ((SubCategoryActivity) c).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("catid", dataBeans.get(i).getId());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame_sub, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }*/

                /*Fragment fragment = new GetDirectionMapFragment();
                FragmentManager fragmentManager = ((SubCategoryActivity) c).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("from","SubCategoryActivity");
                bundle.putString("latitude",String.valueOf(dataBeans.get(categoryViewHolder.getAdapterPosition()).getLatitude_decimal()));
                bundle.putString("longtitude",String.valueOf(dataBeans.get(categoryViewHolder.getAdapterPosition()).getLongitude_decimal()));
                fragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frame_sub, fragment,"Waypoint");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans = dataBeans.get(categoryViewHolder.getAdapterPosition());

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
                    intent.putExtra("wayPointPosition", "" + categoryViewHolder.getAdapterPosition());
                    intent.putParcelableArrayListExtra("markerinfoAllData", (ArrayList<GetAllSubCategoryWaypoint.DataBean>) dataBeans);
                    c.startActivity(intent);
                }else {
                    Constant.SINGLE_WAYPOINT = false;
                    Intent intent = new Intent(c, NavigationLauncherActivity.class);
                    intent.putExtra("from", "SubCategoryActivity");
                    intent.putExtra("latitude", String.valueOf(dataBeans.get(categoryViewHolder.getAdapterPosition()).getLatitude_decimal()));
                    intent.putExtra("longtitude", String.valueOf(dataBeans.get(categoryViewHolder.getAdapterPosition()).getLongitude_decimal()));
                    intent.putExtra("waypointiconVisible", dataBeans.get(categoryViewHolder.getAdapterPosition()).getMapbox_icon_visibility());
                    intent.putParcelableArrayListExtra("allwaypoint", dataBeans);
                    intent.putExtra("WaypointIdentify", String.valueOf(dataBeans.get(categoryViewHolder.getAdapterPosition()).getWaypoint_id()));
                    intent.putExtra("WaypointOnlyDisplay", "true");
                    intent.putExtra("wayPointPosition", "" + i);
                    intent.putExtra("catId",dataBeans.get(categoryViewHolder.getAdapterPosition()).getCat_id());
                    intent.putExtra("mapName",dataBeans.get(categoryViewHolder.getAdapterPosition()).getCat_id()+"_"+dataBeans.get(categoryViewHolder.getAdapterPosition()).getCat_name()+"_"+dataBeans.get(categoryViewHolder.getAdapterPosition()).getId());
                    intent.putExtra("dataBeanString",new Gson().toJson(dataBeans.get(categoryViewHolder.getAdapterPosition())));
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

    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();
                if (constraint.toString().length() > 0) {
                    List<GetAllSubCategoryWaypoint.DataBean> founded = new ArrayList<GetAllSubCategoryWaypoint.DataBean>();

                    for(int i = 0;i<filterDataBeans.size();i++){
                        if(filterDataBeans.get(i).getName().toString().toLowerCase().contains(constraint.toString()) /*&& item.getName().toLowerCase().startsWith(constraint.toString())*/){
                            founded.add(filterDataBeans.get(i));
                        }
                        else{
                            for(int j = 0;j<filterDataBeans.get(i).getSub_cat_array().size();j++){
                                if(filterDataBeans.get(i).getSub_cat_array().get(j).getName().toString().toLowerCase().contains(constraint.toString()) /*&& item.getName().toLowerCase().startsWith(constraint.toString())*/){
                                    founded.add(filterDataBeans.get(i));
                                }
                            }
                        }
                    }

                    result.values = founded;
                    result.count = founded.size();
                }else {
                    result.values = filterDataBeans;
                    result.count = filterDataBeans.size();
                }
                return result;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count != 0){
                    dataBeans.clear();
                    dataBeans.addAll((List<GetAllSubCategoryWaypoint.DataBean>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    public void setDownloadedMapName(ArrayList<String> offlineDownloadedMapNames){
        this.offlineDownloadedMapNames.clear();
        this.offlineDownloadedMapNames.addAll(offlineDownloadedMapNames);
        notifyDataSetChanged();
    }

    private boolean isMapDownloaded(int position){
        String mapName = dataBeans.get(position).getCat_id()+"_"+dataBeans.get(position).getCat_name()+"_"+dataBeans.get(position).getId();
        return offlineDownloadedMapNames.contains(mapName);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_cat_img,imgMapDownloaded;
        TextView textView_cat_name,tvSubCategory;
        RelativeLayout linearLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMapDownloaded = (ImageView) itemView.findViewById(R.id.img_map_downloaded);
            imageView_cat_img = (ImageView) itemView.findViewById(R.id.iv_categories_img);
            textView_cat_name = (TextView) itemView.findViewById(R.id.tv_cate_name);
            tvSubCategory = (TextView)itemView.findViewById(R.id.tvSubCategory);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.custom_cat_ll);
        }
    }
}
