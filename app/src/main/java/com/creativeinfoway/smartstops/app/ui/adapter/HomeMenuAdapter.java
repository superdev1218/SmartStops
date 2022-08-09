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

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.interfaces.ItemClickListner;


public class HomeMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String[] gridViewString;
    private final int[] gridViewImageId;
    private Context mContext;
    private ItemClickListner itemClickListner;


    public HomeMenuAdapter(Context context, String[] gridViewString, int[] gridViewImageId, ItemClickListner itemClickListner) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
        this.itemClickListner = itemClickListner;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridViewAndroid = inflater.inflate(R.layout.custom_home_menu, null);

        return new Holder(gridViewAndroid);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof Holder) {


            ((Holder) viewHolder).textViewAndroid.setText(gridViewString[i]);

            ((Holder) viewHolder).imageViewAndroid.setImageResource(gridViewImageId[i]);

            ((Holder) viewHolder).android_custom_gridview_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    itemClickListner.onItemClick(i);
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return gridViewString.length;
    }


    public class Holder extends RecyclerView.ViewHolder {

        public TextView textViewAndroid;
        public ImageView imageViewAndroid;
        LinearLayout android_custom_gridview_layout;

        public Holder(View view) {
            super(view);
            textViewAndroid = (view).findViewById(R.id.android_gridview_text);
            imageViewAndroid = (ImageView) view.findViewById(R.id.android_gridview_image);
            android_custom_gridview_layout = view.findViewById(R.id.android_custom_gridview_layout);

        }
    }
}
