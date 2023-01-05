package org.jlereback.knitknot.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jlereback.knitknot.Model;

public class FilledCell {
	private final GridCellCoordinate cell;
	private Color color;

	public FilledCell(GridCellCoordinate cell, Color color) {
		this.cell = cell;
		this.color = color;
	}
	public void draw(GraphicsContext context, Model model) {
		context.setFill(model.getColor());
		context.fillRect(cell.getX() - model.getSize() / 2,
				cell.getY() - model.getSize() / 2,
				cell.getSize(),
				cell.getSize());
	}

	public GridCellCoordinate getCell() {
		return cell;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
