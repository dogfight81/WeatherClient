package com.ivansv.weatherclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ivan on 6/1/2017.
 */

public class CityApi {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("LocalizedName")
    private String name;
    @JsonProperty("Country")
    private Country country;

    public CityApi() {
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "CityApi{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", country=" + country +
                '}';
    }
}
