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
                s.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

//        If 2 airlines have same cost then have to include both
//        if (!MsgQueue.isEmpty()) {
//            WorkerToCoordinatorMessage temp;
//            bestAirline.add(MsgQueue.poll());
//            while (!MsgQueue.isEmpty()) {
//                temp = MsgQueue.poll();
//                if (temp.compareTo(bestAirline.peek()) < 0) {
//                    bestAirline.clear();
//                    bestAirline.add(temp);
//                }
//                else if (temp.compareTo(bestAirline.peek()) == 0) {
//                    bestAirline.add(temp);
//                }
//            }
//        }
//        while (!bestAirline.isEmpty()){
//            WorkerToCoordinatorMessage temp = bestAirline.poll();
//            System.out.println(temp.getAirline());
//            System.out.println(temp.getCost());
//            System.out.println(temp.getPath());
//        }
//        while (!MsgQueue.isEmpty()){
//            WorkerToCoordinatorMessage temp = MsgQueue.poll();
//            System.out.println(temp.getAirline());
//            System.out.println(temp.getCost());
//            System.out.println(temp.getPath());
//        }
        return MsgQueue;
    }
}