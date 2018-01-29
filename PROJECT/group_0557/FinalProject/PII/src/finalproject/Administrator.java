package finalproject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Represents an Administrator user that exists in the Store's database.
 *
 */
public class Administrator extends User {

    public Administrator(String username, String password) {
    	this.userName = username;
    	this.password = password;
    }

    /**
     * Attempts to add a new product to the store.
     * @param category
     * @param image
     * @param name
     * @param description
     * @param price
     * @return -> ProductID if successful, -1 otherwise
     * @throws IOException
     */
    public int addProductRequest(Category category, String image, String name, String description, int price) throws IOException {
        int newProductID = Store.addProduct(category, image, name, description, price, sessionID);
        return newProductID;
    }

    /**
     * Attempts to modify a product in the store.
     * @param product
     * @param category
     * @param image
     * @param name
     * @param description
     * @param price
     * @throws IOException
     */
    public void modifyProductRequest(Product product, Category category, String image, String name, String description, int price) throws IOException {
        Store.modifyProduct(product, category, image, name, description, price, this.sessionID);
    }

    /**
     * Attempts to add a new category to the store.
     * @param categoryName
     * @return -> categoryID if successful, -1 otherwise.
     * @throws IOException
     */
    public int addCategoryRequest(String categoryName) throws IOException {
        int newCatID = Store.addCategory(categoryName, this.sessionID); //adds to category
        return newCatID;
    }

    /**
     * Attempts to modify the quantity of a particular product at a particular distribution center
     * @param product
     * @param city
     * @param quantity
     * @throws IOException
     */
    public void modifyQuantityRequest(Product product, City city, int quantity) throws IOException {
        Store.modifyQuantity(product, city, quantity, this.sessionID);
    }

    /**
     * Adds a city to the distribution graph
     * @param name
     * @param distribution
     * @throws IOException
     */
    public void addCityRequest(String name, boolean distribution) throws IOException {
    	Store.addCity(name, distribution, sessionID);
    }

    /**
     * Adds a connection to the distribution graph
     * @param cityA
     * @param cityB
     * @param distance
     * @throws IOException
     */
    public void addEdgeRequest(City cityA, City cityB, int distance) throws IOException {
    	Store.addEdge(cityA, cityB, distance, sessionID);
    }

    /**
     * 
     * @return A HashMap linking Category ID to Category Objects.
     */
    public HashMap<Integer, Category> listCategories() {
        return Store.getCategories();
    }
}

