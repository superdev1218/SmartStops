package com.creativeinfoway.smartstops.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.activity.SubCategoryActivity;
import com.creativeinfoway.smartstops.app.ui.fragment.category.CategoryMapFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.category.ShowWayPointCategoryFragment;
import com.creativeinfoway.smartstops.app.ui.models.GetAllCategories;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>  implements Filterable {

    private Context c;
    private ArrayList<GetAllCategories.DataBean> dataBeans = new ArrayList<>();
    public ArrayList<GetAllCategories.DataBean> dummyDataBeans = new ArrayList<>();
    private boolean isShowSearch;
    private SearchView searchView;
    private static long mLastClickTime = 0;

    public CategoriesAdapter(Context c, List<GetAllCategories.DataBean> data, boolean isShowSearch, SearchView searchView) {
        this.c = c;
        this.dataBeans = (ArrayList<GetAllCategories.DataBean>) data;
        this.dummyDataBeans.addAll(data);
        this.isShowSearch= isShowSearch;
        this.searchView = searchView;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.custom_categories, viewGroup, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        categoryViewHolder.textView_cat_name.setText(String.valueOf(dataBeans.get(i).getName()));

        if(dataBeans.get(i).getImage().contains("www")){
            Glide.with(c).load(dataBeans.get(i).getImage()).into(categoryViewHolder.imageView_cat_img);
        }else{

            String url;

            //http://canadasmartstops.com/smartstop/admin/image/ef0686db19622501d9327a6329294ce4.png

            url = dataBeans.get(i).getImage();
            url = url.replace("http://","http://www.");

            Glide.with(c).load(url).into(categoryViewHolder.imageView_cat_img);
        }

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

                if (isShowSearch){

                    if(dataBeans.get(i).getTotal_sub_cat().equals("0")) {

                        Fragment fragment = new ShowWayPointCategoryFragment();
                        FragmentManager fragmentManager = ((HomeActivity) c).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("catid", dataBeans.get(i).getId());
                        bundle.putString("searchName", dataBeans.get(i).getName());
                        bundle.putBoolean("isShowSearch", isShowSearch);
                        bundle.putString("type", "Category");
                        fragment.setArguments(bundle);
                        fragmentTransaction.add(R.id.frameContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else{
                        Intent intent = new Intent(c, SubCategoryActivity.class);
                        intent.putExtra("subcatId",dataBeans.get(i).getId());
                        intent.putExtra("catName",dataBeans.get(i).getName());
                        intent.putExtra("catimage",dataBeans.get(i).getImage());
                        intent.putExtra("isShowSearch", true);
                        c.startActivity(intent);
                    }
                }else{
                    Fragment fragment = new CategoryMapFragment();
                    FragmentManager fragmentManager = ((HomeActivity) c).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("catid", dataBeans.get(i).getId());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frameContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
                    List<GetAllCategories.DataBean> founded = new ArrayList<GetAllCategories.DataBean>();
                    for(GetAllCategories.DataBean item: dataBeans){
                        if(item.getName().toString().toLowerCase().contains(constraint.toString()) && item.getName().toLowerCase().startsWith(constraint.toString())){
                            founded.add(item);
                        }
                    }
                    result.values = founded;
                    result.count = founded.size();
                }else {
                    result.values = dummyDataBeans;
                    result.count = dummyDataBeans.size();
                }
                return result;

            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results.count != 0){
                    dataBeans.clear();
                    dataBeans.addAll((List<GetAllCategories.DataBean>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_cat_img;
        TextView textView_cat_name;
        LinearLayout linearLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cat_img = (ImageView) itemView.findViewById(R.id.iv_categories_img);
            textView_cat_name = (TextView) itemView.findViewById(R.id.tv_cate_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.custom_cat_ll);
        }
    }

}
