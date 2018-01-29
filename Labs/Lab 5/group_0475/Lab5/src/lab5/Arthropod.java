package lab5;
import java.util.Random;

public class Arthropod extends Organism{
	
	private int numLegs;
	private Random rand = new Random(4);

	public Arthropod(String name, int xCoord, int yCoord, int speed, String direction, int numLegs) {
		super(name, xCoord, yCoord, speed, direction);
		this.numLegs = numLegs;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		direction = "East";
		xCoord += speed;
		yCoord += speed;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "name, xCoord, yCoord, speed, and direction of Anthropod, in that order: " + name + " " + xCoord + " " + yCoord + " " + speed + " " + direction;
	}
	
	public int getNumLegs(){
		return numLegs;
	}
	
	public void setNumLegs(int numLegs){
		this.numLegs = numLegs;
	}

}
