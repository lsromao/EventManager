package com.uni.lu.eventmanager.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uni.lu.eventmanager.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_event);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.editLocation);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                android.util.Log.i("OnMapClick", "Horary!");
            }
        });
//        mMap = googleMap;
//        LatLng Luxembourg = new LatLng(21, 57);
//        mMap.addMarker(new
//                MarkerOptions().position(Luxembourg).title("Luxembourg"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Luxembourg));
//    }
//
//    @Override
//    public void onMapClick(LatLng latLng) {
//        {
//            android.util.Log.i("onMapClick", "Horary!");
//
//        }
    }
}