package com.creativeinfoway.smartstops.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.models.GetFavouriteWaypoint;

import java.util.ArrayList;
import java.util.List;

public abstract class FavouriteWaypointAdapter extends RecyclerView.Adapter<FavouriteWaypointAdapter.MyViewHolder> {
    Context c;
    List<GetFavouriteWaypoint.DataBean> dataBeans = new ArrayList<>();

    public FavouriteWaypointAdapter(Context c, List<GetFavouriteWaypoint.DataBean> data) {
        this.c = c;
        this.dataBeans = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.custom_fav_waypoint, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {


        myViewHolder.tvWaypointName.setText(dataBeans.get(i).getName());
        myViewHolder.tvWaypointaddress.setText(dataBeans.get(i).getAddress());
        myViewHolder.linearLayout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavWayPointClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWaypointName;
        TextView tvWaypointaddress;
        LinearLayout linearLayout_main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout_main=(LinearLayout)itemView.findViewById(R.id.ll_custom_fav_waypoint_main);
            tvWaypointName = (TextView) itemView.findViewById(R.id.custom_fav_waypoint_name);
            tvWaypointaddress = (TextView) itemView.findViewById(R.id.custom_fav_waypoint_address);
        }
    }

    public abstract void  onFavWayPointClick(int i);
}
