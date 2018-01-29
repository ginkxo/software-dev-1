package finalproject;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Test class / main method to run GUI application.
 */
public class ShoppingApp {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		final ShopApp s = new ShopApp("testfile.txt");
		s.setSize(1000, 500);
		//s.pack();
		s.setVisible(true);
		s.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		s.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (!s.hasLoggedOut()) {
					if (JOptionPane.showConfirmDialog(s,
							"You may lose unsaved work! Are you sure?",
							"Really Closing?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else
					System.exit(0);
			}
		});
	}

}
