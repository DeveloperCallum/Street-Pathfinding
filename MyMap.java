import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Processing class for the Depth First Search!
 */
public class MyMap {
    private Graph map;
    private int startNode, endNode;
    private int mapWidth;
    private int numPrivate;
    private int numConstruction;
    private int numNodes;

    private final static String PU = "public";
    private final static String PR = "private";
    private final static String CO = "construction";

    /**
     *  Constructor for building a graph from the input file specified
     * in the parameter; this graph represents the roadmap. If the input file does not exist, this
     * method should throw a MapException.
     * @param path input file
     * @throws MapException
     */

    public MyMap(String path) throws MapException {
        if (path == null || path.isBlank()) {
            throw new MapException("Nap cannot be null!");
        }

        try (Scanner inputFile = new Scanner(new File(path))) {
            int gridSize = Integer.parseInt(inputFile.nextLine());
            startNode = Integer.parseInt(inputFile.nextLine());
            endNode = Integer.parseInt(inputFile.nextLine());
            mapWidth = Integer.parseInt(inputFile.nextLine());
            int mapLength = Integer.parseInt(inputFile.nextLine());

            numPrivate = Integer.parseInt(inputFile.nextLine());
            String line = inputFile.nextLine();

            System.out.println(line);
            numConstruction = Integer.parseInt(line);
            System.out.println(numConstruction);


            numNodes = mapWidth * mapLength;

            map = new Graph(numNodes);

            //Cache - Allow the nodes to be collected, temporarily cached, then processed.
            int datasize = mapLength * 2 - 1;
            String[] data = new String[datasize];

            for (int i = 0; inputFile.hasNextLine(); i++) {
                data[i] = inputFile.nextLine();
            }

            try {
                processNodes(data);
            } catch (GraphException e) {
                //TODO: HANDLE!
                e.printStackTrace();
                return;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the graph representing the roadmap.
     */
    public Graph getGraph() {
        return map;
    }

    /**
     * @return the id of the starting node (this value and the next three
     * values are specified in the input file).
     */
    public int getStartingNode() {
        return startNode;
    }

    /**
     * @return the id of the destination node.
     */
    public int getDestinationNode() {
        return endNode;
    }

    /**
     * @return the maximum number allowed of private roads in the path
     * from the starting node to the destination.
     */
    public int maxPrivateRoads() {
        return numPrivate;
    }

    /**
     * @return the maximum number allowed of construction roads
     * in the path from the starting node to the destination.
     */
    public int maxConstructionRoads() {
        return numConstruction;
    }

    /**
     *  a Java Iterator containing the nodes of a path from the start node to the destination
     * node such that the path uses at most maxPrivate private roads and maxConstruction construction roads, if such a path exists. If the path does not exist, this method returns the value
     * null.
     * @param start The starting node.
     * @param destination The end node.
     * @param maxPrivate The limit of private roads.
     * @param maxConstruction The limit of construction roads.
     * @return
     */
    public Iterator findPath(int start, int destination, int maxPrivate, int maxConstruction) {

        Node nodeStart;
        try {
            //It's possible that start does not equal the stored starting node.
            nodeStart = getGraph().getNode(start);
            //It's possible that start does not equal the stored starting node.
            Node endNode = getGraph().getNode(destination);

            Node[] pass = new Node[numNodes];

            List<Node> data = DFS(0,new LinkedList<>(), nodeStart, endNode, maxPrivate, maxConstruction, 0, 0);

            if (data == null || data.isEmpty()){
                return null;
            }

            return data.iterator();
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    //The algorithm for finding a path. The code does not use the nodes' marking methods.
    // This was because it was easier just to use the path to decide if a node was visited or not and makes for robust, modular and more readable code.
    //In theory this would use less memory as only the visited nodes have an entry, but since getMarked is required it is still present.
    public List<Node> DFS(int step, List<Node> path, Node current, Node end, int maxPrivate, int maxConstruction, int currentPri, int currentCon) throws GraphException {
        path.add(current);

        if (current.getId() == end.getId()) {
            return path;
        }

        Iterator<Edge> process = this.getGraph().incidentEdges(current);

        while (process.hasNext()) {
            Edge e = process.next();
            Node next = e.secondNode();

            if (path.contains(next)) {
                continue;
            }

            int tempPri = currentPri;
            int tempCon = currentCon;

            switch (e.getType()) {
                case PR -> {
                    tempPri++;
                }

                case CO -> {
                    tempCon++;
                }
            }

            if (tempCon > maxConstruction || tempPri > maxPrivate) {
                continue;
            }

            List<Node> data = DFS(step + 1, path, next, end, maxPrivate, maxConstruction, tempPri, tempCon);

            if (data != null) {
                return data;
            }
        }

        path.remove(current);
        return null;
    }

    //Create the grid, the documentation for this is unnecessarily complex, confusing and does not explain how to read the inputs very well; it also reuses the value 'B'
    // " R can be any of the following characters: ’+’ or ’B’. H could be ’B’, ’V’, ’C’, or ’P’ " this makes this explanation meaningless.
    //It would have been better to state that the + are the nodes and around the node you have the type of 'road' that connects it.
    private void processNodes(String[] data) throws GraphException {
        int nodeR = 0;
        int nodeC = 0;

        //For each node line
        for (int dr = 0; dr < data.length; dr += 2) {
            String line = data[dr];

            //For every character in the line
            for (int i = 0; i < line.length(); i++) {
                char letter = line.toUpperCase().charAt(i);

                //if the character is a node
                if (letter == '+') {
                    if ((i + 1) < line.length()) {
                        char type = line.toUpperCase().charAt(i + 1);

                        //If the node has a horizontal road
                        if (dr + 1 < data.length) {
                            String belowLine = data[dr + 1];
                            char letterBelow = belowLine.toUpperCase().charAt(i);

                            String roadType = getRoadType(letterBelow);

                            if (roadType != null) {
                                map.addEdge(map.getNode(nodeC), map.getNode(nodeC + this.mapWidth), roadType);
                            }
                        }

                        String roadType = getRoadType(type);

                        if (roadType != null) {
                            map.addEdge(map.getNode(nodeC), map.getNode(nodeC + 1), roadType);
                        }
                    }

                    nodeC++;
                }
            }

            nodeR++;
        }
    }

    //Gets the type of road the edge has.
    private String getRoadType(char type) {
        String roadType = type == 'P' ? PU : type == 'V' ? PR : type == 'C' ? CO : null;
        return roadType;
    }
}
