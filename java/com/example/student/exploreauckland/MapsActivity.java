package com.example.student.exploreauckland;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.student.exploreauckland.database.AttractionDAOImplementation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private AttractionDAOImplementation mAttractionDAOImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setMyLocationEnabled(true);
        UiSettings uiSettings = mapFragment.getMap().getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mAttractionDAOImplementation = new AttractionDAOImplementation(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        int id = intent.getIntExtra("id", 1);
        ArrayList<Attraction> attractions = new ArrayList<Attraction>();
        try {
            mAttractionDAOImplementation.open();
            if(type.equals("place")){
                attractions.add(mAttractionDAOImplementation.findById(id));
                //Toast.makeText(this, a.getName(), Toast.LENGTH_LONG).show();
            }else if(type.equals("category")){
                attractions = mAttractionDAOImplementation.findByCategory(id);
            }else if(type.equals("favorite")){
                attractions = mAttractionDAOImplementation.findAllFavorite();
            }
            mAttractionDAOImplementation.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int count = 0;
        double sumLat = 0;
        double sumLon = 0;

        for(Attraction at: attractions){
            LatLng position = new LatLng(at.getLatitude(),at.getLongitude());
            map.addMarker(new MarkerOptions().position(position).title(at.getName()));
            sumLat += at.getLatitude();
            sumLon += at.getLongitude();
            count++;
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(sumLat / count, sumLon / count)));

        //Toast.makeText(this, "Lat :" + sumLat/count + " Long: " + sumLon/count, Toast.LENGTH_LONG).show();

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
        map.animateCamera(zoom);
    }
}