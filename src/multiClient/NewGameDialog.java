package multiClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This method handles all dialog regarding starting a new game,
 * or when a user tries to exit a game 
 *
 */
public class NewGameDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public JButton ngButton;
	
	/**
	 * Launch the application.
	 */
	public static void main ( String[] args ) {
		try {
			NewGameDialog dialog = new NewGameDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewGameDialog () {
		setBounds(100,100,450,300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5,5,5,5));
		getContentPane().add(contentPanel,BorderLayout.CENTER);
		setTitle("New Game?");
		{
			JLabel label1 = new JLabel();
			label1.setText("You will lose your progress, are you sure");
			label1.setFont(new Font("Tahoma",Font.CENTER_BASELINE,18));
			contentPanel.add(label1);
			
			JLabel label2 = new JLabel();
			label2.setText("you want to start a new game?");
			label2.setFont(new Font("Tahoma",Font.CENTER_BASELINE,18));
			contentPanel.add(label2);
			contentPanel.setBackground(new Color(222, 184, 135));
			
			ImageIcon plainDragon = new ImageIcon("images/plain_dragon.png");
			Image img = plainDragon.getImage();  //transform it
			Image newImg = img.getScaledInstance(175, 175,  java.awt.Image.SCALE_SMOOTH);
			plainDragon = new ImageIcon(newImg);
			
			JLabel imageLabel = new JLabel("", plainDragon, JLabel.CENTER);
			contentPanel.add(imageLabel);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(222, 184, 135));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane,BorderLayout.SOUTH);
			{
				ngButton = new JButton("New Game");
				/*ngButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO: reset game
						dispose();
					}
				});*/
				ngButton.setActionCommand("New Game Button1");
				buttonPane.add(ngButton);
				getRootPane().setDefaultButton(ngButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	

}
