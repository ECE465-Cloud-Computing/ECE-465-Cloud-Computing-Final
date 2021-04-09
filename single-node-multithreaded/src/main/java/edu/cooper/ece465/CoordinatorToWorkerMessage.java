package edu.cooper.ece465;

import java.io.Serializable;
import java.util.List;

public class CoordinatorToWorkerMessage implements Comparable<CoordinatorToWorkerMessage>, Serializable {
    private String filter;
    private int start;
    private int end;

    public CoordinatorToWorkerMessage(String filter, int start, int end) {
        this.filter = filter;
        this.start = start;
        this.end = end;
    }


    public String getFilter() {
        return filter;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public int compareTo(CoordinatorToWorkerMessage coordinatorToWorkerMessage) {
        return 0;
    }
}