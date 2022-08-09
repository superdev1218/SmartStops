package com.creativeinfoway.smartstops.app.ui.adapter;

import android.app.Activity;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.SubCategoryActivity;
import com.creativeinfoway.smartstops.app.ui.fragment.category.CategoryMapFragment;
import com.creativeinfoway.smartstops.app.ui.fragment.category.ShowWayPointSubCategoryFragment;
import com.creativeinfoway.smartstops.app.ui.models.GetAllSubCategories;

import java.util.ArrayList;
import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.CategoryViewHolder>  implements Filterable {

    private Activity c;
    private ArrayList<GetAllSubCategories.DataBean> dataBeans = new ArrayList<>();
    private ArrayList<GetAllSubCategories.DataBean> filterDataBeans = new ArrayList<>();
    private boolean isShowSearch;
    private SearchView searchView;
    private String catimage;

    public SubCategoriesAdapter(Activity c, List<GetAllSubCategories.DataBean> data, boolean isShowSearch, SearchView searchView, String catimage) {
        this.c = c;
        this.dataBeans = (ArrayList<GetAllSubCategories.DataBean>) data;
        filterDataBeans = new ArrayList<>();
        this.filterDataBeans.addAll(data);
        this.isShowSearch= isShowSearch;
        this.searchView = searchView;
        this.catimage = catimage;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.custom_subcategories, viewGroup, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        categoryViewHolder.textView_cat_name.setText(String.valueOf(dataBeans.get(i).getName()));
        Glide.with(c).load(catimage).into(categoryViewHolder.imageView_cat_img);
        categoryViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchView.clearFocus();

                if (isShowSearch){
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
                    List<GetAllSubCategories.DataBean> founded = new ArrayList<GetAllSubCategories.DataBean>();
                    for(GetAllSubCategories.DataBean item: filterDataBeans){
                        if(item.getName().toString().toLowerCase().contains(constraint.toString()) /*&& item.getName().toLowerCase().startsWith(constraint.toString())*/){
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

                if (results.count != 0){
                    dataBeans.clear();
                    dataBeans.addAll((List<GetAllSubCategories.DataBean>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_cat_img;
        TextView textView_cat_name;
        RelativeLayout linearLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cat_img = (ImageView) itemView.findViewById(R.id.iv_categories_img);
            textView_cat_name = (TextView) itemView.findViewById(R.id.tv_cate_name);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.custom_cat_ll);
        }
    }

}
