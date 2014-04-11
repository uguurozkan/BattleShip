package decoration_Package;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import bsGame_v6.BattleShipGame;

public class WelcomeScreen extends JFrame implements Serializable {
	private JPanel openningPicturePanel;
	private JPanel aboutPanel;
	private JPanel howtoPanel;
	private BattleShipGame bsGame;
	private JLabel openingPictureLabel;
	private JLabel openingLogoLabel;
	private JLabel aboutLabel;
	protected static MusicPlayer musicPlayer;

	public WelcomeScreen(BattleShipGame bs) {
		super("BATTLESHIP GAME 'DESTROY ENEMY FLEET' ");
		this.bsGame = bs;
		constructGUI();
		musicPlayer.runPlayer();
	}

	private void constructGUI() {
		openningPicturePanel = new JPanel();
		openningPicturePanel.setLayout(null);

		aboutPanel = new JPanel();
		aboutPanel.setLayout(null);

		openingPictureLabel = new JLabel(new ImageIcon(
				"src\\Welcome-Screen.jpg"));
		openingLogoLabel = new JLabel(new ImageIcon("src\\logo.jpg"));
		aboutLabel = (new JLabel(new ImageIcon("src\\aboutpart.jpg")));

		openningPicturePanel.add(openingLogoLabel);
		openingLogoLabel.setLocation(600, 10);
		openingLogoLabel.setSize(300, 133);

		howtoPanel = new JPanel();
		howtoPanel.setLayout(null);

		JLabel howtoLabel = new JLabel(new ImageIcon("src\\howto.jpg"));

		// New Game
		JButton newButton = new JButton("New Game");
		newButton.setSize(120, 25);
		newButton.setLocation(755, 410);
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				new StarterWindow(bsGame);
			}
		});
		openningPicturePanel.add(newButton);

		// How to Play
		JButton howtoButton = new JButton("How to Play");
		howtoButton.setSize(120, 25);
		howtoButton.setLocation(755, 441);
		openningPicturePanel.add(howtoButton);
		howtoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openningPicturePanel.setVisible(false);
				add(howtoPanel);
				howtoPanel.setVisible(true);
				repaint();
			}
		});

		// Load
		JButton loadButton = new JButton("Load Game");
		loadButton.setSize(120, 25);
		loadButton.setLocation(755, 472);
		openningPicturePanel.add(loadButton);
		loadButton.addActionListener(new LoadButtonHandler());

		// About
		JButton aboutButton = new JButton("About");
		aboutButton.setSize(120, 25);
		aboutButton.setLocation(755, 503);
		aboutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openningPicturePanel.setVisible(false);
				add(aboutPanel);
				aboutPanel.setVisible(true);
				repaint();
			}
		});
		openningPicturePanel.add(aboutButton);

		// Exit
		JButton exitButton = new JButton("Exit");
		exitButton.setSize(120, 25);
		exitButton.setLocation(755, 534);
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		openningPicturePanel.add(exitButton);

		// Sound on-off
		JRadioButton soundButton = new JRadioButton("Sound on", true);
		soundButton.setSize(82, 20);
		soundButton.setLocation(50, 534);
		soundButton.addActionListener(new RadioButtonHandler(soundButton));
		openningPicturePanel.add(soundButton);

		// Back
		JButton backButton = new JButton("Back");
		// backButton.setBorder(new EmptyBorder(1,1,1,1));
		backButton.setSize(120, 25);
		backButton.setLocation(736, 289);
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				aboutPanel.setVisible(false);
				openningPicturePanel.setVisible(true);
				repaint();
			}
		});
		aboutPanel.add(backButton);

		// back1
		JButton backButton1 = new JButton("Back");
		// backButton.setBorder(new EmptyBorder(1,1,1,1));
		backButton1.setSize(120, 25);
		backButton1.setLocation(690, 130);
		backButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				howtoPanel.setVisible(false);
				openningPicturePanel.setVisible(true);
				repaint();
			}
		});
		howtoPanel.add(backButton1);

		openingPictureLabel.setSize(960, 640);
		openingPictureLabel.setLocation(0, 0);
		openningPicturePanel.add(openingPictureLabel);

		aboutLabel.setLocation(-6, -5);
		aboutLabel.setSize(960, 640);
		aboutPanel.add(aboutLabel);
		aboutPanel.setLayout(null);
		aboutPanel.setSize(960, 640);
		
		howtoPanel.add(howtoLabel);
		howtoLabel.setBounds(0, 0, 960, 640);

		add(openningPicturePanel); // Add panel to frame

		setSize(960, 640);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		// setLocationRelativeTo(null); // Kucuk pc lerde sorun cikartiyor.
		setVisible(true);

	}

	private class RadioButtonHandler implements ActionListener {
		private JRadioButton radioButton;

		public RadioButtonHandler(JRadioButton radioButton) {
			this.radioButton = radioButton;
			musicPlayer = new MusicPlayer("maintheme", "wav");
		}

		public void actionPerformed(ActionEvent event) {
			if (!radioButton.isSelected()) {
				radioButton.setText("Sound off");
				musicPlayer.stopPlayer();
			} else {
				radioButton.setText("Sound on");
				musicPlayer.runPlayer();
			}
		}
	}
}
