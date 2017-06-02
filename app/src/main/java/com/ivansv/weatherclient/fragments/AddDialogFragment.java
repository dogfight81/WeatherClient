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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.adapters.CityApiAdapter;
import com.ivansv.weatherclient.entities.CityApi;
import com.ivansv.weatherclient.services.SearchIntentService;

import java.io.IOException;

public class AddDialogFragment extends DialogFragment {

    private static final String TAG = "AddDialogFragment";
    private EditText etSearchQuery;
    private RecyclerView mRecyclerView;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(country)) {
//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//                City newCity = realm.createObject(City.class);
//                newCity.setCityName(cityName);
//                newCity.setCountry(country);
//                newCity.setLocationKey(String.valueOf(SystemClock.currentThreadTimeMillis()%200));
//                realm.commitTransaction();
//                realm.close();
//                dismiss();
//            } else {
//                Toast.makeText(getContext(), "empty", Toast.LENGTH_SHORT).show();
//            }
        }
    };

    private BroadcastReceiver autoCopleteSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SearchIntentService.ACTION_RESPONSE.equals(intent.getAction())) {
                String data = intent.getStringExtra(SearchIntentService.EXTRA_RESPONSE_JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    CityApi[] cities = objectMapper.readValue(data, CityApi[].class);
                    CityApiAdapter adapter = new CityApiAdapter(cities);
                    mRecyclerView.setAdapter(adapter);
                    Log.d(TAG, "CITIES = " + cities.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, data);
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
        view.findViewById(R.id.btn_add).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchIntentService searchService = new SearchIntentService();
                Intent intent = new Intent(getActivity(), SearchIntentService.class);
                intent.putExtra(SearchIntentService.EXTRA_SEARCH_QUERY, etSearchQuery.getText().toString());
                getActivity().startService(intent);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        etSearchQuery = (EditText) view.findViewById(R.id.et_search_query);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(SearchIntentService.ACTION_RESPONSE);
        getActivity().registerReceiver(autoCopleteSearchReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(autoCopleteSearchReceiver);
    }
}
