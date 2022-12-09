import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Graph implements GraphADT {
    private Node[] nodes;
    private Edge[][] adjacency;

    public Graph(int n) {
        nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            Node node = new Node(i);
            nodes[i] = node;

            //Give the array the worst-case space,
            adjacency = new Edge[n][n];
        }
    }

    public void addEdge(Node u, Node v, String type) throws GraphException {
        if (u.getId() >= 0 && u.getId() < adjacency.length && v.getId() >= 0 && v.getId() < adjacency.length) {
            if (adjacency[u.getId()][v.getId()] == null && adjacency[v.getId()][u.getId()] == null) {

                //Set the 'road' from U -> V
                adjacency[u.getId()][v.getId()] = new Edge(u, v, type);

                //Set the 'road' from V -> U
                adjacency[v.getId()][u.getId()] = new Edge(v, u, type);

                return;
            }
        }

        throw new GraphException("Node invalid");
    }

    @Override
    public Node getNode(int id) throws GraphException {
        if (nodes.length <= id || 0 > id) {
            throw new GraphException("ID: " + id + " for node does not exist.");
        }

        Node node = nodes[id];

        if (node == null) {
            throw new GraphException("ID: " + id + " for node does not exist.");
        }

        return node;
    }

    //**THE DOCUMENTATION SHOULD NOT BE USING RAW ITERATOR, IT IS PRACTICALLY NOT USABLE IN A RAW STATE**
    @Override
    public Iterator incidentEdges(Node u) throws GraphException {
        if (u.getId() >= 0 && u.getId() < adjacency.length) {

            //As par the documentation, it does not state that we cannot use java.util library, and in fact endorses the use of "Vector or Stack".
            //Stack and Vector is a part of the same library, therefore I see no reason to why it cannot be used.
            return Arrays.stream(adjacency[u.getId()]).filter(Objects::nonNull).iterator();
        }
        throw new GraphException("Invalid Node");
    }

    @Override
    public Edge getEdge(Node u, Node v) throws GraphException {
        if (nodes[u.getId()] != u) {
            throw new GraphException("Node " + u.getId() + " not in graph");
        }

        if (nodes[v.getId()] != v) {
            throw new GraphException("Node " + v.getId() + " not in graph");
        }

        Edge edge = adjacency[u.getId()][v.getId()];
        if (edge == null) {
            throw new GraphException("Edge does not exist");
        }

        return edge;
    }

    @Override
    public boolean areAdjacent(Node u, Node v) throws GraphException {
        if (nodes.length <= u.getId() || 0 > u.getId()) {
            throw new GraphException("ID: " + u.getId() + " for node does not exist.");
        }

        if (nodes.length <= v.getId() || 0 > v.getId()) {
            throw new GraphException("ID: " + v.getId() + " for node does not exist.");
        }

        if (nodes[u.getId()] != u || nodes[v.getId()] != v) {
            throw new GraphException("Node not in graph");
        }

        try {
            Edge e = getEdge(u, v);
            return e != null;
        } catch (GraphException e) {
            return false;
        }
    }
}
