package tech.octopusdragon.battleship;

public class Board {
	
	private Fleet fleet;		// The player's fleet
	
	private Square[][] squares;	// The grid of squares on the board

	/**
	 * Constructor
	 * @param rows The number of rows on the board.
	 * @param cols The number of columns on the board.
	 */
	public Board(int rows, int cols) {
		
		fleet = new Fleet();
		
		squares = new Square[rows][cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				squares[i][j] = new Square();
			}
		}
	}
	
	public Ship getShip(int row, int col) {
		return squares[row][col].getShip();
	}
	
	/**
	 * Returns whether a tile is occupied by a ship.
	 * @param row The row of the tile.
	 * @param col The column of the tile.
	 * @return Whether the tile is occupied by a ship.
	 */
	public boolean getOccupied(int row, int col) {
		return (squares[row][col].getShip() != null);
	}
	
	/**
	 * Returns whether a tile has been shot at.
	 * @param row The row of the tile.
	 * @param column The column of the tile.
	 * @return Whether the tile has been shot at.
	 */
	public boolean getShot(int row, int column) {
		return squares[row][column].isShot();
	}
	
	/**
	 * Sets the ship variable of a tile.
	 * @param ship The ship to be placed at the tile.
	 * @param row The row of the tile.
	 * @param column The column of the tile.
	 */
	public void setShip(Ship ship, int row, int column) {
		squares[row][column].setShip(ship);
	}
	
	public Fleet fleet() {
		return fleet;
	}
	
	/**
	 * Shoots a tile.
	 * @param row The row of the tile.
	 * @param column The column of the tile.
	 */
	public void shoot(int row, int column) {
		squares[row][column].shoot();
		
		// Sink the ship if all its squares have been shot
		Ship curShip = squares[row][column].getShip();
		if (curShip == null) return;
		boolean sunk = true;
		for (int i = 0; i < curShip.length(); i++) {
			row = curShip.getLocation()[i].getRow();
			column = curShip.getLocation()[i].getColumn();
			if (!squares[row][column].isShot()) {
				sunk = false;
				break;
			}
		}
		if (sunk)
			curShip.sink();
	}

}
