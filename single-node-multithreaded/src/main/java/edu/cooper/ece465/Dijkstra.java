package edu.cooper.ece465;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dijkstra {
    private Graph graph;
    private Node currNode;
    private List<Integer> nodeDistances;
    private Set<Integer> visitedNodes = new HashSet<>();
    private List<Integer> prevNode;
    // Using an atomicboolean mutable so we can change the value in the threads
    private AtomicBoolean isFinished;
    private List<PriorityQueue<Node>> nodeQueue = new ArrayList<>();

    public WorkerToCoordinatorMessage runAlgo(Graph graph, int numThreads, int startNode, int endNode) throws InterruptedException {
        // Init vars
        this.graph = graph;
        isFinished = new AtomicBoolean(false);
        nodeDistances = new ArrayList<>(graph.getNumNodes());
        prevNode = new ArrayList<>(graph.getNumNodes());
        currNode = new Node(startNode, 0);
        // Init threads + queues for threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            PriorityQueue<Node> threadQueue = new PriorityQueue<>();
            nodeQueue.add(threadQueue);
        }
        // Preset distances to infinity
        for (int i = 0; i < graph.getNumNodes(); i++) {
            nodeDistances.add(i, Integer.MAX_VALUE);
            prevNode.add(i, -1);
        }

        // Set starting node distance to 0
        nodeDistances.set(startNode, 0);

        // Find min node once the threads have finished one iteration of Dijkstra's using await
        FindMinNode findMinNode = new FindMinNode(nodeQueue, isFinished, visitedNodes, currNode, endNode);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads, findMinNode);
        // Break up graph and distribute among threads
        int subgraphSize = graph.getNumNodes() / numThreads;
        int excessNodes = graph.getNumNodes() % numThreads;
        // Init vars
        int start = 0;
        int end;
        // Create + Start threads
        for (int i = 0; i < numThreads; i++) {
            end = start + subgraphSize;
            // if there are excess nodes after distributing, then some subgraphs get one node until all excess nodes gone
            if (excessNodes > 0) {
                end = end + 1;
                excessNodes = excessNodes - 1;
            }

            // Create new thread and run dijkstra on subgraph
            Thread DThread = new DijkstraThread(graph, start, end, visitedNodes, nodeQueue.get(i), nodeDistances,
                    prevNode, currNode, cyclicBarrier, isFinished);
            DThread.start();
            // Add new thread to queue
            threads.add(DThread);

            // Update startNode to w/e endNode was w/ one node overlap in new subgraphs to later connect them together
            start = end;
        }
        // Sync threads
        for (int i = 0; i < numThreads; i++){
            threads.get(i).join();
        }

        start = endNode;
        List<Integer> path = new ArrayList<>();
        while (start > 0) {
            path.add(start);
            start = prevNode.get(start);
        }
        path.add(startNode);
        System.out.println(path);
        return new WorkerToCoordinatorMessage("", path, nodeDistances.get(endNode));
    }

    public static class FindMinNode implements Runnable {

        private List<PriorityQueue<Node>> nodeQueue;
        private AtomicBoolean isFinished;
        private Set<Integer> visitedNodes;
        private Node currNode;
        private int endNode;

        public FindMinNode(List<PriorityQueue<Node>> nodeQueue, AtomicBoolean isFinished, Set<Integer> visitedNodes, Node currNode, int endNode) {
            this.nodeQueue = nodeQueue;
            this.isFinished = isFinished;
            this.visitedNodes = visitedNodes;
            this.currNode = currNode;
            this.endNode = endNode;
        }

        @Override
        public void run() {
            while (true) {
                Node minNode = null;
                int index = 0;

                for (int i = 0; i < nodeQueue.size(); i++){
                    // if thread's queue is not empty, get first node in priorityqueue which will be node w/ smallest dist
                    if (!nodeQueue.get(i).isEmpty()) {
                        Node node = nodeQueue.get(i).peek();
                        //if minNode not found or current node smaller than minNode, set minNode as current node
                        if (minNode == null || Objects.requireNonNull(node).compareTo(minNode) < 0) {
                            minNode = node;
                            index = i;
                        }
                    }
                }
                // if minNode not found b/c queues are empty, the algorithm is finished
                if (minNode == null || minNode.getNode() == endNode){
                    isFinished.set(true);
                    return;
                }
                else if(!visitedNodes.contains(minNode.getNode())) {
                    // min node found and not yet visited, set currNode as minNode then remove minNode from the queue
                    visitedNodes.add(minNode.getNode());
                    currNode.setNode(minNode.getNode());
                    currNode.setDistance(minNode.getDistance());
                    nodeQueue.get(index).remove();
                    return;
                }
                else {
                    // min node found but visited already, remove
                    nodeQueue.get(index).remove();
                }

            }
        }
    }
}
