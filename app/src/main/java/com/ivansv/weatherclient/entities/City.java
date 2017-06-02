package com.ivansv.weatherclient.entities;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class City extends RealmObject {
    @Required
    private String cityName;
    private String country;
    @Required
    private String locationKey;

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

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }
}
