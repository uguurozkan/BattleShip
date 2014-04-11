package ship_Package;

import java.io.Serializable;

/*** Ship class. */

public abstract class Ship implements Serializable {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private String name;
	private int length;
	private int hitNum;

	// private boolean isHit;

	public Ship(String name, int length) {
		this.name = name;
		this.length = length;
		hitNum = 0;
		// initShip();

	}

	// public void initShip(){
	// boolean[] shipArray = new boolean[getLength()-1];
	// for (int i = 0; i < shipArray.length; i++) {
	// shipArray[i] = false;
	// }
	// }

	public String getName() {
		return this.name;
	}

	public int getLength() {
		return this.length;
	}

	// Counts hits
	public void hit() {
		if (!isSunk()) {
			hitNum++;
		}
	}

	// Checks ship is sunk?
	public boolean isSunk() {
		return hitNum == getLength();
	}
}
