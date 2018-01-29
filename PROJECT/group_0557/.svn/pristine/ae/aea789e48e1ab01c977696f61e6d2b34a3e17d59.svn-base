package finalproject;

import java.io.IOException;

public class TestProject {

	public static void main(String[] args) throws IOException {
		
		Project p = new Project();
		boolean result = p.addUser("Ilir","pass",true);// the result must be true
			System.out.println("add Ilir True? " + result);
		boolean result2 = p.addUser("Ilir","pass",true);// the result must be false; user "Ilir" exists already
			System.out.println("add Ilir again False? " + result2);
		int mySession = p.login("Ilir","pass"); // expected result is a positive integer (session id)
			System.out.println("Ilir SessionID? " + mySession);	
		int catID = p.addCategory("TSHIRT",mySession); // expected result is a positive integer, the category ID you have assigned to the new category "TSHIRT"
			System.out.println("TSHIRT CategoryID? " + catID);	
		int someID = p.addCategory("TSHIRT", mySession); // expected result is -1; "TSHIRT" exists
			System.out.println("TSHIRT again CategoryID is -1? " + someID);	
		boolean result3 = p.addUser("Johnny","pass",false);// expected result true
			System.out.println("Johnny added True? " + result3);
		int johnnySession = p.login("Johnny","pass");//expect a session ID, positive integer, not equal to mySesssion
			System.out.println("Johnny SessionID? " + johnnySession);	
		int someID2 = p.addCategory("JSHIRT",johnnySession);// expect -1 because Johnny is not administrator
				System.out.println("Johnny attempt at CategoryID is -1? " + someID2);	
//		p.logout(johnnySession);// now user Johhny cannot do anything else because he logged out. All Johhny data must be saved in the files.
		p.logout(mySession);// user Ilir is also out
		int catID2 = p.addCategory("SHOULDNOTWORK",mySession);//must fail (return -1) because user Ilir logged out
			System.out.println("SHOULDNOTWORK CategoryID -1? " + catID2);	
		mySession = p.login("Ilir", "pass");
			System.out.println("Ilir 2nd SessionID? " + mySession);
		int myProduct = p.addProduct("SHIRT", catID, 13.07, mySession);
			System.out.println("Product ID?" + myProduct);
//		p.addDistributionCenter("Canada", mySession);
		p.store.addCity("ShimSham", false, mySession);
		p.addDistributionCenter("Canada", mySession);
		p.store.addCity("Flammy", false, mySession);
		p.store.addCity("Mexico", false, mySession);
		p.store.addEdge("ShimSham", "Flammy", 200, mySession);
		p.store.addEdge("Mexico", "Canada", 200, mySession);
		p.updateQuantity(myProduct, "Canada", 500, mySession);
		int userID = p.addCustomer("Johnny", "Mexico", "92 this st", johnnySession);
			System.out.println("Johnny userID? " + userID);
		int orderID = p.placeOrder(userID, myProduct, 400, johnnySession);
			System.out.println("orderID? " + orderID);
		Shopper jo = ((Shopper) Store.getUserList().get("Johnny"));
		jo.addToCartandUpdate(Store.getProductList().get(myProduct), 6);
		jo.viewCart();
		City Canada = p.store.graph.cityFromString("Canada");
		jo.viewInvoices();	
		System.out.println(p.getDeliveryRoute(orderID, johnnySession));
		System.out.println(p.invoiceAmount(orderID, johnnySession));
		jo.viewCart();
		
	}

}
