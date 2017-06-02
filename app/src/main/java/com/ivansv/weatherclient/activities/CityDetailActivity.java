package com.ivansv.weatherclient.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.entities.City;

import io.realm.Realm;
import io.realm.RealmResults;

public class CityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        ((TextView)findViewById(R.id.tv_city_name)).setText(getIntent().getStringExtra(MainActivity.EXTRA_CITY_NAME));
        ((TextView)findViewById(R.id.tv_country)).setText(getIntent().getStringExtra(MainActivity.EXTRA_COUNTRY));
        ((TextView)findViewById(R.id.tv_location_key)).setText(getIntent().getStringExtra(MainActivity.EXTRA_LOCATION_KEY));

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                deleteObject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteObject() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("deleting city");
        dialogBuilder.setMessage("Are you sure you want to remove this city from DB?");
        dialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<City> results =
                        realm.where(City.class)
                                .equalTo("locationKey", getIntent().getStringExtra(MainActivity.EXTRA_LOCATION_KEY))
                                .findAll();
                realm.beginTransaction();
                results.deleteFirstFromRealm();
                realm.commitTransaction();
                realm.close();
                finish();
            }
        });
        dialogBuilder.setNegativeButton("no", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
