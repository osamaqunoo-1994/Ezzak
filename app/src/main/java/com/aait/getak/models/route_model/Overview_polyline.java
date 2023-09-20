package com.aait.getak.models.route_model;

import java.io.Serializable;

/**
 * Created by root on 4/13/18.
 */

public class Overview_polyline implements Serializable {
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return points;
    }
}
