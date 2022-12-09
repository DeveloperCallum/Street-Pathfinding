import java.util.*;

/* Program for testing the Graph methods. */
public class TestGraph {

    private final static String PUB = "public";
    private final static String PRIV = "private";
    private final static String CONSTRUCTION = "construction";
    
    public static void main (String[] args) {

        /* Exceptions testing */

	System.out.println("");
	System.out.println("======================================================");
	System.out.println("TestGraph");
	System.out.println("======================================================");
	System.out.println("");

	Graph G = new Graph (1);
	Node u = new Node(0), v = new Node (1);
	Edge uv;
	Iterator neighbours;

	try {
	    G.addEdge(u,v,PRIV);
	    System.out.println("    Test 1 failed: Method addEdge must throw an exception when");
	    System.out.println("           trying to insert and invalid edge.");
	}
	catch (GraphException e){
	    System.out.println("    Test 1 passed");
	}

	try {
	    u = G.getNode(5);
	    System.out.println("   Test 2 failed: Method getNode must throw an exception when");
	    System.out.println("          trying to access inexistent node.");
	}
	catch (GraphException e){
	    System.out.println("    Test 2 passed");
	}

	try {
	    uv = G.getEdge(u,v);
	    System.out.println("    Test 3 failed: Method getEdge must throw an exception when");
	    System.out.println("           trying to access an invalid edge!");
	}
	catch (GraphException e){
	    System.out.println("    Test 3 passed");
	}

	try {
	    neighbours = G.incidentEdges(v);
	    System.out.println("    Test 4 failed: Method incidentEdges must throw an exception when");
	    System.out.println("           trying to access an invalid node!");
	}
	catch (GraphException e){
	    System.out.println("    Test 4 passed");
	}

	try {
	    boolean adjacent = G.areAdjacent(u,v);
	    System.out.println("    Test 5 failed: Method areAdjacent must throw an exception when");
	    System.out.println("           trying to access an invalid node!");
	}
	catch (GraphException e){
	    System.out.println("    Test 5 passed");
	}


        /* Create a graph with 9 nodes and 11 edges. Query the graph and check that
           all edges are stored correctly                                           */


        /* Set of nodes of the graph */
        Node[] V = new Node[9];

        /* Degree of current node */
        int degree;
        int node1, node2;

        int numNodes = 9;

        /* Degrees pf the nodes in test graph */
        int NodeDegree[] = { 2, 3, 2, 3, 4, 3, 2, 3, 2 };

        /* Adjacency matrix for test graph */
        int M[][] = {{0,2,0,2,0,0,0,0,0},{2,0,2,0,2,0,0,0,0},
                     {0,2,0,0,0,1,0,0,0},{2,0,0,0,2,0,1,0,0},
                     {0,2,0,2,0,2,0,2,0},{0,0,1,0,2,0,0,0,2},
                     {0,0,0,1,0,0,0,2,0},{0,0,0,0,2,0,2,0,2},
                     {0,0,0,0,0,2,0,2,0}};

	String s;
	boolean failed;
	int i = 0, j = 0;

        G = new Graph(9);

	failed = false;
        try {
	    /* Get all nodes of the graph */
	    for (i = 0; i < numNodes; ++i) {
		V[i] = G.getNode(i);
		if (V[i].getId() != i) failed = true;
	    }
	    if (!failed) System.out.println("    Test 6 passed");
	    else  System.out.println("    Test 6 failed");
	}
	catch (GraphException e) {
		System.out.println("    Test 6 failed");
	}

	failed = false;
	try {
	/* Insert the edges */
	for (i = 0; i < 9; ++i)
	    for (j = 0; j < i; ++j)
		if (M[i][j] == 1) G.addEdge(V[i],V[j],CONSTRUCTION);
		else if (M[i][j] == 2)G.addEdge(V[i],V[j],PUB);
	}
	catch (GraphException e) {
	    System.out.println("    Test 7 failed");
	    failed = true;
	}
	if (!failed) System.out.println("    Test 7 passed");

	failed = false;
	try {
	    for (i = 0; i < 9; ++i)
		for (j = 0; j < i; ++j) {
		    if (M[i][j] != 0) {
			uv = G.getEdge(V[i],V[j]);
			s = uv.getType();
			if ((M[i][j] == 1 && !s.equals(CONSTRUCTION)) ||
			    (M[i][j] == 2 && !s.equals(PUB))) failed = true;
		    }
		}
	    if (!failed) System.out.println("    Test 8 passed");
	    else  System.out.println("    Test 8 failed");
	}
	catch (GraphException e) {
	    System.out.println("    Test 8 failed");
	}

	try {
	    if (G.areAdjacent(V[0],V[1]) && !G.areAdjacent(V[2],V[4]))
		System.out.println("    Test 9 passed");
	    else System.out.println("    Test 9 failed");
	}
	catch (GraphException e) {
	    System.out.println("    Test 9 failed");
	}

        try {
            for (i = 0; i < numNodes; ++i) {
                u = G.getNode(i);
                neighbours = G.incidentEdges(u);
                degree = 0;
                while (neighbours.hasNext()) {
                    uv = (Edge)neighbours.next();
                    ++degree;
                    node1 = uv.firstNode().getId();
                    node2 = uv.secondNode().getId();

                    if (M[node1][node2] == 0) {
                        System.out.println("    Error: There should not be an edge between");
                        System.out.println("            nodes "+node1+" and "+node2);
                        failed = true;
                    }
                    else if (((uv.getType()).equals(CONSTRUCTION)) && (M[node1][node2] != 1)) {
                        System.out.println("    Error: There should not be a construction road between");
                        System.out.println("           nodes "+node1+" and "+node2);
                        failed = true;
                    }
		    else if (((uv.getType()).equals(PUB)) &&(M[node1][node2] != 2)) {
                        System.out.println("    Error:There should not be a public road between");
                        System.out.println("          nodes "+node1+" and "+node2);
                        failed = true;
                    }
                }
                if (degree != NodeDegree[i]) {
                    System.out.println("    Error:The degree of node "+i+" should be "+NodeDegree[i]+", not "+degree);
                    failed = true;
                }
            }

	    if (!failed) System.out.println("    Test 10 passed");
	    else  System.out.println("    Test 10 failed");

        }
        catch(GraphException e) {
	    System.out.println("    Test 10 failed");
        }

    }
}


