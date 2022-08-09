package com.creativeinfoway.smartstops.app.ui.fragment.category;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.helpers.DBHelper;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.adapter.CategoriesAdapter;
import com.creativeinfoway.smartstops.app.ui.models.GetAllCategories;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

    private ImageView ivback,act_home_img_menu;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private SearchView searchView;
    private CategoriesAdapter categoriesAdapter;
    private LinearLayout rootViewCategory;
    private boolean isKeyboardShowing;
    private  boolean isShowSearch;
    public CategoryFragment() {
        // Required empty public constructor
    }
    private DBHelper dbHelper;

    private int page = 0;
    private List<GetAllCategories.DataBean> categories = new ArrayList<GetAllCategories.DataBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        initIDs(v);
        initRecyclerView();
        addCategories();
        return v;
    }

    private void initIDs(View v) {
        dbHelper = new DBHelper(requireContext());
        ivback = v.findViewById(R.id.iv_back);
        ivback.setOnClickListener(this);
        act_home_img_menu = v.findViewById(R.id.act_home_img_menu);
        act_home_img_menu.setOnClickListener(this);

        recyclerView = v.findViewById(R.id.frag_cat_recyclerview);
        progressBar = v.findViewById(R.id.frag_cat_pb);
        searchView = v.findViewById(R.id.searchView);
        rootViewCategory = v.findViewById(R.id.rootViewCategory);

        isShowSearch = getArguments().getBoolean("isShowSearch");

        if (isShowSearch) {
            searchView.setVisibility(View.VISIBLE);
            searchView.onActionViewExpanded();
        } else {
            searchView.setVisibility(View.GONE);
        }

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (categoriesAdapter != null)
                categoriesAdapter.getFilter().filter(newText);

                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
        });

        rootViewCategory.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        rootViewCategory.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootViewCategory.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                //  onKeyboardVisibilityChanged(true);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                // onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });
    }

    private void onKeyboardVisibilityChanged(boolean b) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private void initRecyclerView(){
        categoriesAdapter = new CategoriesAdapter(getActivity(), categories,isShowSearch,searchView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoriesAdapter);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void addCategories() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getcategory(page);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();
                            GetAllCategories getAllCategories = new Gson().fromJson(strResponse, GetAllCategories.class);
                            if (getAllCategories.getStatus().equals("1")) {
                                categories.addAll(getAllCategories.getData());
                                categoriesAdapter.dummyDataBeans.addAll(getAllCategories.getData());
                                if(getAllCategories.getData().size() < 15){
                                    isLastPage = true;
                                }
                                dbHelper.saveCategories(getAllCategories.getData(),page);
                                fillToRecyclerView();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        categories.addAll(dbHelper.getCategories());
                        categoriesAdapter.dummyDataBeans.addAll(dbHelper.getCategories());
                        isLastPage = true;
                        fillToRecyclerView();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    categories.addAll(dbHelper.getCategories());
                    categoriesAdapter.dummyDataBeans.addAll(dbHelper.getCategories());
                    isLastPage = true;
                    fillToRecyclerView();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.act_home_img_menu:
                if (((HomeActivity) getActivity()).drawer.isDrawerOpen(GravityCompat.START)) {
                    ((HomeActivity) getActivity()).drawer.closeDrawer(GravityCompat.START);
                } else {
                    ((HomeActivity) getActivity()).drawer.openDrawer(GravityCompat.START);
                    ((HomeActivity) getActivity()).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.clearFocus();
        onKeyboardVisibilityChanged(isKeyboardShowing);
    }

    private void fillToRecyclerView(){
        categoriesAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        isLoading = false;
    }

    private boolean isLoading = false;
    private boolean isScrolling = false;
    private boolean isLastPage = false;
    private RecyclerView.OnScrollListener scrollListener= new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
            boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
            boolean isTotalMoreThanVisible = totalItemCount >= 15;
            boolean shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling;
            if(shouldPaginate){
                page += 1;
                isScrolling = false;
                isLoading = true;
                addCategories();
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }
    };
}
