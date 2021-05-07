# ECE-465-Cloud-Computing-Final

The final project is an application of a distributed Dijkstra’s algorithm in the form of an flight trip search system. The goal of the project is a proof of concept for a cloud based centralized flight search system that combines results from multiple airlines to give users the best set of flights to take for a given trip. 

# Functionality
The application stores flight tables from multiple airlines in the form of pre-randomly generated adjacency matrix graphs. The user will select a starting and ending city for the trip and the application will determine the best set of flights to take for each airline using the distributed Dijkstra’s algorithm. The returned set of flights for each airline will be sorted based on price or time, which the user can choose prior to searching. Additionally, users have the ability to register for an account which allows them to save trips to their account from the seach results. These saved trips can be viewed at any later time.

