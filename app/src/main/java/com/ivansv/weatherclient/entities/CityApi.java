package com.ivansv.weatherclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

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
                ", tvCountry=" + country +
                '}';
    }

    public static class Country {

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
}
