package com.creativeinfoway.smartstops.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    private final Activity activity;
    private final LayoutInflater mLayoutInflater;
    private final ArrayList<String> imageList;
    int[] img = {R.drawable._1,R.drawable._2,R.drawable._3};


    public ImagePagerAdapter(Activity activity, ArrayList<String> imageList) {
        this.activity = activity;
        this.imageList = imageList;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image_page_recyclerview, view, false);

        ImageView imageView = itemView.findViewById(R.id.recy_home_image);
        int image = img[position];
        Glide.with(activity).load(R.drawable._1).into(imageView);


        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            container.removeView((LinearLayout) object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}