package multiClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.event.ActionEvent;

/**
 * @author maxbarsh
 *
 */
public class Tutorial extends JFrame {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application. Used mostly for testing and came as a part of the WindowBuilder setup.
	 */
	public static void main ( String[] args ) {
		try {
			Tutorial frame = new Tutorial();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for a new Tutorial 
	 */
	public Tutorial () {
		setBounds(100,100,615,300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		getContentPane().add(contentPanel,BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(contentPanel);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //make it so that there is always a vertical scroll bar to go through the instructions
		contentPanel.setBackground(new Color(222, 184, 135));
		scroll.setVisible(true);
		getContentPane().add(scroll);
		setTitle("Help");
		{
			File file = new File("Rules.txt");
			JTextArea txt = new JTextArea(30, 48);
			txt.setEditable(false);
			txt.setLineWrap(true);
			txt.setBackground(new Color(222, 184, 135));
			String str = "";
			try {
				Scanner in = new Scanner(file);  //read from the Rules text file 
				while(in.hasNext()) {
					str = str + in.nextLine() + " \n";
				}
				txt.setText(str);
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			contentPanel.add(txt);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(222, 184, 135));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane,BorderLayout.SOUTH);

			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(closeButton);
			}
		}
	}

}
