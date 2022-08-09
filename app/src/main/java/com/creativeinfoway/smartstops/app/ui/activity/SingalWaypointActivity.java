package com.creativeinfoway.smartstops.app.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;

import java.util.ArrayList;

public class SingalWaypointActivity extends AppCompatActivity {

    ArrayList<GetAllSubCategoryWaypoint.DataBean> allwaypoint = new ArrayList<>();
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singal_waypoint);

        String from = getIntent().getStringExtra("from");
        allwaypoint = getIntent().getParcelableArrayListExtra("markerinfoAllData");
        position = getIntent().getStringExtra("wayPointPosition");

        Fragment fragment = new MarkerInfoWindowFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putParcelable("markerinfo", com.creativeinfoway.smartstops.app.ui.utils.Constant.dataBeans);
        bundle.putParcelableArrayList("markerinfoAllData",allwaypoint);
        bundle.putString("SinglePosition",position);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
