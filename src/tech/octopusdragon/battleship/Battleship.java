package tech.octopusdragon.battleship;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.*;

public class Battleship extends Application {

	final static int ROWS = 9;	// Number of rows
	final static int COLS = 9;	// Number of columns
	final static double LENGTH = 50.0;	// Length and width of tiles
	final static double SPACING = 30.0;	// Spacing
	final static double PADDING = 35.0;	// Padding
	
	static Label titleLabel;	// Shows the current target
	static Label fleetLabel;	// Says fleet
	static ImageView carrierImage;	// Aircraft carrier image
	static ImageView battleshipImage;	// Battleship image
	static ImageView destroyerImage;	// Destroyer image
	static ImageView submarineImage;	// Submarine image
	static ImageView patrolImage;	// Patrol boat image
	static ImageView curShipImage;	// Selected ship image
	static Tile[][] grid;		// The grid of tiles
	static Label messageLabel;	// The message label
	static Button nextButton;	// Starts the next player's turn
	
	static Board player1;	// Player 1's board
	static Board player2;	// Player 2's board
	static Board curPlayer;	// The current player's opponent's board
	
	static Ship curShip;	// Number of tiles for the current ship
	static Orientation curDir;	// Current direction for ship orientation
	static int curRow;		// The current row of the tile the mouse is over
	static int curCol;		// The current column of the tile the mouse is over
	static boolean badLoc;	// Flag indicating the ship blueprint is in an invalid position
	static boolean setup = false;	// Flag for if both players have set up their boards
	static boolean went = false;	// Flag for if the current turn is over
	static boolean won = false;	// Flag for whether a player has won
	
	@Override
	public void start(Stage primaryStage) {
		
		// Create a title label for showing the current target.
		titleLabel = new Label();
		titleLabel.setFont(new Font("Stencil", 40));
		
		// Create a title for the fleet.
		fleetLabel = new Label("Fleet");
		fleetLabel.setFont(new Font("Stencil", 30));
		
		// Create images for each of the ships
		carrierImage = new ImageView(new Image("carrier.png"));
		carrierImage.setOnMouseClicked(new ShipClickHandler());
		battleshipImage = new ImageView(new Image("battleship.png"));
		battleshipImage.setOnMouseClicked(new ShipClickHandler());
		destroyerImage = new ImageView(new Image("destroyer.png"));
		destroyerImage.setOnMouseClicked(new ShipClickHandler());
		submarineImage = new ImageView(new Image("submarine.png"));
		submarineImage.setOnMouseClicked(new ShipClickHandler());
		patrolImage = new ImageView(new Image("patrol.png"));
		patrolImage.setOnMouseClicked(new ShipClickHandler());
		
		// Put the ship images in a VBox.
		VBox shipBox = new VBox(fleetLabel,
								carrierImage,
								battleshipImage,
								destroyerImage,
								submarineImage,
								patrolImage);
		shipBox.setAlignment(Pos.CENTER_LEFT);
		shipBox.setSpacing(SPACING);
		
		// Create a GridPane filled and fill it with the tiles.
		grid = new Tile[ROWS][COLS];
		GridPane gridPane = new GridPane();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new Tile(LENGTH);
				grid[i][j].setOnMouseClicked(new TileClickHandler());
				grid[i][j].setOnMouseEntered(new TileHoverHandler());
				gridPane.add(grid[i][j], j, i);
			}
		}
		gridPane.setGridLinesVisible(true);
		
		// Put the ship image and grid in an HBox.
		HBox boardBox = new HBox(	shipBox,
									gridPane);
		boardBox.setSpacing(SPACING);
		
		// Create a label for displaying messages.
		messageLabel = new Label();
		messageLabel.setFont(new Font("Stencil", 20));
		
		// Create a button to start the next player's turn.
		nextButton = new Button();
		nextButton.setFont(new Font("Stencil", 30));
		nextButton.setOnAction(new NextButtonHandler());
		
		// Put the nodes in a VBox.
		VBox vbox = new VBox(	titleLabel,
								boardBox,
								messageLabel,
								nextButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(SPACING);
		vbox.setPadding(new Insets(PADDING));
		
		// Set the Scene.
		Scene scene = new Scene(vbox);
		scene.setOnScroll(new ScrollHandler());
		scene.setOnKeyPressed(new ArrowHandler());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Battleship");
		primaryStage.show();
		
		// Start the game
		newGame();
	}
	
	/**
	 * Starts a new game.
	 */
	public void newGame() {
		
		// Reset the players' boards.
		player1 = new Board(ROWS, COLS);
		player2 = new Board(ROWS, COLS);
		
		// The boards have not been set up; No one has won; Start of turn
		setup = false;
		won = false;
		went = false;
		
		// Reset next button.
		nextButton.setText("Next");
		nextButton.setDisable(true);
		
		// Player 1's turn
		curPlayer = player1;
		prepare();
	}
	
	/**
	 * Allows a player to place his or her pieces.
	 */
	public void prepare() {
		
		// Show appropriate text
		if (curPlayer == player1) {
			titleLabel.setText("Player 1's Board");
			messageLabel.setText("P1, place your ships.");
		} else if (curPlayer == player2) {
			titleLabel.setText("Player 2's Board");
			messageLabel.setText("P2, place your ships.");
		}
		
		// Show images for each of the ships
		carrierImage.setImage(new Image("carrier.png"));
		battleshipImage.setImage(new Image("battleship.png"));
		destroyerImage.setImage(new Image("destroyer.png"));
		submarineImage.setImage(new Image("submarine.png"));
		patrolImage.setImage(new Image("patrol.png"));
		
		// Show the board.
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j].setText("", null);
				grid[i][j].setColor(null);
			}
		}
		
		// Player has finished setting up yet.
		went = false;
		nextButton.setDisable(true);
	}
	
	/**
	 * Starts the next player's turn.
	 */
	public void nextTurn() {
		
		// Set the board to the other player.
		if (curPlayer == player2) {
			curPlayer = player1;
			titleLabel.setText("Player 1's Board");
			messageLabel.setText("Player 2's turn.");
		} else if (curPlayer == player1) {
			curPlayer = player2;
			titleLabel.setText("Player 2's Board");
			messageLabel.setText("Player 1's turn.");
		}
		
		// Show the remaining ships.
		if (curPlayer.fleet().carrier().isSunk())
			carrierImage.setImage(new Image("carrier_empty.png"));
		else
			carrierImage.setImage(new Image("carrier.png"));
		
		if (curPlayer.fleet().battleship().isSunk())
			battleshipImage.setImage(new Image("battleship_empty.png"));
		else
			battleshipImage.setImage(new Image("battleship.png"));
		
		if (curPlayer.fleet().destroyer().isSunk())
			destroyerImage.setImage(new Image("destroyer_empty.png"));
		else
			destroyerImage.setImage(new Image("destroyer.png"));

		if (curPlayer.fleet().submarine().isSunk())
			submarineImage.setImage(new Image("submarine_empty.png"));
		else
			submarineImage.setImage(new Image("submarine.png"));

		if (curPlayer.fleet().patrol().isSunk())
			patrolImage.setImage(new Image("patrol_empty.png"));
		else
			patrolImage.setImage(new Image("patrol.png"));
		
		// Show the board.
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (curPlayer.getShot(i, j) == true && curPlayer.getOccupied(i, j) == true)
					grid[i][j].setText("O", Color.RED);
				else if (curPlayer.getShot(i, j) == true && curPlayer.getOccupied(i, j) == false)
					grid[i][j].setText("X", Color.GRAY);
				else
					grid[i][j].setText("", null);
			}
		}
		
		// Player has not gone yet.
		went = false;
		nextButton.setDisable(true);
	}
	
	public void place(MouseEvent event) {
		
		// Do NOT place the ship if it is in an invalid position
		if (badLoc)
			return;
		
		// Replace the image of the ship with the empty version and mark
		// that ship as placed
		if (curShipImage == carrierImage)
			carrierImage.setImage(new Image("carrier_empty.png"));
		
		else if (curShipImage == battleshipImage)
			battleshipImage.setImage(new Image("battleship_empty.png"));
			
		else if (curShipImage == destroyerImage)
			destroyerImage.setImage(new Image("destroyer_empty.png"));
			
		else if (curShipImage == submarineImage)
			submarineImage.setImage(new Image("submarine_empty.png"));
			
		else if (curShipImage == patrolImage)
			patrolImage.setImage(new Image("patrol_empty.png"));
		
		
		// Place the current ship at the current location.
		if (curDir == Orientation.E) {
			for (int i = 0; i < curShip.length(); i++) {
				curPlayer.setShip(curShip, curRow, curCol+i);
				curShip.setLocation(curRow, curCol+i);
				grid[curRow][curCol+i].setColor(Color.BLACK);
			}
		}
		else if (curDir == Orientation.S) {
			for (int i = 0; i < curShip.length(); i++) {
				curPlayer.setShip(curShip, curRow+i, curCol);
				curShip.setLocation(curRow+i, curCol);
				grid[curRow+i][curCol].setColor(Color.BLACK);
			}
		}
		else if (curDir == Orientation.W) {
			for (int i = 0; i < curShip.length(); i++) {
				curPlayer.setShip(curShip, curRow, curCol-i);
				curShip.setLocation(curRow, curCol-i);
				grid[curRow][curCol-i].setColor(Color.BLACK);
			}
		}
		else if (curDir == Orientation.N) {
			for (int i = 0; i < curShip.length(); i++) {
				curPlayer.setShip(curShip, curRow-i, curCol);
				curShip.setLocation(curRow-i, curCol);
				grid[curRow-i][curCol].setColor(Color.BLACK);
			}
		}
		
		// A ship is no longer selected
		curShip = null;
		
		// If all ships have been placed, allow next button to be pressed.
		if (curPlayer.fleet().allPlaced())
			nextButton.setDisable(false);
	}
	
	public void shoot(MouseEvent event) {
		
		// Do nothing if a player has already won the game, the turn is over,
		// or if the tile is already revealed.
		if (won || went|| curPlayer.getShot(curRow, curCol) == true)
			return;
		
		// Record that the corresponding tile has been shot.
		curPlayer.shoot(curRow, curCol);
		
		// Change the symbol of the title indicating a hit or miss.
		if (curPlayer.getOccupied(curRow, curCol) == true) {
			grid[curRow][curCol].setText("O", Color.RED);
			if (curPlayer.getShip(curRow, curCol).isSunk()) {
				messageLabel.setText("You sunk the " + curPlayer.getShip(curRow, curCol).getName() + "!");
				if (curPlayer.fleet().carrier().isSunk())
					carrierImage.setImage(new Image("carrier_empty.png"));
				if (curPlayer.fleet().battleship().isSunk())
					battleshipImage.setImage(new Image("battleship_empty.png"));
				if (curPlayer.fleet().destroyer().isSunk())
					destroyerImage.setImage(new Image("destroyer_empty.png"));
				if (curPlayer.fleet().submarine().isSunk())
					submarineImage.setImage(new Image("submarine_empty.png"));
				if (curPlayer.fleet().patrol().isSunk())
					patrolImage.setImage(new Image("patrol_empty.png"));
			}
			else
				messageLabel.setText("Hit!");
		} else {
			grid[curRow][curCol].setText("X", Color.GRAY);
			messageLabel.setText("Miss...");
		}
		grid[curRow][curCol].setColor(null);
		
		// Check to see if the player has won.
		boolean remainingShip = false;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (curPlayer.getOccupied(i, j) == true && curPlayer.getShot(i, j) == false) {
					remainingShip = true;
					break;
				}
			}
		}
		if (!remainingShip)
			won = true;
		
		// The turn is over
		went = true;
		nextButton.setDisable(false);
		
		// End the game if the player has won.
		if (won) {
			if (curPlayer == player2)
				messageLabel.setText("P1 won!!!");
			else if (curPlayer == player1)
				messageLabel.setText("P2 won!!!");
			nextButton.setText("New Game");
		}
	}
	
	public void highlightToPlace() {
		// Highlight the blueprint for the ship
		if (curShip != null) {
	
			// Reset the board color.
			for (int i = 0; i < ROWS; i++)
				for (int j = 0; j < COLS; j++)
					if (curPlayer.getOccupied(i, j) == true)
						grid[i][j].setColor(Color.BLACK);
					else
						grid[i][j].setColor(null);
			
			// Highlight the potential placement in blue.
			try {
				if (curDir == Orientation.E) {
					for (int i = 0; i < curShip.length(); i++) {
						if (grid[curRow][curCol+i].getColor() == Color.BLACK)
							throw new ArrayIndexOutOfBoundsException();
						grid[curRow][curCol+i].setColor(Color.BLUE);
					}
				}
				else if (curDir == Orientation.S) {
					for (int i = 0; i < curShip.length(); i++) {
						if (grid[curRow+i][curCol].getColor() == Color.BLACK)
							throw new ArrayIndexOutOfBoundsException();
						grid[curRow+i][curCol].setColor(Color.BLUE);
					}
				}
				else if (curDir == Orientation.W) {
					for (int i = 0; i < curShip.length(); i++) {
						if (grid[curRow][curCol-i].getColor() == Color.BLACK)
							throw new ArrayIndexOutOfBoundsException();
						grid[curRow][curCol-i].setColor(Color.BLUE);
					}
				}
				else if (curDir == Orientation.N) {
					for (int i = 0; i < curShip.length(); i++) {
						if (grid[curRow-i][curCol].getColor() == Color.BLACK)
							throw new ArrayIndexOutOfBoundsException();
						grid[curRow-i][curCol].setColor(Color.BLUE);
					}
				}
				
				badLoc = false;
			}
			
			// If the ship does not fit, change the color to red.
			catch (ArrayIndexOutOfBoundsException e) {
				if (curDir == Orientation.E) {
					for (int i = 0; i < curShip.length(); i++) {
						if (curCol+i >= COLS) break;
						grid[curRow][curCol+i].setColor(Color.RED);
					}
				}
				else if (curDir == Orientation.S) {
					for (int i = 0; i < curShip.length(); i++) {
						if (curRow+i >= ROWS) break;
						grid[curRow+i][curCol].setColor(Color.RED);
					}
				}
				else if (curDir == Orientation.W) {
					for (int i = 0; i < curShip.length(); i++) {
						if (curCol-i < 0) break;
						grid[curRow][curCol-i].setColor(Color.RED);
					}
				}
				else if (curDir == Orientation.N) {
					for (int i = 0; i < curShip.length(); i++) {
						if (curRow-i < 0) break;
						grid[curRow-i][curCol].setColor(Color.RED);
					}
				}
				
				badLoc = true;
			}
		}
	}
	
	public void highlightToShoot() {
		// Reset the board color.
		for (int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				grid[i][j].setColor(null);
		
		// Change color of selected tile to yellow.
		grid[curRow][curCol].setColor(Color.YELLOW);
	}
	
	public class ShipClickHandler implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			
			// Get the clicked ship
			curShipImage = (ImageView)event.getSource();
			
			// Set the current ship to the clicked ship.
			if (curShipImage == carrierImage && !curPlayer.fleet().carrier().isPlaced())
				curShip = curPlayer.fleet().carrier();
			else if (curShipImage == battleshipImage && !curPlayer.fleet().battleship().isPlaced())
				curShip = curPlayer.fleet().battleship();
			else if (curShipImage == destroyerImage && !curPlayer.fleet().destroyer().isPlaced())
				curShip = curPlayer.fleet().destroyer();
			else if (curShipImage == submarineImage && !curPlayer.fleet().submarine().isPlaced())
				curShip = curPlayer.fleet().submarine();
			else if (curShipImage == patrolImage && !curPlayer.fleet().patrol().isPlaced())
				curShip = curPlayer.fleet().patrol();
			
			// Default orientation is E
			curDir = Orientation.E;
		}
	}
	
	public class ScrollHandler implements EventHandler<ScrollEvent> {
		
		@Override
		public void handle(ScrollEvent event) {
			
			// Do nothing if no ship is selected
			if (curShip == null)
				return;
			
			// Change orientation clockwise if scroll down.
			if (event.getDeltaY() < 0) {		// Y < 0 means down
				if (curDir == Orientation.E)
					curDir = Orientation.S;
				else if (curDir == Orientation.S)
					curDir = Orientation.W;
				else if (curDir == Orientation.W)
					curDir = Orientation.N;
				else if (curDir == Orientation.N)
					curDir = Orientation.E;
			}
			
			// Change orientation counterclockwise if scroll up.
			else if (event.getDeltaY() > 0) {	// Y > 0 means up
				if (curDir == Orientation.E)
					curDir = Orientation.N;
				else if (curDir == Orientation.N)
					curDir = Orientation.W;
				else if (curDir == Orientation.W)
					curDir = Orientation.S;
				else if (curDir == Orientation.S)
					curDir = Orientation.E;
			}
			
			// Highlight the blueprint for the ship if one is selected
			if (curShip != null)
				highlightToPlace();
		}
	}
	
	public class ArrowHandler implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent event) {
			
			// Do nothing if no ship is selected
			if (curShip == null)
				return;
			
			// Change orientation clockwise if right.
			if (event.getCode() == KeyCode.RIGHT) {
				if (curDir == Orientation.E)
					curDir = Orientation.S;
				else if (curDir == Orientation.S)
					curDir = Orientation.W;
				else if (curDir == Orientation.W)
					curDir = Orientation.N;
				else if (curDir == Orientation.N)
					curDir = Orientation.E;
			}
			
			// Change orientation counterclockwise if left.
			else if (event.getCode() == KeyCode.LEFT) {
				if (curDir == Orientation.E)
					curDir = Orientation.N;
				else if (curDir == Orientation.N)
					curDir = Orientation.W;
				else if (curDir == Orientation.W)
					curDir = Orientation.S;
				else if (curDir == Orientation.S)
					curDir = Orientation.E;
			}
			
			// Highlight the blueprint for the ship if one is selected
			if (curShip != null)
				highlightToPlace();
		}
	}
	
	public class TileClickHandler implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			
			// Place a ship if in the preparation phase.
			if (curShip != null)
				place(event);
			
			// Otherwise, shoot.
			else if (setup)
				shoot(event);
		}
	}
	
	public class TileHoverHandler implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			
			// Set the row and column of the clicked tile.
			curRow = GridPane.getRowIndex((Tile)event.getSource());
			curCol = GridPane.getColumnIndex((Tile)event.getSource());

			// Highlight the blueprint for the ship
			if (curShip != null) {
				highlightToPlace();
			}
			else if (setup && !went)
				highlightToShoot();
		}
	}
	
	public class NextButtonHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			
			// Do nothing if the turn is not over.
			if (!setup) {
				if (curPlayer == player1) {
					curPlayer = player2;
					prepare();
				} else {
					setup = true;
					curPlayer = player1;
					// Reset the board color.
					for (int i = 0; i < ROWS; i++)
						for (int j = 0; j < COLS; j++)
							grid[i][j].setColor(null);
					nextTurn();
				}
			
			// If the game is over, start a new game.
			} else if (won) {
				newGame();
			}
			
			// Move on to the next player.
			else {
				nextTurn();
			}
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
	}

}
