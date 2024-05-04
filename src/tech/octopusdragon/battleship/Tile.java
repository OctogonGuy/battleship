package tech.octopusdragon.battleship;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class Tile extends StackPane {
	
	private Rectangle square;	// The square shape on the bottom
	private Text text;			// The text on top
	
	/**
	 * Constructor
	 * @param length The length and width of the tile.
	 */
	public Tile(double length) {
		
		square = new Rectangle(length, length, Color.ANTIQUEWHITE);
		text = new Text();
		text.setFont(new Font("Rockwell Bold", 35));
		this.getChildren().add(square);
		this.getChildren().add(text);
	}
	
	/**
	 * Returns the tile's color
	 * @return The tile's color.
	 */
	public Color getColor() {
		return (Color)square.getFill();
	}
	
	/**
	 * Sets the text and text color.
	 * @param text The text.
	 * @param color The text color.
	 */
	public void setText(String text, Color color) {
		this.text.setText(text);
		this.text.setFill(color);
	}
	
	/**
	 * Changes the tile's color
	 * @param color The tile's new color. null for default.
	 */
	public void setColor(Color color) {
		if (color != null)
			square.setFill(color);
		else
			square.setFill(Color.ANTIQUEWHITE);
	}

}
