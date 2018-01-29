package finalproject;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.HashMap;

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
	}


		
	// Made some private parse methods below that will be called by the more
	// encompassing methods parseUsers etc just to break the code up.

    // ******************************************************************************************************
    // *																									*
    // *  Below here are methods for READING purposes. Info is sent from the DB to our store.               *
    // *																									*
    // ******************************************************************************************************

	    
	/**
	 * Returns the current list of users
	 * @param
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public HashMap<String, User> loadUsers(HashMap<String, City> cityList, HashMap<Integer, Product> productList) throws NumberFormatException, IOException {
		// form: UserAdmin(true or false),Username,Password,City(city),Address,UserID,Cart<ProductID:quantity!etc>
        HashMap<String, User> listOfUsers = new HashMap<String,User>();
        HashMap<Shopper, String> userCarts = new HashMap<Shopper, String>();
        Scanner scanner = new Scanner(CSVpath);
        scanner.next("Categories,,");
        scanner.nextLine();
        while(!scanner.hasNext("Users,,")) {
            scanner.nextLine();
        }
        scanner.nextLine();
        while(scanner.hasNext()) {
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
                        userInformation[2]
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
                    currentShopper.addToCart(productList.get(Integer.valueOf(splitter[0])),Integer.valueOf(splitter[1]));
            }
        }
		return listOfUsers;
	}

	/**
	 * Returns the current list of products
	 * @param cities -> the graph of cities that the store services.
	 * @throws FileNotFoundException 
	 */
	public HashMap<Integer, Product> loadProducts(Graph cities) throws FileNotFoundException { //TODO why input param?
		//TODO start at a key for a product token, return a hashmap of all the products until the end token
        HashMap<Integer,Product> listOfProducts = new HashMap<Integer,Product>();
        HashMap<Product,String> distributionCenters = new HashMap<Product,String>();
        ArrayList<City> cityList = cities.getCities();
        // form: CategoryID,ProductImage,ProductName,ProductDescription,DoublePrice,IntProductID
        // TODO where is ProductID? The hashmap has to map ID to product, rather than name.
        Scanner scanner = new Scanner(this.CSVpath);
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
                    currentProduct.getStock().put(citiesByName.get(splitter[0]),Integer.valueOf(splitter[1]));
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
        Scanner scanner = new Scanner(this.CSVpath);
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
     * @throws FileNotFoundException
     */
    public Graph loadCities() throws FileNotFoundException {
    	Graph cityMap = new Graph();
//    	HashMap<String, City> cityMap = new HashMap<String, City>();
        HashMap<City, String> cityConnectionString = new HashMap<City,String>();
        // form: CityName,DistributionBoolean,<city1:int!city2:int!city3:int>
        Scanner scanner = new Scanner(this.CSVpath);
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
        System.out.println("new data: " + newUserData);
        replacement(userID, newUserData);

	}

    private String createUserData(User user) {
        ArrayList<String> cartStorage = new ArrayList<String>();
        String userData = user.getUserName()+","+user.getPassword();
        if (user instanceof Administrator) {
            String adminData = "true,"+userData;
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

        Scanner scanner = new Scanner(CSVpath);
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
        Scanner scanner = new Scanner(CSVpath);
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

        try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PrintWriter deletThis = new PrintWriter(CSVpath);
        deletThis.write("");

        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = new PrintWriter(updates);
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

        try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        PrintWriter deletThis = new PrintWriter(CSVpath);
        deletThis.write("");


        Files.copy(updates.toPath(), new FileOutputStream(CSVpath));

        deletThis.close();

        PrintWriter deletThis2 = new PrintWriter(updates);
        deletThis2.write("");
        deletThis2.close();



    }

    public static void main(String[] args) throws IOException {

        String filepath = "database.csv";
        DBManager database = new DBManager(filepath);

        Category newCategory = new Category("X Shirts",7);
        Category newCategory2 = new Category("Y Shirts", 12);
        //database.addCategory(newCategory);
        //database.addCategory(newCategory2);

        /**

        Product newProduct = new Product(newCategory,"blahblah.jpg","Delicious Fruit","A very nice fruit from a far away land",77.54);
        newProduct.setProductID(29403940);

        database.addProduct(newProduct);

        City baku = new City("Baku",false);
        City burnaby = new City("Burnaby",true);
        City kigali = new City("Kigali",true);
        baku.cityConnections.put(burnaby,300);
        burnaby.cityConnections.put(baku,300);

        database.addCity(baku);
        database.addCity(burnaby);
        database.addCity(kigali);

        User admin1 = new Administrator("ash ketchum","ilovepokemans");
        User shopper1 = new Shopper("batman","thejoker",kigali,"42 yonge avenue",66666666,new HashMap<Product,Integer>());

        database.addUser(admin1);
        database.addUser(shopper1);

        //fix: kigali with no connected cities looks like:
        //Kigali,true,<> (or check if this should be the default)

         **/

        Product newProduct = new Product(newCategory,"blahblah.jpg","Delicious Fruit","A very nice fruit from a far away land",77.54);
        Product newProduct2 = new Product(newCategory,"blbaja.jpg","Random thing","Hello world",34.54);
        newProduct.setProductID(29403940);
        newProduct2.setProductID(39302840);

        database.addProduct(newProduct);
        database.addProduct(newProduct2);

        newProduct.setDescription("lolololol");

        database.updateProduct(newProduct);




    }
}

	

