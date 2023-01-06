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
		context.setFill(color);
		context.fillRect(cell.x() - model.getSize() / 2,
				cell.y() - model.getSize() / 2,
				cell.size(),
				cell.size());
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

	public FilledCell getShapeDuplicate() {
		return null;
	}
}
