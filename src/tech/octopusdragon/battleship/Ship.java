package tech.octopusdragon.battleship;

public class Ship {
	
	private int curIndex;	// Current location placement index
	
	private String name;
	
	private boolean sunk;
	
	private Location[] location;

	/**
	 * Constructor
	 * @param length The number of tiles the ship will occupy.
	 */
	public Ship(String name, int length) {
		this.name = name;
		location = new Location[length];
		sunk = false;
		curIndex = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public Location[] getLocation() {
		return location;
	}
	
	public boolean isSunk() {
		return sunk;
	}
	
	public boolean isPlaced() {
		return curIndex >= length();
	}
	
	public void setLocation(int row, int column) {
		if (!isPlaced()) {
			location[curIndex] = new Location(row, column);
			curIndex++;
		}
	}
	
	public void sink() {
		sunk = true;
	}
	
	public int length() {
		return location.length;
	}

}
