package com.aait.getak.models.route_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NortheastEntity {
    @Expose
    @SerializedName("lng")
    private double lng;
    @Expose
    @SerializedName("lat")
    private double lat;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
