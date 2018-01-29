package finalproject;

public class Product {
	//image
	private int productID, quantity, price; //price in cents
	private String description, name, category;
	
	public Product(String category, /* ,image, */ String name, String description, int price, int quantity){
		//this.image = image; TODO
		this.category = category;
		this.description = description;
		this.price = price;
		this.name = name;
		this.quantity = quantity;
	}
	
	public int getProductID(){
		return productID;
	}
	
	public void setProductID(int p){
		this.productID = p;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public String getName() {
		return this.name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getCategory(){
		return category;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public int getPrice(){
		return price;
	}
	
	public void setPrice(int p){
		this.price = p;
	}

}
