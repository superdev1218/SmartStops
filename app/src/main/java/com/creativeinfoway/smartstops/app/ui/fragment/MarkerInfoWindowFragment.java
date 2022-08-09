package com.creativeinfoway.smartstops.app.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.Constant;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.activity.NavigationLauncherActivity;
import com.creativeinfoway.smartstops.app.ui.adapter.ImagePagerAdapter;
import com.creativeinfoway.smartstops.app.ui.adapter.SliderAdapterExample;
import com.creativeinfoway.smartstops.app.ui.models.GetFavouriteWaypoint;
import com.creativeinfoway.smartstops.app.ui.models.WayPointByCategory;
import com.google.android.gms.maps.model.LatLng;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.Serializable;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.checkSinglePermission;
import static com.creativeinfoway.smartstops.app.ui.activity.HomeActivity.startCall911;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkerInfoWindowFragment extends Fragment implements View.OnClickListener {
    LinearLayout llCall, llDirections;
    ImageView ivBack;
    TextView tvName, tvAddress, tvPhonenum, tvEmail,txtWayPointNumber;
    ImageView imgBg;

    LatLng latLng;

    String waypointimage;

    ArrayList<GetAllSubCategoryWaypoint.DataBean> getAllWayPoints;
    String from;
    RelativeLayout first_layout;
    private Activity activity;
    private ArrayList<String> imageList = new ArrayList<>();
    private SliderView sliderView;
    private Parcelable getAllLatLongPoints2;
    private String position;
    private LinearLayout layout_1,layout_2,layout_3;

    public MarkerInfoWindowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View v = inflater.inflate(R.layout.fragment_marker_info_window, container, false);

        initids(v);

        from = getArguments().getString("from");
        if (from.equals("0")) {
            intentDataFromHomeMain();
        } else if (from.equals("2")) {
            intentDataFromFavWayPointFragment();

        } else {
            intentDataFromCategoryMapFragment();
        }

        return v;

    }

    private void intentDataFromFavWayPointFragment() {
        Serializable getFavouriteWaypoint = getArguments().getSerializable("markerinfo_from_fav_waypoint_map");

        waypointimage = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getWaypoint_image();

        Constant.waypointName = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getName();
        Constant.wayPointID = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getWaypoint_id();

        tvName.setText(((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getName().isEmpty() ? "Not Available" : ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getName());

        String phone = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getPhone_number();
        String address = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getAddress();
        String email = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getEmail();

        if(phone.isEmpty()){
            layout_3.setVisibility(View.GONE);
        }
        if(address.isEmpty()){
            layout_1.setVisibility(View.GONE);
        }
        if(email.isEmpty()){
            layout_2.setVisibility(View.GONE);
        }

        tvPhonenum.setText(phone.isEmpty() ? "Not Available" :((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getPhone_number());
        tvAddress.setText(String.valueOf((address.isEmpty() ? "Not Available" :((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getAddress())));
        tvEmail.setText(String.valueOf(email.isEmpty() ? "Not Available" :((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getEmail()));
        Glide.with(getContext()).load(((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getWaypoint_image()).placeholder(R.drawable.smstp).into(imgBg);
        latLng = new LatLng(Double.parseDouble(((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getLatitude_decimal()), Double.parseDouble(((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getLongitude_decimal()));
        txtWayPointNumber.setText(String.valueOf((((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getWaypoint_id().isEmpty() ? "Not Available" :((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getWaypoint_id())));

        imageList.clear();
        String i1 = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getOption_image_1();
        String i2 = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getOption_image_2();
        String i3 = ((GetFavouriteWaypoint.DataBean) getFavouriteWaypoint).getOption_image_3();
        if(i1!=null && !i1.equals("")){
            imageList.add(i1);
        }if(i2 !=null && !i2.equals("")){
            imageList.add(i2);
        }if(i3 !=null && !i3.equals("")){
            imageList.add(i3);
        }

        if(imageList.size() == 0){
            first_layout.setVisibility(View.GONE);
        }

        SliderAdapterExample adapterExample = new SliderAdapterExample(activity);
        adapterExample.addItem(i1);
        adapterExample.addItem(i2);
        adapterExample.addItem(i3);

        sliderView.setSliderAdapter(adapterExample);

    }

    private void intentDataFromCategoryMapFragment() {
        Serializable wayPointByCategory = getArguments().getSerializable("markerinfo_from_cat_map");

        waypointimage = ((WayPointByCategory.DataBean) wayPointByCategory).getWaypoint_image();

        String phone = ((WayPointByCategory.DataBean) wayPointByCategory).getPhone_number();
        String address = ((WayPointByCategory.DataBean) wayPointByCategory).getAddress();
        String email = ((WayPointByCategory.DataBean) wayPointByCategory).getEmail();

        if(phone.isEmpty()){
            layout_3.setVisibility(View.GONE);
        }
        if(address.isEmpty()){
            layout_1.setVisibility(View.GONE);
        }
        if(email.isEmpty()){
            layout_2.setVisibility(View.GONE);
        }

        tvName.setText(String.valueOf(((WayPointByCategory.DataBean) wayPointByCategory).getName().isEmpty() ? "Not Available" :((WayPointByCategory.DataBean) wayPointByCategory).getName()));
        tvPhonenum.setText(String.valueOf(((WayPointByCategory.DataBean) wayPointByCategory).getPhone_number().isEmpty() ? "Not Available" :((WayPointByCategory.DataBean) wayPointByCategory).getPhone_number()));
        tvAddress.setText(String.valueOf((((WayPointByCategory.DataBean) wayPointByCategory).getAddress().isEmpty() ? "Not Available" :((WayPointByCategory.DataBean) wayPointByCategory).getAddress())));
        tvEmail.setText(String.valueOf(((WayPointByCategory.DataBean) wayPointByCategory).getEmail().isEmpty() ? "Not Available" :((WayPointByCategory.DataBean) wayPointByCategory).getEmail()));
        Glide.with(getContext()).load(((WayPointByCategory.DataBean) wayPointByCategory).getWaypoint_image()).placeholder(R.drawable.smstp).into(imgBg);
        latLng = new LatLng(Double.parseDouble(((WayPointByCategory.DataBean) wayPointByCategory).getLatitude_decimal()), Double.parseDouble(((WayPointByCategory.DataBean) wayPointByCategory).getLongitude_decimal()));
        txtWayPointNumber.setText(String.valueOf((((WayPointByCategory.DataBean) wayPointByCategory).getWaypoint_id().isEmpty() ? "Not Available" :((WayPointByCategory.DataBean) wayPointByCategory).getWaypoint_id())));

        imageList.clear();
        String i1 = ((WayPointByCategory.DataBean) wayPointByCategory).getOption_image_1();
        String i2 = ((WayPointByCategory.DataBean) wayPointByCategory).getOption_image_2();
        String i3 = ((WayPointByCategory.DataBean) wayPointByCategory).getOption_image_3();
        if(!i1.equals("")){
            imageList.add(i1);
        }if(!i2.equals("")){
            imageList.add(i2);
        }if(!i3.equals("")){
            imageList.add(i3);
        }

        if(imageList.size() == 0){
            first_layout.setVisibility(View.GONE);
        }

        SliderAdapterExample adapterExample = new SliderAdapterExample(activity);
        adapterExample.addItem(i1);
        adapterExample.addItem(i2);
        adapterExample.addItem(i3);

        sliderView.setSliderAdapter(adapterExample);

    }

    private void intentDataFromHomeMain() {
        Parcelable getAllLatLongPoints = getArguments().getParcelable(("markerinfo"));

        getAllLatLongPoints2 = getAllLatLongPoints;
        position = getArguments().getString("SinglePosition");

        getAllWayPoints = getArguments().getParcelableArrayList(("markerinfoAllData"));

        waypointimage = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getWaypoint_image();

        String phone = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getPhone_number();
        String address = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getAddress();
        String email = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getEmail();

        if(phone.isEmpty()){
            layout_3.setVisibility(View.GONE);
        }
        if(address.isEmpty()){
            layout_1.setVisibility(View.GONE);
        }
        if(email.isEmpty()){
            layout_2.setVisibility(View.GONE);
        }


        tvName.setText(String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getName().isEmpty() ? "Not Available" :((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getName()));
        tvPhonenum.setText(String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getPhone_number().isEmpty() ? "Not Available" :((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getPhone_number()));
        tvAddress.setText(String.valueOf((((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getAddress()).isEmpty() ? "Not Available" :((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getAddress()));
        tvEmail.setText(String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getEmail().isEmpty() ? "Not Available" :((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getEmail()));
        Glide.with(getContext()).load(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getWaypoint_image()).placeholder(R.drawable.smstp).into(imgBg);
        latLng = new LatLng(Double.parseDouble(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getLatitude_decimal()), Double.parseDouble(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getLongitude_decimal()));
        txtWayPointNumber.setText(String.valueOf((((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getWaypoint_id()).isEmpty() ? "Not Available" :((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getWaypoint_id()));

        imageList.clear();
        String i1 = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getOption_image_1();
        String i2 = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getOption_image_2();
        String i3 = ((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints).getOption_image_3();
        if(i1 != null) {
            if (!i1.equals("")) {
                imageList.add(i1);
            }
        }if(i2 != null){
            if(!i2.equals("")){
                imageList.add(i2);
            }
        }if(i3 != null){
            if(!i3.equals("")){
                imageList.add(i3);
            }
        }

        if(imageList.size() == 0){
            first_layout.setVisibility(View.GONE);
        }

        SliderAdapterExample adapterExample = new SliderAdapterExample(activity);
        adapterExample.addItem(i1);
        adapterExample.addItem(i2);
        adapterExample.addItem(i3);

        sliderView.setSliderAdapter(adapterExample);


    }

    private void initids(View view) {
        ivBack = view.findViewById(R.id.act_info_window_iv_back);
        llCall = view.findViewById(R.id.act_infow_window_call);
        llDirections = view.findViewById(R.id.act_infowindow_directions);
        imgBg = view.findViewById(R.id.act_marker_info_bg);
        txtWayPointNumber = view.findViewById(R.id.txtWayPointNumber);
        tvName = view.findViewById(R.id.act_marker_info_name);
        tvAddress = view.findViewById(R.id.act_marker_info_address);
        tvEmail = view.findViewById(R.id.act_marker_info_email);
        tvPhonenum = view.findViewById(R.id.act_marker_info_phone);
        first_layout = view.findViewById(R.id.first_layout2);
        sliderView = view.findViewById(R.id.imageSlider);
        layout_1 = view.findViewById(R.id.layout_1);
        layout_2 = view.findViewById(R.id.layout_2);
        layout_3 = view.findViewById(R.id.layout_3);

        llCall.setOnClickListener(this);
        llDirections.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == ivBack) {

            if(from.equals("0")){
                getActivity().finish();

            }else {
                getActivity().onBackPressed();
            }
        }
        if (v == llCall) {
            if (checkSinglePermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                startCall911(getContext());
            }
        }
        if (v == llDirections) {

           /* Fragment fragment = new GetDirectionMapFragment();
            FragmentManager fragmentManager = ((HomeActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("from","MarkerInfoWindowFragment");
            bundle.putString("latitude",String.valueOf(latLng.latitude));
            bundle.putString("longtitude",String.valueOf(latLng.longitude));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.content_home, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
//            Intent intent = new Intent(getContext(), SearchResultActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            startActivity(intent);

            if(!com.creativeinfoway.smartstops.app.ui.utils.Constant.SINGLE_WAYPOINT) {
                Intent intent = new Intent(getActivity(), NavigationLauncherActivity.class);
                intent.putExtra("from", "MarkerInfoWindowFragment");
                intent.putExtra("latitude", String.valueOf(latLng.latitude));
                intent.putExtra("longtitude", String.valueOf(latLng.longitude));
                intent.putExtra("waypointimage", waypointimage);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putParcelableArrayListExtra("markerinfoAllData", getAllWayPoints);
                activity.startActivity(intent);
            }else {
                Intent intent = new Intent(activity, NavigationLauncherActivity.class);
                intent.putExtra("from", "SubCategoryActivity");
                intent.putExtra("latitude", String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints2).getLatitude_decimal()));
                intent.putExtra("longtitude", String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints2).getLongitude_decimal()));
                intent.putExtra("wayPointPosition", "" + position);
                intent.putExtra("WaypointOnlyDisplay", "true");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("WaypointIdentify", String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints2).getWaypoint_id()));
                intent.putExtra("waypointiconVisible", String.valueOf(((GetAllSubCategoryWaypoint.DataBean) getAllLatLongPoints2).getMapbox_icon_visibility()));
                intent.putParcelableArrayListExtra("allwaypoint", getAllWayPoints);
                activity.startActivity(intent);
            }

        }
    }
}
