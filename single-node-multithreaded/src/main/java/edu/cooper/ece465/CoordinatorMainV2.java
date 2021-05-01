package edu.cooper.ece465;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CoordinatorMainV2 {
    private static Coordinator coordinator;

    public static void main(String[] args) {

        int portNumber = Integer.parseInt(args[0]);
        String[] workerIP = new String[args.length-1];
        for(int i=1; i < args.length; i++) {
            workerIP[i-1] = args[i];
        }

//        String workerIP = args[0];
//        int[] portNumber = new int[args.length-1];
//        for(int i=1; i < args.length; i++) {
//            portNumber[i-1] = Integer.parseInt(args[i]);
//        }

        coordinator = new Coordinator(workerIP, portNumber);

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    handleClient(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String line = br.readLine();
        System.out.println(line);
        String[] requestLine = line.split(" ");
        String method = requestLine[0];
        String path = requestLine[1];

        String query = path.split("\\?")[1];
        Map<String, String> params = queryToMap(query);
        System.out.println(params);

        Gson gson = new Gson();
        PriorityQueue<WorkerToCoordinatorMessage> result = coordinator.runAlgo(params.get("filter").toUpperCase(), Integer.parseInt(params.get("start")), Integer.parseInt(params.get("end")));

        List<WorkerToCoordinatorMessage> tempList = new ArrayList<>();
        while (!result.isEmpty()){
            WorkerToCoordinatorMessage temp = result.poll();
            tempList.add(temp);
        }

        String responseString = gson.toJson(tempList);
        System.out.println(responseString);

        sendResponse(client, "200 OK", String.format("application/json; charset=%s", StandardCharsets.UTF_8), responseString);
    }

    private static void sendResponse(Socket client, String status, String contentType, String content) throws IOException {
        final String CRLF = "\n\r";
        OutputStream clientOutput = client.getOutputStream();
        String res =
                "HTTP/1.1 " + status + CRLF + "Content-Type: " + contentType + CRLF + CRLF + content + CRLF + CRLF;
        clientOutput.write(res.getBytes(StandardCharsets.UTF_8));
        clientOutput.flush();
        client.close();
    }

    private static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
