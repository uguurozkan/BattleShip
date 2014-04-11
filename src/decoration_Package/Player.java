package decoration_Package;

import java.io.Serializable;

import battleField_Package.Grid;
import ship_Package.AircraftCarrier;
import ship_Package.Battleship;
import ship_Package.Destroyer;
import ship_Package.PatrolBoat;
import ship_Package.Ship;
import ship_Package.Submarine;

/*** Player class */

public class Player implements Serializable {
	private String name;
	private Grid grid;
	private Ship[] ships;

	// public boolean isDefeated;

	public Player(String name) {
		this.name = name;
		initShips();
		grid = new Grid(this);
	}

	private void initShips() { // Order: AC, BS, SM, D, PB
		ships = new Ship[5];
		ships[0] = new AircraftCarrier();
		ships[1] = new Battleship();
		ships[2] = new Submarine();
		ships[3] = new Destroyer();
		ships[4] = new PatrolBoat();
	}

	public Grid getGrid() {
		return this.grid;
	}

	public String getName() {
		return this.name;
	}

	public Ship[] getShips() {
		return ships;
	}

	// Checks for being defeat
	public boolean isDefeated() {
		int sunkShipCounter = 0;
		for (int i = 0; i < ships.length; i++) {
			if (ships[i].isSunk())
				sunkShipCounter++;
		}
		return sunkShipCounter == ships.length;
	}
}
