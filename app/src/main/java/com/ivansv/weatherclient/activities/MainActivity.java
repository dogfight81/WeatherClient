package com.ivansv.weatherclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

import com.ivansv.weatherclient.Constants;
import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.adapters.CitiesAdapter;
import com.ivansv.weatherclient.entities.CityRealm;
import com.ivansv.weatherclient.fragments.AddDialogFragment;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {




    private CitiesAdapter citiesAdapter = new CitiesAdapter();
    private Realm realm;
    private RealmResults<CityRealm> realmResults;

    private RealmChangeListener<RealmResults<CityRealm>> realmChangeListener = new RealmChangeListener<RealmResults<CityRealm>>() {
        @Override
        public void onChange(RealmResults<CityRealm> element) {
            citiesAdapter.setListData(element);
            realmResults.removeChangeListener(this);
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
        public void onItemClick(int position) {
            Intent intent = new Intent(MainActivity.this, CityDetailActivity.class);
            CityRealm city = citiesAdapter.getCity(position);
            intent.putExtra(Constants.EXTRA_CITY_NAME, city.getCityName());
            intent.putExtra(Constants.EXTRA_COUNTRY, city.getCountry());
            intent.putExtra(Constants.EXTRA_LOCATION_KEY, city.getLocationKey());
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
    protected void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        upateList();
    }

    @Override
    protected void onStop() {
        realm.close();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                realmResults = realm.where(CityRealm.class).contains("searchName", newText.toLowerCase()).findAllAsync();
                realmResults.addChangeListener(realmChangeListener);
                citiesAdapter.setQueryText(newText.toLowerCase());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void upateList() {
        realmResults = realm.where(CityRealm.class).findAllAsync();
        realmResults.addChangeListener(realmChangeListener);
    }

}
