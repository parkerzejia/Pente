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
 * @author maxbarsh
 *
 */
public class WinDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private JButton ngButton = new JButton("New Game");
	private String message;
	/**
	 * Launch the application.
	 */
	public static void main ( String[] args ) {
		try {
			WinDialog dialog = new WinDialog(1);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinDialog (int winningPlayer) {
		setBounds(100,100,450,300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5,5,5,5));
		getContentPane().add(contentPanel,BorderLayout.CENTER);
		{
			JLabel label = new JLabel();
			if(winningPlayer==1) {
		 message = "Congratulations!! YOU wins!!";
			}else {
		 message = "LOSE! just try again...";
			}
			
			label.setText(message);
			label.setFont(new Font("Tahoma",Font.CENTER_BASELINE,18));
			contentPanel.add(label);
			contentPanel.setBackground(new Color(222, 184, 135));
			
			ImageIcon winDragon = new ImageIcon("images/win_dragon.png");
			Image img = winDragon.getImage();  //transform it
			Image newImg = img.getScaledInstance(200, 220,  java.awt.Image.SCALE_SMOOTH);
			winDragon = new ImageIcon(newImg);
			
			JLabel imageLabel = new JLabel("", winDragon, JLabel.CENTER);
			imageLabel.setVisible(true);
			contentPanel.add(imageLabel);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(222, 184, 135));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane,BorderLayout.SOUTH);
			{
				JButton doneButton = new JButton("Done");
				doneButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				doneButton.setActionCommand("Done");
				buttonPane.add(doneButton);
				getRootPane().setDefaultButton(doneButton);
			}
			{
				
				ngButton.setActionCommand("New Game Button2");
				buttonPane.add(ngButton);
			}
		}
	}
	
	public JButton getNewGameButton() {
		return ngButton;
	}

}
