package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shopper {
	private ArrayList<Invoice> invoices;
	private HashMap<Product, Integer> cart;

	public Shopper() {
		invoices = new ArrayList<Invoice>();
		cart = new HashMap<Product, Integer>();
	}
	
	public void addToCart(Product product, int quantity){
		if (product.getQuantity() >= quantity) {
			if (cart.containsKey(product)){
				cart.replace(product, cart.get(product) + quantity);
			}
			else {
				cart.put(product, quantity);
			}
			product.setQuantity(product.getQuantity() - quantity);
		}
	}
	
	public void removeFromCart(Product product, int quantity){
		if (cart.containsKey(product)){ //do we have to check if product exists or is it a given?
			if (cart.get(product) <= quantity) cart.remove(product);
			else {cart.put(product, cart.get(product) - quantity);}
			product.setQuantity(product.getQuantity() + quantity);
		}
	}
	
	public void viewCart(){
		if (!cart.isEmpty()) {
			System.out.println("Your cart contains:");
			for(Map.Entry<Product, Integer> entry : cart.entrySet()) //http://stackoverflow.com/questions/9371667/foreach-loop-in-java-for-dictionary
			{
				System.out.println(entry.getKey().getName() + ": " + entry.getValue());
			}
			System.out.println("");
		} else {System.out.println("your cart is empty!");}
	}

	public boolean checkOut(){
		if (!cart.isEmpty()) {
		invoices.add(new Invoice(cart));
		cart.clear();
		return true;}
		return false;
	}
	
	public void viewInvoices(){
		for (Invoice i : invoices) System.out.println(i);
	}

}
