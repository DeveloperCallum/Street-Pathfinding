/**
 * An edge is practically a road that joins two nodes together.
 */
public class Edge {
    private Node edgeU;
    private Node edgeV;
    private String type;

    /**
     * The constructor for the class. The first two parameters are the endpoints of the edge. The last parameter is the type of the edge, which for this
     * assignment can be either ”public” (if the edge represents a public road), ”private” (if the edge
     * represents a private road), or ”construction” (if the edge represents a road with construction
     * work on it)
     * @param u The first node
     * @param v The second node
     * @param type The type of road, can be "public", "construction", "private".
     */
    public Edge(Node u, Node v, String type) {
        edgeU = u;
        edgeV = v;
        this.type = type;
    }

    /**
     * @return the first endpoint of the edge.
     */
    public Node firstNode() {
        return edgeU;
    }

    /**
     * @return the second endpoint of the edge
     */
    public Node secondNode() {
        return edgeV;
    }

    /**
     * @return the type of the edge.
     */
    public String getType() {
        return type;
    }
}
