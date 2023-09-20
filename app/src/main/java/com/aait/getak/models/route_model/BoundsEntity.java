package com.aait.getak.models.route_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BoundsEntity {
    @Expose
    @SerializedName("southwest")
    private SouthwestEntity southwest;
    @Expose
    @SerializedName("northeast")
    private NortheastEntity northeast;

    public SouthwestEntity getSouthwest() {
        return southwest;
    }

    public void setSouthwest(SouthwestEntity southwest) {
        this.southwest = southwest;
    }

    public NortheastEntity getNortheast() {
        return northeast;
    }

    public void setNortheast(NortheastEntity northeast) {
        this.northeast = northeast;
    }
}
