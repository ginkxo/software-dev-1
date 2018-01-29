package exercise2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import stack.*;

public class Graph<T> implements GraphInterface<T> {
	public static final int DEF_CAPACITY = 10;
	public static final int NULL_EDGE = 0;
	public static final int DEFAULT_WEIGHT = 1;
	private int numVertices;
	private int maxVertices;
	private T[] vertices;
	private int[][] edges;
	private boolean[] marks; // marks[i] is mark for vertices[i]

	public Graph() {
		numVertices = 0;
		maxVertices = DEF_CAPACITY;
		vertices = (T[]) new Object[DEF_CAPACITY];
		marks = new boolean[DEF_CAPACITY];
		edges = new int[DEF_CAPACITY][DEF_CAPACITY];
	}
	
	public Graph(int maxV)
	// Instantiates a graph with capacity maxV.
	{
		numVertices = 0;
		maxVertices = maxV;
		vertices = (T[]) new Object[maxV];
		marks = new boolean[maxV];
		edges = new int[maxV][maxV];
	}

	public boolean isEmpty()
	// Returns true if this graph is empty; otherwise, returns false.
	{
		return (numVertices == 0);
	}

	public boolean isFull()
	// Returns true if this graph is full; otherwise, returns false.
	{
		return (numVertices == maxVertices);
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 2 marks
	// A quality implementation gets up to 2 marks
	public void addVertex(T vertex) throws GraphIsFullException,
			VertexExistsException {
		// If graph is full, it throws GraphIsFullException
		// If vertex is already in this graph, it throws VertexExistsException
		// Otherwise adds vertex to this graph.
		//
		// Your code goes here
		if (this.isFull()) {
			throw new GraphIsFullException();
		} else if (this.BasicArraySearch(vertex)) {
			throw new VertexExistsException();
		} else {
			this.numVertices += 1;
			int i = 0;
			while (this.vertices[i]!=null) {
				i++;
			} 
			this.vertices[i] = vertex;
			
			//for (int i=0; i<this.vertices.length; i++) {
			//	if (this.vertices[i]==null) {
			//		this.vertices[i] = vertex;
			//	}
			//}
		}
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 2 marks
	// A quality implementation gets up to 2 marks
	public void addEdge(T fromVertex, T toVertex)
	// Adds an edge with the specified weight from fromVertex to toVertex.
	{
         // Your code goes here
		int fromIndex = this.IndexGetter(fromVertex);
		int toIndex = this.IndexGetter(toVertex);
		
		if(fromIndex != -1 && toIndex != -1 && fromIndex != toIndex) {
			this.edges[fromIndex][toIndex] = DEFAULT_WEIGHT;
			this.edges[toIndex][fromIndex] = DEFAULT_WEIGHT;
		}
		
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 2 marks

	public Queue<T> getToVertices(T vertex)
	// Returns a queue of the vertices that are adjacent from vertex.
	{
		int vIndex = this.IndexGetter(vertex);
		Queue<T> locals = new LinkedList<T>();
		
		for (int i = 0; i<this.maxVertices; i++) {
			if (edges[vIndex][i]==DEFAULT_WEIGHT ){
				locals.add(vertices[i]);
			}
		}
		
		return locals;

		
	}

	public void clearMarks()
	// Sets marks for all vertices to false.
	{
		for (int i = 0; i < numVertices; i++)
			marks[i] = false;
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 1 mark

	public void markVertex(T vertex)
	// Sets mark for vertex to true.
	{
		int indexer = -1;
		for (T vert: this.vertices) {
			if (vert == null) {
				continue;
			}
			else if (vert.equals(vertex)) {
				indexer = this.IndexGetter(vertex);
				
			}
		}
		if (indexer != -1) {
			this.marks[indexer] = true;
		} else {
			
		}
		
		//for (int i=0; i<this.maxVertices; i++) {
		//	if(this.vertices[i]!=null) {
		//		this.marks[i] = true;
		//	}
		//}
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 1 mark

	public boolean isMarked(T vertex)
	// Returns true if vertex is marked; otherwise, returns false.
	{
		int vIndex = this.IndexGetter(vertex);
		if (marks[vIndex] == true) {
			return true;
		} else {
			return false;
		}
	}

	// Please complete the code for the following method.
	// A correctly working method gets up to 5 marks
	// A quality implementation gets up to 3 marks
	private Set<T> DFSVisit(T startVertex)
	// Uses depth-first search algorithm to visit as much vertices as
	// possible
	// starting from startVertex.
	// In the process, keeps track of all vertices reachable from
	// startVertex
	// by adding them to a Set<T> variable.
	// Once done, returns the Set<T> that we build up.
	//
	{
		Set<T> visited = new HashSet<T>();
		Set<T> fullVertices = new HashSet<T>();
		fullVertices = DFSBasic(startVertex, visited); // helper method
		clearMarks();
		return fullVertices;

		
	}
	

	// Please complete the code for the following method.
	// A correctly working method gets up to 2 marks
	// A quality implementation gets up to 2 marks
	public ArrayList<Set<T>> connectedComponents()
	// Returns a list of connected components of the graph
	// For each vertex that does not belong to the connected components
	// already on the list,
	// call DFSVisit to obtain a set of vertices connected to the current
	// vertex
	// Add the set to the list
	{
		ArrayList<Set<T>> components = new ArrayList<Set<T>>();
		int flag = 0; //counter of instances of the checked vertex
		for (T x: vertices) {
			for (Set<T> sets: components) {
				//iterate through vertices and check if the vertex is in 
				//each of the sets in the array
				//if it isn't, call DFSVisit on it to grab the vertex and
				//all its adjacent vertices (a connected component)
				if(sets.contains(x)) {
					flag++;
				}
			}
			if (flag == 0 && x != null) {
				Set<T> component = DFSVisit(x);
				components.add(component);	
			}
			flag = 0; //reset flag to 0 for the next vertex to be checked
			
		}
		
		return components;
	}
	
	//helper method 1
	public boolean BasicArraySearch(T vertex) {
		for(T x: this.vertices){
			if (vertex.equals(x)) {
				return true;
			} 
			
		}
		return false;
		
	}
	
	//helper method 2
	public int IndexGetter(T vertex) {
		for(int i=0; i<this.vertices.length; i++) {
			if (this.vertices[i] == null){
				continue;
			}
			else if (this.vertices[i].equals(vertex)) {
				return i;
			}
		}
		return -1;
	}
	
	//helper method 3
	//the DFS algorithm requires a visited vertices counter outside of its actual algorithm
	//this is accomplished by using the marks features and embedding DFSBasic into DFSVisit
	private Set<T> DFSBasic (T vertex, Set<T> searchedVertices) {
		searchedVertices.add(vertex);
		markVertex(vertex);
		//System.out.println(searchedVertices);
		Queue<T> localVertex = this.getToVertices(vertex);
		
		if (localVertex.peek() == null){
			return searchedVertices;
		} else {
			while(localVertex.isEmpty() == false) {
				//System.out.println(localVertex.peek());
				if (isMarked(localVertex.peek())) {
					T trash = localVertex.poll(); //toss out values already checked; avoids stack overflow
				} else {
					searchedVertices.addAll(DFSBasic(localVertex.poll(),searchedVertices));
				}
				
			}
			return searchedVertices;
		}
		
		//T localVertex = this.getToVertices(startVertex).poll();
		//searchedVertices.add(startVertex);
		//if (localVertex != null) {
		//	this.DFSVisit(localVertex);
		//} else {
		//	return searchedVertices;
		//}
		//return searchedVertices;
	}
	
	public static void main(String[] args) throws GraphIsFullException, VertexExistsException {
		Graph<Integer> graphie = new Graph(3);
		
		System.out.println(graphie.isEmpty());
		System.out.println(graphie.isFull());
		
		graphie.addVertex(2);
		graphie.addVertex(3);
		
		System.out.println(Arrays.toString(graphie.vertices));
		
		graphie.addVertex(7);
		
		System.out.println(Arrays.toString(graphie.vertices));
		
		graphie.addEdge(2, 3);
		graphie.addEdge(2, 7);
		
		   for(int i = 0; i < 3; i++)
		   {
		      for(int j = 0; j < 3; j++)
		      {
		         System.out.printf("%5d ", graphie.edges[i][j]);
		      }
		      System.out.println();
		   }

		//System.out.println(graphie.getToVertices(2).peek());
		System.out.println(graphie.DFSVisit(2));
		System.out.println(graphie.DFSVisit(7));
		System.out.println(graphie.DFSVisit(2));
		System.out.println(graphie.connectedComponents());
		
		Graph<Integer> graphy = new Graph(6);
		
		graphy.addVertex(1);
		graphy.addVertex(2);
		graphy.addVertex(3);
		graphy.addVertex(4);
		graphy.addVertex(5);
		graphy.addVertex(6);
		
		graphy.addEdge(1, 2);
		graphy.addEdge(1, 3);
		graphy.addEdge(1, 4);
		graphy.addEdge(5, 6);
		
		System.out.println(graphy.connectedComponents());
	}

}
