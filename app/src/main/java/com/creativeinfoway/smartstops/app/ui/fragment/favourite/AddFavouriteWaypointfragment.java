package com.creativeinfoway.smartstops.app.ui.fragment.favourite;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.activity.HomeActivity;
import com.creativeinfoway.smartstops.app.ui.models.GetAllCategories;
import com.creativeinfoway.smartstops.app.ui.models.GetPostalCode;
import com.creativeinfoway.smartstops.app.ui.models.GetProvince;
import com.creativeinfoway.smartstops.app.ui.models.GetSubCategory;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;
import com.creativeinfoway.smartstops.app.ui.utils.RetrofitUtils;
import com.creativeinfoway.smartstops.app.ui.utils.SmartStopsPref;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFavouriteWaypointfragment extends Fragment implements View.OnClickListener {

    AutoCompleteTextView autoTvProvince, autoTvPostal, autoTvwaypointCat, autoTvwaypointSubCat;
    EditText mEtName, mEtAddress, mEtEmail, mEtPhone;
    List<String> countrylist = new ArrayList<>();
    List<String> postallist = new ArrayList<>();
    List<String> provinceList = new ArrayList<>();
    List<String> waypointCatlist = new ArrayList<>();
    List<String> waypointSubCatlist = new ArrayList<>();
    String catId;
    LinearLayout btnSubmit;
    String latitude, longitude, address, userid;
    SmartStopsPref smartStopsPref;
    AutoCompleteTextView autoTvCountry;
    ImageView imgeback;

    ArrayAdapter<String> waypointCatAdapater, subCatAdapter, provinceAdapter, postalAdapter;
    GetAllCategories getAllCategories;

    GetSubCategory getSubCategory;
    GetProvince getProvince;
    GetPostalCode getPostalCode;

    ImageView img1, img2, img3;
    File[] files = new File[3];
    int imgPos;

    private TextView txtError;

    public AddFavouriteWaypointfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_favourite_waypoint, container, false);
        smartStopsPref = new SmartStopsPref(getContext());
        initIDs(v);
        parseIntentData();
        return v;
    }

    private void parseIntentData() {
        latitude = getArguments().getString("latitude");
        longitude = getArguments().getString("longitude");
        address = getArguments().getString("address");
        mEtAddress.setText(address);
//        userid = smartStopsPref.getString(Constant.USERID);
        userid = smartStopsPref.getString(Constant.NEWUSERID);

        waypointCatAdapater = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_layout, waypointCatlist);
        autoTvwaypointCat.setAdapter(waypointCatAdapater);

        autoTvwaypointCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catId = getAllCategories.getData().get(position).getId();

                initWaypointSubcatSpinner();
            }
        });

        subCatAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_layout, waypointSubCatlist);
        autoTvwaypointSubCat.setAdapter(subCatAdapter);

        provinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_layout, provinceList);
        autoTvProvince.setAdapter(provinceAdapter);

        postalAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_layout, postallist);
        autoTvPostal.setAdapter(postalAdapter);

    }

    private void initWayPointCategorySpinner() {
        waypointCatlist.clear();
        try {
            Call<ResponseBody> responseBodyCall = RetrofitUtils.callAPI().getcategory();
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();
                            getAllCategories = new Gson().fromJson(strResponse, GetAllCategories.class);
                            if (getAllCategories.getStatus().equals("1")) {

                                for (int i = 0; i < getAllCategories.getData().size(); i++) {
                                    waypointCatlist.add(getAllCategories.getData().get(i).getName());
                                }

                                autoTvwaypointCat.showDropDown();
                                waypointCatAdapater.notifyDataSetChanged();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void initWaypointSubcatSpinner() {
        waypointSubCatlist.clear();
        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().getSubCategories(catId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();
                        try {
                            String strResponse = responseBody.string();
                            getSubCategory = new Gson().fromJson(strResponse, GetSubCategory.class);
                            if (getSubCategory.getStatus().equals("1")) {
                                for (int i = 0; i < getSubCategory.getData().size(); i++) {
                                    waypointSubCatlist.add(getSubCategory.getData().get(i).getName());
                                }

                                autoTvwaypointSubCat.showDropDown();

                                subCatAdapter.notifyDataSetChanged();
                            } else {
                                autoTvwaypointSubCat.setText("No Sub-Category Available for selected Category");

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void initProvinceSpinner() {
        provinceList.clear();
        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().getProvince();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();

                        try {
                            String strResponse = responseBody.string();

                            getProvince = new Gson().fromJson(strResponse, GetProvince.class);
                            if (getProvince.getStatus().equals("1")) {

                                for (int i = 0; i < getProvince.getData().size(); i++) {
                                    provinceList.add(getProvince.getData().get(i).getState());
                                }

                                autoTvProvince.showDropDown();
                                provinceAdapter.notifyDataSetChanged();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }


    private void initIDs(View v) {

        txtError = v.findViewById(R.id.txtError);
        autoTvCountry = v.findViewById(R.id.auto_tv_select_country);
        imgeback = v.findViewById(R.id.iv_back);
        imgeback.setOnClickListener(this);
        autoTvProvince = v.findViewById(R.id.auto_tv_select_prince);
        autoTvPostal = v.findViewById(R.id.auto_tv_select_postal);
        autoTvwaypointCat = v.findViewById(R.id.auto_tv_select_waypoint_cat);
        autoTvwaypointSubCat = v.findViewById(R.id.auto_tv_select_waypoint_sub_cat);
        mEtName = v.findViewById(R.id.frag_add_fav_way_et_waypointname);
        mEtAddress = v.findViewById(R.id.frag_add_fav_way_et_address);
        mEtEmail = v.findViewById(R.id.frag_add_fav_way_et_email);
        mEtPhone = v.findViewById(R.id.frag_add_fav_way_et_phone);
        btnSubmit = v.findViewById(R.id.frag_add_fav_way_btn_submit);

        img1 = v.findViewById(R.id.img1);
        img2 = v.findViewById(R.id.img2);
        img3 = v.findViewById(R.id.img3);

        btnSubmit.setOnClickListener(this);
        autoTvCountry.setOnClickListener(this);
        autoTvProvince.setOnClickListener(this);
        autoTvPostal.setOnClickListener(this);
        autoTvwaypointCat.setOnClickListener(this);
        autoTvwaypointSubCat.setOnClickListener(this);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery(1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openGallery(2);
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openGallery(3);
            }
        });

    }

    private void openGallery(int i) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2000);
        } else {
            imgPos = i;

            Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1);
            }

//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
            // startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (imgPos == 1) {
                        files[0] = new File(uri.toString());
                        img1.setImageURI(uri);
                        img2.setVisibility(View.VISIBLE);
                    } else if (imgPos == 2) {
                        files[1] = new File(uri.toString());
                        img2.setImageURI(uri);
                        img3.setVisibility(View.VISIBLE);
                    } else if (imgPos == 3) {
                        files[2] = new File(uri.toString());
                        img3.setImageURI(uri);

                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Error ::: ", "App crashed");
                // Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initCountrySpinner() {
        countrylist.clear();
        countrylist.add("Canada");
        countrylist.add("US");
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_layout, countrylist);
        autoTvCountry.setAdapter(countryAdapter);
        autoTvCountry.showDropDown();
    }

    private void initPostalSpinner() {
        postallist.clear();
        try {
            Call<ResponseBody> call = RetrofitUtils.callAPI().getPostalCode();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBody responseBody = response.body();

                        try {
                            String strResponse = responseBody.string();
                            getPostalCode = new Gson().fromJson(strResponse, GetPostalCode.class);
                            if (getPostalCode.getStatus().equals("1")) {

                                for (int i = 0; i < getPostalCode.getData().size(); i++) {
                                    postallist.add(getPostalCode.getData().get(i).getZip_code());
                                }

                                autoTvPostal.showDropDown();

                                postalAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(), "" + getPostalCode.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnSubmit) {
            if (checkValidation()) {
                onSubmit();
            }
        } else if (v == autoTvCountry) {
            initCountrySpinner();
        } else if (v == autoTvPostal) {
            initPostalSpinner();
        } else if (v == autoTvProvince) {
            initProvinceSpinner();
        } else if (v == autoTvwaypointCat) {
            initWayPointCategorySpinner();
        } else if (v == autoTvwaypointSubCat) {
            initWaypointSubcatSpinner();
        } else if (v == imgeback) {
            getActivity().onBackPressed();
        }
    }

    private void onSubmit() {

        String name = mEtName.getText().toString();
        String address = mEtAddress.getText().toString();
        String email = mEtEmail.getText().toString();
        String phone = mEtPhone.getText().toString();
        String country = autoTvCountry.getText().toString();
        String province = autoTvProvince.getText().toString();
        String postal = autoTvPostal.getText().toString();
        String waypoint_cat = autoTvwaypointCat.getText().toString();
        String waypoint_sub_cat = autoTvwaypointSubCat.getText().toString();

        if (waypoint_sub_cat.equals("No Sub-Category Available for selected Category")) {
            waypoint_sub_cat = "";
        }
        HashMap<String, File> filesHashMap = new HashMap<>();

        String[] paramList = {"option_image_1", "option_image_2", "option_image_2"};

        try {
            Log.e("userID is : ", smartStopsPref.getString(Constant.NEWUSERID).trim());

            MultipartBody.Part filePart1 = null,filePart2= null,filePart3= null;

        MultipartBody.Builder multipartBody =   new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (int i = 0; i < files.length; i++) {

                if (files[i] != null) {
                    filePart1 = MultipartBody.Part.createFormData(paramList[i], files[i].getName(), RequestBody.create(MediaType.parse("image/*"), files[i]));
                    multipartBody.addPart(filePart1);
                }
            }

            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[filesHashMap.size()];

            for (int index = 0; index < filesHashMap.size(); index++) {
                RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), files[index].getAbsolutePath());
                surveyImagesParts[index] = MultipartBody.Part.createFormData(paramList[index], files[index].getName(), propertyImage);
            }


            RequestBody _waypoint_cat = RequestBody.create(MediaType.parse("text/plain"), waypoint_sub_cat);
            RequestBody _waypoint_sub_cat = RequestBody.create(MediaType.parse("text/plain"), waypoint_sub_cat);
            RequestBody _latitude = RequestBody.create(MediaType.parse("text/plain"), latitude);
            RequestBody _longitude = RequestBody.create(MediaType.parse("text/plain"), longitude);
            RequestBody _name = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody _address = RequestBody.create(MediaType.parse("text/plain"), address);
            RequestBody _postal = RequestBody.create(MediaType.parse("text/plain"), postal);
            RequestBody _country = RequestBody.create(MediaType.parse("text/plain"), country);
            RequestBody _province = RequestBody.create(MediaType.parse("text/plain"), waypoint_cat);
            RequestBody _email = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody _phone = RequestBody.create(MediaType.parse("text/plain"), phone);
            RequestBody _userID = RequestBody.create(MediaType.parse("text/plain"), smartStopsPref.getString(Constant.NEWUSERID));

            Call<ResponseBody> call = RetrofitUtils.callAPI().favouriteWayPoint(_waypoint_cat,_waypoint_sub_cat,_latitude,_longitude,_name,_address,_postal,_country,_province,_email,_phone,_userID, surveyImagesParts);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // {"status":"1","msg":"waypoint add successfully."}
                    ResponseBody responseBody = response.body();
                    try {
                        String strResponse = responseBody.string();
                        JSONObject jsonObject = new JSONObject(strResponse);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(getActivity(), String.valueOf(jsonObject.getString("msg")), Toast.LENGTH_SHORT).show();
                            Fragment fragment = new FavouriteWayPointFragment();
                            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameContainer, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getActivity(), "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    private boolean checkValidation() {
        if (mEtName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Waypoint Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAddress.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtEmail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPhone.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoTvCountry.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoTvProvince.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Province", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoTvPostal.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Postal Code", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoTvwaypointCat.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Waypoint Category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoTvwaypointSubCat.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Waypoint Sub-Category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (files[0] == null) {
            Toast.makeText(getActivity(), "Please Select at-least one Image ", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

