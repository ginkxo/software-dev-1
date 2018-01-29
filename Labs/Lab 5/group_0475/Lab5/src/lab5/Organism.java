package lab5;

public abstract class Organism {
	
	protected String name;
	protected int xCoord;
	protected int yCoord;
	protected int speed;
	protected String direction;
	
	public Organism(String name, int xCoord, int yCoord, int speed, String direction){
		this.name = name;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.speed = speed;
		this.direction = direction;
		
	}
	
	public abstract void move();
	
	public abstract String toString();
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getXCoord() {
		return this.xCoord;
	}
	
	public void setXCoord(int xcoord) {
		this.xCoord = xcoord;
	}
	
	public int getYCoord() {
		return yCoord;
	}
	
	public void setYCoord(int ycoord) {
		this.yCoord = ycoord;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}

}
