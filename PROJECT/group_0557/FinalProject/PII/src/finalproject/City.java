package finalproject;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Represents a single city in the graph. May or may not be a distribution center. May or may not
 * be connected to other cities.
 *
 */
public class City {
	protected String name;
	protected boolean distribution;
	protected HashMap<City, Integer> cityConnections;
	

    public City(String name, boolean distribution) {
    	this.name = name;
    	this.distribution = distribution;
    	this.cityConnections = new HashMap<City, Integer>();
    }

    /**
     * Prints to console a display of connections from this city and their distance.
     */
    public void showEdges(){
    	for(Entry<City, Integer> connection : cityConnections.entrySet()) //http://stackoverflow.com/questions/9371667/foreach-loop-in-java-for-dictionary
			{
				System.out.println(connection.getKey().getName() + ": " + connection.getValue());
			}
    }
    public String getName() {
    	return name;
    }
}