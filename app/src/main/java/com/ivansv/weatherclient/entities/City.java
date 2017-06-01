package com.ivansv.weatherclient.entities;

import io.realm.RealmModel;

public class City implements RealmModel {
    private String cityName;
    private String country;
    private long locationKey;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(long locationKey) {
        this.locationKey = locationKey;
    }
}
