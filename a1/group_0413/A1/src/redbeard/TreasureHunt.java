package redbeard;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import heap.HeapEmptyException;
import heap.HeapFullException;
import world.Grid;
import world.Node;

//TODO: We might need to initialize the path variable somehow because pathLength shouldn't return 0...
//TODO: processCommand() - How to end the game and how to drop a sonar...
//TODO: play() - Error Checking and Method Correctness


public class TreasureHunt {



	private final int DEFAULT_SONARS = 3; // default number of available sonars
	private final int DEFAULT_RANGE = 200; // default range of a sonar
	protected Grid islands; // the world where the action happens!
	protected int height, width, landPercent;
	protected int sonars, range; // user defined number of sonars and range
	protected String state; // state of the game (STARTED, OVER)
	protected ArrayList<Node> path; // the path to the treasure!

	public TreasureHunt() throws HeapFullException, HeapEmptyException {
		// The default constructor
        this.sonars = DEFAULT_SONARS;
        this.range = DEFAULT_RANGE;
        this.islands = new Grid();
        this.height = this.islands.getHeight();
        this.width = this.islands.getWidth();
        this.landPercent = this.islands.getPercent();
        this.state = "STARTED";
//        this.path = this.islands.retracePath(this.islands.getBoat(), this.islands.getTreasure(DEFAULT_RANGE));
        this.path = new ArrayList<Node>();
	}

	public TreasureHunt(int height, int width, int landPercent, int sonars,
			int range) throws HeapFullException, HeapEmptyException {
		// The constructor that uses parameters
		this.sonars = sonars;
		this.range = range;
		this.height = height;
		this.width = width;
		this.landPercent = landPercent;
		this.islands = new Grid(width, height, landPercent);
        this.state = "STARTED";
        this.path = new ArrayList<Node>();
//        this.path = this.islands.retracePath(this.islands.getBoat(), this.islands.getTreasure(this.range));
	}

	private void processCommand(String command) throws HeapFullException,
			HeapEmptyException {
		// The allowed commands are: 
		// SONAR to drop the sonar in hope to detect treasure
		// GO direction to move the boat in some direction
		// For example, GO NW means move the boat one cell up left (if the cell is navigable; if not simply ignore the command)

        if (command.equals("SONAR")){
            System.out.println(range);
            if(sonars > 0) {
                sonars--;
                if(islands.getTreasure(range) != null) {
                    islands.findPath(islands.getBoat(), islands.getTreasure(range));
                    this.path = islands.retracePath(islands.getBoat(), islands.getTreasure(range));
                }
            }else{
                state = "OVER";
            }

        }else if(command.substring(0,2).equals("GO")){
            islands.move(command.substring(2, command.length()));
        }

	}

	public int pathLength() {
		if (path == null)
			return 0;
		else return path.size();
	}

	public String getMap() {
		return islands.drawMap();
	}

	public void play(String pathName) throws IOException,
            HeapFullException, HeapEmptyException {
		// Read a batch of commands from a text file and process them.

        FileReader fileReader = new FileReader(pathName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String data, line;

        while ((line = bufferedReader.readLine()) != null){

            data = line;
            data = data.replaceAll("\\s+", "");

            if(!data.isEmpty()){

                if (data.equals("SONAR") || data.substring(0,2).equals("GO")){
                    processCommand(data);
                }

            }

        }

        bufferedReader.close();
        fileReader.close();

	}

}