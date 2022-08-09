package com.creativeinfoway.smartstops.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.interfaces.ItemClickListner;
import com.creativeinfoway.smartstops.app.ui.models.GetAllCategories;

import java.util.ArrayList;
import java.util.List;

public class LegendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private Context context;
private  ArrayList<GetAllCategories.DataBean> dataBeanArrayList;

        private ItemClickListner itemClickListner;

    public LegendAdapter(Context context, List<GetAllCategories.DataBean> dataBeanArrayList, ItemClickListner itemClickListner) {
        this.context = context;
        this.dataBeanArrayList = (ArrayList<GetAllCategories.DataBean>) dataBeanArrayList;
        this.itemClickListner = itemClickListner;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_legent, viewGroup, false);
        return new LegendAdapterHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if (viewHolder instanceof LegendAdapterHolder){

            ((LegendAdapterHolder) viewHolder).txtLegendTitle.setText(dataBeanArrayList.get(i).getName());
            Glide.with(context).load(dataBeanArrayList.get(i) .getImage()).into(((LegendAdapterHolder) viewHolder).imgLegend);





            ((LegendAdapterHolder) viewHolder).linItemLegend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemClickListner.onItemClick(i);


                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataBeanArrayList.size();
    }

    class LegendAdapterHolder extends RecyclerView.ViewHolder{

        TextView txtLegendTitle;
        LinearLayout linItemLegend;
        ImageView imgLegend;

        public LegendAdapterHolder(@NonNull View itemView) {
            super(itemView);

            linItemLegend = itemView.findViewById(R.id.linItemLegend);

            imgLegend = itemView.findViewById(R.id.imgLegend);
            txtLegendTitle = itemView.findViewById(R.id.txtLegendTitle);
        }
    }
}
