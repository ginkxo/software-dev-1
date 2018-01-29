package exercise2;

// Please complete the code for the following method.
// A correctly working class gets up to 2 marks

public class GraphIsFullException extends Exception{
	
// Your code goes here
	public GraphIsFullException() {
		super("The graph is already full.");
	} 
	
	public GraphIsFullException(String message) {
		super(message);
	}
}
