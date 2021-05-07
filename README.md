# ECE-465-Cloud-Computing-Final

The final project is an application of a distributed Dijkstra’s algorithm in the form of an flight trip search system. The goal of the project is a proof of concept for a cloud based centralized flight search system that combines results from multiple airlines to give users the best set of flights to take for a given trip. The project emphasizes processing speed and ease of use.

# Application Functionality
The application stores flight tables from multiple airlines in the form of pre-randomly generated adjacency matrix graphs. The user will select a starting and ending city for the trip and the application will determine the best set of flights to take for each airline using the distributed Dijkstra’s algorithm. The returned set of flights for each airline will be sorted based on price or time, which the user can choose prior to searching. Additionally, users have the ability to register for an account which allows them to save trips to their account from the seach results. These saved trips can be viewed at any later time.

# Backend Search Algorithm
The search algorithm is a distributed version of Dijstra's algorithm run on multiple graphs. Since each airline has a unique flight table, Dijstra's must be run on each graph and the result from each graph must be compared and sorted based on cost before returning the final seach results. In order to achieve this, the coordinator and worker structure is used. The coordinator receives the client request and alerts the workers to perform Dijstra's. Each worker acts as the server for a single airline and only performs Dijstra's on a single graph. Once each worker finishes the algorithm, the result is sent back to the coordinator. Once the coordinator receives results from all the workers, it sorts the results based on cost and returns them to the client. The coordinator, worker, and Dijstra's algorithm used in decribed below in more detail.

## Coordinator

## Worker

## Multi-threaded Dijstras
