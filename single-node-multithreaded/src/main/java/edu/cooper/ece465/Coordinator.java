package edu.cooper.ece465;

import java.io.*;
import java.net.Socket;
import java.util.PriorityQueue;

public class Coordinator {
    private String[] workerIP;
    private int portNumber;
    private PriorityQueue<WorkerToCoordinatorMessage> MsgQueue = new PriorityQueue<>();
    private PriorityQueue<WorkerToCoordinatorMessage> bestAirline;

    public Coordinator(String[] workerIP, int portNumber) {
        this.workerIP = workerIP;
        this.portNumber = portNumber;
    }

    public PriorityQueue<WorkerToCoordinatorMessage> runAlgo(String filter, int start, int end) {
        MsgQueue.clear();
        for(String ip : workerIP) {
            try(Socket s = new Socket(ip, portNumber)) {
                // Setup write to client
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                // Setup read from client
                ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));

                objectOutputStream.writeObject(new CoordinatorToWorkerMessage(filter, start, end));
                objectOutputStream.reset();

                WorkerToCoordinatorMessage workerResponse = (WorkerToCoordinatorMessage)objectInputStream.readObject();
                MsgQueue.add(workerResponse);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return MsgQueue;
    }
}