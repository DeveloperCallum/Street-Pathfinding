public class Node {
    private int nodeId;
    private boolean checkedMark = false;
    public Node(int id) {
        nodeId = id;
        checkedMark = false;
    }

    public void markNode(boolean mark) {
        checkedMark = mark;
    }

    public boolean getMark() {
        return checkedMark;
    }
    public int getId() {
        return nodeId;
    }

}
