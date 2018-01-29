package exercise2;

// Please complete the code for the following method.
// A correctly working class gets up to 2 marks

public class VertexExistsException extends Exception{
	
// Your code goes here
	public VertexExistsException() {
		super("The vertex already exists.");
	}
	
	public VertexExistsException(String message) {
		super(message);
	}

}