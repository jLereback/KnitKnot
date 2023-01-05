package org.jlereback.knitknot.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jlereback.knitknot.Model;

import java.util.Objects;

public final class GridCellCoordinate {
	private final double x;
	private final double y;
	private final int size;


	public GridCellCoordinate(double x, double y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public boolean isInsideCell(double posX, double posY) {
		double distanceToX = Math.abs(posX - x);
		double distanceToY = Math.abs(posY - y);

		return distanceToX <= size >> 1 && distanceToY <= size >> 1;
	}

	public void draw(GraphicsContext context, Model model) {
		context.setFill(model.getColor());
		context.fillRect(x - (size >> 1),
				y - (size >> 1),
				size,
				size);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (GridCellCoordinate) obj;
		return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x) &&
			   Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y) &&
			   this.size == that.size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, size);
	}

	@Override
	public String toString() {
		return "GridCellCoordinate[" +
			   "x=" + x + ", " +
			   "y=" + y + ", " +
			   "size=" + size + ']';
	}


}
