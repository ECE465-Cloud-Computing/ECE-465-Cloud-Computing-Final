package edu.cooper.ece465;

import java.io.Serializable;
import java.util.List;

public class WorkerToCoordinatorMessage implements Comparable<WorkerToCoordinatorMessage>, Serializable {
    private String airline;
    private List<Integer> path;
    private int cost;

    public WorkerToCoordinatorMessage(String airline, List<Integer> path, int cost) {
        this.airline = airline;
        this.path = path;
        this.cost = cost;
    }

    @Override
    public int compareTo(WorkerToCoordinatorMessage o) {
        return Integer.compare(this.cost, o.cost);
    }

    public String getAirline() {
        return airline;
    }

    public List<Integer> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }
}
