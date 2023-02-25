# Street Pathfinding

# Requirements
You must write all the code
yourself. You cannot use code from the textbook, the Internet, or any other sources. **You cannot
use Java’s Hashtable class or hashCode() method.**
### Coding Style
Your mark will be based partly on your coding style.
* Variable and method names should be chosen to reflect their purpose in the program.
* Comments, indenting, and white spaces should be used to improve readability.
* No unnecessary instance variables must be used in your code.
* All instance variables must be declared `private`, to maximize information hiding.

### Marking
Your mark will be computed as follows:

* Program compiles, produces meaningful output: 2 marks.
* Tests for the Graph class: 4 marks.
* Tests for the MyMap class: 4 marks.
* Coding style: 2 marks.
* Graph implementation: 4 marks.
* EvaluateMyMap implementation: 4 marks

# Constraints for submission:
* **DO NOT** put your code in sub-directories.
* **DO NOT** compress your files or submit a .zip, .rar, .gzip, or any other compressed file. Only the .java files that you have written must be submitted.

If you do not follow these guidelines, you might lose marks.

# Introduction
Write a program that finds a path between two specified locations in a roadmap that satisfies certain conditions specified below. 

The roadmap has 3 kinds of roads:
**public roads**, **private roads** that have a fee to be used, and **construction roads** that are under repair,
so the traffic through them is slow.

Your program will receive as input a file with a description of the roadmap, the starting point `s`, the destination `e`, the maximum number `p` of private roads that the path from `s` to `e` can use, and the maximum number `c` of construction roads that the path from `s` to e can use. 

Your program then will try to find a path from `s` to `e` as specified. You must store the roadmap as an undirected graph. 

Every edge of the graph represents a road and every node represents either the intersection of two roads or the end of a road. There are two special nodes in this graph denoting the starting point `s` and the destination `e`. A modified depth first search traversal, for example, can be used to find a path as required, if one exists.

# Node.java

This class represent a node of the graph. You must implement these public methods:
* `Node(int id)`: This is the constructor for the class, and it creates a node with the given `id`.
The `id` of a node is an `integer` value between 0 and n − 1, where `n` is the number of nodes in
the graph.


* `markNode(boolean mark)`: Marks the node with the specified value, either `true` or `false`.
This is useful when traversing the graph to know which nodes have already been visited.


* `boolean getMark()`: Returns the value with which the node has been marked.


* `int getId()`: Returns the `id` of this node.

# Edge.java
This class represents an edge of the graph. You must implement these public methods:

* `Edge(Node u, Node v, String type)`: The constructor for the class. The first two parameters are the endpoints of the edge. The last parameter is the type of the edge, which for this
assignment can be either `public` (if the edge represents a public road), `private` (if the edge
represents a private road), or `construction` (if the edge represents a road with construction
work on it).


* `Node firstNode()`: Returns the first endpoint of the edge.


* `Node secondNode()`: Returns the second endpoint of the edge.


* `String getType()`: Returns the type of the edge

# Graph.java
This class represents an undirected graph. **You must use an adjacency matrix or an adjacency
list representation for the graph**. 

For this class, you must implement all the public methods specified in the GraphADT interface plus the constructor.

```java
public interface GraphADT {

public void addEdge(Node nodeu, Node nodev, String edgeType) throws GraphException;

public Node getNode(int id) throws GraphException;

public Iterator incidentEdges(Node u) throws GraphException;

public Edge getEdge(Node u, Node v) throws GraphException;

public boolean areAdjacent(Node u, Node v) throws GraphException;
}
```

This class must be declared as follows:

* `Graph(int n)`: Creates a graph with `n` nodes and no edges. This is the constructor for the
class. The `id` of the nodes are `0, 1...` , n−1.


* `Node getNode(int id)`: Returns the node with the specified `id`. If no node with this `id`
exists, the method should throw a `GraphException`.


* `addEdge(Node u, Node v, String edgeType)`: Adds an edge of the given type connecting
`u` and `v`. This method throws a `GraphException` if either node does not exist or if in the
graph there is already an edge connecting the given nodes.


* `Iterator incidentEdges(Node u)`: Returns a `Iterator` storing all the edges incident
on node `u`. It returns `null` if u does not have any edges incident on it.


* `Edge getEdge(Node u, Node v)`: Returns the edge connecting nodes `u` and `v`. This method
throws a GraphException if there is no edge between `u` and `v`.


* `boolean areAdjacent(Node u, Node v)`: Returns `true` if nodes `u` and `v` are adjacent; it
returns `false` otherwise.

The last four methods throw a `GraphException` if `u` or `v` are not nodes of the graph.

# MyMap.java

This class represents the roadmap. A graph will be used to store the map and to try to find a path
from the starting point to the destination satisfying the conditions stated above. For this class you
must implement the following public methods

`MyMap(String inputFile)`: Constructor for building a graph from the input file specified
in the parameter; this graph represents the roadmap. If the input file does not exist, this
method should throw a `MapException`. 


`Graph getGraph()`: Returns a `graph` representing the roadmap.


`int getStartingNode()`: Returns the `id` of the starting node (this value and the next three
values are specified in the input file).


`int getDestinationNode()`: Returns the id of the destination node.int getDestinationNode(): Returns the `id` of the destination node.


`int maxPrivateRoads()`: Returns the maximum number allowed of private roads in the path
from the starting node to the destination.


`int maxConstructionRoads()`: Returns the maximum number allowed of construction roads
in the path from the starting node to the destination.


`Iterator findPath(int start, int destination, maxPrivate, maxConstruction)`: Returns an `Iterator` containing the nodes of a path from the start node to the destination
node such that the path uses at most `maxPrivate` private roads and maxConstruction construction roads, if such a path exists. If the path does not exist, this method returns the value
`null`.

# InputFIle

The input file is a text file with the following format:
```
SCALE
START
END
WIDTH
LENGTH
PRIVATE ROADS
CONSTRUCTION ROADS
RHRHRH· · ·RHR
HXHXHX· · ·HXH
RHRHRH· · ·RHR
HXHXHX· · ·HXH
.
.
.
RHRHRH· · ·RHR
```

Each one of the first seven lines of the input file contains one number:

* `SCALE` is the scale factor used to display the map on the screen. Your program will not use this
 value. If the map appears too small on your screen, you must increase this value. Similarly,
 if the map is too large, choose a smaller value for the scale.

 
* `START` is the starting node.

 
*  `END` is the destination node.

 
*  `WIDTH` is the width of the map. The roads of the map are arranged in a grid. The number of
 vertical roads in each row of this grid is the width of the map.

 
*  `LENGTH` is the length of the map, or the number of horizontal roads in each column of the
 grid. So the total number of nodes in the corresponding graph is WIDTH × LENGTH.

 
* `PRIVATE ROADS` is the maximum allowed number of private roads in the path from START to
 END.
 

* `CONSTRUCTION ROADS` is the maximum allowed number of construction roads in the path from START to END.

For the rest of the input file, `R` can be any of the following characters: `+` or `B`. `H` could be
`B`, `V`, `C`, or `P`. 

The meaning of the above characters is as follows:

* `+`: denotes either and intersection of two roads, or a dead end


* `V`: denotes a private road


* `P`: denotes a public road


* `C`: denotes a construction road


* `B`: denotes a block of houses, a building, or a park

# Finding a Path

Your program must find **any** path from the starting vertex to the destination vertex that uses at
most the specified number of private and construction roads. 

The path can use any number of public roads. If there are several such paths, your program might return any one of them.
The solution can be found, for example, by using a modified DFS traversal. While traversing the
graph, your algorithm needs to keep track of the vertices along the path that the DFS traversal has
followed. If the current path has already used all allowed private roads, then no more private-road
edges can be added to it. Similarly, if the current path has already used all allowed construction
roads, no more construction-road edges can be added to the path.

For example, consider the above graph and let the maximum allowed number of private roads be
1 and the maximum allowed number of construction roads be 1. Assume that the algorithm visits
vertex 0, then 4, and then 5. As the algorithm traverses the graph, all visited vertices get marked.
While at vertex 5, the algorithm cannot next visit vertices 6 or 9, since then two private roads
would have been used by the current path. Hence, the algorithm goes next to vertex 1. However,
the destination cannot be reached from here, so the algorithm must go back to vertex 5, and then
back to vertices 4 and 0. Note that vertices 1, 5 and 4 must be unmarked when the algorithm
steps back, as otherwise the algorithm will not be able to find a solution. Next, the algorithm will
move from vertex 0 to vertex 1, and then to 5. Since edge (1, 5) represents a construction road, the
current path has used 1 construction road and no private roads. The algorithm can then move to
node 6 and then to 10 as the rest of the path uses only one private road. Therefore, the solution
produced by the algorithm is: 0, 1, 5, 6, and 10.

**You do not have to implement the above algorithm if you do not want to. Feel free to
design your own solution for the problem.**