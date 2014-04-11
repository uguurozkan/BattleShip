package decoration_Package;

import java.io.Serializable;

/*** Game class */

public class Game implements Serializable {
	public static final int GRID_SIZE = 10; // The height and width of the grid

	private Player player1, player2, currentPlayer;

	public Game(String player1Name, String player2Name) {
		this.player1 = new Player(player1Name);
		this.player2 = new Player(player2Name);
		currentPlayer = player1;
	}

	public Player getPlayer1() {
		return this.player1;
	}

	public Player getPlayer2() {
		return this.player2;
	}

	public void nextTurn() { // Changes the turn
		if (whoWon() == null) {
			if (whoseTurn() == player1)
				currentPlayer = player2;
			else
				currentPlayer = player1;
		}
	}

	// Checks for winning situation
	public Player whoWon() {
		if (player1.isDefeated())
			return player2; // player2 won the game
		else if (player2.isDefeated())
			return player1; // player1 won the game

		return null; // No winner
	}

	// TODO Check for winning situation!
	// TODO DONE!!!
	public Player whoseTurn() {
		if (whoWon() != null)
			return null;

		return currentPlayer;
	}
}
