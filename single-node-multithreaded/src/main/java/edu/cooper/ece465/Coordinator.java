package edu.cooper.ece465;

import java.io.*;
import java.net.Socket;

public class Coordinator {
    private String[] workerIP;
    private int portNumber;

    public Coordinator(String[] workerIP, int portNumber) {
        this.workerIP = workerIP;
        this.portNumber = portNumber;
    }

    public void test() {
        for(String ip : workerIP) {
            try(Socket s = new Socket(ip, portNumber)) {
                OutputStream outputStream = s.getOutputStream();
                InputStream inputStream = s.getInputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                dataOutputStream.writeUTF("Hello from the server");
                dataOutputStream.flush();

                String message = dataInputStream.readUTF();
                System.out.println("Message received from worker was: " + message);
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}