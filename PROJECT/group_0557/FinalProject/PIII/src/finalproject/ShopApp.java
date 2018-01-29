//last update: aug 4 7:00 pm

package finalproject;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

/**
 * This class implements the GUI interface that connects to the Store back end and CSV files.
 *
 */
public class ShopApp extends JFrame {
	private Store store;
	private Administrator admin;
	private Shopper shopper;
	private int sessionID;
	private JMenuBar bar;
	private LoginListener listener;
	private LogoutListener logoutListener;
	private ModProdListener modifyProdListener;
	private BodyListener bodListener;
	private SearchListener searchListener;
	private RefreshListener refreshListener;
	private RegisterListener regListener;
	private AdminListener aListener;
	private CustomerListener cListener;
	private JMenu startMenu, actionMenu, customerMenu;
    private JMenuItem loginItem, logoutItem, registerAsShopperItem, registerAsAdminItem, searchSubmenu, searchPriceRange, searchCategory, refreshItem;
    private JMenuItem categoryAction, productAction, warehouseAction, quantityAction, routeAction, cityAction, reportAction; //admin actions
    private JMenuItem viewCart, viewInvoices, checkout; //ShopApp actions
    private JLabel welcome;
    private boolean loggedIn, loggedOut;
    private List<JLabel> prodForSale;
    private String userType; //Guest/Shopper/Admin
    JPanel testPanel;
    JScrollPane testScroll;
    JFrame myCart;
    
    private HashMap<JButton, ArrayList<Object>> boxes3, boxInfo;
    
	/**
	 * @param DBpath
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public ShopApp(String DBpath) throws NumberFormatException, IOException {
		super("CSC207 Shopping App");
		setContentPane(new JLabel(new ImageIcon("Products\\bg.jpg")));
		setLayout(new GridLayout(0,3));
		testPanel = new JPanel( new GridLayout(0,3));
		store = new Store(DBpath);
		
		welcome = new JLabel();
		welcome.setFont(new Font ("Sanserif", Font.ITALIC, 10));
		welcome.setEnabled(false);
	    loggedIn = false;
	    loggedOut = true;
		prodForSale = new ArrayList<>();
		searchListener = new SearchListener();
		
		boxInfo = new HashMap<JButton, ArrayList<Object>>();
		modifyProdListener = new ModProdListener();
		
		boxes3 = new HashMap<JButton, ArrayList<Object>>();
		bodListener = new BodyListener();
		addProducts();
		
		for (JButton key: boxes3.keySet()){
			key.setText("Log in to click");
		}
		
		bar = new JMenuBar();
		setJMenuBar(bar);
		setStartMenu();
		setAdminMenu();
		actionMenu.setEnabled(false);
		setCustMenu();
		customerMenu.setEnabled(false);
		userType = "Guest";
				
		JScrollPane pane = new JScrollPane(testPanel);
        setContentPane(pane);
	}
	
	/**
	 * Creates start menu.
	 */
	public void setStartMenu(){
		startMenu = new JMenu("Start");
		bar.add(startMenu);
		
		loginItem = new JMenuItem("Log In"); //log in button
		loginItem.setMnemonic('I');
		startMenu.add(loginItem);
		listener = new LoginListener();
		loginItem.addActionListener(listener);
		
		registerAsShopperItem = new JMenuItem("Register as shopper"); //register buttons
		registerAsShopperItem.setMnemonic('R');
		registerAsShopperItem.setEnabled(true);
		startMenu.add(registerAsShopperItem);
		regListener = new RegisterListener();
		registerAsShopperItem.addActionListener(regListener);
		
		registerAsAdminItem = new JMenuItem("Register as administrator"); 
		registerAsAdminItem.setEnabled(true);
		startMenu.add(registerAsAdminItem);
		registerAsAdminItem.addActionListener(regListener);
		
		logoutItem = new JMenuItem("Log Out"); //log out button
		logoutItem.setMnemonic(KeyEvent.VK_T);
		logoutItem.setEnabled(false);
		startMenu.add(logoutItem);
		logoutListener = new LogoutListener();
		logoutItem.addActionListener(logoutListener);
		
		startMenu.addSeparator(); //search button
		searchSubmenu = new JMenu("Search...");
		searchSubmenu.setMnemonic(KeyEvent.VK_S);
		searchPriceRange = new JMenuItem("by price range");
		searchPriceRange.addActionListener(searchListener);
		searchSubmenu.add(searchPriceRange);
		searchCategory = new JMenuItem("by category");
		searchCategory.addActionListener(searchListener);
		searchSubmenu.add(searchCategory);
		startMenu.add(searchSubmenu);
		
		startMenu.addSeparator(); //refresh button
		refreshItem = new JMenuItem("View all items (Refresh)");
		refreshItem.setEnabled(true);
		startMenu.add(refreshItem);
		refreshListener = new RefreshListener();
		refreshItem.addActionListener(refreshListener);
	}
	
	/**
	 * Creates and links the items in the Administrator only menu tab.
	 */
	public void setAdminMenu(){
		actionMenu = new JMenu("Administrator actions");
		bar.add(actionMenu);
		
		categoryAction = new JMenuItem("Add Category");
		actionMenu.add(categoryAction);
		aListener = new AdminListener();
		categoryAction.addActionListener(aListener);
		
		productAction = new JMenuItem("Add Product");
		actionMenu.add(productAction);
		productAction.addActionListener(aListener);
		
		warehouseAction = new JMenuItem("Add Warehouse");
		actionMenu.add(warehouseAction);
		warehouseAction.addActionListener(aListener);
		
		quantityAction = new JMenuItem("Modify Quantities"); 
		actionMenu.add(quantityAction);
		quantityAction.addActionListener(aListener);
		
		routeAction = new JMenuItem("Add Route"); 
		actionMenu.add(routeAction);
		routeAction.addActionListener(aListener);
		
		cityAction = new JMenuItem("Add City"); 
		actionMenu.add(cityAction);
		cityAction.addActionListener(aListener);
		
		reportAction = new JMenuItem("Produce Sales Report"); 
		actionMenu.add(reportAction);
		reportAction.addActionListener(aListener);
	}
	
	/**
	 * Creates and links the items in the Shopper only menu tab.
	 */
	public void setCustMenu(){
		customerMenu = new JMenu("Customer actions");
		bar.add(customerMenu);
		
		cListener = new CustomerListener();
		
		viewCart = new JMenuItem("View Items in Cart");
		customerMenu.add(viewCart);
		viewCart.addActionListener(cListener);
		
		viewInvoices = new JMenuItem("View Invoices");
		customerMenu.add(viewInvoices);
		viewInvoices.addActionListener(cListener);
		
		checkout = new JMenuItem("Checkout");
		customerMenu.add(checkout);
		checkout.addActionListener(cListener);
	}
	
	/**
	 * @return whether the user has logged out.
	 */
	public boolean hasLoggedOut() {
		return loggedOut;
	}
	
	private class RegisterListener implements ActionListener {
		//For when you press "Register" in the start menu
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//TODOconnect to back-end
			if (arg0.getActionCommand().equals("Register as shopper")){
				JTextField newUsername = new JTextField();
				JTextField newPassword = new JPasswordField();
				ArrayList<City> cityChoices = Store.graph.getCities();
				
				ArrayList<String> cNames = new ArrayList<String>();
				for(City city : cityChoices){
					cNames.add(city.getName());
				}
				
				String[] cityNames = new String[cNames.size()];
				for(int i = 0; i < cNames.size(); i++) {
					cityNames[i] = cNames.get(i);
				}
				JComboBox<Object> cityList = new JComboBox<Object>(cityNames);
				
				JTextField shopperAddress = new JTextField();
				
				Object[] message = {
				    "Enter desired username:", newUsername,
				    "Enter desired password:", newPassword,
				    "Enter your address:", shopperAddress,
				    "Select your city:", cityList 
				};
		 
				int option = JOptionPane.showConfirmDialog(null, message, "Register new user", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					String userName = newUsername.getText();
					String userPass = newPassword.getText();
					String userAddress = shopperAddress.getText();
					City userCity = cityChoices.get(cNames.indexOf(cityList.getSelectedItem()));
					System.out.println(userName);
					System.out.println(userPass);
					System.out.println(userAddress);
					System.out.println(userCity);
					System.out.println(store);
					try {
						store.createShopper(userName, userPass, userCity, userAddress);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else if (arg0.getActionCommand().equals("Register as administrator")){
				JTextField newUsername = new JTextField();
				JTextField newPassword = new JPasswordField();
				JTextField secretCode = new JPasswordField();
				
				Object[] message = {
				    "Enter desired username:", newUsername,
				    "Enter desired password:", newPassword,
				    "Enter verification code:", secretCode
				};
		 
				int option = JOptionPane.showConfirmDialog(null, message, "Register new user", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					String userName = newUsername.getText();
					String userPass = newPassword.getText();
					String code = secretCode.getText();
					if (code.equals("hotlinebling")) {
						try {
							store.createAdmin(userName, userPass);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private class LogoutListener implements ActionListener { 
		//For when you press "log out" in the start menu
		
		public void actionPerformed(ActionEvent arg0) {
			loginItem.setEnabled(true);
			registerAsShopperItem.setEnabled(true);
			registerAsAdminItem.setEnabled(true);
			logoutItem.setEnabled(false);
			loggedIn = false;
			loggedOut = true;
			bar.remove(4); //removes welcome(name)
			store.logout(sessionID);
			actionMenu.setEnabled(false);
			customerMenu.setEnabled(false);
		
			for (JButton key: boxes3.keySet()){
				key.setText("Log in to click");
			}	
			userType = "Guest";
			revalidate();
		}
	}
	
	private class AdminListener implements ActionListener {
		//For all of the buttons under Administrator functions

		@Override
		public void actionPerformed(ActionEvent arg0) {	
			if (arg0.getActionCommand().equals("Add Category")){
				JTextField newCategory = new JTextField();
				Object[] message = {
				    "Type in name for new category:", newCategory,
				};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Add category", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    String categoryName = newCategory.getText();
				    try {
						Store.addCategory(categoryName, sessionID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else if (arg0.getActionCommand().equals("Add Product")){		
				ArrayList<Category> cat = new ArrayList<Category>();
				for (Category c : Store.getCategories().values()) {
					cat.add(c);
				}
				ArrayList<String> cNames = new ArrayList<String>(); 
				
				for (Category c : cat){
					cNames.add(c.getName());
				}
				
				String[] catNames = new String[cNames.size()];
				for(int i = 0; i < cNames.size(); i++) {
    				catNames[i] = cNames.get(i);
				}
				
				JComboBox<Object> catList = new JComboBox<Object>(catNames);
				JTextField imageFileName = new JTextField();
				JTextField prodName = new JTextField();
				JTextField prodDescription = new JTextField();
				JTextField prodPrice = new JTextField();
				
				Object[] message = {
					"Choose product category:", catList,
					"Enter image filename:", imageFileName,
					"Enter name for product:", prodName,
				    
				    "Enter product description:", prodDescription,
				    "Enter product price:", prodPrice
				};
		     
				int option = JOptionPane.showConfirmDialog(null, message, "Add product", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					Category newProdCat = cat.get(cNames.indexOf((String) catList.getSelectedItem())); 
					String newProdFileName = imageFileName.getText();
					String newProdName = prodName.getText();
					String newProdDesc = prodDescription.getText();
					Double newProdPrice = Double.parseDouble(prodPrice.getText());
					Product newProduct = new Product(newProdCat, newProdFileName, newProdName, newProdDesc, newProdPrice);
					try {
						Store.addProduct(newProduct, sessionID);
					} catch (IOException e) {
						e.printStackTrace();
					}
					addNewProducts(newProduct);
						
					for (JButton key: boxes3.keySet()){
						key.setText("Modify product");
					}
					revalidate();
				} 
			}
			else if (arg0.getActionCommand().equals("Add Warehouse")){
				ArrayList<City> cityChoices = Store.graph.getNonDistCenters();
				ArrayList<String> cNames = new ArrayList<String>();
				for(City city : cityChoices){
					cNames.add(city.getName());
				}
				
				String[] cityNames = new String[cNames.size()];
				for(int i = 0; i < cNames.size(); i++) {
					cityNames[i] = cNames.get(i);
				}
				JComboBox<Object> cityList = new JComboBox<Object>(cityNames);
				
				Object[] message = {
				    "Choose new warehouse location:", cityList
				};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Add warehouse location", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					City selectedCity = cityChoices.get(cNames.indexOf(cityList.getSelectedItem()));
					try {
						Store.modifyCity(selectedCity, true, sessionID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
			else if (arg0.getActionCommand().equals("Modify Quantities")){
				ArrayList<Product> availableProducts = new ArrayList<Product>(); //products drop-down menu
		
				for(Map.Entry<JButton, ArrayList<Object>> item : boxes3.entrySet()){
				    Product anItem = (Product) item.getValue().get(0);
				    availableProducts.add(anItem);
				}
				
				ArrayList<String> pNames = new ArrayList<String>();
				
				for (Product p : availableProducts){
					pNames.add(p.getName());
				}
				
				String[] productNames = new String[pNames.size()];
				for(int i = 0; i < pNames.size(); i++) {
    				productNames[i] = pNames.get(i);
				}
				JComboBox<Object> productList = new JComboBox<Object>(productNames);

				ArrayList<City> warehouseChoices = Store.graph.getDistributionCenters(); //warehouse drop-down menu
				ArrayList<String> wNames = new ArrayList<String>();
				for(City warehouse : warehouseChoices){
					wNames.add(warehouse.getName());
				}
				
				String[] warehouseNames = new String[wNames.size()];
				for(int i = 0; i < wNames.size(); i++) {
					warehouseNames[i] = wNames.get(i);
				}
				JComboBox<Object> warehouseList = new JComboBox<Object>(warehouseNames);
	
				JTextField prodAmount = new JTextField();

				Object[] message = {
					"Choose product to replenish:", productList,
				    "Choose warehouse:", warehouseList,
				    "Enter correct quantity number:", prodAmount
				};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Add warehouse location", JOptionPane.OK_CANCEL_OPTION);
				
				if (option == JOptionPane.OK_OPTION) {
					int newQuantity = Integer.parseInt(prodAmount.getText());
					Product chosenProduct = availableProducts.get(pNames.indexOf(productList.getSelectedItem()));
					City warehouseCity = warehouseChoices.get(wNames.indexOf(warehouseList.getSelectedItem()));
					try {
						Store.modifyQuantity(chosenProduct, warehouseCity, newQuantity, sessionID);
						String prodDescription = writeProdDescription(chosenProduct);
						JButton theButton = new JButton();
						
						for (Entry<JButton, ArrayList<Object>> entry : boxes3.entrySet()) {
							if(entry.getValue().get(0).equals(chosenProduct)){
								theButton = entry.getKey();
							}
						}
						((JLabel) boxes3.get(theButton).get(2)).setText(prodDescription);
					} catch (IOException e) {
						e.printStackTrace();
					}	
				} 
			}
			
			else if (arg0.getActionCommand().equals("Add Route")){
				ArrayList<City> cityChoices = Store.graph.getCities();
				
				ArrayList<String> cNames = new ArrayList<String>();
				for(City city : cityChoices){
					cNames.add(city.getName());
				}
				
				String[] cityNames = new String[cNames.size()];
				for(int i = 0; i < cNames.size(); i++) {
					cityNames[i] = cNames.get(i);
				}
				JComboBox<Object> cityAList = new JComboBox<Object>(cityNames);
				JComboBox<Object> cityBList = new JComboBox<Object>(cityNames);

				JTextField distance = new JTextField();
				
				Object[] message = {
					"Choose map point A:", cityAList,
					"Choose map point B:", cityBList,
				    "Enter distance:", distance
				};
	
				int option = JOptionPane.showConfirmDialog(null, message, "Add warehouse location", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if(!(cityAList.getSelectedItem().equals(cityBList.getSelectedItem()))){ //checks if cities chosen are different
						
						City cityA = cityChoices.get(cNames.indexOf(cityAList.getSelectedItem()));
						City cityB = cityChoices.get(cNames.indexOf(cityBList.getSelectedItem()));
						int travelDistance = Integer.parseInt(distance.getText());
						
						if(!(cityA.cityConnections.containsKey(cityB))){ //checks if connection already exists
							try {
								Store.addEdge(cityA, cityB, travelDistance, sessionID);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "You can't add a route to the same cities, silly!");
					}
				} 
			}
			
			else if (arg0.getActionCommand().equals("Add City")){
				String[] options = new String[]{"Yes", "No"};
				JComboBox<Object> isDistCenter = new JComboBox<Object>(options);

				JTextField newCityName = new JTextField();
				
				Object[] message = {
					"Enter name for city:", newCityName,
					"Is this a distribution center?:", isDistCenter  
				};
		       
				int option = JOptionPane.showConfirmDialog(null, message, "Add new city", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					boolean distribution = false;
					if(isDistCenter.getSelectedItem().equals("Yes")){
						distribution = true;
					}
					
					String cityName = newCityName.getText();
					try {
						Store.addCity(cityName, distribution, sessionID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
			
			else if (arg0.getActionCommand().equals("Produce Sales Report")){
				String report = Store.produceSalesReport();
				JOptionPane.showMessageDialog(null, report);
			}
		}
	}
	
	private class CustomerListener implements ActionListener {
		//For all of the buttons under Customer functions

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(arg0.getActionCommand()); //returns name of JMenuItem eg String "Add Category;
			
			if (arg0.getActionCommand().equals("View Items in Cart")){
				myCart = new JFrame();
				
				JPanel cartPanel = new JPanel(new GridLayout(0,3));
				JScrollPane cartPane = new JScrollPane(cartPanel);
				myCart.setContentPane(cartPane);
				
				//myCart.setLayout(new GridLayout(0,3));
				myCart.setSize(1000, 500);
				
				HashMap<Product, Integer> inCart = shopper.getCart();
				
				ArrayList<Product> products = new ArrayList<Product>();
				ArrayList<Integer> amount = new ArrayList<Integer> ();
				
				double cost = 0;
				
				for(Map.Entry<Product, Integer> entry : inCart.entrySet()){ //calculates total cost
					products.add(entry.getKey());
					amount.add(entry.getValue());
					cost += ((entry.getKey().getPrice()) * entry.getValue());
				}
				
				myCart.setTitle("Total: $" + Double.toString(cost)); //displays total cost as window title
				
				for (Product p : products){
					JLabel prod = new JLabel();
					JLabel box2 = new JLabel();
					JButton box3 = new JButton();
					ImageIcon prodImage = new ImageIcon("images/"+p.getImage());
					prod.setIcon(prodImage);		
					String box2Message = "<html>";
					box2Message += p.getName();
					box2Message += "<br>Quantity: ";
					box2Message += String.valueOf(amount.get(products.indexOf(p)));
					box2Message += "<br></html>";
					box2.setText(box2Message);
					box3.setText("Change item quantity");
					cartPanel.add(prod);
					cartPanel.add(box2);
					cartPanel.add(box3);
					box3.addActionListener(modifyProdListener);
					ArrayList<Object> box = new ArrayList<Object>();
					box.add(p);
					box.add(prod);
					box.add(box2);
					boxInfo.put(box3, box);
				}
				
				myCart.setResizable(true);
				myCart.setVisible(true);
			}
			
			else if (arg0.getActionCommand().equals("View Invoices")){
				HashMap<Integer, Invoice> invoice = shopper.getAllInvoices();
				
				ArrayList<Integer> invoiceNums = new ArrayList<Integer>();
				
				for(Map.Entry<Integer, Invoice> entry : invoice.entrySet()){
				    invoiceNums.add(entry.getKey());
				}
				
				Integer[] invoiceNumbers = new Integer[invoiceNums.size()];
				for(int i = 0; i < invoiceNums.size(); i++) {
					invoiceNumbers[i] = invoiceNums.get(i);
				}
				
				JComboBox<Object> invoiceChoices= new JComboBox<Object>(invoiceNumbers);
				
				Object[] message = {
						"Choose invoices to view:", invoiceChoices
					};

				int option = JOptionPane.showConfirmDialog(null, message, "View invoices", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					Invoice selectedInvoice = shopper.getInvoice((int) invoiceChoices.getSelectedItem());
					String invoiceMessage = selectedInvoice.toString();
					
					JOptionPane.showMessageDialog(null, invoiceMessage);
				} 
			}
			
			else if (arg0.getActionCommand().equals("Checkout")){
				JLabel checkoutMessage = new JLabel("Are you sure?");
				
				Object[] message = {
						"Check out confirmation.", checkoutMessage
					};

				int option = JOptionPane.showConfirmDialog(null, message, "Checkout items", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					try {
						shopper.checkOut();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
		}
	}
	
	private class ModProdListener implements ActionListener {
		//For when you are a customer who has opened up viewCart, and want to modify the quantity of items in cart
		
		@Override 
		public void actionPerformed(ActionEvent arg0){
			JButton clickedButton = (JButton) arg0.getSource();
			Product prodFromButton = (Product) boxInfo.get(clickedButton).get(0);
			System.out.println(prodFromButton.getName());
			
			JLabel instructions = new JLabel("Input new quantity (0 to remove)");
			JLabel productName = new JLabel(prodFromButton.getName());
			JLabel productDesc = new JLabel(prodFromButton.getDescription());
			JLabel productPrice = new JLabel(String.valueOf(prodFromButton.getPrice()));
			JTextField newQuantity = new JTextField();
			Object[] message = {
					"Modify cart.", instructions,
					"Product name:", productName,
					"Product description:", productDesc,
				    "Product price: $", productPrice,
				    "Enter desired quantity (0 to remove):", newQuantity
				};
			int option = JOptionPane.showConfirmDialog(null, message, "Modify product info", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				int changedQuantity = Integer.parseInt(newQuantity.getText());
				try {
					shopper.modifyCart(prodFromButton, changedQuantity);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				String box2Message = "<html>";
				box2Message += prodFromButton.getName();
				box2Message += "<br>Quantity: ";
				box2Message += String.valueOf(shopper.getCart().get(prodFromButton));
				box2Message += "<br></html>";	
				((JLabel) boxInfo.get(clickedButton).get(2)).setText(box2Message); //changes name

				double cost = 0;
				HashMap<Product, Integer> inCart = shopper.getCart();
				
				for(Map.Entry<Product, Integer> entry : inCart.entrySet()){
			
					cost += ((entry.getKey().getPrice()) * entry.getValue());
				}
				
				myCart.setTitle("Total: $"+Double.toString(cost));
				//modifies info on main screen:
				String prodDescription = writeProdDescription(prodFromButton);
				
				System.out.println(prodDescription);
				JButton theButton = new JButton();
				
				for (Entry<JButton, ArrayList<Object>> entry : boxes3.entrySet()) {
					if(entry.getValue().get(0).equals(prodFromButton)){
						theButton = entry.getKey();
					}
				}
				((JLabel) boxes3.get(theButton).get(2)).setText(prodDescription);
				
				revalidate();
			}
		}
	}
	
	private class BodyListener implements ActionListener {
		//For the buttons in the "body" of the JFrame when you open the application. 
		//Has different functions depending if you're a guest, administrator, or customer
		
		@Override
		public void actionPerformed(ActionEvent arg0){
			JButton clickedButton = (JButton) arg0.getSource();
			Product prodFromButton = (Product) boxes3.get(clickedButton).get(0);
			System.out.println(prodFromButton.getName());
			if (userType.equals("Guest")){
				System.out.println("log in to use this");
			}
			else if (userType.equals("Admin")){
				JLabel instructions = new JLabel("Leave blank if no change is desired.");
				JTextField newProdName = new JTextField(prodFromButton.getName());
				JTextField newProdDesc = new JTextField(prodFromButton.getDescription());
				String oldPrice = Double.toString(prodFromButton.getPrice());
				JTextField newProdPrice = new JTextField(oldPrice);
			
				Object[] message = {
					"Modify Product.", instructions,
					"Change product name:", newProdName,
					"Change product description:", newProdDesc,
				    "Change product price (double):", newProdPrice
				};
		    
				int option = JOptionPane.showConfirmDialog(null, message, "Modify product info", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					prodFromButton.setName(newProdName.getText());
					prodFromButton.setDescription(newProdDesc.getText());
						
					double priceValue = Double.parseDouble(newProdPrice.getText());
					prodFromButton.setPrice(priceValue);
					String prodDescription = writeProdDescription(prodFromButton);
				
					((JLabel) boxes3.get(clickedButton).get(2)).setText(prodDescription); //changes description
					try {
						Store.modifyProduct(prodFromButton,
											prodFromButton.getCategory(),
											prodFromButton.getImage(),
											newProdName.getText(),
											newProdDesc.getText(),
											priceValue, sessionID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 	
			}
			else if (userType.equals("Shopper")){
				JLabel prodName = new JLabel(prodFromButton.getName());
				JTextField desiredQuantity = new JTextField();
				Object[] message = {
					"Product:", prodName,
				    "Enter desired quantity (integer):", desiredQuantity
				};
	
				int option = JOptionPane.showConfirmDialog(null, message, "Add item to cart", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					try {
						shopper.addToCartandUpdate(prodFromButton, Integer.parseInt(desiredQuantity.getText()));
						String prodDescription = writeProdDescription(prodFromButton);
						JButton theButton = new JButton();
						
						for (Entry<JButton, ArrayList<Object>> entry : boxes3.entrySet()) {
							if(entry.getValue().get(0).equals(prodFromButton)){
								theButton = entry.getKey();
							}
					
						}
						
						((JLabel) boxes3.get(theButton).get(2)).setText(prodDescription);
						
					} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
					}
				} 
			}
			revalidate();
		}
	}
	
	private class SearchListener implements ActionListener {
		//for the search options
		
		@Override 
		public void actionPerformed(ActionEvent arg0){
			Object searchOption = arg0.getActionCommand();
			if(searchOption.equals("by price range")){
				JTextField min = new JTextField();
				JTextField max = new JTextField();		
				
				Object[] message = {
				    "Min Price:", min,
				    "Max Price:", max
				};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Search", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					double minPrice = 0;
					double maxPrice = 999999;
				
					try {
						minPrice = (Double.valueOf(min.getText()));
					} catch (NumberFormatException e) {e.printStackTrace();}
					try {
						maxPrice = (Double.valueOf(max.getText()));
					} catch (NumberFormatException e) {e.printStackTrace();}
					ArrayList<Product> inPriceRange = Store.searchByPriceRange(minPrice, maxPrice); 
					
					testPanel.removeAll();
					boxes3.clear();
					for (Product i : inPriceRange){
						addNewProducts(i);
					}
				}	
			}
			else if(searchOption.equals("by category")){
				ArrayList<Category> cat = new ArrayList<Category>();
				for (Category c : Store.getCategories().values()) {
					cat.add(c);
				}
				ArrayList<String> cNames = new ArrayList<String>(); 
				for (Category c : cat){
					cNames.add(c.getName());
				}
				String[] catNames = new String[cNames.size()];
				for(int i = 0; i < cNames.size(); i++) {
    				catNames[i] = cNames.get(i);
				}
				JComboBox<Object> catList = new JComboBox<Object>(catNames);
				Object[] message = {
					    "Choose category to search in:", catList
					};
					
					int option = JOptionPane.showConfirmDialog(null, message, "Search", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						Category prodCat = cat.get(cNames.indexOf((String) catList.getSelectedItem()));
						
						ArrayList<Product> inCategory = Store.searchByCategory(prodCat); 
						
						testPanel.removeAll();
						boxes3.clear();
						for (Product i : inCategory){
							addNewProducts(i);
						}
					}
			}
			revalidate();
		}
	}
	
	private class RefreshListener implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent arg0){
			testPanel.removeAll();
			boxes3.clear();
			addProducts(); //add products from scratch
		}
	}

	private class LoginListener implements ActionListener {
		//For the "log in" button in the Start menu

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				User user = login();
				if(user != null) {
					int sesh = user.getSessionID();
					sessionID = sesh;
					loginItem.setEnabled(false);
					registerAsShopperItem.setEnabled(false);
					registerAsAdminItem.setEnabled(false);
					logoutItem.setEnabled(true);
					loggedIn = true;
					loggedOut = false;
					if (Store.isAdmin(sessionID)) {
						actionMenu.setEnabled(true);	
						userType = "Admin";
						admin = (Administrator) Store.getsessionIDList().get(sessionID);
						for (JButton key: boxes3.keySet()){
							key.setText("Modify product");
						}
					}
					else if (Store.isShopper(sessionID)) {
						customerMenu.setEnabled(true);		
						userType = "Shopper";
						shopper = (Shopper) Store.getsessionIDList().get(sessionID);
						
						for (JButton key: boxes3.keySet()){
							key.setText("Add to cart");
						}
					}
					
					bar.add(add(Box.createHorizontalGlue()));
					bar.add(welcome);
					for(JLabel l : prodForSale)
						l.setToolTipText("Add to shopping cart!");
					revalidate();
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private User login() throws IOException {
		//authenticates user upon login
		
		JTextField username = new JTextField();
		JTextField password = new JPasswordField();		
		
		Object[] message = {
		    "Username:", username,
		    "Password:", password
		};
		User user = null;
		int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			String name = username.getText();
			String pw = new String(((JPasswordField) password).getPassword());
			System.out.println("NAME: " + name + " PASS: " + pw);
			user = Store.login(username.getText(), new String(((JPasswordField) password).getPassword()));
		    if(user != null)
		    	welcome.setText("Welcome, " + username.getText() + "!  ");
		    
		    revalidate();
		   
		} else {
		    return null;
		}
		return user;
	}
	
	private void addProducts() { //loads products from file
		Collection<Product> products = Store.getProductList().values();
		for(Product p : products) {
			addNewProducts(p);
			revalidate();
		}
	}
	
	private void addNewProducts(Product p) {
		JLabel prod, box2;
        JButton box3;
		ImageIcon prodImage = new ImageIcon("images/"+p.getImage());
		String prodDescription = writeProdDescription(p);
		
		prod = new JLabel();
		box2 = new JLabel();
		box3 = new JButton();
		prod.setIcon(prodImage);
		prod.setToolTipText("Login to buy products!");
		box2.setText(prodDescription);
		testPanel.add(prod);
		testPanel.add(box2);
		testPanel.add(box3);
		box3.addActionListener(bodListener);
		setBox3Message(box3);
		prodForSale.add(prod);
		
		ArrayList<Object> boxes3info = new ArrayList<Object>();
		boxes3info.add(p);
		boxes3info.add(prod);
		boxes3info.add(box2);
		
		boxes3.put(box3, boxes3info);
		
		/*for(Map.Entry<JButton, ArrayList<Object>> entry : boxes3.entrySet()){
			Object thing = entry.getValue().get(0);
			System.out.println(thing);
		}*/
		//System.out.println(boxes3.values());
	}
	
	private void setBox3Message(JButton box3){
		if (!(userType == null)){
			if(userType.equals("Guest")){
				box3.setText("Log in to click");
			}
			
			else if(userType.equals("Admin")){
				box3.setText("Modify product");
			}
			
			else if(userType.equals("Shopper")){
				box3.setText("Add to cart");
			}
		}	
	}
	
	private String writeProdDescription(Product p){
		String prodDescription = "<html>";
		prodDescription += p.getName() + "<br>";
		prodDescription += "$" + p.getPrice() + "<br>";
		prodDescription += "Quantity: " + p.getQuantity() + "<br>";
		prodDescription += p.getDescription() + "<br></html>";
		return prodDescription;
	}
}