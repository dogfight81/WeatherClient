package com.ivansv.weatherclient.entities;

import com.fasterxml.jackson.annotation.JsonProperty;



public class CurrentCondition {

    @JsonProperty("WeatherText")
    private String condition;

    @JsonProperty("MobileLink")
    private String link;

    @JsonProperty("Temperature")
    private Temperature temperature;

    public String getCondition() {
        return condition;
    }

    public String getLink() {
        return link;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public CurrentCondition() {
    }

    public CurrentCondition(String condition, String link, Temperature temperature) {
        this.condition = condition;
        this.link = link;
        this.temperature = temperature;
    }

    public static class Temperature {

        @JsonProperty("Metric")
        private Metric metric;

        public Temperature() {
        }

        public Temperature(Metric metric) {
            this.metric = metric;
        }

        public Metric getMetric() {
            return metric;
        }
    }

    public static class Metric {

        @JsonProperty("Value")
        private String value;
        @JsonProperty("Unit")
        private String unit;
        @JsonProperty("UnitType")
        private String unitType;

        public Metric() {
        }

        public Metric(String value, String unit, String unitType) {
            this.value = value;
            this.unit = unit;
            this.unitType = unitType;
        }

        public String getValue() {
            return value;
        }
    }



    @Override
    public String toString() {
        return "cc: {condition: " + condition + "; link: " + link + "; temp: " + getTemperature().getMetric().getValue() + "}";
    }
}
