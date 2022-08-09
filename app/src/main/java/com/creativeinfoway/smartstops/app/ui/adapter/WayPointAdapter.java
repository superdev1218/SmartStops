package com.creativeinfoway.smartstops.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.activity.NavigationLauncherActivity;
import com.creativeinfoway.smartstops.app.ui.activity.SingalWaypointActivity;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.google.gson.Gson;
import com.mapbox.navigation.base.route.Router;

import java.util.ArrayList;
import java.util.List;

public class WayPointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

        private Activity context;
        private ArrayList<GetAllSubCategoryWaypoint.DataBean> dataBeans;
        private ArrayList<GetAllSubCategoryWaypoint.DataBean> filterDataBeans;
        private SearchView searchView;
        private String type;
        private  long mLastClickTime = 0;

    public WayPointAdapter(Activity context, List<GetAllSubCategoryWaypoint.DataBean> dataBeans, SearchView searchView,String type) {
        this.dataBeans = (ArrayList<GetAllSubCategoryWaypoint.DataBean>) dataBeans;
        this.context =  context;
        this.searchView = searchView;
        this.type = type;
        filterDataBeans = new ArrayList<>();
        this.filterDataBeans.addAll(dataBeans);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_way_point,parent,false);

        return new WayPointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof WayPointHolder){
            ((WayPointHolder) holder).txtWayPointName.setText(dataBeans.get(holder.getAdapterPosition()).getName());
            String url=dataBeans.get(holder.getAdapterPosition()).getWaypoint_icon_image();
            if(url.contains("http://")) {
                 url = dataBeans.get(holder.getAdapterPosition()).getWaypoint_icon_image().replace("http://", "https://");
            }
            loadImage(url, (WayPointHolder) holder);

            if(isMapDownloaded(position)){
                ((WayPointHolder) holder).imgMapDownloaded.setVisibility(View.VISIBLE);
            } else {
                ((WayPointHolder) holder).imgMapDownloaded.setVisibility(View.GONE);
            }

        }
    }

    void loadImage(String url,WayPointHolder holder){
        Glide.with(context).load(url).error(R.drawable.icon_logo).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(((WayPointHolder) holder).imgWayPoint);
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                    for(GetAllSubCategoryWaypoint.DataBean item: filterDataBeans){
                        if(item.getName().toString().toLowerCase().contains(constraint.toString()) && item.getName().toLowerCase().startsWith(constraint.toString())){
                            founded.add(item);
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

                dataBeans.clear();
                dataBeans.addAll((List<GetAllSubCategoryWaypoint.DataBean>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private ArrayList<String> offlineDownloadedMapNames = new ArrayList<>();

    public void setDownloadedMapName(ArrayList<String> offlineDownloadedMapNames){
        this.offlineDownloadedMapNames.clear();
        this.offlineDownloadedMapNames.addAll(offlineDownloadedMapNames);
        notifyDataSetChanged();
    }

    private boolean isMapDownloaded(int position){
        String mapName = dataBeans.get(position).getCat_id()+"_"+dataBeans.get(position).getCat_name()+"_"+dataBeans.get(position).getId();
        return offlineDownloadedMapNames.contains(mapName);
    }

    private class WayPointHolder extends RecyclerView.ViewHolder {

        ImageView imgWayPoint;
        TextView txtWayPointName;
//        LinearLayout linAdapterWayPoint;
        ImageView imgMapDownloaded;

        public WayPointHolder(View view) {
            super(view);

            txtWayPointName = view.findViewById(R.id.txtWayPointName);
            imgWayPoint = view.findViewById(R.id.imgWayPoint);
//            linAdapterWayPoint = view.findViewById(R.id.linAdapterWayPoint);
            imgMapDownloaded = view.findViewById(R.id.img_map_downloaded);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // 1000 = 1second
                        Log.d("=====1"," on Click Subcategory");
                        return;
                    }

                    mLastClickTime = SystemClock.elapsedRealtime();
                    searchView.setQuery("", false);
                    //searchView.setIconified(true);
                    //searchView.clearFocus();

                    //Fragment fragment = new GetDirectionMapFragment();
                    if(type.equals("Category")){
                       /* FragmentManager fragmentManager = ((HomeActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("from","ShowWayPointCategory");
                        bundle.putString("latitude",String.valueOf(dataBeans.get(getAdapterPosition()).getLatitude_decimal()));
                        bundle.putString("longtitude",String.valueOf(dataBeans.get(getAdapterPosition()).getLongitude_decimal()));
                        fragment.setArguments(bundle);
                        fragmentTransaction.add(R.id.content_home, fragment,"Waypoint");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/

                        com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans = dataBeans.get(getAdapterPosition());

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
                            Intent intent = new Intent(context, SingalWaypointActivity.class);
                            intent.putExtra("from", "0");
                            intent.putExtra("wayPointPosition", "" + getAdapterPosition());
                            intent.putParcelableArrayListExtra("markerinfoAllData", (ArrayList<GetAllSubCategoryWaypoint.DataBean>) dataBeans);
                            context.startActivity(intent);
                        }else {

                            Intent intent = new Intent(context, NavigationLauncherActivity.class);
                            intent.putExtra("from", "SubCategoryActivity");
                            intent.putExtra("latitude", String.valueOf(dataBeans.get(getAdapterPosition()).getLatitude_decimal()));
                            intent.putExtra("longtitude", String.valueOf(dataBeans.get(getAdapterPosition()).getLongitude_decimal()));
                            intent.putExtra("wayPointPosition", "" + getAdapterPosition());
                            intent.putExtra("WaypointOnlyDisplay", "true");
                            intent.putExtra("WaypointIdentify", String.valueOf(dataBeans.get(getAdapterPosition()).getWaypoint_id()));
                            String waypointiconVisible ;
                            if(dataBeans.get(getAdapterPosition()).getMapbox_icon_visibility() == null){
                                waypointiconVisible = "false";
                            } else{
                                waypointiconVisible = dataBeans.get(getAdapterPosition()).getMapbox_icon_visibility();
                            }
                            intent.putExtra("waypointiconVisible", waypointiconVisible);
                            intent.putParcelableArrayListExtra("allwaypoint", dataBeans);
                            intent.putExtra("catId",dataBeans.get(getAdapterPosition()).getCat_id());
                            intent.putExtra("mapName",dataBeans.get(getAdapterPosition()).getCat_id()+"_"+dataBeans.get(getAdapterPosition()).getCat_name()+"_"+dataBeans.get(getAdapterPosition()).getId());
                            intent.putExtra("dataBeanString",new Gson().toJson(dataBeans.get(getAdapterPosition())));
                            NavigationLauncherActivity.categoryWayPoints = dataBeans;
                            context.startActivity(intent);
                        }


                    }
                    else{
                       /* FragmentManager fragmentManager = ((SubCategoryActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("from","SubCategoryActivity");
                        bundle.putString("latitude",String.valueOf(dataBeans.get(getAdapterPosition()).getLatitude_decimal()));
                        bundle.putString("longtitude",String.valueOf(dataBeans.get(getAdapterPosition()).getLongitude_decimal()));
                        fragment.setArguments(bundle);
                        fragmentTransaction.add(R.id.frame_sub, fragment,"Waypoint");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/
                    }
                }
            });
        }
    }
}
