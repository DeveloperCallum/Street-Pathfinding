import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

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

    //TODO FIX
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

    public Graph getGraph() {
        return map;
    }

    public int getStartingNode() {
        return startNode;
    }

    public int getDestinationNode() {
        return endNode;
    }

    public int maxPrivateRoads() {
        return numPrivate;
    }

    public int maxConstructionRoads() {
        return numConstruction;
    }

    public Iterator findPath(int start, int destination, int maxPrivate, int maxConstruction) {

        Node nodeStart;
        try {
            //It's possible that start does not equal the stored starting node.
            nodeStart = getGraph().getNode(start);
            //It's possible that start does not equal the stored starting node.
            Node endNode = getGraph().getNode(destination);

            Node[] pass = new Node[numNodes];
            Node[] data = DFS(0,pass, nodeStart, endNode, maxPrivate, maxConstruction, 0, 0);

            if (data == null){
                return null;
            }

            return Arrays.stream(data).filter(Objects::nonNull).iterator();
        } catch (GraphException e) {
            throw new RuntimeException(e);
        }
    }

    //Everytime a new node gets tested add it to the returned stack. If DFS does not return NULL consider that the path
    public Node[] DFS(int step, Node[] path, Node current, Node end, int maxPrivate, int maxConstruction, int currentPri, int currentCon) throws GraphException {
        current.markNode(true); //The node has been visited!
        path[step] = current;

        if (current.getId() == end.getId()) {
            return path;
        }

        Iterator<Edge> process = this.getGraph().incidentEdges(current);

        while (process.hasNext()) {
            Edge e = process.next();
            Node next = e.secondNode();

            if (next.getMark()) {
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

            Node[] data = DFS(step + 1, path, next, end, maxPrivate, maxConstruction, tempPri, tempCon);

            if (data != null) {
                return data;
            }

        }

        path[step] = null;
        return null;
    }

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

    private String getRoadType(char type) {
        String roadType = type == 'P' ? PU : type == 'V' ? PR : type == 'C' ? CO : null;
        return roadType;
    }
}
