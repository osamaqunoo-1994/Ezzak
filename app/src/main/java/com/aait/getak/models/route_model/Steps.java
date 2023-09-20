package com.aait.getak.models.route_model;

import java.io.Serializable;

/**
 * Created by root on 4/13/18.
 */

public class Steps implements Serializable {
    private String html_instructions;

    private DurationEntity duration;

    private DistanceEntity distance;

    private EndLocationEntity end_location;

    private Polyline polyline;

    private StartLocationEntity start_location;

    private String travel_mode;

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public DurationEntity getDuration() {
        return duration;
    }

    public void setDuration(DurationEntity duration) {
        this.duration = duration;
    }

    public DistanceEntity getDistance() {
        return distance;
    }

    public void setDistance(DistanceEntity distance) {
        this.distance = distance;
    }

    public EndLocationEntity getEnd_location() {
        return end_location;
    }

    public void setEnd_location(EndLocationEntity end_location) {
        this.end_location = end_location;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocationEntity getStart_location() {
        return start_location;
    }

    public void setStart_location(StartLocationEntity start_location) {
        this.start_location = start_location;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }
}
