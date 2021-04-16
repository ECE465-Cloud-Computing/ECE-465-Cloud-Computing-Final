package edu.cooper.ece465;

import java.util.Arrays;

public class CoordinatorMain {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        String filter = args[1];
        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        String[] workerIP = new String[args.length-4];
        for(int i=4; i < args.length; i++) {
            workerIP[i-4] = args[i];
        }

        System.out.println(workerIP.length);
        System.out.println(Arrays.toString(workerIP));
        Coordinator coordinator = new Coordinator(workerIP, portNumber, filter, start, end);
        coordinator.test();

//        String workerIP = args[0];
//        String filter = args[1];
//        int start = Integer.parseInt(args[2]);
//        int end = Integer.parseInt(args[3]);
//        int[] portNumber = new int[args.length-4];
//        for(int i=4; i < args.length; i++) {
//            portNumber[i-4] = Integer.parseInt(args[i]);
//        }
//
//        System.out.println(Arrays.toString(portNumber));
//        Coordinator coordinator = new Coordinator(workerIP, portNumber, filter, start, end);
//        coordinator.test();
    }
}
