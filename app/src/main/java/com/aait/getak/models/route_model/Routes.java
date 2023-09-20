package com.aait.getak.models.route_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 4/13/18.
 */

public class Routes implements Serializable {
    private String summary;

    private BoundsEntity bounds;

    private String copyrights;

    private List<String> waypoint_order;

    private List<Legs> legs;

    private List<String> warnings;

    private Overview_polyline overview_polyline;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BoundsEntity getBounds() {
        return bounds;
    }

    public void setBounds(BoundsEntity bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<String> getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(List<String> waypoint_order) {
        this.waypoint_order = waypoint_order;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public Overview_polyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(Overview_polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }
}
