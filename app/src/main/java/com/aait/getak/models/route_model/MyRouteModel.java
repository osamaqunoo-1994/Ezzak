package com.aait.getak.models.route_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 4/13/18.
 */

public class MyRouteModel implements Serializable {


    private List<GeocodedWaypointsEntity> geocoded_waypoints;

    private String status;

    private List<Routes> routes;

    public List<GeocodedWaypointsEntity> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<GeocodedWaypointsEntity> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }
}
