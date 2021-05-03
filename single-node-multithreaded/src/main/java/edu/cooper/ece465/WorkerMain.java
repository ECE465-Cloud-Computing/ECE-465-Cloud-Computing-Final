package edu.cooper.ece465;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkerMain {
    private static final int NUM_THREADS = 2;

    private static Graph moneyGraph;
    private static Graph timeGraph;

    public static void main(String[] args) throws IOException {
        int portnum = Integer.parseInt(args[0]);
        String airline = args[1];

        moneyGraph = Util.readGraph(airline.toUpperCase(Locale.ROOT)+"_money.txt");
        timeGraph = Util.readGraph(airline.toUpperCase(Locale.ROOT)+"_time.txt");

        startServer(portnum, airline);

    }

    private static void startServer(int portNumber, String airline) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection established on port " + portNumber);

                Dijkstra dijkstra = new Dijkstra();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                CoordinatorToWorkerMessage coordinatorToWorkerMessage = (CoordinatorToWorkerMessage) objectInputStream.readObject();
                String filter = coordinatorToWorkerMessage.getFilter();
                int startNode = coordinatorToWorkerMessage.getStart();
                int endNode = coordinatorToWorkerMessage.getEnd();

                WorkerToCoordinatorMessage workerToCoordinatorMessage = null;

                if ("PRICE".equals(filter)) {
                    workerToCoordinatorMessage = dijkstra.runAlgo(moneyGraph, NUM_THREADS, startNode, endNode);
                } else if ("TIME".equals(filter)) {
                    workerToCoordinatorMessage = dijkstra.runAlgo(timeGraph, NUM_THREADS, startNode, endNode);
                }

                workerToCoordinatorMessage.setAirline(airline);
                objectOutputStream.writeObject(workerToCoordinatorMessage);
                objectOutputStream.reset();

            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
