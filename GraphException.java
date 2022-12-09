public class GraphException extends Exception {
  public GraphException(String mssg) {
    super(mssg);
  }

  public GraphException() {
      System.out.println("Exception in class Graph");
  }
}