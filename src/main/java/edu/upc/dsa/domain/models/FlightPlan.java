package edu.upc.dsa.domain.models;

import java.util.LinkedList;
import java.util.List;

public class FlightPlan {
    private String id;
    private String dronId;
    private String pilotId;
    private String dateTime;
    private double estimatedHours;
    private List<Position> route;

    // Constructor buit obligatori per a Jackson / JSON
    public FlightPlan() {
        this.route = new LinkedList<>();
    }

    public FlightPlan(String id, String dronId, String pilotId, String dateTime, double estimatedHours) {
        this.id = id;
        this.dronId = dronId;
        this.pilotId = pilotId;
        this.dateTime = dateTime;
        this.estimatedHours = estimatedHours;
        this.route = new LinkedList<>();
    }

    public void addPosition(double latitude, double longitude) {
        this.route.add(new Position(latitude, longitude));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDronId() {
        return dronId;
    }

    public void setDronId(String dronId) {
        this.dronId = dronId;
    }

    public String getPilotId() {
        return pilotId;
    }

    public void setPilotId(String pilotId) {
        this.pilotId = pilotId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public List<Position> getRoute() {
        return route;
    }

    public void setRoute(List<Position> route) {
        this.route = route;
    }
}