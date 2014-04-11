package decoration_Package;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bsGame_v6.BattleShipGame;

/*** StarterWindow Class */

public class StarterWindow implements Serializable {
	private JTextField textField4Name;
	private JPanel namePanel;
	private JPanel confirmPanel;
	private JPanel mainPanel;
	private JFrame starterFrame;
	private BattleShipGame bsGame;

	public StarterWindow(BattleShipGame bs) {
		bsGame = bs;
		constructNameForm();
	}

	private void constructNameForm() {

		// NamePanel
		namePanel = new JPanel();
		textField4Name = new JTextField(20);
		namePanel.setLayout(new BorderLayout());
		namePanel.add(new JLabel("Please enter your name ADMIRAL: "),
				BorderLayout.WEST);
		namePanel.add(textField4Name, BorderLayout.EAST);

		// ConfirmPanel
		confirmPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				WelcomeScreen.musicPlayer.stopPlayer();
				clickedOkButton();
			}
		});
		confirmPanel.setLayout(new BorderLayout());
		confirmPanel.add(okButton, BorderLayout.CENTER);

		// MainPanel
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		mainPanel.setLayout(new BorderLayout(2, 2));
		mainPanel.add(namePanel, BorderLayout.CENTER);
		mainPanel.add(confirmPanel, BorderLayout.SOUTH);

		// Frame
		starterFrame = new JFrame("BattleShips Game");
		starterFrame.add(mainPanel);
		starterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		starterFrame.pack();
		starterFrame.setResizable(false);
		starterFrame.setLocationRelativeTo(null);
		starterFrame.setVisible(true);
	}

	public void clickedOkButton() {
		bsGame.setName(getName());
		starterFrame.dispose();

		bsGame.startGame();
	}

	public String getName() {
		String name = textField4Name.getText();
		return "Admiral " + name;
	}
}
