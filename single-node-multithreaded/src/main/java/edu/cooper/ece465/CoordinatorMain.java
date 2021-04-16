package edu.cooper.ece465;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CoordinatorMain {
    private static Coordinator coordinator;

    public static void main(String[] args) throws IOException {
//        int portNumber = Integer.parseInt(args[0]);
//        String filter = args[1];
//        int start = Integer.parseInt(args[2]);
//        int end = Integer.parseInt(args[3]);
//        String[] workerIP = new String[args.length-4];
//        for(int i=4; i < args.length; i++) {
//            workerIP[i-4] = args[i];
//        }

        //coordinator.test();

        String workerIP = args[0];
        String filter = args[1];
        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        int[] portNumber = new int[args.length-4];
        for(int i=4; i < args.length; i++) {
            portNumber[i-4] = Integer.parseInt(args[i]);
        }

        System.out.println(Arrays.toString(portNumber));
        coordinator = new Coordinator(workerIP, portNumber);
//        coordinator.test();

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 5000), 0);
        server.createContext("/", new MyHttpHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
    }

    private static class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            if("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                handleResponse(exchange, params);
            }
        }

        private void handleResponse(HttpExchange httpExchange, Map<String, String> params) {
            final Headers headers = httpExchange.getResponseHeaders();

            Gson gson = new Gson();
            PriorityQueue<WorkerToCoordinatorMessage> result = coordinator.runAlgo(params.get("filter"), Integer.parseInt(params.get("start")), Integer.parseInt(params.get("end")));
            String responseString = gson.toJson(result);

            headers.set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
            final byte[] rawResponseBody = responseString.getBytes(StandardCharsets.UTF_8);
            try {
                httpExchange.sendResponseHeaders(200, rawResponseBody.length);
                httpExchange.getResponseBody().write(rawResponseBody);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

        private Map<String, String> queryToMap(String query) {
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
}




