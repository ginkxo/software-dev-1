package finalproject;

import java.io.IOException;
import java.util.HashMap;

public class StoreTester {
	static Store shop;

	public static void main(String[] args) throws IOException {
		shop = new Store("testfile.txt");
		for (Product p : Store.getProductList().values()) {System.out.println(p);}
//		HashMap<Integer, Category> x = Store.getCategories();
//		System.out.println(shop.printCategories());
//		System.out.println(Store.graph);
//		for (Product p : shop.getProductList().values()) {System.out.println(p);}
//		System.out.println(shop.getProductList().get(7).getStock());
		@SuppressWarnings("unused")
		Administrator George = (Administrator) shop.login("George",  "password");
		Shopper Lynn = (Shopper) shop.login("Lynn", "password");
//		System.out.println(Lynn.getClass());
//		int ID = Lynn.getSessionID();
//		int ID2 = George.getSessionID();
//		shop.logout(Lynn.getSessionID());
		HashMap<Integer, Product> products = Store.getProductList();
		Product q = null;
		for (Product p : products.values()) 
			{q = p; break;}
		System.out.println(Lynn.printCart());
		int ID1 = Lynn.checkOut();
//		int ID1 = Lynn.buyItNow(q, 1);
		System.out.println(Lynn.getInvoice(ID1));
//		int newProd = George.addProductRequest(Store.getCategory(31000003), "","Pants", "", 12);
		System.out.println("THE PROD TO MODIFY IS: " + q.getName());
		System.out.println("CAT 3 IS: " + Store.getCategory(31000003).getName());
//		George.modifyProductRequest(q, Store.getCategory(31000003), "", "new name", "", 0);
		System.out.println("final quants are");
		for (Product p : Store.getProductList().values()) {System.out.println(p);}

		System.out.println(Lynn.printCart());

	}
}
	