package com.ivansv.weatherclient;

public abstract class Constants {

    public static final String PATH_SEARCH = "/locations/v1/cities/autocomplete";
    public static final String PATH_WEATHER = "/currentconditions/v1/";

    public static final String ACTION_SEARCH_RESPONSE = "response";
    public static final String ACTION_WEATHER_RESPONSE = "weather_response";

    public static final String EXTRA_SEARCH_QUERY = "search";
    public static final String EXTRA_JSON = "json";
    public static final String EXTRA_CITY_NAME = "cityName";
    public static final String EXTRA_COUNTRY = "country";
    public static final String EXTRA_LOCATION_KEY = "location";
}
