package finalproject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Represents a product in the store. Keeps track of all data pertaining to the product 
 * as well as the stock level available at all distribution centers in the store.
 *
 */
public class Product {
	private int productID, quantity;
	private double price;
	private String description, name, image;
	private Category category;
	private HashMap<City, Integer> stock;
	
	
	/**
	 * Constructor that creates a Product object. The constructor loads the Product with information about category, image, 
	 * name, description, price, and quantity. 
	 * 
	 * @param category		Category type of the Product, for example a T-shirt
	 * @param image			An Image of the Product. 
	 * @param name 			The name of the Product.
	 * @param description	The description of the Product.
	 * @param price			The price of the Product.
	 */
	public Product(Category category, String image, String name, String description, double price){
		this.image = image;
		this.category = category;
		this.description = description;
		this.price = price;
		this.name = name;
		this.image = image;
		this.quantity = 0;
		this.stock = new HashMap<City,Integer>();
	}

	/**
	 * Constructor that recreates a Product object from the CSV. The constructor loads the Product with information about category, image, 
	 * name, description, price, and quantity. 
	 * 
	 * @param category		CategoryID of the category
	 * @param image			An Image of the Product. 
	 * @param name 			The name of the Product.
	 * @param description	The description of the Product.
	 * @param price			The price of the Product.
	 * @param ID 			The product ID number.
	 */
	public Product(int category, String image, String name, String description, double price, int ID){
		this.image = image;
		this.category = Store.getCategory(category);
		this.description = description;
		this.price = price;
		this.name = name;
		this.image = image;
		this.quantity = 0;
		this.productID = ID;
		this.stock = new HashMap<City,Integer>();
	}


	/**
	 * Adds a Distribution Center with 0 quantity to stock.
	 * @param center
	 */
	public void addDistributionCenter(City center) {
		if (!stock.containsKey(center) && center.distribution) stock.put(center, 0);
	}

	/**
	 * Sets the stock at a particular distribution center to quantity. 
	 * Adjusts overall available quantity accordingly
	 * @param center -> the distribution center where the quantity is changing.
	 * @param quantity -> the new quantity amount at center.
	 */
	public void setStock(City center, int quantity) {
		if (stock.containsKey(center)) {
			int prevQuant = this.stock.get(center);
			stock.put(center, quantity);
			this.quantity += quantity - prevQuant;			
		}
	}

	/**
	 * Sets the stock at a particular distribution center to quantity. 
	 * Adjusts overall available quantity accordingly. Then updates the CSV.
	 * @param center -> the distribution center where the quantity is changing.
	 * @param quantity -> the new quantity amount at center.
	 * @throws IOException 
	 */
	public void setStockandUpdate(City center, int quantity) throws IOException {
		setStock(center, quantity);
		Store.getDBManager().modifyQuantity(this, center, quantity);
	}

	/**
	 * Sets the stock at a particular distribution center to quantity. 
	 * Adjusts overall available quantity accordingly. Then updates the CSV.
	 * Do not update overall quantity number because that quantity was already reserved.
	 * @param center -> the distribution center where the quantity is changing.
	 * @param quantity -> the new quantity amount at center.
	 * @throws IOException 
	 */
	public void setStockandUpdateViaCheckout(City center, int quantity) throws IOException {
		stock.put(center, quantity);
		Store.getDBManager().modifyQuantity(this, center, quantity);
	}
	
	
	
	/**
	 * Returns the ID of the Product. 
	 */
	public int getProductID(){
		return productID;
	}
	
	/**
	 * Sets a unique ID number of the Product.
	 * 
	 * @param ID 			Identification number to be assigned to the Product.
	 */
	public void setProductID(int ID){
		this.productID = ID;
	}
	
	/**
	 * Returns the quantity of the product.
	 */
	public int getQuantity(){
		return quantity;
	}
	
	/**
	 * Updates the quantity of the product
	 * 
	 * @param quantity		Desired quantity of product to be set to.
	 */
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	/**
	 * Returns the filename of the image of the Product. 
	 */
	public String getImage(){
		return image;
	}

	/**
	 * Sets the image of the Product by providing a filename to the image. 
	 * 
	 * @param image			Filename of the image associated with the Product.	
	 */
	public void setImage(String image){
		this.image = image;
	}

	/**
	 * Returns the name of the Product.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the description of the Product.
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Updates the description of the Product. 
	 * 
	 * @param description	Updated description of the Product.
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Returns the category of the Product.
	 */
	public Category getCategory(){
		return category;
	}
	
	/**
	 * Updates the category type of the Product.
	 * 
	 * @param category		Updated category of the Product.
	 */
	public void setCategory(Category category){
		this.category = category;
	}
	
	/**
	 * Returns the unit price of the Product.
	 */
	public double getPrice(){
		return price;
	}
	
	/**
	 * Updates the price of the Product.
	 * 
	 * @param price			The desired new price for the Product.
	 */
	public void setPrice(double price){
		this.price = price;
	}

	public HashMap<City, Integer> getStock() {
		return stock;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		String str = "Name: " + this.name + ", ID: " + productID;
		if (!description.equals("")) {str += ", Description: " + description;}
		str += ", Price: $" + price + ", Stock: " + quantity;
		for (City c : stock.keySet()) {
			str += "\n" + c.getName() + ": " + stock.get(c);
		}
		return str;		
	}
}