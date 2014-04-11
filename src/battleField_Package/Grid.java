package battleField_Package;

import java.io.Serializable;
import java.util.Random;

import decoration_Package.Game;
import decoration_Package.Player;

import ship_Package.Ship;

/*** Grid class */

public class Grid implements Serializable {
	private Player player;
	private Cell[][] cellGrid;
	private Cell[] forShip;
	private int randomTopLeftX, randomTopLeftY, randomOrientation;

	public Grid(Player player) {
		this.player = player;
		initGrid(player);
	}

	private void initGrid(Player player) {
		cellGrid = new Cell[Game.GRID_SIZE][Game.GRID_SIZE];

		for (int i = 0; i < Game.GRID_SIZE; i++) {
			for (int j = 0; j < Game.GRID_SIZE; j++) {
				cellGrid[i][j] = new Cell(player, i, j);
			}
		}
	}

	// TODO Complete this!
	// TODO DONE!!!
	public void autoDeploy(Ship ship) {
		Random rNum = new Random();
		boolean isLocated = false;
		while (!isLocated) {
			// randomGenerator();
			randomTopLeftX = rNum.nextInt(10);
			randomTopLeftY = rNum.nextInt(10);
			randomOrientation = rNum.nextInt(2);
			if (isValidDeployment(ship, randomTopLeftX, randomTopLeftY,
					randomOrientation)) {
				isLocated = true;
				deploy(ship, randomTopLeftX, randomTopLeftY, randomOrientation);

				// Control purpose only
				// System.out.println(player.getName() + "   " + ship.getName()
				// + " x:" + randomTopLeftX + " y:" + randomTopLeftY
				// + " pos:" + randomOrientation);
				// }
				// Control purpose only
				// else {
				// isLocated = false;
				// System.out.println(player.getName() + "   " + ship.getName()
				// + " not  " + " x:" + randomTopLeftX + "  y:"
				// + randomTopLeftY + "  pos:" + randomOrientation);
			}
		}

	}

	// private void randomGenerator() {
	// Random rNum = new Random();
	// randomTopLeftX = rNum.nextInt(10);
	// randomTopLeftY = rNum.nextInt(10);
	// randomOrientation = rNum.nextInt(2) + 1;
	// }

	// TODO Complete here!
	// TODO Already attack ta bir sorun var DONE!!!
	// TODO Sunk ta sorun var. hocaya sor
	public Cell autoTargetMe() {
		boolean shotIsOk = false;
		boolean hitExists = false;
		int x = 0, y = 0;
		Cell cell = getCell(x, y);

		// Checks for hits
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			for (int j = 0; j < Game.GRID_SIZE; j++) {
				if (getCell(i, j).getCellStatus() == Cell.ATTACK_HIT) {
					hitExists = true;
					x = i;
					y = j;
					break;
				}
			}
		}

		// if there was a hit
		if (hitExists) {
			// Look for down
			for (int c = y + 1; (c < Game.GRID_SIZE)
					&& (getCell(x, c).noAttemptButHit()); c++) {
				if (getCell(x, c).noAttempt()) {
					cell = getCell(x, c);
					shotIsOk = true;
					break;
				}
			}

			// Look for up
			for (int c = y - 1; !shotIsOk && (c >= 0)
					&& (getCell(x, c).noAttemptButHit()); c--) {
				if (getCell(x, c).noAttempt()) {
					cell = getCell(x, c);
					shotIsOk = true;
					break;
				}
			}

			// Look for left
			for (int r = x - 1; !shotIsOk && (r >= 0)
					&& (getCell(r, y).noAttemptButHit()); r--) {
				if (getCell(r, y).noAttempt()) {
					cell = getCell(r, y);
					shotIsOk = true;
					break;
				}
			}

			// Look for right
			for (int r = x + 1; !shotIsOk && (r < Game.GRID_SIZE)
					&& (getCell(r, y).noAttemptButHit()); r--) {
				if (getCell(r, y).noAttempt()) {
					cell = getCell(r, y);
					shotIsOk = true;
					break;
				}
			}

		} else {
			while (!shotIsOk) {
				Random rNum = new Random();
				x = rNum.nextInt(10);
				y = rNum.nextInt(10);

				if (getCell(x, y).noAttempt()) {
					cell = getCell(x, y);
					shotIsOk = true;
				}
			}
		}

		return cell;
	}

	public Cell[] coveredCells(Ship ship, int topLeftX, int topLeftY,
			int orientation) {
		forShip = new Cell[ship.getLength()];
		// ship.setPosition(topLeftX, topLeftY, orientation);

		if (topLeftX < Game.GRID_SIZE && topLeftY < Game.GRID_SIZE
				&& topLeftX >= 0 && topLeftY >= 0) {
			if (orientation == Ship.HORIZONTAL) {
				for (int i = 0; i < ship.getLength(); i++) {
					if (topLeftX + i >= Game.GRID_SIZE /* || topLeftX < 0 */) {
						forShip[i] = null;
					} else {
						forShip[i] = getCell(topLeftX + i, topLeftY);
					}
				}
			} else if (orientation == Ship.VERTICAL) {
				for (int j = 0; j < ship.getLength(); j++) {
					if (topLeftY + j >= Game.GRID_SIZE /* || topLeftY < 0 */) {
						forShip[j] = null;
					} else {
						forShip[j] = getCell(topLeftX, topLeftY + j);
					}
				}
			}
		}

		return forShip;
	}

	public boolean deploy(Ship ship, int topLeftX, int topLeftY, int orientation) {
		if (isValidDeployment(ship, topLeftX, topLeftY, orientation)) {
			if (orientation == Ship.HORIZONTAL) {
				for (int i = 0; i < ship.getLength(); i++) {
					cellGrid[i + topLeftX][topLeftY].occupyWith(ship);
				}
			} else if (orientation == Ship.VERTICAL) {
				for (int j = 0; j < ship.getLength(); j++) {
					cellGrid[topLeftX][j + topLeftY].occupyWith(ship);
				}
			}
			return true;
		}
		return false;
	}

	public void sunkShipCellStatus() {
		for (int x = 0; x < Game.GRID_SIZE; x++) {
			for (int y = 0; y < Game.GRID_SIZE; y++) {
				if (getCell(x, y).isHit()
						&& getCell(x, y).getOccupyingShip().isSunk())
					getCell(x, y).setCellStatus(Cell.ATTACK_SUNK);
			}
		}
	}

	// TODO Check for bounds
	// DONE!!
	public Cell getCell(int x, int y) {
		if (x < 0 || y < 0 || x > Game.GRID_SIZE || x > Game.GRID_SIZE)
			return null;

		return cellGrid[x][y];
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean isValidDeployment(Ship ship, int topLeftX, int topLeftY,
			int orientation) {
		if (topLeftX < 0 || topLeftY < 0 || topLeftX > Game.GRID_SIZE
				|| topLeftY > Game.GRID_SIZE)
			return false;

		if (orientation == Ship.HORIZONTAL) {
			if (topLeftX + ship.getLength() > Game.GRID_SIZE) {
				return false;
			}

			for (int i = 0; i < ship.getLength(); i++) {
				if (cellGrid[i + topLeftX][topLeftY].isOccupied() == true)
					return false;
			}

		} else if (orientation == Ship.VERTICAL) {
			if (topLeftY + ship.getLength() > Game.GRID_SIZE) {
				return false;
			}

			for (int j = 0; j < ship.getLength(); j++) {
				if (cellGrid[topLeftX][j + topLeftY].isOccupied() == true)
					return false;
			}
		}

		// Control purpose only
		// else {
		// System.out.println(" There is a problem here!");
		// }

		return true;
	}
}
