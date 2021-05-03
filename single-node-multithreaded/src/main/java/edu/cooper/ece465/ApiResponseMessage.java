package edu.cooper.ece465;

import java.io.Serializable;
import java.util.List;

public class ApiResponseMessage implements Serializable {
    private String airline;
    private List<String> path;
    private int cost;

    public ApiResponseMessage(String airline, List<String> path, int cost) {
        this.airline = airline;
        this.path = path;
        this.cost = cost;
    }

    public String getAirline() {
        return airline;
    }

    public List<String> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
