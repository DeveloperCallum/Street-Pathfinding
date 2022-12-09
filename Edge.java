/**
 * An edge is practically a road that joins two nodes together.
 */
public class Edge {
    private Node edgeU;
    private Node edgeV;
    private String type;

    public Edge(Node u, Node v, String type) {
        edgeU = u;
        edgeV = v;
        this.type = type;
    }

    public Node firstNode() {
        return edgeU;
    }

    public Node secondNode() {
        return edgeV;
    }

    public String getType() {
        return type;
    }
}
