package com.creativeinfoway.smartstops.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.fragment.MarkerInfoWindowFragment;
import com.creativeinfoway.smartstops.app.ui.models.GetFavouriteWaypoint;
import com.google.gson.Gson;

public class SubFavouriteWayPointActivity extends AppCompatActivity {

    String type,result;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_favourite_way_point);

        type = getIntent().getStringExtra("type");
        result = getIntent().getStringExtra("result");
        position = getIntent().getIntExtra("position",0);

        if(type.equals("MarkerInfoWindowFragment")){

            GetFavouriteWaypoint getFavouriteWaypoint = new Gson().fromJson(result, GetFavouriteWaypoint.class);

            Fragment fragment = new MarkerInfoWindowFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("from", "2");
            bundle.putSerializable("markerinfo_from_fav_waypoint_map", getFavouriteWaypoint.getData().get(position));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameSubContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if(type.equals("AddWayPointFrgment")){
            /*Fragment fragment = new AddWayPointFrgment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameSubContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            Intent intent = new Intent(SubFavouriteWayPointActivity.this,AddWayPointActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
