package com.ivansv.weatherclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.adapters.CitiesAdapter;
import com.ivansv.weatherclient.entities.City;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<City>>, View.OnClickListener {

    private CitiesAdapter citiesAdapter = new CitiesAdapter();
    private Realm realm;
    private RealmResults<City> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvCitiesList = (RecyclerView) findViewById(R.id.rv_cities_list);
        rvCitiesList.setLayoutManager(new LinearLayoutManager(this));
        rvCitiesList.setAdapter(citiesAdapter);

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(City.class).findAllAsync();
        realmResults.addChangeListener(this);

        findViewById(R.id.fab_add).setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        realm.close();
        realmResults.removeChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onChange(RealmResults<City> element) {
        citiesAdapter.setListData(element);
    }

    @Override
    public void onClick(View v) {



    }
}
