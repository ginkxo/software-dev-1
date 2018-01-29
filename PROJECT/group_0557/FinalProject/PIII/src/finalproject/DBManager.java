package finalproject;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;

/**
 * Class dedicated to reading and writing to the CSV file that stores all data for the store.
 *
 */
public class DBManager {
	private int NextUserID, NextProductID, NextCategoryID; // max <type>ID parsed + 1 - send these numbers to Store once done parsing.
	private File CSVpath; // The location of the file we will read and write to
    private File updates; // This file will be used as a temp-memory when writing back to CSVPath; initially empty

	// These are just a thought so that we don't have to search for where to add stuff to each time,
	// But that 'efficient' method would make us more prone to messing up our file so feel free
	// to delete these if you think they're dangerous / unnecessary.
	private int CategoryLocation; // The line in the CSV storing the list of categories
	private int EndofUsers; //The line in the CSV that new users should be inserted at
	private int EndofProducts; // The line in the CSV that new products should be inserted at

	
	DBManager(String CSVpath) {
	    this.CSVpath = new File(CSVpath);
        this.updates = new File("updates.csv"); //TODO:fix this path
        if (!updates.exists()) {
        	System.out.println("creating");
        	try {
				updates.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}


		
	// Made some private parse methods below that will be called by the more
	// encompassing methods parseUsers etc just to break the code up.

    // ******************************************************************************************************
    // *																									*
    // *  Below here are methods for READING purposes. Info is sent from the DB to our store.               *
    // *																									*
    // ******************************************************************************************************

	private Scanner makeScanner() {
		if (CSVpath.exists()) {
			try {
				Scanner scanner = new Scanner(CSVpath);
				return scanner;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				CSVpath.createNewFile();
		        FileWriter writer = new FileWriter(CSVpath, true);
		        String separator = System.getProperty("line.separator");
		        writer.append(
		        		"Categories,," + separator +
		        		"Cities,," + separator +
		        		"Products,," + separator +
		        		"Users,," + separator +
		        		"true,George,password,10102142" + separator +
		        		"Invoices,," + separator);
		        writer.close();
		        Scanner scanner = new Scanner(CSVpath);
		        return scanner;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	    
	/**
	 * Returns the current list of users
	 * @param productList 
	 * @return HashMap<username: User object>
	 * @throws NumberFormatException 
	 */
	public HashMap<String, User> loadUsers(HashMap<Integer, Product> productList) {
		// form: UserAdmin(true or false),Username,Password,City(city),Address,UserID,Cart<ProductID:quantity!etc>
        HashMap<String, User> listOfUsers = new HashMap<String,User>();
        HashMap<Shopper, String> userCarts = new HashMap<Shopper, String>();
        Scanner scanner = makeScanner();
        scanner.next("Categories,,");
        scanner.nextLine();
        while(!scanner.hasNext("Users,,")) {
            scanner.nextLine();
        }
        scanner.nextLine();
        while(!scanner.hasNext("Invoices,,")) { //TODO: KEEP NOTE OF THIS!! used to be (scanner.hasNext());
            String[] userInformation = scanner.nextLine().split(",");
            boolean isAdmin = Boolean.parseBoolean(userInformation[0]);
            if (!isAdmin) {
                Shopper s = new Shopper(
                        userInformation[1],
                        userInformation[2],
                        Store.graph.cityFromString(userInformation[3]),
                        userInformation[4],
                        Integer.parseInt(userInformation[5]),
                        new HashMap<Product,Integer>()
                );
                userCarts.put(s, userInformation[6]);
                listOfUsers.put(s.getUsername(), s);
            } else {
                Administrator a = new Administrator(
                        userInformation[1],
                        userInformation[2],
                        Integer.parseInt(userInformation[3])
                );
                listOfUsers.put(a.getUserName(), a);
            }
        }
        scanner.close();

        //TODO: associate user carts with the users, similar to what we do with cities
        Iterator it = userCarts.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Shopper currentShopper = (Shopper) pair.getKey();
            String stringCart = (String) pair.getValue();
            String[] itemCart = stringCart.replaceAll("<|>","").split("!");
            for (int i = 0; i<itemCart.length; i++) {
                String[] splitter = itemCart[i].split(":");
                //currentShopper.cart.put(productList.get(splitter[0]),Integer.valueOf(splitter[1]));
                if (splitter.length > 1)
					try {
						currentShopper.addToCart(productList.get(Integer.valueOf(splitter[0])),Integer.valueOf(splitter[1]));
					} catch (NumberFormatException|IOException e) {
						e.printStackTrace();
					}
            }
        }
		return listOfUsers;
	}

    /**
     * Loads all stored invoices and adds them to the invoice list in the appropriate shopper object.
     */
    public void loadInvoices() {
        //
        // THIS MUST ONLY BE CALLED AFTER THE USER LIST IS BUILT IN STORE, OTHERWISE IT WON'T WORK
        //
        HashMap<String,User> currentUserList = Store.getUserList();
        HashMap<Integer,Product> currentProductList = Store.getProductList();
        Scanner scanner = makeScanner();
        scanner.next("Categories,,");
        scanner.nextLine();
        while (!scanner.hasNext("Invoices,,")) {
            scanner.nextLine();
        }
        scanner.nextLine();
        while (scanner.hasNext()) {
            // form: InvoiceID,UserName,shippingCost,<City1!City2!City3>,<Product1ID:&5.0?32.76&!Product2ID:&7.0?294.43&>
            // extracted information: total items, items cost, shipping cost

            String[] invoiceInformation = scanner.nextLine().split(",");
            HashMap<Product,double[]> cartInfo = new HashMap<>();
            // parsing the cart first
            // cart form: <Product1ID:&5.0?32.76&!Product2ID:&7.0?294.43&>
            String[] cartInformation = invoiceInformation[4].replaceAll("<|>","").split("!");
            // current form: [Product1ID:&5.0?32.76&, Product2ID:&7.0?294.43&]
            for (int i = 0; i<cartInformation.length; i++) {
                String[] productInfo = cartInformation[i].split(":");
                Integer productID = Integer.parseInt(productInfo[0]);
                Product product = currentProductList.get(productID);
                String[] doublesInfo = productInfo[1].replaceAll("&","").split("#");
                double quantity = Double.parseDouble(doublesInfo[0]);
                double price = Double.parseDouble(doublesInfo[1]);

                double[] priceAndQuantity = {quantity, price};

                cartInfo.put(product,priceAndQuantity);

            }

            double itemsCost = 0;
            int totalItems = 0;
            Iterator it = cartInfo.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                double[] quantities = (double[]) pair.getValue();
                totalItems+=(int)quantities[0];
                itemsCost+=(quantities[0] * quantities[1]);
            }

            String[] cityInformation = invoiceInformation[3].replaceAll("<|>","").split("!");
            ArrayList<String> cityNames = new ArrayList<String>(Arrays.asList(cityInformation));

            Invoice inv = new Invoice(
                    totalItems,
                    Integer.parseInt(invoiceInformation[0]),
                    invoiceInformation[1],
                    itemsCost,
                    Double.parseDouble(invoiceInformation[2]),
                    cityNames,
                    cartInfo
            );

            User shopperWithInvoice = currentUserList.get(invoiceInformation[1]);
            if (shopperWithInvoice instanceof Shopper) {
                ((Shopper) shopperWithInvoice).addInvoice(inv);
            }


        }

    }

	/**
	 * Returns the current list of products
	 * @param cities -> the graph of cities that the store services.
	 * @return HashMap<product ID: Product object>
	 */
	public HashMap<Integer, Product> loadProducts(Graph cities) { //TODO why input param?
		//TODO start at a key for a product token, return a hashmap of all the products until the end token
        HashMap<Integer,Product> listOfProducts = new HashMap<Integer,Product>();
        HashMap<Product,String> distributionCenters = new HashMap<Product,String>();
        ArrayList<City> cityList = cities.getCities();
        // form: CategoryID,ProductImage,ProductName,ProductDescription,DoublePrice,IntProductID
        // TODO where is ProductID? The hashmap has to map ID to product, rather than name.
        Scanner scanner = makeScanner();
        scanner.next("Categories,,");
        scanner.nextLine();
        while(!scanner.hasNext("Products,,")) {
            scanner.nextLine();
        }
        scanner.nextLine();
        while(!scanner.hasNext("Users,,")) {
            String[] productInformation = scanner.nextLine().split(",");
            Product p = new Product(
                    Integer.parseInt(productInformation[0]),
                    productInformation[1],
                    productInformation[2],
                    productInformation[3],
                    Double.parseDouble(productInformation[4]),
                    Integer.parseInt(productInformation[5])
            );
            distributionCenters.put(p, productInformation[6]);
            listOfProducts.put(p.getProductID(),p);
        }
        scanner.close();

        //building a map of cities to their names for quick access

        HashMap<String, City> citiesByName = new HashMap<>();

        for (City currentCity : cityList) {
            citiesByName.put(currentCity.getName(),currentCity);
        }

        //adding the cities to the stock as seen

        Iterator it = distributionCenters.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Product currentProduct = (Product) pair.getKey();
            String stringCityInfo = (String) pair.getValue();
            String[] distroCenters = stringCityInfo.replaceAll("<|>","").split("!");
            for (int i = 0; i<distroCenters.length; i++) {
                String[] splitter = distroCenters[i].split(":");
                //currentShopper.cart.put(productList.get(splitter[0]),Integer.valueOf(splitter[1]));
                if (splitter.length > 1)
                {
                    currentProduct.getStock().put(citiesByName.get(splitter[0]),Integer.valueOf(splitter[1]));
                    currentProduct.setQuantity(currentProduct.getQuantity() + Integer.valueOf(splitter[1]));
                }
            }
        }

        return listOfProducts;
	}

    /**
     * loads the categories from the database
     * @return categoryList - the list of categories in the database
     * @throws FileNotFoundException 
     */
    public HashMap<Integer, Category> loadCategories() throws FileNotFoundException {
    	// * Changed from type String to type Category
    	// form: CategoryName,CategoryID
        HashMap<Integer, Category> categoryList = new HashMap<Integer, Category>();
        Scanner scanner = makeScanner();
        scanner.next("Categories,,");
        scanner.nextLine();
        while (!scanner.hasNext("Cities,,")) {
            String[] catInformation = scanner.nextLine().split(",");
            int ID = Integer.parseInt(catInformation[1]);
            categoryList.put(ID, new Category(catInformation[0],ID));
        }
        scanner.close();
        return categoryList;
    }

    /**
     * loads the cities from the database
     * @return a Graph of the cities and their connections
     */
    public Graph loadCities() {
    	Graph cityMap = new Graph();
//    	HashMap<String, City> cityMap = new HashMap<String, City>();
        HashMap<City, String> cityConnectionString = new HashMap<City,String>();
        // form: CityName,DistributionBoolean,<city1:int!city2:int!city3:int>
        Scanner scanner = makeScanner();
        scanner.next("Categories,,");
        scanner.nextLine();
        while(!scanner.hasNext("Cities,,")) {
            scanner.nextLine();
        }
        scanner.nextLine();
        while(!scanner.hasNext("Products,,")) {
            String[] cityInformation = scanner.nextLine().split(",");
            City city = new City(cityInformation[0],Boolean.parseBoolean(cityInformation[1]));
            cityMap.addCity(city);
            if (cityInformation.length > 2) {cityConnectionString.put(city,cityInformation[2]);}
        }

        scanner.close();

        // using the Iterator, we will iterate through the keys of cityConnectionString
        // at each point here, we break apart the assigned string and define the city's adjacent conns as following:
            // blahblah.put(cityMap.get("cityname"), "string's integer converted to int")

        Iterator it = cityConnectionString.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            City currentCity = (City) pair.getKey();
            String stringCities = (String) pair.getValue();
            String[] adjCitiesString = stringCities.replaceAll("<|>","").split("!");
            for (int i = 0; i<adjCitiesString.length; i++) {
                String[] splitter = adjCitiesString[i].split(":");
                if (splitter.length > 1)
                    cityMap.addEdge(currentCity,  cityMap.cityFromString(splitter[0]), Integer.valueOf(splitter[1]));
            }
        }

        return cityMap;
    }
    
    // ******************************************************************************************************
    // *																									*
    // *  Below here are methods for WRITING purposes. Info is sent from store to update our permanent DB.  *
    // *																									*
    // ******************************************************************************************************

    /**
	 * Updates the stored information about an existing user.
	 * @param user
	 */
	void updateUser(User user) throws IOException {
		//TODO searches for the user matching the user information, and overwrites line
        String newUserData = createUserData(user);
        // search and replace
        String userID = Integer.toString(user.getUserID());
        replacement(userID, newUserData);

	}

    private String createUserData(User user) {
        ArrayList<String> cartStorage = new ArrayList<String>();
        //TODO I just added the userID here
        String userData = user.getUserName()+","+user.getPassword();
        if (user instanceof Administrator) {
            String adminData = "true,"+userData+","+user.getUserID();
            return adminData;
        } else {
            String shopperData = "false,"+userData+",";
            if (((Shopper)user).getCity()!=null){
                shopperData += ((Shopper)user).getCity().getName()+",";
            } else {
                shopperData += ",";
            }

            shopperData += ((Shopper)user).getAddress()+","+user.getUserID()+",";

            Iterator it = ((Shopper)user).getCart().entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                Product currentProduct = (Product) pair.getKey();
                Integer quantity = (Integer) pair.getValue();
                cartStorage.add(currentProduct.getProductID() + ":" + Integer.toString(quantity));
            }

            shopperData += "<"+String.join("!",cartStorage)+">";
            return shopperData;
        }
    }

	/**
	 * Adds a newly created user to the database.
	 * @param user
	 */
	void addUser(User user) throws IOException {
		//TODO Should update and add be seperate? The only benefit is that when we add a new user,
		// we don't have to look through the entire userlist to see if it already exists. Since Store
		// will always know which it's doing anyway, then it might make sense to keep these separate
		// Even though updateUser will likely just overwrite the entire User line with a new version of
		// that user.
        //
        // form: UserAdmin(true or false),Username,Password,City(city),Address,UserID,Cart<ProductID:quantity!etc>
        /**
        ArrayList<String> cartStorage = new ArrayList<String>();
        String userData = user.getUserName()+","+user.getPassword();
        if (user instanceof Administrator) {
            String adminData = "true,"+userData;
            lineWriter("",adminData);
        } else {
            String shopperData = "false,"+userData+","+
                    ((Shopper)user).getCity().getName()+","+
                    ((Shopper)user).getAddress()+","+
                    user.getUserID()+",";

            Iterator it = ((Shopper)user).getCart().entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                Product currentProduct = (Product) pair.getKey();
                Integer quantity = (Integer) pair.getValue();
                cartStorage.add(currentProduct.getProductID() + ":" + Integer.toString(quantity));
            }

            shopperData += "<"+String.join("!",cartStorage)+">";
            lineWriter("",shopperData);
        }
         **/
        String userData = createUserData(user);

        lineWriter("Invoices,,",userData);

        /**
        Scanner scanner = makeScanner();
        FileWriter writer = new FileWriter(updates,true);
        String separator = System.getProperty("line.separator");

        while(scanner.hasNext()) {
            writer.append(scanner.nextLine() + separator);
        }

        writer.append(userData + separator);

        scanner.close();
        writer.close();

        PrintWriter deletThis = new PrintWriter(CSVpath);
        deletThis.write("");

        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = new PrintWriter(updates);
        deletThis2.write("");
        deletThis2.close();
         **/
	}

    void addInvoice(Invoice invoice) throws IOException {
        String invoiceData = createInvoiceData(invoice);
        Scanner scanner = makeScanner();
        FileWriter writer = new FileWriter(updates,true);
        String separator = System.getProperty("line.separator");

        while(scanner.hasNext()) {
            writer.append(scanner.nextLine() + separator);
        }

        writer.append(invoiceData + separator);

        scanner.close();
        writer.close();

        PrintWriter deletThis = openWriter(CSVpath);
        deletThis.write("");

        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = openWriter(updates);
        deletThis2.write("");
        deletThis2.close();

    }

    private String createInvoiceData(Invoice invoice) {
//        HashMap<String,City> cityMap = Store.getCities();
        Graph cityGraph = Store.graph;
        String startCityName = invoice.getDeliveryRoute().get(0);
        City startCity = cityGraph.cityFromString(startCityName);
        double shortestRouteCost = invoice.getShippingCost();

        String invoiceInfo = "";
        invoiceInfo+=invoice.getInvoiceID()+",";
        invoiceInfo+=invoice.getShopperID()+",";
        invoiceInfo+=shortestRouteCost+",";
        invoiceInfo+="<"+String.join("!",invoice.getDeliveryRoute())+">";

        ArrayList<String> cartStorage = new ArrayList<>();
        Iterator it = invoice.getPurchaseList().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Product product = (Product) pair.getKey();
            double[] quantities = (double[]) pair.getValue();

            String toStore = "";

            Integer productID = product.getProductID();
            double itemQuantity = quantities[0];
            double itemPrice = quantities[1];

            toStore+=productID.toString()+":&"+itemQuantity+"#"+itemPrice+"&";

            cartStorage.add(toStore);


        }
        invoiceInfo+=",<"+String.join("!",cartStorage)+">";

        return invoiceInfo;


    }

	/**
	 * Updates the given product in the database.
	 * @param product
	 * @throws IOException
	 */
	void updateProduct(Product product) throws IOException {
		//TODO identical to add, but instead of appending at the end, we replace the existing line
        // form: CategoryID,ProductImage,ProductName,ProductDescription,DoublePrice,IntProductID
        String productData = product.getCategory().getID()+","+
                product.getImage()+","+
                product.getName()+","+
                product.getDescription()+","+
                product.getPrice()+","+
                product.getProductID()+",";

        ArrayList<String> distroStorage = new ArrayList<String>();
        Iterator it = product.getStock().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            City currentCity = (City) pair.getKey();
            Integer distance = (Integer) pair.getValue();
            distroStorage.add(currentCity.getName() + ":" + Integer.toString(distance));
        }
        productData += "<"+String.join("!",distroStorage)+">";
        String productID = Integer.toString(product.getProductID());
        replacement(productID,productData);
	}

	/**
	 * Adds a new product to the database.
	 * @param product
	 * @throws IOException
	 */
	void addProduct (Product product) throws IOException {
		//TODO now must save to csv
        // form: CategoryID,ProductImage,ProductName,ProductDescription,DoublePrice,IntProductID
        String productData = product.getCategory().getID()+","+
                product.getImage()+","+
                product.getName()+","+
                product.getDescription()+","+
                product.getPrice()+","+
                product.getProductID()+",";

        ArrayList<String> distroStorage = new ArrayList<String>();
        Iterator it = product.getStock().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            City currentCity = (City) pair.getKey();
            Integer distance = (Integer) pair.getValue();
            distroStorage.add(currentCity.getName() + ":" + Integer.toString(distance));
        }

        productData += "<"+String.join("!",distroStorage)+">";

        lineWriter("Users,,", productData);

	}

	/**
	 * Adds a newly created category to the database.
	 * @param category
	 * @throws IOException
	 */
    void addCategory (Category category) throws IOException {
        //TODO now must save to csv; append at end
        // form: categoryName,categoryID
    	String categoryData = category.getName() + "," + category.getID();

        lineWriter("Cities,,",categoryData);

    }

    private String createCityData(City city) {
        String cityData = city.getName()+","+Boolean.toString(city.distribution)+",";
        ArrayList<String> adjacencyStorage = new ArrayList<String>();
        Iterator it = city.cityConnections.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            City currentCity = (City) pair.getKey();
            Integer distance = (Integer) pair.getValue();
            adjacencyStorage.add(currentCity.getName() + ":" + Integer.toString(distance));
        }

        cityData += "<"+String.join("!",adjacencyStorage)+">";

        return cityData;
    }
    
    /**
     * Adds a newly created city to the database.
     * @param city
     * @throws IOException
     */
    void addCity(City city) throws IOException {
    	//TODO now must save to csv; append at end
        // form: CityName,DistributionBoolean,<city1:int!city2:int!city3:int>
        /**
        String cityData = city.getName()+","+Boolean.toString(city.distribution)+",";
        ArrayList<String> adjacencyStorage = new ArrayList<String>();
        Iterator it = city.cityConnections.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            City currentCity = (City) pair.getKey();
            Integer distance = (Integer) pair.getValue();
            adjacencyStorage.add(currentCity.getName() + ":" + Integer.toString(distance));
        }

        cityData += "<"+String.join("!",adjacencyStorage)+">";
         **/

        String cityData = createCityData(city);
        lineWriter("Products,,",cityData);
    }

    /**
     * Adds a newly created connection (edge) between cities to the database.
     * @param a
     * @param b
     * @throws IOException
     */
    void addEdge(City a, City b) throws IOException {
    	//TODO
        Graph cities = Store.graph;
        boolean bothExist = cities.getCities().contains(a) && cities.getCities().contains(b);
        if (bothExist) {

            updateCity(a);
            updateCity(b);

        }
    }

    /**
     * Updates an existing city in the database.
     * @param city
     * @throws IOException
     */
    void updateCity(City city) throws IOException {

        String cityData = createCityData(city);
        replacement(city.getName() + ",",cityData);

    }

    /**
     * Modify the quantity in center city to newStock for given product.
     * @param product -> the product for which the stock is changing
     * @param city -> the city where the stock is changing
     * @param newStock -> the new stock number
     */
	void modifyQuantity(Product product, City city, int newStock) throws IOException {
		// TODO	does this actually work?
        product.setStock(city, newStock);
        updateProduct(product);
	}

	/**
	 * Adds distribution center to list of centers in stock hashmap for all products with quantity 0.
	 * @param newCity -> The new distribution center added.
	 */
	void addCenterToProducts(City newCity) throws IOException {
        Graph cities = Store.graph;
        HashMap<Integer, Product> products = loadProducts(cities);
        Iterator it = products.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Product currentProduct = (Product) pair.getValue();
            if (!currentProduct.getStock().containsKey(newCity)) {
                currentProduct.getStock().put(newCity, 0);
            }
            updateProduct(currentProduct);
        }
		// TODO checking if this works out
	}

    private void lineWriter (String barrier, String content) throws IOException {
        //TODO test this
        // mechanism: exactly like replacement but we don't skip over lines and just append an extra line
        // to the bottom of each heading section, so we use the next section's header as the "key" for where
        // the scanner should stop, instead of a counter
    	Scanner scanner = makeScanner();
    	FileWriter writer = new FileWriter(updates,true);
        String separator = System.getProperty("line.separator");

        while(!scanner.hasNext(barrier)) {
            writer.append(scanner.nextLine() + separator);
        }

        writer.append(content + separator);

        while(scanner.hasNext()) {
            writer.append(scanner.nextLine() + separator);
        }

        scanner.close();
        writer.close();

//        try {
//			TimeUnit.MILLISECONDS.sleep(250);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        PrintWriter deletThis = openWriter(CSVpath);
        deletThis.write("");

        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = openWriter(updates);
        deletThis2.write("");
        deletThis2.close();

    }

    private void replacement(String key, String replacingString) throws IOException{
        //TODO test this
        // mechanism: scans through each line in the file, adding to a counter
        // when a line with a unique key for the strong to modify is found (userID, productID),
        // stop the counter here, now reset the scanner and iterate times the counter's number
        // we will be writing to updates, emptying csvpath, copying everything from updates to csvpath, emptying updates
        // this should bring you back to the line to replace <-- CHECK FOR FENCEPOST ERRORS !!
        // instead of writing that line to the file we write the new data and skip over that line
        // continue as normal
        // counter was needed to pinpoint the inline location -- RandomAccessFile would not work because lines are
        // different lengths
        Scanner search = new Scanner(CSVpath);
        FileWriter writer = new FileWriter(updates, true);
        String separator = System.getProperty("line.separator");
        int counter = 0;
        while (search.hasNext()) {
            String currentLine = search.nextLine();
            if (currentLine.contains(key)) {
                break;
            } else {
                counter+=1;
            }
        }

        search.close();
        Scanner scan = new Scanner(CSVpath);

        for(int i = 0; i<counter; i++) {
            writer.append(scan.nextLine() + separator);
        }

        writer.append(replacingString + separator);
        scan.nextLine();

        while(scan.hasNext()) {
            writer.append(scan.nextLine() + separator);
        }

        scan.close();
        writer.close();

//        try {
//			TimeUnit.MILLISECONDS.sleep(250);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        PrintWriter deletThis = openWriter(CSVpath);
        deletThis.write("");


        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = openWriter(updates);
        deletThis2.write("");
        deletThis2.close();
    }

    private PrintWriter openWriter(File file) {
    	boolean success = false;
        while (success == false) {
	        try {
		        PrintWriter deleter = new PrintWriter(file);
		        return deleter;
	        } catch (FileNotFoundException e) {
	        	try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        }
        }
        return null;
    }
}

	

