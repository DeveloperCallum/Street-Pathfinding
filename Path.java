import java.util.*;
import java.io.*;

public class Path {

    public static void main (String[] args) {
	Node u, v;
	DrawMap display;
	int delay = 0;

	if (args.length != 1 && args.length != 2)
	    System.out.println("Usage: java Path inputFile");
	else {
	    if (args.length == 2) delay = Integer.parseInt(args[1]);
	    display = new DrawMap(args[0]); // Display roadmap
	    try {
		MyMap streetMap = new MyMap(args[0]);
		BufferedReader in = new BufferedReader(
					new InputStreamReader(System.in));
		System.out.println("Press a key to continue");
		String line = in.readLine();

                /* Invoke student code to compute the path */
		Iterator solution = streetMap.findPath(streetMap.getStartingNode(),
		                            streetMap.getDestinationNode(),
		                            streetMap.maxPrivateRoads(),
		                            streetMap.maxConstructionRoads());

		if (solution != null) {
		    /* Display the path found */
		    if (solution.hasNext()) u = (Node)solution.next();
		    else return;
		    while (solution.hasNext()) {
			v = (Node)solution.next();
			Thread.sleep(delay);
			display.drawEdge(u,v);
			u = v;
		    }
		}
		else {
		    System.out.println("No solution was found");
		    System.out.println("");
		}

		in = new BufferedReader(
					new InputStreamReader(System.in));
		System.out.println("Press a key to finish");
	        line = in.readLine();

	    }
	    catch (MapException e) {
		System.out.println("Error reading input file");
	    }
	    catch (IOException in) {
		System.out.println("Error reading from keyboard");
	    }
	    catch (Exception ex) {
		System.out.println(ex.getMessage());
	    }

	    display.dispose();
	    System.exit(0);
	}
    }
}
