package com.ivansv.weatherclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivansv.weatherclient.Constants;
import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.entities.CityRealm;
import com.ivansv.weatherclient.entities.CurrentCondition;
import com.ivansv.weatherclient.services.FetchWeatherService;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

public class CityDetailActivity extends AppCompatActivity {


    private TextView tvCurrentCondition;
    private Button btnDetails;
    private ProgressBar pbLoading;

    private String locationKey;
    private String mobileLink;


    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_WEATHER_RESPONSE)) {

                pbLoading.setVisibility(View.INVISIBLE);
                String json = intent.getStringExtra(Constants.EXTRA_JSON);
//                Log.d(TAG, "json: " + json);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    CurrentCondition[] condition = objectMapper.readValue(json, CurrentCondition[].class);
                    tvCurrentCondition.setText(String.format("%s, %s \u2103 ", condition[0].getCondition(), condition[0].getTemperature().getMetric().getValue()));
                    mobileLink = condition[0].getLink();
                    btnDetails.setVisibility(View.VISIBLE);
//                    Log.d(TAG, condition[0].toString());
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), R.string.msg_err_parsing, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    };

    private View.OnClickListener onBtnDetailsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mobileLink));
            startActivity(openLinkIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        String cityName = getIntent().getStringExtra(Constants.EXTRA_CITY_NAME);
        String country = getIntent().getStringExtra(Constants.EXTRA_COUNTRY);
        locationKey = getIntent().getStringExtra(Constants.EXTRA_LOCATION_KEY);

        TextView tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvCurrentCondition = (TextView) findViewById(R.id.tv_current_condition);
        btnDetails = (Button) findViewById(R.id.btn_details);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        tvCityName.setText(String.format("%s, %s", cityName, country));

        btnDetails.setOnClickListener(onBtnDetailsClickListener);

        fetchData();

    }

    @Override
    protected void onStart() {
        registerReceiver(weatherReceiver, new IntentFilter(Constants.ACTION_WEATHER_RESPONSE));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(weatherReceiver);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteObject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteObject() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.tittle_deleting);
        dialogBuilder.setMessage(R.string.msg_deleting_confirmation);
        dialogBuilder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<CityRealm> results =
                        realm.where(CityRealm.class)
                                .equalTo("locationKey", getIntent().getStringExtra(Constants.EXTRA_LOCATION_KEY))
                                .findAll();
                realm.beginTransaction();
                results.deleteFirstFromRealm();
                realm.commitTransaction();
                realm.close();
                finish();
            }
        });
        dialogBuilder.setNegativeButton(R.string.btn_no, null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void fetchData() {
        pbLoading.setVisibility(View.VISIBLE);
        btnDetails.setVisibility(View.INVISIBLE);
        Intent weatherServiceIntent = new Intent(this, FetchWeatherService.class);
        weatherServiceIntent.putExtra(Constants.EXTRA_LOCATION_KEY, locationKey);
        startService(weatherServiceIntent);
    }

}
