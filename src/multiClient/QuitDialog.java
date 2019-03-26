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
public class QuitDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Constructor for a new quit dialog box.
	 */
	public QuitDialog () {
		setBounds(100,100,450,300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5,5,5,5));
		getContentPane().add(contentPanel,BorderLayout.CENTER);
		setTitle("Quit?");
		{
			JLabel label = new JLabel();
			label.setText("Are you sure you want to quit?");
			label.setFont(new Font("Tahoma",Font.CENTER_BASELINE,18));
			contentPanel.add(label);
			contentPanel.setBackground(new Color(222, 184, 135));
			
			ImageIcon sadDragon = new ImageIcon("images/sad_dragon.png");
			Image img = sadDragon.getImage();  //transform it
			Image newImg = img.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
			sadDragon = new ImageIcon(newImg);
			
			JLabel imageLabel = new JLabel("", sadDragon, JLabel.CENTER);
			contentPanel.add(imageLabel);
			
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.setBackground(new Color(222, 184, 135));
			getContentPane().add(buttonPane,BorderLayout.SOUTH);
			{
				JButton quitButton = new JButton("QUIT");
				quitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				quitButton.setActionCommand("QUIT");
				buttonPane.add(quitButton);
				getRootPane().setDefaultButton(quitButton);
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
		setVisible(true);
	}
	

}
