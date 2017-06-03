package com.ivansv.weatherclient.services;

import android.app.IntentService;
import android.content.Intent;

import com.ivansv.weatherclient.Constants;
import com.ivansv.weatherclient.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FetchWeatherService extends IntentService {

    public FetchWeatherService() {
        super("FetchWeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String locationKey = intent.getStringExtra(Constants.EXTRA_LOCATION_KEY);
        final String base_api_url = getBaseContext().getResources().getString(R.string.base_api_url);
        final String api_key = getBaseContext().getResources().getString(R.string.api_key);
        String url = base_api_url + Constants.PATH_WEATHER + locationKey + "?apikey=" + api_key;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            Intent responseIntent = new Intent();
            responseIntent.setAction(Constants.ACTION_WEATHER_RESPONSE);
            responseIntent.putExtra(Constants.EXTRA_JSON, response.body().string());
            sendBroadcast(responseIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
