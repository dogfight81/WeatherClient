package com.ivansv.weatherclient.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivansv.weatherclient.Constants;
import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.activities.MainActivity;
import com.ivansv.weatherclient.adapters.CityApiAdapter;
import com.ivansv.weatherclient.entities.CityApi;
import com.ivansv.weatherclient.entities.CityRealm;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;
import com.ivansv.weatherclient.services.SearchIntentService;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddDialogFragment extends DialogFragment {

    private EditText etSearchQuery;
    private RecyclerView rvCities;
    private CityApiAdapter cityApiAdapter;
    private ProgressBar pbLoading;

    private Realm realm;

    private View.OnClickListener onBtnSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pbLoading.setVisibility(View.VISIBLE);
            rvCities.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getActivity(), SearchIntentService.class);
            intent.putExtra(Constants.EXTRA_SEARCH_QUERY, etSearchQuery.getText().toString());
            getActivity().startService(intent);
        }
    };

    private OnRecyclerItemClickListener onItemClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onItemClick(int position) {
            RealmResults<CityRealm> results = realm.where(CityRealm.class)
                    .equalTo("locationKey", cityApiAdapter.getCity(position).getKey()).findAll();

            if (results.isEmpty()) {
                realm.beginTransaction();
                CityRealm city = realm.createObject(CityRealm.class);
                city.setCityName(cityApiAdapter.getCity(position).getName());
                city.setCountry(cityApiAdapter.getCity(position).getCountry().getLocalizedName());
                city.setLocationKey(cityApiAdapter.getCity(position).getKey());
                realm.commitTransaction();
                ((MainActivity)getActivity()).upateList();
                dismiss();
            } else {
                Toast.makeText(getContext(), R.string.msg_already_added, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private BroadcastReceiver searchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_SEARCH_RESPONSE)) {
                String json = intent.getStringExtra(Constants.EXTRA_JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    CityApi[] cities = objectMapper.readValue(json, CityApi[].class);
                    if (cities.length == 0) {
                        Toast.makeText(getContext(), R.string.msg_nothing_found, Toast.LENGTH_SHORT).show();
                    }
                    cityApiAdapter.setListData(cities);
                    rvCities.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.msg_err_parsing, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_search).setOnClickListener(onBtnSearchClickListener);
        rvCities = (RecyclerView) view.findViewById(R.id.rv_cities);
        etSearchQuery = (EditText) view.findViewById(R.id.et_search_query);
        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        cityApiAdapter = new CityApiAdapter();
        cityApiAdapter.setOnItemClickListener(onItemClickListener);
        rvCities.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCities.setAdapter(cityApiAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEARCH_RESPONSE);
        getActivity().registerReceiver(searchReceiver, intentFilter);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(searchReceiver);
        realm.close();

    }
}
