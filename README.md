# ECE-465-Cloud-Computing-Final

The final project is an application of a distributed Dijkstra’s algorithm in the form of an flight trip search system. The goal of the project is a proof of concept for a cloud based centralized flight search system that combines results from multiple airlines to give users the best set of flights to take for a given trip. The project emphasizes search speed and ease of use.

# Application Functionality
The application stores flight tables from multiple airlines in the form of pre-randomly generated adjacency matrix graphs. The user will select a starting and ending city for the trip and the application will determine the best set of flights to take for each airline using the distributed Dijkstra’s algorithm. The returned set of flights for each airline will be sorted based on price or time, which the user can choose prior to searching. Additionally, users have the ability to register for an account which allows them to save trips to their account from the seach results. These saved trips can be viewed at any later time.

# Backend

## Search Algorithm
The search algorithm is a distributed version of Dijstra's algorithm run on multiple graphs. Since each airline has a unique flight table, Dijstra's must be run on each graph and the result from each graph must be compared and sorted based on cost before returning the final seach results. In order to achieve this, the coordinator and worker structure is used. The coordinator receives the client request and alerts the workers to perform Dijstra's. Each worker acts as the server for a single airline and only performs Dijstra's on a single graph. Once each worker finishes the algorithm, the result is sent back to the coordinator. Once the coordinator receives results from all the workers, it sorts the results based on cost and returns them to the client. The coordinator, worker, and Dijstra's algorithm used in decribed below in more detail.

### Coordinator
The coordinator listens on a Java socket for any client requests. Upon receiving a request, it parses the request and retrieves the starting and ending locations, as well as the filter (price or time). It then converts the locations to indexes recognizable by the graphs using a pre-made hashmap that maps location to index. Afterwords, the coordinator spawns threads, each of which connects to a worker node and sends a custom serializable message that contains the starting and ending index as well as the filter. This alerts the worker nodes to perform Dijstra's on their respective graphs. The coordinator waits until all workers have finished and puts the results from each worker in a priority queue to sort them based on cost. Finally, it formats and sends the sorted results back to the client. 

### Worker
Each worker node stores two graphs in adjacency matrix form representing the flight table for a specific airline, one for price as its edge cost, and another for time. The worker node listens for network requests through a Java socket. Once a request is received, the worker pulls the starting and ending nodes (in the graph), as well as the filter (price or time) from the request message. Using the filter, the worker node selects the correct graph to use and runs Dijstra's algorithm using the starting and ending node. Once the algorithm is finished, the worker node sends the result back to the server in a custom serializable message that contains the the shortest path as well as the associated cost. 

### Multi-threaded Dijstras
In order to achieve fast speed on the seach algorithm, each worker uses a multi-threaded version of Dijstra's to quickly process large graphs (over 1000 nodes in this application). In a multi-threaded implementation, the algorithm takes an adjacency matrix represented graph and break it up into subgroups of nodes; each of these subgroups will be assigned to a thread and processed. The threads all perform Dijkstra's on their respective chunks, updating the node weights locally, and putting the nodes into a global priority queue shared among the threads. Each thread is blocked upon its own completion of an iteration through the use of a CyclicBarrier in the Java concurrency library. Once all the threads finish executing, the shared priority queue is used to selected the global minimum node, which is then broadcasted to all threads to be used as the soruce node in the next iteration of the algorithm. The algorithm finishes once the end node has been visited. This information is stored in a global AtomicBoolean that is accessible by each thread, and updated at the end of each iteration. The algorithm outputs the shortest path from the source to the end node and the associated distance.
