package lab6;

import javax.swing.JFrame;

public class TestAddress {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       EnterAddress dataScreen = new EnterAddress("hi.csv");
       dataScreen.pack();
       dataScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       dataScreen.setVisible(true);
	}	

}