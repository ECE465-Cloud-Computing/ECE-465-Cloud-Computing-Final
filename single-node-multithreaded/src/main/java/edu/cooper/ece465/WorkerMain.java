package edu.cooper.ece465;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkerMain {
    private static final int NUM_THREADS = 2;

    private static Graph moneyGraph;
    private static Graph timeGraph;

    public static void main(String[] args) throws IOException {
//        String host_ip = args[0];
        int portnum = Integer.parseInt(args[0]);
        String airline = args[1];

        moneyGraph = Util.readGraph("1_money.txt");
        timeGraph = Util.readGraph("1_time.txt");

        startServer(portnum, airline);

//        Util.writeResults("singleoutput.txt", results, 0);
    }

//    private static List<Integer> runDijkstra(Graph graph1, Graph graph2, int numThreads, int portnum) {
//        Dijkstra dijkstra = new Dijkstra();
//
//        try {
//            ServerSocket serverSocket = new ServerSocket(portnum);
//            Socket s = serverSocket.accept();
//            System.out.println("Connection establish with on port: " + portnum);
//            network_runalgo(graph1, graph2, numThreads, s);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
////        return dijkstra.runAlgo(graph1, numThreads);
//
//    }

//    private static void network_runalgo(Graph graph1, Graph graph2, int numThreads, Socket s) throws IOException, InterruptedException {
//        DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
//        DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
//
//        String rec_msg = dataInputStream.readUTF();
//        System.out.println("Received_Message: " + rec_msg);
//
//        dataOutputStream.writeUTF("Sending Message.");
//        dataOutputStream.flush();
//
//        System.out.println("Finished receiving and sending.");
//    }

    private static void startServer(int portNumber, String airline) {
        Dijkstra dijkstra = new Dijkstra();
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection established on port " + portNumber);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                CoordinatorToWorkerMessage coordinatorToWorkerMessage = (CoordinatorToWorkerMessage) objectInputStream.readObject();
                String filter = coordinatorToWorkerMessage.getFilter();
                int startNode = coordinatorToWorkerMessage.getStart();
                int endNode = coordinatorToWorkerMessage.getEnd();

                WorkerToCoordinatorMessage workerToCoordinatorMessage = null;

                if ("MONEY".equals(filter)) {
                    workerToCoordinatorMessage = dijkstra.runAlgo(moneyGraph, NUM_THREADS, startNode, endNode);
                } else if ("TIME".equals(filter)) {
                    workerToCoordinatorMessage = dijkstra.runAlgo(timeGraph, NUM_THREADS, startNode, endNode);
                }

                workerToCoordinatorMessage.setAirline(airline);
                objectOutputStream.writeObject(workerToCoordinatorMessage);
                objectOutputStream.reset();

                socket.close();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}