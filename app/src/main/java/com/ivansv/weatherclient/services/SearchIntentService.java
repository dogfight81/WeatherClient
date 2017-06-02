package com.ivansv.weatherclient.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ivansv.weatherclient.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchIntentService extends IntentService {

    private static final String TAG = "SearchIntentService";

    public static final String ACTION_RESPONSE = "response";
    private static final String ACTION_BAZ = "com.ivansv.weatherclient.action.BAZ";

    public static final String EXTRA_SEARCH_QUERY = "search";
    public static final String EXTRA_RESPONSE_JSON = "json";
    private final String API_KEY = "S2t8gIhdiW108cz3eFFJ0NKpJdQSdZkT";
    private final String BASE_URL = "/locations/v1/cities/autocomplete";

    private String baseApiUrl;

    public SearchIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String query = intent.getStringExtra(EXTRA_SEARCH_QUERY);
        baseApiUrl = getBaseContext().getResources().getString(R.string.base_api_url);
        String url = baseApiUrl + BASE_URL + "?apikey=" + API_KEY + "&q=" + query;
        Log.d(TAG, "onHandleIntent: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            Intent responseIntent = new Intent();
            responseIntent.setAction(ACTION_RESPONSE);
            responseIntent.putExtra(EXTRA_RESPONSE_JSON, response.body().string());
            Log.d(TAG, "onHandleIntent: +" );
            sendBroadcast(responseIntent);
        } catch (IOException e) {
            Log.d(TAG, "--");
            e.printStackTrace();
        }

    }
}
