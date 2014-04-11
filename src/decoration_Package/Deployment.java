package decoration_Package;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.JButton;

import battleField_Package.Cell;
import battleField_Package.Grid;

import ship_Package.Ship;

/*** Deployment Class */

public class Deployment implements DisplayListener, Serializable {
	public JButton autoDeployButton;
	public JButton rotateShipButton;
	private final int INITIAL_POSITION = -1;
	private int x, y, nextShipIndex = 0, direction = Ship.HORIZONTAL;
	private Cell[] cells = new Cell[0];
	private boolean isValidPosition = false;
	private GameWindow gwindow;
	private Player humanPlayer, cpu;

	public Deployment(GameWindow gw) {
		gwindow = gw;
		setPlayer();
		setCPU();
	}

	public void setPlayer() {
		humanPlayer = gwindow.getHumanPlayer();
	}

	public void setCPU() {
		cpu = gwindow.getCPU();
	}

	public Player getCPU() {
		return cpu;
	}

	// Check whether deployment is complete
	public boolean isComplete() {
		return nextShipIndex >= gwindow.getHumanPlayer().getShips().length;
	}

	// Deploy ships automatically
	private void autoDeployCurrentShip() {
		humanPlayer.getGrid().autoDeploy(getCurrentShip());
		++nextShipIndex;
	}

	// Identify current ship
	public Ship getCurrentShip() {
		Ship[] ships = gwindow.getHumanPlayer().getShips();
		if (nextShipIndex >= ships.length)
			return null;
		else
			return ships[nextShipIndex];
	}

	// Runs the autoDeploy Button
	public void clickedAutoDeployButton() {
		while (!isComplete()) {
			autoDeployCurrentShip();
		}
		complete();
	}

	public void clickedRotateShipButton() {
		rotateShip();
	}

	private void complete() {
		autoDeployButton.setEnabled(false);
		rotateShipButton.setEnabled(false);
		gwindow.getHumanDisplay().setEnabled(false);

		nextShipIndex = humanPlayer.getShips().length;
		// setDeploymentPos(INITIAL_POSITION, INITIAL_POSITION);
		resetDeployPos();
		repaint();
		// Waiting here couple of seconds
		gwindow.whenDeploymentComplete();
	}

	private void setDeploymentPos(int i, int j) {
		x = i;
		y = j;
		resetVars();
	}

	// Deploys current ship
	private void deployCurrentShip() {
		humanPlayer.getGrid().deploy(getCurrentShip(), x, y, direction);
		++nextShipIndex;
		gwindow.playOnShotMusic();
		resetVars();
	}

	// Draws shadow under ship in deployment phase
	public void drawShadow() {
		Color color = isValidPosition ? Color.GREEN : Color.RED;
		Display humanDisplay = gwindow.getHumanDisplay();
		for (Cell coveredcell : cells) {
			if (coveredcell == null) {
				continue;
			}
			humanDisplay.paintCell(coveredcell.getX(), coveredcell.getY(),
					color);
		}
	}

	// Restarts variables
	private void resetVars() {
		if (x == INITIAL_POSITION || y == INITIAL_POSITION || isComplete()) {
			cells = new Cell[0];
			isValidPosition = false;
		} else {
			Grid humanGrid = humanPlayer.getGrid();
			cells = humanGrid.coveredCells(getCurrentShip(), x, y, direction);
			isValidPosition = humanGrid.isValidDeployment(getCurrentShip(), x,
					y, direction);
		}
	}

	private void resetDeployPos() {
		setDeploymentPos(INITIAL_POSITION, INITIAL_POSITION);
	}

	// Rotates ship
	private void rotateShip() {
		if (direction == Ship.HORIZONTAL)
			direction = Ship.VERTICAL;
		else if (direction == Ship.VERTICAL)
			direction = Ship.HORIZONTAL;

		resetVars(); // TODO There is bug here. DONE!!!
		repaint();
	}

	// Update information
	public void repaint() {
		gwindow.getHumanDisplay().repaint();
		drawShadow();
	}

	// Deploys computer ships
	private void deployComputerShips() {
		for (Ship ship : getCPU().getShips()) {
			getCPU().getGrid().autoDeploy(ship);
		}

	}

	public void guiStarter() {
		deployComputerShips();
		autoDeployButton.setEnabled(true);
		rotateShipButton.setEnabled(true);
		gwindow.getHumanDisplay().setEnabled(true);
		gwindow.repaint();
	}

	// Starts and advances the deployment
	@Override
	public void onGridClicked(Display disp, MouseEvent e, int x, int y) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			rotateShip();
		} else {
			setDeploymentPos(x, y);
			if (isValidPosition) {
				deployCurrentShip();
				repaint();

				if (isComplete())
					complete();
			} else {
				System.out.println("Not valid position.");
			}
		}
	}

	// Completes deployment
	@Override
	public void onGridEntered(Display disp, MouseEvent e, int x, int y) {
		setDeploymentPos(x, y);
		repaint();
	}

	// Resets everything
	@Override
	public void onGridExited(Display disp, MouseEvent e, int x, int y) {
		resetVars();
		repaint();
	}
}
