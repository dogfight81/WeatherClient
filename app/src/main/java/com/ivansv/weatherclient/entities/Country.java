package com.ivansv.weatherclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ivan on 6/1/2017.
 */

public class Country {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("LocalizedName")
    private String localizedName;

    public Country(String id, String localizedName) {
        this.id = id;
        this.localizedName = localizedName;
    }

    public Country() {
    }

    public String getId() {
        return id;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", localizedName='" + localizedName + '\'' +
                '}';
    }
}
