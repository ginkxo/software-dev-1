package finalproject;

import java.io.IOException;
import java.util.HashMap;

public class StoreTester2 {
	static Store shop;

	public static void main(String[] args) throws IOException {
		shop = new Store("testfile.txt");
		for (Product p : Store.getProductList().values()) {System.out.println(p);}
		Administrator George = (Administrator) shop.login("George",  "password");
		HashMap<Integer, Product> products = Store.getProductList();
		Product q = null;
		for (Product p : products.values()) 
			{q = p; break;}
// there are 2 products in testfile.txt. If you remove the break; here, it changes which one q refers to
		System.out.println("THE PROD TO MODIFY IS: " + q.getName());
		System.out.println("CAT 3 IS: " + Store.getCategory(31000003).getName());
		George.modifyProductRequest(q, Store.getCategory(31000003), "", "new name", "", 0);
	}
}