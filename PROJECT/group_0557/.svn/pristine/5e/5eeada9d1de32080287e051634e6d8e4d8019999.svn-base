package finalproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Extended API for phase 3
 * @author George
 */
public class ProjectV1 extends Project {

	/** 
	 * As a general purchase procedure:
	 *	1) A shopper logs in
	 *	2) Shopper adds items to the cart
	 *	3) If available, items get added accordingly
	 *	4) Checkout (method in API, or button in GUI) generates a new order (and automatically computes 
	 *     shipping and generates a new invoice).
     *
	 *	Now - a word on inventory maintenance - explained via an example.
     *
	 *	Say shopper s adds product p in his cart; the desired quantity comes from warehouse A and warehouse B because none 
	 *  of them has enough.
	 *	Please observe at this stage the shopper has not checked out yet; the shopper is still logged in to the system. 
	 *	To make it more practical, say warehouse A has 10 pieces, warehouse B has 7 pieces, the shopper has added to cart 15 pieces.
	 *	As long as the shopper is logged in, other shoppers should be able to see only 2 pieces available.
     *
	 *	At this point, we have two cases:
	 *	1) Shopper hits checkout. The system should do the following:
	 *	-reduce the inventory (permanently) to 2 pieces.
	 *	-compute the nearest warehouse to the shopper's city. Let this warehouse be the warehouse in city C.
	 *	-the shipping has to be computed from C.
	 *	-So here what should happen is: the company has to move the merchandise from warehouse A (10 units) and from warehouse B 
	 *   (5 units) [or, if you desire so, 7 units from warehouse B and 8 units from warehouse A] to the warehouse C. 
	 *   This operation is "invisible" for us (that is you can assume the merchandise moves using procedures to be programmed in the future)
     *
	 *	2) Shopper hits logout WITHOUT doing a checkout. Please store the shopping cart and recover the inventory. 
	 *     So to make it clear, the shopping cart contains 15 units from product p, the shopper is logged out, other shoppers 
	 *     see 17 available (in total).
     *
	 *	3) The shopper logs in again. If in the mean time the merchandise sold out (in full or in part) the product in question 
	 *     must be removed from shopping cart (if you are in GUI, the user should get a notification message). If the merchandise is 
	 *     still there go to point 1 above.
	 */
	
	
	
	
	
	ProjectV1() throws NumberFormatException, IOException {
		super();
	}

	/**
	 * This method attempts to add quantity units from the product identified by prodID to the cart.
	 * Returns true if the operation succeeds, false, if then operation fails.
	 * 
	 * @param productID
	 * @param quantity
	 * @param sessionID
	 * @param custID
	 * @return true if successful, false otherwise
	 * @throws IOException 
	 */
	public boolean addToShoppingCart(int productID, int quantity, int sessionID, int custID) throws IOException {
		if (!Store.isShopper(sessionID)) {
			return false;
		}
		Shopper shopper = (Shopper) Store.getsessionIDList().get(sessionID);
		Product product = null;
		for (Product p : Store.getProductList().values()) {
			if (p.getProductID() == (productID)) {
				product = p;
				break;
			}
		}
		if (product == null) {System.out.println("oops");return false;}
		System.out.println("okay here we goooooo!");
		return shopper.addToCartandUpdate(product, quantity);
	}
	
	/** 
	 * Returns the shopping cart content of the shopper userName.
	 * 
	 * An item in the shopping cart must look like this sample:
	 * prodID: 111
	 * center: (An arraylist with components) "Toronto","Barrie"
	 * quantity: (An arraylist with components) 2,3
	 * 
	 * The sample means this particular item on the shopping cart is the product with product ID=111,
	 * the total quantity in the cart is 5, this quantity is taken 2 pieces from the warehouse located in Toronto
	 * and 3 pieces from the warehouse located in Barrie.
	 *  
	 * @param userName
	 * @return List of shopping cart items.
	 */
	public List<ShoppingCartItem> getShoppingCart(String userName) {
		if (!Store.isShopper(Store.getUserList().get(userName).getSessionID())) return null;
		Shopper shopper = (Shopper) Store.getUserList().get(userName);
		HashMap<Product, Integer> purchaseList = shopper.getCart();	
		City center = Store.graph.getBestDistributionCenter(shopper.getCity());
		List<ShoppingCartItem> toReturn = new ArrayList<ShoppingCartItem>();

		for (Product p : purchaseList.keySet())
			{
				String prodID = String.valueOf(p.getProductID());
				ArrayList<String> centers = new ArrayList<String>();
				ArrayList<Integer> quantities = new ArrayList<Integer>();
				// how many of this product do we need
				int toFulfill = (int) purchaseList.get(p);
				int centerStock = p.getStock().get(center);
				// First we check our fulfillment center
				if (centerStock >= toFulfill) {
					centers.add(center.getName());
					quantities.add(toFulfill);
					// Fulfillment center does not have enough of this product
				} else {
					// First we deplete that center
					if (centerStock >= 0) {
						centers.add(center.getName());
						quantities.add(centerStock);
						toFulfill -= centerStock;
					}
					// Second we arbitrarily take stock from other centers until fulfilled
					for (City d : Store.graph.getDistributionCenters())
					{
						if (!d.equals(center))
						{
							int distStock = p.getStock().get(d);
							if (distStock >= toFulfill) {
								centers.add(d.getName());
								quantities.add(toFulfill);
								break;
							} else
							{
								if (distStock > 0) {
									centers.add(d.getName());
									quantities.add(distStock);									
									toFulfill -= distStock;
								}
							}
						}
					}
				}
			toReturn.add(new ShoppingCartItem(prodID, centers, quantities));
			}
		return toReturn;	
	}
	

	/**
	 * This method checks out the shopping cart, generates an order and returns order ID.
	 * It is the equivalent of the checkout button in your GUI screen.
	 * @param sessionID
	 * @param custID
	 * @return the order ID
	 * @throws IOException 
	 */
	public int checkout(int custID, int sessionID) throws IOException {
		if (Store.isShopper(sessionID)) {
			Shopper shopper = (Shopper) Store.getsessionIDList().get(sessionID);
			return shopper.checkOut();
		}
		return -1;
	}
	
	
	public class ShoppingCartItem {
		private String prodID;
		private List<String> center;
		private List<Integer> quantity;
		
		public ShoppingCartItem(String prodID, List<String> center,
				List<Integer> quantity) {
			this.prodID = prodID;
			this.center = center;
			this.quantity = quantity;
		}

		public ShoppingCartItem(String prodID) {
			this.prodID = prodID;
			this.center = new ArrayList<String>();
			this.quantity = new ArrayList<Integer>();			
		}
		public String getProdID() {
			return prodID;
		}

		public void setProdID(String prodID) {
			this.prodID = prodID;
		}

		public List<String> getCenter() {
			return center;
		}

		public void setCenter(List<String> center) {
			this.center = center;
		}

		public List<Integer> getQuantity() {
			return quantity;
		}

		public void setQuantity(List<Integer> quantity) {
			this.quantity = quantity;
		}
		
		
	}

}