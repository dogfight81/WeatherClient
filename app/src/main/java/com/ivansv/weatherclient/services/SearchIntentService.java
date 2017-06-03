package com.ivansv.weatherclient.services;

import android.app.IntentService;
import android.content.Intent;

import com.ivansv.weatherclient.Constants;
import com.ivansv.weatherclient.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchIntentService extends IntentService {


    public SearchIntentService() {
        super("searchService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String query = intent.getStringExtra(Constants.EXTRA_SEARCH_QUERY);
        final String base_api_url = getBaseContext().getResources().getString(R.string.base_api_url);
        final String api_key = getBaseContext().getResources().getString(R.string.api_key);
        String url = base_api_url + Constants.PATH_SEARCH + "?apikey=" + api_key + "&q=" + query;
//        Log.d(TAG, "onHandleIntent: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String json = response.body().string();
            Intent responseIntent = new Intent();
            responseIntent.setAction(Constants.ACTION_SEARCH_RESPONSE);
            responseIntent.putExtra(Constants.EXTRA_JSON, json);
            sendBroadcast(responseIntent);
//            Log.d(TAG, "json: " + json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
