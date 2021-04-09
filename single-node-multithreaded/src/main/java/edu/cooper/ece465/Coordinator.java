package edu.cooper.ece465;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

public class Coordinator {
    private String workerIP;
    private int[] portNumber;
    private PriorityQueue<WorkerToCoordinatorMessage> MsgQueue = new PriorityQueue<>();
    private String filter;
    private PriorityQueue<WorkerToCoordinatorMessage> bestAirline = new PriorityQueue<>();
    private int start;
    private int end;


    public Coordinator(String workerIP, int[] portNumber, String filter, int start, int end) {
        this.workerIP = workerIP;
        this.portNumber = portNumber;
        this.filter = filter.toUpperCase(Locale.ROOT);
        this.start = start;
        this.end = end;
    }

    public void test() {
        for(int port : portNumber) {
            try(Socket s = new Socket(workerIP, port)) {
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
        while (!MsgQueue.isEmpty()){
            WorkerToCoordinatorMessage temp = MsgQueue.poll();
            System.out.println(temp.getAirline());
            System.out.println(temp.getCost());
            System.out.println(temp.getPath());
        }
    }
}