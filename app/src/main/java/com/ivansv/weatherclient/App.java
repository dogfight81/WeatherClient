package com.ivansv.weatherclient;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cities")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
