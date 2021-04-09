package edu.cooper.ece465;

public class CoordinatorMain {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        String filter = args[1];
        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        String[] workerIP = new String[args.length-1];
        for(int i=4; i < args.length-1; i++) {
            workerIP[i-4] = args[i];
        }

        Coordinator coordinator = new Coordinator(workerIP, portNumber, filter, start, end);
        coordinator.test();
    }
}
