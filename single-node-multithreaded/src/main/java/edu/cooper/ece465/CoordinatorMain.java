package edu.cooper.ece465;

public class CoordinatorMain {
    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        String[] workerIP = new String[args.length-1];
        for(int i=1; i < args.length; i++) {
            workerIP[i-1] = args[i];
        }

        Coordinator coordinator = new Coordinator(workerIP, portNumber);
        coordinator.test();
    }
}
