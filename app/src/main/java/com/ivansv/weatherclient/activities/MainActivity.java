package com.ivansv.weatherclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.adapters.CitiesAdapter;
import com.ivansv.weatherclient.entities.City;
import com.ivansv.weatherclient.fragments.AddDialogFragment;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CITY_NAME = "cityName";
    public static final String EXTRA_COUNTRY = "country";
    public static final String EXTRA_LOCATION_KEY = "location";

    private CitiesAdapter citiesAdapter = new CitiesAdapter();
    private Realm realm;
    private RealmResults<City> realmResults;

    private RealmChangeListener<RealmResults<City>> realmChangeListener = new RealmChangeListener<RealmResults<City>>() {
        @Override
        public void onChange(RealmResults<City> element) {
            citiesAdapter.setListData(element);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_add:
                    (new AddDialogFragment()).show(getSupportFragmentManager(), "addDialog");
                    break;
            }
        }
    };

    private OnRecyclerItemClickListener onRecyclerItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(City city) {
            Intent intent = new Intent(MainActivity.this, CityDetailActivity.class);
            intent.putExtra(EXTRA_CITY_NAME, city.getCityName());
            intent.putExtra(EXTRA_COUNTRY, city.getCountry());
            intent.putExtra(EXTRA_LOCATION_KEY, city.getLocationKey());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvCitiesList = (RecyclerView) findViewById(R.id.rv_cities_list);
        rvCitiesList.setLayoutManager(new LinearLayoutManager(this));
        rvCitiesList.setAdapter(citiesAdapter);

        findViewById(R.id.fab_add).setOnClickListener(onClickListener);
        citiesAdapter.setOnItemClickListener(onRecyclerItemClickListener);

    }

    @Override
    protected void onResume() {
        realm = Realm.getDefaultInstance();
        realmResults = realm.where(City.class).findAllAsync();
        realmResults.addChangeListener(realmChangeListener);
        super.onResume();
    }

    @Override
    protected void onPause() {
        realmResults.removeChangeListener(realmChangeListener);
        realm.close();
        super.onPause();
    }


}
