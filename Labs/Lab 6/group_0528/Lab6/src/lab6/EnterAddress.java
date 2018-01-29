package lab6;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterAddress extends JFrame {
	
	private JPanel panel;
	private JTextField[] textFields;
	private GridLayout gLayout;
	private ButtonHandler handler;
	private JButton save, cancel;
	private String filename;
	private Address address;
	
	
	public EnterAddress(String filename) {
		super("Enter an address");
		
		this.filename = filename;
		
		gLayout = new GridLayout(6,2);
		panel = new JPanel();
		panel.setLayout(gLayout);
		textFields = new JTextField[5];
		for(int i = 0; i < textFields.length; i++)
			textFields[i] = new JTextField(20);

		addLabel("Number and Street",panel);
		addBox(textFields[0], panel);
		addLabel("City", panel);
		addBox(textFields[1], panel);
		addLabel("Province", panel);
		addBox(textFields[2], panel);
		addLabel("Postal Code", panel);
		addBox(textFields[3], panel);
		addLabel("Country", panel);
		addBox(textFields[4], panel);
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		handler = new ButtonHandler();
		addButton(save, panel, handler);
		addButton(cancel, panel, handler);
		
		this.add(panel);
		
	}
	
	private void addLabel(String labelText, JPanel panel) {
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
		
		public void actionPerformed(ActionEvent action){
			
			if (action.getActionCommand() == save.getActionCommand()){
				address = new Address(textFields[0].getText(),textFields[1].getText(),textFields[2].getText(),textFields[3].getText(),textFields[4].getText());
				try {
					FileWriter writer = new FileWriter(filename, true);
					writer.append(address.toString());
					writer.append("\n");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					}
			}
			else{
				for (int i=0; i<5; i++) {
					textFields[i].setText("");
				}
			}
				
					
		

		}



		
	}
}