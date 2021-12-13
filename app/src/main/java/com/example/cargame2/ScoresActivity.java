package com.example.cargame2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class ScoresActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ListFragment listFragment;
    private mapFragment mapFragment;
    private TextView info;
    private GoogleMap map;
    private MaterialButton returnToMain;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_sheet);

        info = findViewById(R.id.info);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myDB");
        listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        listFragment.setCallBackList(callBack_List);
        getSupportFragmentManager().beginTransaction().add(R.id.list_frame, listFragment).commit();

        mapFragment = new mapFragment();
        mapFragment.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.map_frame, mapFragment).commit();

        returnToMain = findViewById(R.id.highscore_BTN_returnToMain);
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoresActivity.this, MainActivity.class));
            }
        });
    }
    CallBack_List callBack_List = new CallBack_List() {
        @Override
        public void setMainTitle(String str) {
            info.setText(str);
        }

        @Override
        public void rowSelected(int i) {
            String fromJSON = MSPv3.getInstance(getApplicationContext()).getStringSP("MY_DB","");
            GameDB myDB = new Gson().fromJson(fromJSON,GameDB.class);
            if(myDB !=null) {
                if (i < myDB.getRecords().size()) {
                    Record record = myDB.getRecords().get(i);
                    callBack_map.locationSelected(record);
                }
            }
        }
    };


    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void mapClicked(double lat, double lon) {
        }

        @Override
        public void locationSelected(Record record) {
            mapFragment.onClicked(record);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
        map.addMarker(new MarkerOptions().position(mark).title("I am here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
    }
}
