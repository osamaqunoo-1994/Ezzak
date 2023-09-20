package com.aait.getak.models.route_model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 4/13/18.
 */

public class Legs implements Serializable {
    private DurationEntity duration;

    private DistanceEntity distance;

    private EndLocationEntity end_location;

    private String start_address;

    private String end_address;

    private StartLocationEntity start_location;

    private List<String> traffic_speed_entry;

    private List<String> via_waypoint;

    private List<Steps> steps;

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

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public StartLocationEntity getStart_location() {
        return start_location;
    }

    public void setStart_location(StartLocationEntity start_location) {
        this.start_location = start_location;
    }

    public List<String> getTraffic_speed_entry() {
        return traffic_speed_entry;
    }

    public void setTraffic_speed_entry(List<String> traffic_speed_entry) {
        this.traffic_speed_entry = traffic_speed_entry;
    }

    public List<String> getVia_waypoint() {
        return via_waypoint;
    }

    public void setVia_waypoint(List<String> via_waypoint) {
        this.via_waypoint = via_waypoint;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
}
