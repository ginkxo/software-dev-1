package exercise2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class UseGraph {

	/**
	 * @param args
	 * @throws VertexExistsException
	 * @throws GraphIsFullException
	 */

	// Please complete the code for the following method.
	// A correctly working method gets up to 5 marks
	// A quality implementation gets up to 3 marks
	static Graph<String> loadGraph(String pathName)
			throws FileNotFoundException, GraphIsFullException,
			VertexExistsException
	// Loads a String graph from a text file formatted as follows:
	// N: some number -> max number of vertices
	// V: vertexname -> at most N lines for this format
	// E: vertex1, vertex2 -> each line gives an edge from vertex1 to vertex2
	// (vertex1, vertex2 must be strings)
	// Example:
	// N: 5
	// V: A
	// V: B
	// V: C
	// V: D
	// E: A,B
	// E: A,C
	// E: C,D
	// The loadGraph must create the following graph:
	//
	// A --- B
	// |
	// |
	// C --- D
	//
	// If the text file is empty or contains at least the N: ... line, the graph
	// must be empty (not null!)
	//
	{
		// Your code goes here
		
		try {
			File file = new File(pathName);
			Scanner scan = new Scanner(file);
			
			int maxvalue = 0;
			String holder = scan.nextLine().replaceAll("\\s+","");
			if (holder.equals("")){
				maxvalue = 0;
			}
			else if (holder.substring(0, 2).equals("N:") && holder.substring(2,holder.length()).equals("")==false) {
				maxvalue = Integer.parseInt(holder.substring(2, holder.length()));
			} 
			Graph<String> g = new Graph<String>(maxvalue);
					
			while (scan.hasNextLine()) {
				String setter = scan.nextLine().replaceAll("\\s+", "");
				if (setter.substring(0,  2).equals("N:")) {
					continue;
				} else if (setter.substring(0,  2).equals("V:")) {
					g.addVertex(setter.substring(2, setter.length()));
				} else if (setter.substring(0, 2).equals("E:")) {
					String[] splitted = setter.substring(2, setter.length()).split(",");
					g.addEdge(splitted[0], splitted[1]);
				} else {
					scan.close();
				}

			}
			return g;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Graph<String> g2 = new Graph<String>(0);
		return g2;
		
	}

	public static void main(String[] args) throws FileNotFoundException,
			GraphIsFullException, VertexExistsException {
		// TODO Auto-generated method stub
		String pathname = args[0];
		Graph<String> g1 = loadGraph(pathname);
		System.out.println(g1.connectedComponents());
	}

}
