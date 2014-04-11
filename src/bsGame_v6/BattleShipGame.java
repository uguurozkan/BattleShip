package bsGame_v6;

/* CS102 project 
 * Spring 2012
 * Done by:Cholpon  Abdyzhaparova 
 *         Ugur  Ozkan 
 *         Osman Dogan Yirmebesoglu
 */

import java.io.Serializable;

import decoration_Package.GameWindow;
import decoration_Package.WelcomeScreen;

/*** Main Class */

public class BattleShipGame implements Serializable {
	private String name;

	public BattleShipGame() {
		// new StarterWindow(this);
		new WelcomeScreen(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void startGame() {
		new GameWindow(this);
	}

	public static void main(String[] args) {
		new BattleShipGame();
	}
}
