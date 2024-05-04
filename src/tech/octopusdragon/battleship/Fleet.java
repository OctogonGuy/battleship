package tech.octopusdragon.battleship;

public class Fleet {
	
	private Ship carrier;
	private Ship battleship;
	private Ship destroyer;
	private Ship submarine;
	private Ship patrol;
	
	public Fleet() {
		carrier = new Ship("Aircraft Carrier", 5);
		battleship = new Ship("Battleship", 4);
		destroyer = new Ship("Destroyer", 3);
		submarine = new Ship("Submarine", 3);
		patrol = new Ship("Patrol Boat", 2);
	}
	
	public Ship carrier() {
		return carrier;
	}
	
	public Ship battleship() {
		return battleship;
	}
	
	public Ship destroyer() {
		return destroyer;
	}
	
	public Ship submarine() {
		return submarine;
	}
	
	public Ship patrol() {
		return patrol;
	}
	
	public boolean allPlaced() {
		return 	carrier.isPlaced() &&
				battleship.isPlaced() &&
				destroyer.isPlaced() &&
				submarine.isPlaced() &&
				patrol.isPlaced();
	}
	
	public boolean allSunk() {
		return 	carrier.isSunk() &&
				battleship.isSunk() &&
				destroyer.isSunk() &&
				submarine.isSunk() &&
				patrol.isSunk();
	}

}
