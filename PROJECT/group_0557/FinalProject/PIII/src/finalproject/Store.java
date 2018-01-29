package finalproject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The central hub for everything in this package. Whenever a user wants to make a change, they
 * usually have to go through Store to accomplish it. Store ensures that only users with correct
 * permissions are able to carry out tasks. Store also ensures synchronization between the data
 * held in memory and the data stored in the CSV files.
 *
 */
@SuppressWarnings("unused")
public class Store {
	
	private static int userIDGenerator = 10000000;
	private static int categoryIDGenerator = 40000000;
	private static int productIDGenerator = 70000000;
	private int revenueGenerated;
	private static HashMap<String, User> userList; // connects username to user object.
	private static HashMap<Integer, Product> productList; // connects productID to Product object.
	private static DBManager csvDB;
	private static HashMap<Integer, User> sessionIDList; // connects sessionID to User object
	private static HashMap<Integer, Category> categories;
//    private static HashMap<String, City> cities; // connects city name to city object
    protected static Graph graph;

	/**
	 * Constructor that takes a .csv file pathname. 
	 * Information such as products and users stored in the csv file is loaded 
	 * into the class as different data structures.
	 * 
	 * @param csvPath	The .csv file that the constructor loads from.
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public Store(String csvPath) throws NumberFormatException, IOException{
		csvDB = new DBManager(csvPath);
        categories = csvDB.loadCategories(); //empty arraylist for product categories
        graph = csvDB.loadCities();
        productList = csvDB.loadProducts(graph); //returns hashmap of products
		userList = csvDB.loadUsers(productList); //returns hashmap of users
		sessionIDList = new HashMap<Integer, User>(); //empty hashmap of sessionIDs
        csvDB.loadInvoices(); //load up the invoices
		
		// set userIDGenerator to appropriate value
		for (User user : userList.values()) {
			if (user.getUserID() >= userIDGenerator) userIDGenerator = user.getUserID() + 1;
		}
		// set categoryIDGenerator to appropriate value
		for (Category cat : categories.values()) {
			if (cat.getID() >= categoryIDGenerator) categoryIDGenerator = cat.getID() + 1;
		}
		// set productIDGenerator to appropriate value
		for (Product prod : productList.values()) {
			if (prod.getProductID() >= productIDGenerator) productIDGenerator = prod.getProductID() + 1;
		}	
	}
	
	
	/**
	 * Returns a username if a successful login is performed by a user. A successful log-in by a user will also generate
	 * a unique sessionID as an authentication token. 
	 * <p>
	 * A log-in is successful if the username the user provides is in the csv file the Store reads from, and the password 
	 * provided is correct. 
	 * 
	 * @param username	The username to login to the Store
	 * @param password	The password to go with the username provided
	 * @return The user who logged in. null if login unsuccessful.
	 * @throws IOException 
	 */
	public static User login(String username, String password) throws IOException{
		if (userList.get(username) != null) {
			if (userList.get(username).getPassword().equals(password)) {
				User user = null;
				if (Shopper.class.equals(userList.get(username).getClass())) {
						user = (Shopper) userList.get(username);
						verifyCart((Shopper)user);
				} else if (Administrator.class.equals(userList.get(username).getClass())) {
						user = (Administrator) userList.get(username);
				}  else {return null;}
				Random rand = new Random();
				int newID = rand.nextInt(900000) + 100000;
				while (sessionIDList.containsKey((Integer)newID)) newID = rand.nextInt(900000) + 100000;
				user.setSessionID(newID);
				sessionIDList.put(user.getSessionID(), user);
				return user;
			}

		}
		return null;
	}
	
	/**
	 * Logs out user, removes sessionID from list of active session. If the user was a shopper,
	 * releases all reserved quantities to other potential shoppers.
	 * @param sessionID
	 */
	public void logout(int sessionID) {
		User user = null;
		if (Shopper.class.equals(sessionIDList.get(sessionID).getClass())) {
			user = (Shopper) sessionIDList.get(sessionID);
			for (Product p : ((Shopper) user).cart.keySet()) {
				// Free up all quantity user had reserved in cart
				p.setQuantity(p.getQuantity() + ((Shopper)user).cart.get(p));
			}
		} else if (Administrator.class.equals(sessionIDList.get(sessionID).getClass())) {
			user = (Administrator) sessionIDList.get(sessionID);
		}
		sessionIDList.remove(sessionID);
		user.setSessionID(0);
	}
	
	/**
	 * Verifies that the items in the user's cart are still in stock. If item stock is lower 
	 * than shopper's cart total, lower the cart total to the stock total. If stock is 0, remove
	 * the item from the user's cart.
	 * @param user -> the shopper whose cart we are verifying.
	 * @return true if the cart was modified, false if cart was not changed.
	 * @throws IOException 
	 */
	private static boolean verifyCart(Shopper user) throws IOException {
		boolean changed = false;
		for (Product item : user.cart.keySet()) {
			if (user.cart.get(item) <= item.getQuantity()) {
				item.setQuantity(item.getQuantity() - user.cart.get(item));
			} else if (user.cart.get(item) > item.getQuantity() && item.getQuantity() > 0) {
				user.cart.put(item, item.getQuantity());
				item.setQuantity(0);
				changed = true;
			} else if (item.getQuantity() <= 0) {
				user.cart.remove(item);
				changed = true;			
			}
		}
		if (changed) csvDB.updateUser(user);
		return changed;
	}

	/**
	 * Creates and adds a new Shopper into the Store. The new shopper is put into the list of Users, and then written 
	 * to the CSV file the Store reads from. 
	 * 
	 * @param username	The username to login to the Store
	 * @param password	The password to go with the username provided
	 * @return 			true if successful, false if not
	 * @throws IOException 

	 */
	public boolean createShopper(String username, String password) throws IOException{
		if (Store.getUserList().keySet().contains(username)) return false;
		else {
		Shopper s = new Shopper(username,password);
		s.setUserID(userIDGenerator);
		userIDGenerator++;
		userList.put(s.getUserName(), s);
		csvDB.addUser(s);
		return true;
		}
	}

	/**
	 * Create a new Shopper object and add it to the database.
	 * @param username
	 * @param password
	 * @param city
	 * @param address
	 * @return true is successful, false otherwise.
	 * @throws IOException
	 */
	public boolean createShopper(String username, String password, City city, String address) throws IOException{
		if (Store.getUserList().keySet().contains(username)) return false;
		else {
		Shopper s = new Shopper(username,password);
		s.setAddress(address);
		s.setCity(city);
		s.setUserID(userIDGenerator);
		userIDGenerator++;
		userList.put(s.getUserName(), s);
		csvDB.addUser(s);
		return true;
		}
	}
	
	/**
	 * @param custName		Customer's real name
	 * @param city			City object representing the city the Shopper lives in
	 * @param address		Street address of the Shopper
	 * @param sessionID 	A valid sessionID of a logged in Customer
	 * @return				The shopper's customer ID, -1 if it fails.
	 * @throws IOException 
	 */
	public int modifyShopper(String custName, City city, String address, int sessionID) throws IOException {
		if (isShopper(sessionID)) {
			Shopper shopper = (Shopper) Store.sessionIDList.get(sessionID);
			shopper.setRealName(custName);
			shopper.setCity(city);
			shopper.setAddress(address);
			csvDB.updateUser(shopper);
			return shopper.getUserID();
		}
		return -1;		
	}

	/**
	 * Creates and adds a new Administrator into the Store. The new Administrator is put into the list of users, and then 
	 * written to the CSV file the Store reads from. 
	 * 
	 * @param username	The username to login to the Store
	 * @param password	The password to go with the username provided
	 * @return 			true if successful, false if not
	 * @throws IOException 
	 */
	public boolean createAdmin(String username, String password) throws IOException{
		if (Store.getUserList().keySet().contains(username)) return false;
		else {
		Administrator a = new Administrator(username, password);
		a.setUserID(userIDGenerator);
		userIDGenerator++;
		userList.put(a.getUserName(), a);
		csvDB.addUser(a);
		return true;
		}
	}
	
	
	
	/**
	 * Adds a new Product into the store. The new Product is then put into the list of products, and then written to the
	 * CSV file the Store reads from. The new Product will have an initial quantity of zero. 
	 * <p>
	 * A Product can only be added if the Store determines that an Administrator is accessing this method. A sessionID
	 * that belongs to an Administrator must be provided.
	 * 
	 * @param category		Category type of the Product, for example a T-shirt
	 * @param image			An Image of the Product. 
	 * @param name 			The name of the Product.
	 * @param description	The description of the Product.
	 * @param price			The price of the Product.
	 * @param sessionID		The sessionID of the user who is attempting to use this method. 
	 * @return -> The productID. -1 if unsuccessful.
	 * @throws IOException 
	 */
	public static int addProduct(Category category, String image, String name, String description, double price, int sessionID) throws IOException{
		if (isAdmin(sessionID)) {
			Product p = new Product(category, image, name, description, price);
			p.setProductID(productIDGenerator);
			for (City c : graph.getDistributionCenters())
			{
				p.addDistributionCenter(c);
			}
			productIDGenerator++;
			productList.put(p.getProductID(), p);
			csvDB.addProduct(p);
			return p.getProductID();
		}
		return -1;

	}

	/**
	 * Add a new product to the store.
	 * @param product
	 * @param sessionID
	 * @return -> ProductID if successful. -1 otherwise.
	 * @throws IOException
	 */
	public static int addProduct(Product product, int sessionID) throws IOException {
		if (isAdmin(sessionID)) {
			Product p = product;
			p.setProductID(productIDGenerator);
			for (City c : graph.getDistributionCenters())
			{
				p.addDistributionCenter(c);
			}
			productIDGenerator++;
			productList.put(p.getProductID(), p);
			csvDB.addProduct(p);
			return p.getProductID();
		}
	return -1;
	}
	/**
	 * Modifies a Product that currently exists in the Store. The updated Product is written back into the CSV file the
	 * Store reads from. 
	 * <p>
	 * A Product can only be modified if the Store determines that an Administrator is accessing this method. A sessionID
	 * that belongs to an Administrator must be provided. 
	 * 
	 * @param product		The Product that is being modified. 
	 * @param category 
	 * @param image			An Image of the Product. 
	 * @param name 			The name of the Product.
	 * @param description	The description of the Product.
	 * @param price			The price of the Product
	 * @param sessionID		The sessionID of the user who is attempting to use this method. 
	 * @throws IOException 
	 */
	public static void modifyProduct(Product product, Category category, String image,
			String name, String description, double price, int sessionID) throws IOException{
		if (isAdmin(sessionID)){
			product.setCategory(category);
			product.setDescription(description);
			product.setPrice(price);
			product.setName(name);
			product.setImage(image);
			csvDB.updateProduct(product);
		}
	}
	
	/**
	 * Modifies the quantity in a particular warehouse for a particular product.
	 * @param product -> The product that is being modified.
	 * @param city -> The city in which the distribution center stock is changing.
	 * @param newStock -> The quantity value to change to
	 * @param sessionID -> a sessionID to check if a logged in admin is attempting the change.
	 * @return -> true is successful, false otherwise.
	 * @throws IOException
	 */
	public static boolean modifyQuantity(Product product, City city, int newStock, int sessionID) throws IOException {
		if (isAdmin(sessionID) && newStock >= 0){
			product.setStock(city, newStock);
			csvDB.modifyQuantity(product, city, newStock);
			return true;
		}
		return false;
	}

	/**
	 * Returns a HashMap of Users in this Store.
	 * @return HashMap<username: User object>
	 *
	 */
	public static HashMap<String, User> getUserList(){
		return userList;
	}
	
	/**
	 * Returns a HashMap of Products in this store. 
	 * @return HashMap<productID: Product object>
	 */
	public static HashMap<Integer, Product> getProductList(){
		return productList;
	}
		
	/**
	 * Check if this is a valid sessionID
	 * @param sessionID
	 * @return true is this sessionID belongs to a logged in user, false otherwise.
	 */
	public static boolean authenticate(int sessionID){
		User checker = sessionIDList.get(sessionID);
		return checker != null;
		
	}
	
	/**
	 * Returns a boolean value to check if an authenticated user is an Administrator by checking the user's sessionID.
	 * @param sessionID		The sessionID of the user who is attempting to use this method. 
	 * @return True is sessionID belongs to a logged in administrator, false otherwise.
	 */
	public static boolean isAdmin(int sessionID){
		if (authenticate(sessionID)){
			User user = sessionIDList.get(sessionID); 
			return user.getClass().equals(Administrator.class);
		}
		return false;
	}

	/**
	 * Returns a boolean value to check if an authenticated user is an Administrator by checking the user's sessionID.
	 * @param sessionID		The sessionID of the user who is attempting to use this method. 
	 * @return True is sessionID belongs to a logged in shopper, false otherwise.
	 */
	public static boolean isShopper(int sessionID){
		if (authenticate(sessionID)){
			User user = sessionIDList.get(sessionID); 
			return user.getClass().equals(Shopper.class);
		}
		return false;
	}

	/**
	 * Adds a category of products to the ArrayList of categories in the Store. 
	 * <p>
	 * A category can only be added if the Store determines that an Administrator is accessing this method. A sessionID
	 * that belongs to an Administrator must be provided. 
	 * 
	 * @param category		Category of Product to be added.
	 * @param sessionID		The sessionID of the user attempting to use this method. 	
	 * @return CategoryID if successful, -1 otherwise.
	 * @throws IOException 

	 */
	public static int addCategory(String category, int sessionID) throws IOException{
		if (isAdmin(sessionID)){
			for (Category c : Store.getCategories().values()) {
				if (c.getName().equals(category)) return -1;
			}
			Category newCat = new Category(category, categoryIDGenerator);
			categories.put(categoryIDGenerator, newCat);
			categoryIDGenerator++;
			csvDB.addCategory(newCat);
			return newCat.getID();
		}
		return -1;
	}

	/**
	 * Creates a string showing all products sold, their quantity sold, and the total revenue from those sales.
	 * @return A string of all sales in the store to date.
	 */
	public static String produceSalesReport() {
		ArrayList<Product> ProductSold = new ArrayList<Product>();
		ArrayList<Integer> QuantitySold = new ArrayList<Integer>();
		ArrayList<Double> TotalCost = new ArrayList<Double>();
		Double totalSales = (double) 0;
		
		for (User u : userList.values()) {
			if (Shopper.class.equals(u.getClass())) {
				Shopper s = (Shopper) u;
				for (Invoice i : s.getAllInvoices().values()) {
					for (Product p : i.getPurchaseList().keySet()) {
						if (ProductSold.contains(p)) {
							int index = ProductSold.indexOf(p);
							QuantitySold.set(index, (int) (QuantitySold.get(index)+i.getPurchaseList().get(p)[0]));
							TotalCost.set(index,
									(TotalCost.get(index)+i.getPurchaseList().get(p)[0]*i.getPurchaseList().get(p)[1]));
							totalSales += i.getPurchaseList().get(p)[0]*i.getPurchaseList().get(p)[1];
						} else {
							ProductSold.add(p);
							QuantitySold.add( (int) i.getPurchaseList().get(p)[0]);
							TotalCost.add(i.getPurchaseList().get(p)[0] * i.getPurchaseList().get(p)[1]);							
							totalSales += i.getPurchaseList().get(p)[0]*i.getPurchaseList().get(p)[1];
						}
						
					}
				}
			}
		}
		
		String report = "SALES REPORT\n"
					  + "============\n"
					  + "NAME    ||     QUANTITY SOLD     ||     REVENUE\n";
		for (int i=0; i<ProductSold.size() ; i++) {
			report += ProductSold.get(i).getName() + ":                  " + QuantitySold.get(i) + "                  $" + TotalCost.get(i) + "\n";
		}
		report += "===========\nTotal sales: $" + totalSales;
		return report;
	}

	/**
	 * Modifies a city to either become a warehouse or cease to be a warehouse.
	 * @param city
	 * @param distribution -> true if changing to warehouse, false if changing from warehouse to regular city.
	 * @param sessionID
	 * @throws IOException
	 */
	public static void modifyCity(City city, boolean distribution, int sessionID) throws IOException {
		if (isAdmin(sessionID))
		{
			if (city == null || city.distribution == distribution) return;
			city.distribution = distribution;
			for (Integer p : productList.keySet()) {
				Product currentProduct = productList.get(p);
				currentProduct.addDistributionCenter(city);
				csvDB.addCenterToProducts(city);
			}
			Store.getDBManager().updateCity(city);
			
		}
	}

	/**
	 * Adds a new city to the Store's distribution graph.
	 * @param name -> city name
	 * @param isDistribution -> whether the city has a warehouse or not.
	 * @param sessionID -> sessionID of user attempting change.
	 * @throws IOException
	 */
	public static void addCity(String name, boolean isDistribution, int sessionID) throws IOException {
		if (isAdmin(sessionID)){
			City NewCity = new City(name, isDistribution);
			graph.addCity(NewCity);
			csvDB.addCity(NewCity);
			if (isDistribution) {
				// Add this distribution center with quantity 0 to all existing products
				for (Integer p : productList.keySet()) {
					Product currentProduct = productList.get(p);
					currentProduct.addDistributionCenter(NewCity);
					csvDB.addCenterToProducts(NewCity);
				}
			}
		}
	}
	
	/**
	 * Adds a connection between cityA and cityB of length distance.
	 * @param cityA
	 * @param cityB
	 * @param distance
	 * @param sessionID
	 * @throws IOException
	 */
	public static void addEdge(City cityA, City cityB, int distance, int sessionID) throws IOException {
		if (isAdmin(sessionID)){
			if (graph.addEdge(cityA, cityB, distance))
			csvDB.addEdge(cityA, cityB); // has to add the edge to both A and B?
		}
	}

	/**
	 * Adds a connection between the cities with names cityA and cityB of length distance.
	 * @param cityA
	 * @param cityB
	 * @param distance
	 * @param sessionID
	 * @throws IOException
	 */
	public void addEdge(String cityA, String cityB, int distance, int sessionID) throws IOException {
		City a = graph.cityFromString(cityA);
		City b = graph.cityFromString(cityB);
		if (a != null && b != null) {
			addEdge(a, b, distance, sessionID);
		}
	}

    public static HashMap<Integer, Category> getCategories() {
    	return categories;
    }

    /**
     * @param catID
     * @return The category with the catID entered
     */
    public static Category getCategory(Integer catID) {
    	return categories.get(catID);
    }

	/**
	 * @param id -> ProdID to lookup.
	 * @return the product with the specified ProdID.
	 */
	public static Product lookForProduct(int id) {
		return productList.get(id);
	}

	/**
	 * Search for all products within a price range.
	 * @param start -> minimum price
	 * @param end -> maximum price
	 * @return List of products within range (inclusive)
	 */
	public static ArrayList<Product> searchByPriceRange (double start, double end) {
		ArrayList<Product> productsInRange = new ArrayList<>();
		Iterator it = productList.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) it.next();
			Product currentProduct = (Product) pair.getValue();
			if (currentProduct.getPrice() >= start && currentProduct.getPrice() <= end) {
				productsInRange.add(currentProduct);
			}

		}

		return productsInRange;
	}

	/**
	 * @param category
	 * @return A list of all products of the specified category.
	 */
	public static ArrayList<Product> searchByCategory (Category category) {
		ArrayList<Product> productsByCategory = new ArrayList<>();
		Iterator it = productList.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) it.next();
			Product currentProduct = (Product) pair.getValue();
			if (currentProduct.getCategory().equals(category)) {
				//TODO will we need to fix this .equals? marking here for future debugging
				productsByCategory.add(currentProduct);
			}
		}

		return productsByCategory;
	}

    public static HashMap<Integer, User> getsessionIDList() {
    	return sessionIDList;
    }
    
    public static DBManager getDBManager() {
    	return csvDB;
    }
    
    protected static HashMap<Integer, User> getUsersByID() {
        HashMap<Integer, User> usersByID = new HashMap<>();
        Iterator it = userList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            User currentUser = (User) pair.getValue();
            usersByID.put(currentUser.getUserID(), currentUser);
        }
        return usersByID;
    }

	

}
