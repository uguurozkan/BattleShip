package decoration_Package;

import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JButton;

import battleField_Package.Cell;

/** Attack Class */

public class Attack implements DisplayListener, Serializable {
	public JButton autoTargetButton;
	private GameWindow gWindow;

	public Attack(GameWindow gw) {
		gWindow = gw;
	}

	// Activate Auto Target button
	public void clickedAutoTargetButton() {
		Cell target = gWindow.getCPU().getGrid().autoTargetMe();
		humanTurn(target.getX(), target.getY());
	}

	// Set current turn as computer's turn
	private void computerTurn() {
		Player cpu = gWindow.getCPU();
		Cell target = gWindow.otherPlayer(cpu).getGrid().autoTargetMe();
		isTurnSwitchedSuccessfully(cpu, target.getX(), target.getY());
	}

	// Set current turn as human's turn
	private void humanTurn(int x, int y) {
		Player human = gWindow.getHumanPlayer();
		boolean isSuccessful = isTurnSwitchedSuccessfully(human, x, y);
		if (!isSuccessful)
			System.out.println("it's not your turn!");
	}

	// Advances the game
	private void nextTurn() {
		Player winner = gWindow.getGame().whoWon();
		if (winner != null) {
			gWindow.gameFinished(winner);
		} else {
			gWindow.getGame().nextTurn();
			currentTurn();
		}
	}

	// Checks for successful turn switches
	private boolean isTurnSwitchedSuccessfully(Player attacker, int x, int y) {
		if (gWindow.getGame().whoseTurn() != attacker)
			return false;

		Player defender = gWindow.otherPlayer(attacker);
		int shot = defender.getGrid().getCell(x, y).tryAttack();
		displayTurnEnd(attacker, shot);
		if (shot != Cell.ATTACK_ALREADY)
			nextTurn();

		return true;
	}

	// Starts current turn
	private void currentTurn() {
		Player currentPlayer = gWindow.getGame().whoseTurn();
		if (currentPlayer != gWindow.getHumanPlayer())
			computerTurn();
	}

	private void displayTurnEnd(Player attacker, int shot) {
		Display displayer = gWindow.getEnemyDisplayFor(attacker);
		if (shot == Cell.ATTACK_ALREADY)
			System.out.println("Attacked already");
		else
			displayer.repaint();
	}

	// Construct GUI
	public void guiStarter() {
		autoTargetButton.setEnabled(true);
		gWindow.getHumanDisplay().setEnabled(false);
		gWindow.getCPUDisplay().setEnabled(true);
		gWindow.repaint();
		currentTurn();

	}

	@Override
	public void onGridClicked(Display disp, MouseEvent e, int x, int y) {
		if (gWindow.getDeploymentHandler().isComplete())
			humanTurn(x, y); // TODO iskence bitti sonunda bulduk hatayi.
	}

	@Override
	public void onGridEntered(Display disp, MouseEvent e, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGridExited(Display disp, MouseEvent e, int x, int y) {
		// TODO Auto-generated method stub

	}
}
