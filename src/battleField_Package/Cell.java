package battleField_Package;

import java.io.Serializable;

import decoration_Package.Game;
import decoration_Package.Player;
import ship_Package.Ship;

/*** Cell class. */

public class Cell implements Serializable {
	// Attack constants
	public static final int ATTACK_ALREADY = 1001;
	public static final int ATTACK_HIT = 1002;
	public static final int ATTACK_MISS = 1003;
	public static final int ATTACK_SUNK = 1004;

	// Display constants
	public static final int DISPLAY_BLANK = 2001;
	public static final int DISPLAY_HIT = 2002;
	public static final int DISPLAY_MISS = 2003;
	public static final int DISPLAY_OCCUPIED = 2004;
	public static final int DISPLAY_SUNK = 2005;

	private int x, y, cellStatus;
	private Player player;
	private Ship occupyingShip;
	private boolean occupied, attacked;

	public Cell(Player owningPlayer, int x, int y) {
		occupied = false;
		attacked = false;
		if (x >= 0 && x < Game.GRID_SIZE)
			this.x = x;
		if (y >= 0 && y < Game.GRID_SIZE)
			this.y = y;
		this.player = owningPlayer;
	}

	public void setCellStatus(int cellStatus) {
		this.cellStatus = cellStatus;
	}

	// TODO There is a bug here!
	// TODO DONE!!!
	public int displayState(Player viewingPlayer) { // For display
		if (getCellStatus() == Cell.ATTACK_SUNK)
			return Cell.DISPLAY_SUNK;

		if (getCellStatus() == Cell.ATTACK_HIT)
			return Cell.DISPLAY_HIT;

		if (getCellStatus() == Cell.ATTACK_MISS)
			return Cell.DISPLAY_MISS;

		if (isOccupied() && (viewingPlayer == getPlayer()))
			return Cell.DISPLAY_OCCUPIED;

		if (getCellStatus() == Cell.ATTACK_ALREADY)
			return 0;

		return Cell.DISPLAY_BLANK;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Ship getOccupyingShip() {
		return this.occupyingShip;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isAttacked() {
		return attacked;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public boolean noAttempt() {
		if (getCellStatus() == ATTACK_ALREADY)
			return false;
		if (getCellStatus() == ATTACK_HIT)
			return false;
		if (getCellStatus() == ATTACK_MISS)
			return false;
		if (getCellStatus() == ATTACK_SUNK)
			return false;

		return true;
	}

	public boolean noAttemptButHit() {
		if (getCellStatus() == ATTACK_ALREADY)
			return false;
		if (getCellStatus() == ATTACK_MISS)
			return false;
		if (getCellStatus() == ATTACK_SUNK)
			return false;

		return true;
	}

	public int getCellStatus() { // hit?, miss?, sunk?
		return cellStatus;
	}

	public boolean isHit() { // Has this cell been hit?
		return isOccupied() && isAttacked();
	}

	public void occupyWith(Ship ship) { // Cell is occupied with ship.
		this.occupyingShip = ship;
		occupied = true;
	}

	// TODO There is a mistake
	// TODO DONE!!!
	public int tryAttack() {

		if (wasAttacked()) {
			cellStatus = Cell.ATTACK_ALREADY;
			return cellStatus;
		}

		attacked = true;

		if (isHit()) {
			getOccupyingShip().hit();
			if (getOccupyingShip().isSunk()) {
				cellStatus = Cell.ATTACK_SUNK;
				return cellStatus;
			} else {
				cellStatus = Cell.ATTACK_HIT;
				return cellStatus;
			}
		}

		cellStatus = Cell.ATTACK_MISS;
		return cellStatus;
	}

	public boolean wasAttacked() {
		return isAttacked();
	}
}
