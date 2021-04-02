package edu.cooper.ece465;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String host_ip = args[0];
        int portnum = Integer.parseInt(args[1]);

        Graph graph1 = Util.readGraph("1_$cost.txt");
        Graph graph2 = Util.readGraph("1_time.txt");


        List<Integer> results = runDijkstra(graph1, graph2, 2);

        Util.writeResults("singleoutput.txt", results, 0);
    }

    private static List<Integer> runDijkstra(Graph graph1, Graph graph2, int numThreads) throws InterruptedException {
        Dijkstra dijkstra = new Dijkstra();
        return dijkstra.runAlgo(graph1, numThreads);
    }
}
