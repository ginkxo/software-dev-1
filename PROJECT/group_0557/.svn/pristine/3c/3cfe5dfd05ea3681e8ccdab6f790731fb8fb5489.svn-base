package finalproject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is intended to be your API
 * @author Ilir
 *
 */
public class Project {
	Store store;
	
	Project() throws NumberFormatException, IOException {
		store = new Store("storedatas.txt");
	}
	
	/**
	 * This method must add a new shopper user or administrator user.
	 * @param userID
	 * @param password
	 * @param admin -> if true, add an administrator user, otherwise add a shopper user
	 * @return -> true if operation successful, false otherwise
	 * @throws IOException 
	 */
	public boolean addUser(String userID, String password, boolean admin) throws IOException {
		if (admin) {
			return store.createAdmin(userID, password);
		}
		else {
			return store.createShopper(userID, password);
		}
	}
	
	/**
	 * Authenticates a user an creates an active work session
	 * @param userID 
	 * @param password
	 * @return -> SessionID if authentication successful, -1 otherwise.
	 * @throws IOException 
	 */
	public int login(String userID, String password) throws IOException {
		User user = store.login(userID,  password);
		if (user != null) return user.getSessionID();
		else return -1;
	}
	
	/**
	 * Makes sessionID unavailable for connection
	 * @param sessionID
	 */
	public void logout(int sessionID) {
		store.logout(sessionID);
	}
	
	/**
	 * This method must add a new category in your application.
	 * @param catName -> The name of the category to be added.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> The ID of the category you created if successful, -1 if not successful.
	 * @throws IOException 
	 */
	public int addCategory(String catName, int sessionID) throws IOException {
		return Store.addCategory(catName, sessionID);
	}
	
	/**
	 * Adds a distribution center to your application.
	 * If the given distribution center exists, or sesionID invalid, do nothing.
	 * @param city -> The city where distribution center must be based.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @throws IOException 
	 */
	public void addDistributionCenter(String city, int sessionID) throws IOException {
		Store.addCity(city, true, sessionID);
	}
	
	/**
	 * Adds a new Customer to your application; the customer record that belongs 
	 * to a newly added shopper user that has no customer record on the system.
	 * @param custName -> The name of the customer
	 * @param city -> The city of the customer address
	 * @param street -> The street address of the customer
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The added customer ID
	 * @throws IOException 
	 */
	public int addCustomer(String custName, String city, String street, int sessionID) throws IOException {
		City userCity = Store.graph.cityFromString(city);
		return store.modifyShopper(custName, userCity, street, sessionID);
	}
	
	/**
	 * Adds a new Product to your application
	 * @param prodName -> The product name
	 * @param category -> The product category.
	 * @param price -> The product sales price
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> Product ID if successful, -1 otherwise.
	 * @throws IOException 
	 */
	public int addProduct(String prodName, int category, double price, int sessionID) throws IOException {
		Category cat = Store.getCategories().get(category);
		return Store.addProduct(cat, "", prodName, "", price, sessionID);
	}
	
	/**
	 * Computes the available quantity of prodID in a specific distribution center.
	 * @param prodID
	 * @param center
	 * @return -> Available quantity or -1 if prodID or center does not exist in the database
	 */
	public int prodInquiry(int prodID, String center) {
		if (Store.graph.cityFromString(center) != null &&
			Store.getProductList().get(prodID) != null) {
			City city = Store.graph.cityFromString(center);
			if (city.distribution) {
			return Store.getProductList().get(prodID).getStock().get(city);
			}
		}
		return -1;
	}
	
	/**
	 * Updates the stock quantity of the product identified by prodID
	 * @param prodID -> The product ID to be updated
	 * @param distCentre -> Distribution Center (in effect a city name)
	 * @param quantity -> Quantity to add to the existing quantity
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * If currently the product 112 has quantity 100 in Toronto,
	 * after the statement updateQuantity(112, "Toronto", 51)
	 * same product must have quantity 151 in the Toronto distribution center. 
	 * @return -> true if the operation could be performed, false otherwise.
	 * @throws IOException 
	 */
	public boolean updateQuantity(int prodID, String distCentre, int quantity, int sessionID) throws IOException {
		if (Store.getProductList().get(prodID) != null &&
				Store.graph.cityFromString(distCentre) != null &&
				Store.graph.cityFromString(distCentre).distribution && Store.isAdmin(sessionID))
		{
				Product p = Store.getProductList().get(prodID);
				return Store.modifyQuantity(p, Store.graph.cityFromString(distCentre), quantity, sessionID);
		}
		return false;
	}
	
	/**
	 * Adds two nodes cityA, cityB to the shipping graph
	 * Adds a route (an edge to the shipping graph) from cityA to cityB with length distance
	 * If the nodes or the edge (or both) exist, does nothing
	 * @param cityA 
	 * @param cityB
	 * @param distance -> distance (in km, between cityA and cityB)
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @throws IOException 
	 */
	public void addRoute(String cityA, String cityB, int distance, int sessionID) throws IOException {
		if (Store.isAdmin(sessionID)) {
			if (Store.graph.cityFromString(cityA) == null) Store.addCity(cityA, false, sessionID);
			if (Store.graph.cityFromString(cityB) == null) Store.addCity(cityB, false, sessionID);
			Store.addEdge(Store.graph.cityFromString(cityA), Store.graph.cityFromString(cityB), distance, sessionID);
			}
	}

	/**
	 * Attempts an order in behalf of custID for quantity units of the prodID
	 * @param custID -> The customer ID
	 * @param prodID -> The product ID
	 * @param quantity -> The desired quantity
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The orderID if successful, -1 if not.
	 * @throws IOException 
	 */
	public int placeOrder(int custID, int prodID, int quantity, int sessionID) throws IOException {
		Shopper customer = null;
		for (User u : Store.getUserList().values())
		{
			if (u.getUserID() == custID && Store.isShopper(u.sessionID) && u.sessionID == sessionID)
			{
				customer = (Shopper) u;
				return customer.buyItNow(Store.getProductList().get(prodID), quantity);
			}
		}
		return -1;
	}
    
	/**
	 * Returns the best (shortest) delivery route for a given order 
	 * @param orderID -> The order ID we want the delivery route
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The actual route as an array list of cities, null if not successful
	 */
	public ArrayList<String> getDeliveryRoute(int orderID, int sessionID) {
		if (Store.isShopper(sessionID)) {
			Shopper shopper = (Shopper) Store.getsessionIDList().get(sessionID);
			if (shopper.getInvoice(orderID) != null) {
			return shopper.getInvoice(orderID).getDeliveryRoute();
			}
		}
		return null;
	}
	
	/** 
	 * Computes the invoice amount for a given order.
	 * Please use the fixed price 0.01$/km to compute the shipping cost 
	 * @param orderID
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return the total cost of the order associated with this invoice
	 */
	public double invoiceAmount(int orderID, int sessionID) {
		if (Store.isShopper(sessionID)) {
			Shopper shopper = (Shopper) Store.getsessionIDList().get(sessionID);
			if (shopper.getInvoice(orderID) != null) {
			return shopper.getInvoice(orderID).getShippingCost() + shopper.getInvoice(orderID).getItemsCost();
			}
		}
		return -1;
	}
}