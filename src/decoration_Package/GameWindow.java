package decoration_Package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import bsGame_v6.BattleShipGame;

/*** GameWindow Class */

public class GameWindow implements Serializable {

	public final int CPU = 1;
	public final int HUMAN = 0;

	private Display[] playersDisplays;
	protected static JFrame gameFrame;
	protected static JPanel mainPanel;
	private Attack attackHandler;
	private Deployment deploymentHandler;
	private Game game;
	private boolean isComplete = false;
	private JPanel gridPanel;
	private JPanel buttonPanel;
	private JButton autoDeploy, autoTarget, rotateShip;
	private Display humanDisplay, cpuDisplay;
	private BattleShipGame bsGame;

	public BattleShipGame getBsGame() {
		return bsGame;
	}

	public boolean isComplete() {
		return this.isComplete;
	}

	public Display getHumanDisplay() {
		return playersDisplays[HUMAN];
	}

	public Display getCPUDisplay() {
		return playersDisplays[CPU];
	}

	public Deployment getDeploymentHandler() {
		return deploymentHandler;
	}

	// Shows enemy's grid NOTE: not ships. only grid
	public Display getEnemyDisplayFor(Player player) {
		if (playersDisplays[0].getPlayer() == player) {
			return playersDisplays[CPU];
		} else {
			return playersDisplays[HUMAN];
		}
	}

	public GameWindow(BattleShipGame bs) {
		this.bsGame = bs;
		game = new Game(bs.getName(), "Admiral Baris AKTEMUR");
		deploymentHandler = new Deployment(this);
		attackHandler = new Attack(this);

		constructGUI();
		guiStarter();
		// playInGameMusic();
	}

	/** Music Player Methods */
	protected void playMusicA() {
		Musics.musicA.runPlayer();
	}

	public void playOnShotMusic() {
		Musics.musicShot.runPlayer();
	}

	protected void stopMusicA() {
		Musics.musicA.stopPlayer();
	}

	protected void playMusicB() {
		Musics.musicB.runPlayer();
	}

	protected void stopMusicB() {
		Musics.musicB.stopPlayer();
	}

	protected void playMusicC() {
		Musics.musicC.runPlayer();
	}

	protected void stopMusicC() {
		Musics.musicC.stopPlayer();
	}

	protected void playOnDeploymentMusic() {
		Musics.musicPlayerOnDeployment.runPlayer();
	}

	protected void stopOnDeploymentMusic() {
		Musics.musicPlayerOnDeployment.stopPlayer();
	}

	private void playGameVictoryMusic() {
		Musics.musicPlayerGameVictory.runPlayer();
	}

	private void stopGameVictoryMusic() {
		Musics.musicPlayerGameVictory.stopPlayer();
	}

	private void playGameDefeatedMusic() {
		Musics.musicPlayerGameDefeated.runPlayer();
	}

	private void stopGameDefeatedMusic() {
		Musics.musicPlayerGameDefeated.stopPlayer();
	}

	public Game getGame() {
		return this.game;
	}

	public Player getHumanPlayer() {
		return game.getPlayer1();
	}

	public Player getCPU() {
		return game.getPlayer2();
	}

	// Determines other player
	public Player otherPlayer(Player player) {
		Player oPlayer = game.getPlayer1();
		if (oPlayer != player)
			return oPlayer;
		return game.getPlayer2();
	}

	// Finishes the game and ask for new one.
	public void gameFinished(Player winner) {
		// stopInGameMusic();

		this.isComplete = true;

		if (winner == getHumanPlayer()) {
			playGameVictoryMusic();
			stopMusicA();
			stopMusicB();
			stopMusicC();

			gameFrame.dispose();
			gameFrame = new JFrame("You are VICTORIOUS");
			JPanel winPanel = new JPanel();
			gameFrame.setLayout(null);

			JLabel winLabel = new JLabel(new ImageIcon("src\\victory.jpg"));
			winPanel.add(winLabel);

			gameFrame.add(winPanel);
			winPanel.setBounds(0, 0, 960, 640);
			gameFrame.setSize(960, 640);
			gameFrame.setVisible(true);
			winPanel.revalidate();
			gameFrame.repaint();

			playAgain(winner);
		} else {
			playGameDefeatedMusic();
			stopMusicA();
			stopMusicB();
			stopMusicC();

			gameFrame.dispose();
			gameFrame = new JFrame("You are DEFATED");
			JPanel defPanel = new JPanel();
			gameFrame.setLayout(null);

			JLabel defLabel = new JLabel(new ImageIcon("src\\defeat.jpg"));
			defPanel.add(defLabel);

			gameFrame.add(defPanel);
			defPanel.setBounds(0, -5, 960, 640);
			gameFrame.setSize(960, 640);
			gameFrame.setVisible(true);
			defPanel.revalidate();
			gameFrame.repaint();

			playAgain(winner);
		}
		gameFrame.repaint();

	}

	private void playAgain(Player winner) {

		int result = JOptionPane.showConfirmDialog(gameFrame, winner.getName()
				+ " has won the game!\n" + " Would you like to play again?",
				"Game over!", JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.NO_OPTION) {
			stopGameDefeatedMusic();
			stopGameVictoryMusic();
			System.exit(0);

		} else {
			stopGameVictoryMusic();
			stopGameDefeatedMusic();
			// gameFrame.remove(mainPanel);
			// mainPanel.removeAll();
			gameFrame.dispose(); // erase frame

			new GameWindow(getBsGame());
		}
	}

	public void repaint() {
		for (int i = 0; i < playersDisplays.length; i++) {
			playersDisplays[i].repaint();
		}
		deploymentHandler.drawShadow();
	}

	public void guiStarter() {
		deploymentHandler.guiStarter();
	}

	public void constructGUI() {

		gridPanel = new JPanel();
		humanDisplay = new Display(getHumanPlayer(), this);
		cpuDisplay = new Display(getCPU(), this);

		playersDisplays = new Display[2];
		playersDisplays[HUMAN] = humanDisplay;
		playersDisplays[CPU] = cpuDisplay;

		gridPanel.setLayout(new GridLayout(1, 2));
		gridPanel.add(humanDisplay.getContentPanel());
		gridPanel.add(cpuDisplay.getContentPanel());

		humanDisplay.setListener(deploymentHandler);
		cpuDisplay.setListener(attackHandler);

		buttonPanel = new JPanel();
		autoDeploy = new JButton("Auto Deploy");
		autoTarget = new JButton("Auto Target");
		rotateShip = new JButton("Rotate Ship");

		autoDeploy.setEnabled(false);
		autoTarget.setEnabled(false);
		rotateShip.setEnabled(false);

		deploymentHandler.autoDeployButton = autoDeploy;
		attackHandler.autoTargetButton = autoTarget;
		deploymentHandler.rotateShipButton = rotateShip;

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(rotateShip);
		buttonPanel.add(autoDeploy);
		buttonPanel.add(autoTarget);

		autoDeploy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				deploymentHandler.clickedAutoDeployButton();
			}
		});

		autoTarget.addActionListener(new AutoTargetHandler(attackHandler));

		rotateShip.addActionListener(new RotateShipHandler(deploymentHandler));

		// Menu
		// File Tab
		Menu file = new Menu("File");

		MenuItem newGame = new MenuItem("New Game", new MenuShortcut(
				KeyEvent.VK_N));
		// newGame.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopGameDefeatedMusic();
				stopGameVictoryMusic();
				gameFrame.dispose();
				new GameWindow(getBsGame());
			}
		});

		MenuItem save = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
		// save.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		save.addActionListener(new SaveButtonHandler());

		MenuItem load = new MenuItem("Load", new MenuShortcut(KeyEvent.VK_L));
		// load.setShortcut(new MenuShortcut(KeyEvent.VK_L));
		load.addActionListener(new LoadButtonHandler());

		MenuItem exit = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_Q));
		// exit.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameFrame.dispose();
			}
		});

		file.add(newGame);
		file.addSeparator();
		file.add(save);
		file.add(load);
		file.addSeparator();
		file.add(exit);

		// Music Player Tab
		Menu musicPlayer = new Menu("Music Player");
		Menu on = new Menu("Change Music");
		MenuItem music1 = new MenuItem("Avenger");
		MenuItem music2 = new MenuItem("Transformer");
		MenuItem music3 = new MenuItem("Call a strike");

		music1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				playMusicA();
				stopMusicB();
				stopMusicC();
			}
		});

		music2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				stopMusicA();
				playMusicB();
				stopMusicC();
			}
		});

		music3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				stopMusicA();
				stopMusicB();
				playMusicC();

			}
		});

		on.add(music1);
		on.add(music2);
		on.add(music3);
		// on.add(music4);
		// on.add(music5);

		MenuItem off = new MenuItem("Off");
		off.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Musics.musicPlayerGameDefeated.stopPlayer();
				Musics.musicPlayerGameVictory.stopPlayer();
				Musics.musicPlayerOnDeployment.stopPlayer();

				stopMusicA();
				stopMusicB();
				stopMusicC();
			}
		});

		musicPlayer.add(on);
		musicPlayer.add(off);

		// Help Tab
		Menu help = new Menu("Help");
		MenuItem howToPlay = new MenuItem("How to Play");
		howToPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame howtoFrame = new JFrame("how to play");
				JPanel howtoPanel = new JPanel();
				howtoFrame.setLayout(null);

				JLabel howtoLabel = new JLabel(new ImageIcon("src\\howto.jpg"));
				howtoPanel.add(howtoLabel);

				howtoFrame.add(howtoPanel);
				howtoPanel.setBounds(0, -7, 960, 640);
				howtoFrame.setSize(960, 640);
				howtoFrame.setVisible(true);
				howtoPanel.revalidate();
				howtoFrame.repaint();

			}
		});

		MenuItem about = new MenuItem("About");
		about.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFrame aboutFrame = new JFrame();
				JPanel aboutPanel = new JPanel();
				aboutFrame.setLayout(null);

				aboutPanel.add(new JLabel(new ImageIcon("src\\aboutpart.jpg")));
				aboutFrame.add(aboutPanel);
				aboutPanel.setBounds(0, -7, 960, 640);
				aboutFrame.setSize(960, 640);
				aboutFrame.setVisible(true);

				aboutFrame.repaint();

			}
		});

		help.add(howToPlay);
		help.add(about);

		// Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.add(file);
		menuBar.add(musicPlayer);
		menuBar.add(help);

		// Battle Field
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(gridPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		gameFrame = new JFrame("BattleShip Game - Destroy Enemy Fleet");
		gameFrame.setLayout(new BorderLayout());
		gameFrame.add(mainPanel);
		gameFrame.pack();
		gameFrame.setMenuBar(menuBar);
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
	}

	// Completes deployment phase and starts attack phase
	public void whenDeploymentComplete() {
		new FadeAway(this);
		gameFrame.repaint();

		attackHandler.guiStarter();
	}

}

class FadeAway implements Serializable {
	private GameWindow gw;

	public FadeAway(GameWindow gw) {
		this.gw = gw;
		init();
	}

	private void init() {
		GameWindow.mainPanel.setVisible(false);

		JPanel panel = new JPanel();
		JLabel label = new JLabel();

		panel.setLayout(null);
		label.setBounds(280, 111, 1000, 100);
		panel.add(label);
		GameWindow.gameFrame.add(panel);

		GameWindow.gameFrame.repaint();

		Timer timer = new Timer(190, new LabelHandlerr(label));
		timer.start();

		try {
			Fader fader = new Fader(panel, label, gw);
			fader.start();
		} catch (Exception e) {
			System.out.println(" ");
		}

		GameWindow.gameFrame.repaint();

	}
}

class Fader extends Thread implements Serializable {
	private JPanel panel;
	private JLabel label;
	private GameWindow gw;

	public Fader(JPanel panel, JLabel label, GameWindow gw) {
		this.panel = panel;
		this.label = label;
		this.gw = gw;
	}

	public void run() {
		int c = 0;
		panel.setBackground(new Color(c, c, c));
		gw.playOnDeploymentMusic();

		try {
			Thread.sleep(9000);
		} catch (InterruptedException e1) {

		}

		panel.remove(label);

		while (c < 250) {
			panel.setBackground(new Color(c, c, c));
			c += 10;
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// Ignore the exception
			}
		}
		GameWindow.gameFrame.remove(panel);
		GameWindow.mainPanel.setVisible(true);
		gw.stopOnDeploymentMusic();
	}
}

class LabelHandlerr implements ActionListener, Serializable {
	private static String TEXT = "DEPLOYMENT COMPLETE...";
	private int index = 0;
	private JLabel label;

	public LabelHandlerr(JLabel label) {
		this.label = label;
	}

	public void actionPerformed(ActionEvent e) {

		label.setForeground(Color.GREEN);
		Font font = new Font("Verdana", Font.BOLD, 36);
		label.setFont(font);

		label.setText(TEXT.substring(0, index));
		if (index < TEXT.length()) {
			index++;
		}
	}
}

// TODO burada bir sorun var sonra bakmayi unutma
class AutoTargetHandler implements ActionListener, Serializable {
	Attack attackHandler;

	public AutoTargetHandler(Attack AttackHandler) {
		this.attackHandler = AttackHandler;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		attackHandler.clickedAutoTargetButton();
	}
}

class RotateShipHandler implements ActionListener, Serializable {

	Deployment deploymentHanlder;

	public RotateShipHandler(Deployment deploymentHandler) {
		this.deploymentHanlder = deploymentHandler;
	}

	public void actionPerformed(ActionEvent event) {
		deploymentHanlder.clickedRotateShipButton();
	}
}

class SaveButtonHandler implements ActionListener, Serializable {
	private ObjectOutputStream output;

	public void actionPerformed(ActionEvent e) {
		openFile();
		save();
		closeFile();
		JOptionPane.showMessageDialog(GameWindow.gameFrame, "Game Saved!");
	}

	public void openFile() {
		try {
			output = new ObjectOutputStream(
					new FileOutputStream("src/game.ser"));
		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	public void save() {

		try {
			output.writeObject(GameWindow.mainPanel);
		} catch (IOException e) {
			System.err.println("Couldn't write the object." + e.getMessage());
		}
	}

	public void closeFile() {
		try {
			if (output != null)
				output.close();
		} catch (IOException ioException) {
			System.err.println("Error closing file.");
		}
	}
}

class LoadButtonHandler implements ActionListener, Serializable {
	private static ObjectInputStream input;

	public void actionPerformed(ActionEvent e) {
		openFile();
		load();
		closeFile();
		JOptionPane.showMessageDialog(GameWindow.gameFrame, "Game Loaded!");
	}

	public void openFile() {
		try {
			input = new ObjectInputStream(new FileInputStream("src/game.ser"));
		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	public void load() {
		try {
			JPanel newJPanel = (JPanel) input.readObject();
			GameWindow.gameFrame.remove(GameWindow.mainPanel);
			GameWindow.gameFrame.add(newJPanel);
			GameWindow.mainPanel = newJPanel;

			GameWindow.gameFrame.repaint();

		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void closeFile() {
		try {
			if (input != null)
				input.close();
		} catch (IOException ioException) {
			System.err.println("Error closing file.");
		}
	}

}
