package com.aait.getak.models.route_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodedWaypointsEntity {
    @Expose
    @SerializedName("types")
    private List<String> types;
    @Expose
    @SerializedName("place_id")
    private String placeId;
    @Expose
    @SerializedName("geocoder_status")
    private String geocoderStatus;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }
}
