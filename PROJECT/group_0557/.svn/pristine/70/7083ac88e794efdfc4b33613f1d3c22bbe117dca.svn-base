//last update: aug 3 3:21 am

package finalproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;




/**
 * Graph is a class that handles information about all the cities in one simulation.
 * A Graph object stores a list of existing cities in one simulation,
 * updates paths between cities, and finds consecutive paths from distribution cities
 * to destination cities. 
 *
 */

public class Graph {
	private ArrayList<City> cities;
	
	public Graph() {
    	cities = new ArrayList<City>();
    }
    
    /**
     * Adds a City object to the list of cities in Graph. Any City object can be added to this list, 
     * regardless of being a distribution center or not.
     * @param city	-> the city to be added to the list
     */
    
    public void addCity(City city){
    	cities.add(city);
    }

	public ArrayList<City> getCities() {
		return cities;
	}
    
    /**
     * Adds a route from City A to City B, noting the distance between the two. 
     * City B becomes a key in City A's cityConnections, where the distance is the corresponding value, 
     * and vice versa. 
     * @param a -> the first city
     * @param b -> the second city
     * @param distance -> the distance between the two cities
     * @return true is edge added successfully, false otherwise.
     */
    
    public boolean addEdge(City a, City b, int distance){
    	if (!a.cityConnections.containsKey(b) && !a.equals(b)) {
    		a.cityConnections.put(b, distance);
    		b.cityConnections.put(a, distance);
    		return true;
    	}
    	return false;
    }

    @Override
    public String toString() {
    	String output = "";
    	for (City city : cities) {
    		output += city.name + ": ";
    		for (City connection : city.cityConnections.keySet()) {
    			output += "[" + connection.getName() + ", " +
    					city.cityConnections.get(connection) + "], ";
    		}
    		output = output.substring(0, output.length() - 2);
    		output += "\n";
    	}
    	return output;
    }

    /**
     * Determines best distribution center to ship from to city "start".
     * @param start -> The customer's location
     * @return -> The cheapest distribution center to ship from.
     */
    public City getBestDistributionCenter(City start) {
    	try {
    		@SuppressWarnings("unchecked")
			City x = ((ArrayList<City>) getShortestHelper(start)[1]).get(0);
    		return x;
    	} catch (ClassCastException|NullPointerException e) {return null;}
    }    	
    /**
     * Computes shipping cost based on customer's location and best distribution center for that customer.
     * @param start -> The customer's location
     * @return -> Total distance of optimal shipping route / 100 to use fixed cost of $0.01 per km.
     */
    public double getShortestRouteCost(City start) {
    	try {
    		double x = ((double) getShortestHelper(start)[0] / 100);
    		return x;
    	} catch (ClassCastException e) {return -1;}
    }
    
    /**
     * Find the shortest shipping route in graph to start location.
     * @param start -> The customer's location.
     * @return An array list of cities completing the most cost efficient path from the best distribution center for the shopper,
     * and ending at the shopper's city. Null if no path is possible.
     */
	public ArrayList<City> getShortestRoute(City start) {
    	try {
    		@SuppressWarnings("unchecked")
			ArrayList<City> x = (ArrayList<City>) getShortestHelper(start)[1];
    		return x;
    	} catch (ClassCastException e) {return null;}
    }

    /**
     * Find the shortest shipping route in graph to start location.
     * @param start -> The customer's location.
     * @return An arraylist of cities completing the most cost efficient path from the best distribution center for the shopper,
     * and ending at the shopper's city. Null if no path is possible.
     */
    private Object[] getShortestHelper(City start) {
    	class Route{
    		City currentCity;
    		ArrayList<City> visitedCities;
    		int distance;
    		Route(City currentCity, ArrayList<City> visitedCities, int distance) {
    			this.currentCity = currentCity;
    			this.visitedCities = new ArrayList<City>();
    			if (visitedCities != null) {
    				for (City city: visitedCities) this.visitedCities.add(city);
    			}
    			this.distance = distance;
    		}
    		void addVisited(City city) {
   			visitedCities.add(city);
    		}
    	}
    	LinkedList<Route> toVisit = new LinkedList<Route>();
    	int shortestPath = -1;
    	Route bestPath = new Route(start,null,0);

    	toVisit.add(new Route(start,null,0));
    	while (!toVisit.isEmpty()) {
        	Route currentNode = toVisit.removeFirst();
        	if (shortestPath != -1 && currentNode.distance > shortestPath) {
        		continue;
        	}
        	if (currentNode.currentCity.distribution) {
        		if ((shortestPath == -1) || (currentNode.distance < shortestPath)) {
        			shortestPath = currentNode.distance;
        			bestPath = currentNode;
        			continue;
        		}	
        	}
        	for (City city : currentNode.currentCity.cityConnections.keySet()) {        		
        		if (!currentNode.visitedCities.contains(city)) {
        			ArrayList<City> newVisited = new ArrayList<City>();
        			for (City city2 : currentNode.visitedCities) newVisited.add(city2);
        			newVisited.add(currentNode.currentCity);
        			int newDistance = currentNode.distance;
        			newDistance += currentNode.currentCity.cityConnections.get(city);
        			Route newRoute = new Route(city, newVisited, newDistance);
        			toVisit.add(newRoute);
        		}
        	}
    	}
		bestPath.addVisited(bestPath.currentCity);
		Collections.reverse(bestPath.visitedCities);
    	if (bestPath != null && bestPath.visitedCities.get(0).distribution) {
    		return new Object[] {((double)bestPath.distance), bestPath.visitedCities};}
    	else {return new Object[2];}
    }
    
    /**
     * @param cityName
     * @return City object with name cityName if it exists, otherwise return null.
     */
    public City cityFromString(String cityName) {
    	for (City i : cities) {
    		if (i.getName().equals(cityName)) return i;
    	}
    	return null;
    }

	/**
	 * @return ArrayList of cities that do not have distribution centers.
	 */
	public ArrayList<City> getNonDistCenters() {
		ArrayList<City> toReturn = new ArrayList<City>();
		for (City c : cities) {
			if (c.distribution == false)
				toReturn.add(c);
		}
		return toReturn;
	}
	//

	/**
	 * @return ArrayList of cities that do have distribution centers.
	 */
	public ArrayList<City> getDistributionCenters() {
		ArrayList<City> toReturn = new ArrayList<City>();
		for (City c : cities) {
			if (c.distribution)
				toReturn.add(c);
		}
		return toReturn;
	}
}