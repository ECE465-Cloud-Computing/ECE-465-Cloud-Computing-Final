package edu.cooper.ece465;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
//        String host_ip = args[0];
//        int portnum = Integer.parseInt(args[1]);
        Graph graph = Util.readGraph("./input.txt");
        int result = runDijkstra(graph, null, 2, "test", 1);
        System.out.println("Result: " + result);
//        Util.writeResults("singleoutput.txt", results, 0);
    }

    private static int runDijkstra(Graph graph1, Graph graph2, int numThreads, String host_ip, int portnum) throws InterruptedException {
        Dijkstra dijkstra = new Dijkstra();

//        try {
//            Socket s = new Socket(host_ip, portnum);
//            System.out.println("Connection establish with " + host_ip + "::" + portnum);
//            network_runalgo(graph1, graph2, numThreads, s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return dijkstra.runAlgo(graph1, numThreads, 3);

    }

    private static void network_runalgo(Graph graph1, Graph graph2, int numThreads, Socket s) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(s.getInputStream());

        String rec_msg = dataInputStream.readUTF();
        System.out.println("Received_Message: " + rec_msg);

        dataOutputStream.writeUTF("Sending Message.");
        dataOutputStream.flush();

        System.out.println("Finished receiving and sending.");

    }
}
