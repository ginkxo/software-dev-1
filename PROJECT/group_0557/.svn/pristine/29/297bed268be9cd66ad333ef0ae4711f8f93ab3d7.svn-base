//package finalproject;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ShopApp extends JFrame implements ActionListener{
	
	private JPanel panel;
	private JTextField[] textFields;
	private GridLayout gLayout;
	//private ButtonHandler handler;
	private JButton login, register, logout;
	private String filename;
	//private Address address;
	
	private JButton[] userInfo;
	private JButton[] adminInfo;
	private boolean loggedIn;
	
	
	public ShopApp(){
		super("PokeStore");
		setLayout(null);
		setSize(764,520);
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		loggedIn = false;
		
		/*gLayout = new GridLayout(6,2);
		panel = new JPanel();
		panel.setLayout(gLayout);*/
		
		
		//textFields = new JTextField[5];
		//for(int i = 0; i < textFields.length; i++)
			//textFields[i] = new JTextField(20);

		/*addLabel("Number and Street",panel);
		addBox(textFields[0], panel);
		addLabel("City", panel);
		addBox(textFields[1], panel);
		addLabel("Province", panel);
		addBox(textFields[2], panel);
		addLabel("Postal Code", panel);
		addBox(textFields[3], panel);
		addLabel("Country", panel);
		addBox(textFields[4], panel);*/
		
		setDefaultPage();
		//defalt page:
		/*userInfo = new JButton[3];
		userInfo[0] = new JButton("Login");
		userInfo[1] = new JButton("Log Out");
		userInfo[2] = new JButton("Register");
		
		for(int i = 0; i<3; i++){
			userInfo[i].addActionListener(this);
			userInfo[i].setSize(200, 30);
			userInfo[i].setLocation(i*252 + 30, 10);
			add(userInfo[i]);
		}*/
			
		/*login = new JButton("Login");
		logout = new JButton("Log Out");
		register = new JButton("Register");
		handler = new ButtonHandler();
		addButton(login, panel, handler);
		addButton(logout, panel, handler);
		addButton(register, panel, handler);
		
		this.add(panel);*/
		
	}
	
	
	
	/*private void addLabel(String labelText, JPanel panel) {
		JLabel label = new JLabel(labelText);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(label);
		panel.add(p);
	}
	
	private void addBox(JTextField tField, JPanel panel) {
		JPanel p = new JPanel();
		p.add(tField);
		panel.add(p);
	}
	
	private void addButton( JButton b, JPanel panel, ButtonHandler h) {
		JPanel p = new JPanel();
		b.addActionListener(h);
		p.add(b);
		panel.add(p);
	}

	private class ButtonHandler implements ActionListener {

		// Your code goes here
		@Override
		public void actionPerformed(ActionEvent action) {
			if (action.getActionCommand().equals("Login")) {
				
				
			}
			if (action.getActionCommand().equals("Log Out")) {
				for (int i = 0; i < 5; i ++){
					textFields[i].setText("");
				}
			}
		}
	}*/
	
	public void setDefaultPage(){
		userInfo = new JButton[3];
		userInfo[0] = new JButton("Login");
		userInfo[1] = new JButton("Log Out");
		userInfo[2] = new JButton("Register");
		
		for(int i = 0; i<3; i++){
			userInfo[i].addActionListener(this);
			userInfo[i].setSize(200, 30);
			userInfo[i].setLocation(i*252 + 30, 10);
			add(userInfo[i]);
		}
	}
	
	public void setAdminPage(){
		adminInfo = new JButton[3];
		adminInfo[0] = new JButton("Log Out");
		adminInfo[1] = new JButton("IDK");
	
		
		for(int i = 0; i<2; i++){
			adminInfo[i].addActionListener(this);
			adminInfo[i].setSize(200, 30);
			adminInfo[i].setLocation(i*252 + 30, 10);
			add(adminInfo[i]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (userInfo[0] == source){
			if(loggedIn == true){
				JOptionPane.showMessageDialog(null, "You're already logged in!");
			}
			else{
				String username = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Type in Username");
				String password = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Type in Password");
				loggedIn = true;
				for(int i = 0; i<3; i++){
					userInfo[1] = null;
				}
				setAdminPage();
			}
			
		}
		if (userInfo[1] == source){
			JOptionPane.showMessageDialog(null, "Bye!");
			loggedIn = false;
		}
		if (userInfo[2] == source){
			if(loggedIn == true){
				JOptionPane.showMessageDialog(null, "Please log off your current account first!");
			}
			else{
				String username = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Type in Desired Username");
				String password = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Type in Desired Password");
				if(!(username == null) && !(password == null)){
					String firstLetter = String.valueOf(password.charAt(0));
					for(int i = 0; i < password.length() - 1; i++){
						firstLetter+="*";
					}
					JOptionPane.showMessageDialog(null, "Your new username: " + username + "\nYour new Password: " + firstLetter);
				}
			}	
		}
	}
	
	public static void main(String [] args){
		ShopApp shop = new ShopApp();
	
	}
}
