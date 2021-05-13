# ECE-465-Cloud-Computing-Final

The final project is an application of a distributed Dijkstra’s algorithm in the form of an flight trip search system. The goal of the project is a proof of concept for a cloud based centralized flight search system that combines results from multiple airlines to give users the best set of flights to take for a given trip. The project emphasizes search speed through cloud infrastructures and ease of use.

# Application Functionality
The application stores flight tables from multiple airlines in the form of pre-randomly generated adjacency matrix graphs. The user will select a starting and ending city for the trip and the application will determine the best set of flights to take for each airline using the distributed Dijkstra’s algorithm. The returned set of flights for each airline will be sorted based on price or time, which the user can choose prior to searching. Additionally, users have the ability to register for an account which allows them to save trips to their account from the seach results. These saved trips can be viewed at a later time and can be deleted.

# Backend
The backend consists of two main components: the seach API and the user-based APIs. The seach API contains the algorithm for trip seaches and the user-based APIs contain user related requests such as registering and saving a trip. These two components are described in more detail below.

## Search API
The search algorithm is a distributed version of Dijkstra's algorithm run on multiple graphs. Since each airline has a unique flight table, Dijkstra's must be run on each graph and the result from each graph must be compared and sorted based on cost before returning the final seach results. In order to achieve this, the coordinator and worker structure is used. The coordinator receives the client request and alerts the workers to perform Dijkstra's. Each worker acts as the server for a single airline and only performs Dijkstra's on a single graph. Once each worker finishes the algorithm, the result is sent back to the coordinator. Once the coordinator receives results from all the workers, it sorts the results based on cost and returns them to the client. The coordinator, worker, and Dijkstra's algorithm used in decribed below in more detail.

### Coordinator
The coordinator listens on a Java socket for any client requests. Upon receiving a request, it parses the request and retrieves the starting and ending locations, as well as the filter (price or time). It then converts the locations to indexes recognizable by the graphs using a pre-made hashmap that maps location to index. Afterwords, the coordinator spawns threads, each of which connects to a worker node and sends a custom serializable message that contains the starting and ending index as well as the filter. This alerts the worker nodes to perform Dijkstra's on their respective graphs. The coordinator waits until all workers have finished and puts the results from each worker in a priority queue to sort them based on cost. Finally, it formats and sends the sorted results back to the client. 

### Worker
Each worker node stores two graphs in adjacency matrix form representing the flight table for a specific airline, one for price as its edge cost, and another for time. The worker node listens for network requests through a Java socket. Once a request is received, the worker pulls the starting and ending nodes (in the graph), as well as the filter (price or time) from the request message. Using the filter, the worker node selects the correct graph to use and runs Dijkstra's algorithm using the starting and ending node. Once the algorithm is finished, the worker node sends the result back to the server in a custom serializable message that contains the the shortest path as well as the associated cost. 

### Multi-threaded Dijkstra's
In order to achieve fast speed on the seach algorithm, each worker uses a multi-threaded version of Dijkstra's to quickly process large graphs (over 1000 nodes in this application). In a multi-threaded implementation, the algorithm takes an adjacency matrix represented graph and break it up into subgroups of nodes; each of these subgroups will be assigned to a thread and processed. The threads all perform Dijkstra's on their respective chunks, updating the node weights locally, and putting the nodes into a global priority queue shared among the threads. Each thread is blocked upon its own completion of an iteration through the use of a CyclicBarrier in the Java concurrency library. Once all the threads finish executing, the shared priority queue is used to selected the global minimum node, which is then broadcasted to all threads to be used as the soruce node in the next iteration of the algorithm. The algorithm finishes once the end node has been visited. This information is stored in a global AtomicBoolean that is accessible by each thread, and updated at the end of each iteration. The algorithm outputs the shortest path from the source to the end node and the associated distance.

## User-based APIs
User-based APIs handle requests that modify the user database on the backend. The database is document-oriented with the following schema for a single document: ```[username: String, password: String, trips: List[String]]```. All documents are unique on username meaning two users cannot have the same username. Users have the ability to create account, login, save trip, and clear trips. This is implemented using REST format on User and Trips resources where create account is POST on User, login is GET on User, save trip is POST on Trips, and clear trips is DELETE on Trips. Create account simply creates a new document with the requested username and password; it throws an error if the username already exists. Login checks for existing username and a match in password; it throws an error if username is not found or if passwords do not match. Save trip takes the username and trip in the request simply adds the trip to the corresponding user. Delete trip takes the username in the request and clears the trips list for the corresponding user.

# Frontend
The frontend is a simple web-application written in ReactJS. It has the following pages: register, login, search, and trips. The register and login pages allows the user to register for an account or login to their account, respectively. The search page allows the user to search for flights by inputing a starting and ending location as well as a filter (price or time). The search results are displayed on screen showing the best set of flights for each airline sorted by cost or time in ascending order. For each trip in the search results, users can click the save trip button to save it to their account. Users must be logged in to save trips. Finally, the trips pages displays the saved trips for the logged in user and can be cleared by clicking on the clear trips button. This page can only be accessed is the user is logged in.

# Cloud architecture
The application utilizes EC2 with VPC for the search algorithm computing, Lambda and DynamoDB for user-based APIs and database, API Gateway for routing client requests, S3 to store static flight table graphs, and Amplify for frontend deployment. All deployments are done using AWS CLI through bash scripts with the exception of API Gateway because permission for it is denied in CLI. These services are descibed in more detail below.

![alt text](https://github.com/ECE465-Cloud-Computing/ECE-465-Cloud-Computing-Final/blob/main/Cloud%20Computing%20Final%20Project%20Diagram%203.jpg?raw=true)

## EC2
EC2 instances are used for the Search API since speed is a necessity here and EC2 gives greater control over multi-threading and network communication than Lambda. One EC2 instance is used as coordinator and the rest are used as worker node computing servers. All EC2 instances are wrapped in a custom VPC for security reasons. Elatic IP is used to ensure that the IP address for the EC2 instance is consistant through different deployments.

## Lambda and DynamoDB
Lambda and DynamoDB are responsible for user-based APIs. A lambda function is setup for each of the following requests: create account, login, save trip, and clear trips. Each lambda function receives requests and extract the request parameters. It uses these parameters to execute queries to the DynamoDB database. All lambda functions are written in Node.js. DynamoDB is setup with a single table with the following schema: ```[username: String, password: String, trips: List[String]]```. 

## API Gateway
API Gateway is used for proper routing of client requests. Two resources, User and Trips, are setup in REST API format. Search requests are routed to EC2 while user-based requests are routed to lambda where create account is POST on User, login is GET on User, save trip is POST on Trips, and clear trips is DELETE on Trips. Response code mappings are done to properly handle error responses.

## S3
S3 is used to store static flight table graphs. These graphs are pulled by EC2 worker instances on startup.

## Amplify
Amplify is used to deploy the frontend which is written in ReactJS. It deploys the React app from a github repository and handles requests by sending them to the API Gateway. 

