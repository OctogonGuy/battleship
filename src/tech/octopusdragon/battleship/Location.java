package tech.octopusdragon.battleship;

public class Location {

	private int row;	// The row of the coordinate
	
	private int column;	// The column of the coordinate
	
	/**
	 * Constructor
	 * @param row The row of the coordinate.
	 * @param column The column of the coordinate.
	 */
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}

}
