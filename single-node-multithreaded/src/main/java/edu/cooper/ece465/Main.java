package edu.cooper.ece465;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
//        String host_ip = args[0];
        int portnum = Integer.parseInt(args[0]);

//        Graph graph1 = Util.readGraph("1_$cost.txt");
//        Graph graph2 = Util.readGraph("1_time.txt");


        List<Integer> results = runDijkstra(null, null, 2, portnum);

//        Util.writeResults("singleoutput.txt", results, 0);
    }

    private static List<Integer> runDijkstra(Graph graph1, Graph graph2, int numThreads, int portnum) {
        Dijkstra dijkstra = new Dijkstra();

        try {
            ServerSocket serverSocket = new ServerSocket(portnum);
            Socket s = serverSocket.accept();
            System.out.println("Connection establish with on port: " + portnum);
            network_runalgo(graph1, graph2, numThreads, s);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
//        return dijkstra.runAlgo(graph1, numThreads);

    }

    private static void network_runalgo(Graph graph1, Graph graph2, int numThreads, Socket s) throws IOException, InterruptedException {
        DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(s.getInputStream());

        String rec_msg = dataInputStream.readUTF();
        System.out.println("Received_Message: " + rec_msg);

        dataOutputStream.writeUTF("Sending Message.");
        dataOutputStream.flush();

        System.out.println("Finished receiving and sending.");
    }
}
