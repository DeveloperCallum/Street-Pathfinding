/**
 * This class represent a node of the graph
 */
public class Node {
    private int nodeId;
    private boolean checkedMark = false;

    /**
     * This is the constructor for the class and it creates a node with the given id.
     * The id of a node is an integer value between 0 and n − 1, where n is the number of nodes in
     * the graph
     *
     * @param id The ID for the node
     */
    public Node(int id) {
        nodeId = id;
        checkedMark = false;
    }

    /**
     * This is the constructor for the class and it creates a node with the given id.
     * The id of a node is an integer value between 0 and n − 1, where n is the number of nodes in
     * the graph
     *
     * fyi: was not useful.
     * @param mark
     */
    public void markNode(boolean mark) {
        checkedMark = mark;
    }

    /**
     * @return the value with which the node has been marked.
     */
    public boolean getMark() {
        return checkedMark;
    }

    /**
     * @return the id of this node.
     */
    public int getId() {
        return nodeId;
    }

}
