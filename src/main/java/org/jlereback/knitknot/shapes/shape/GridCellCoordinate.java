package org.jlereback.knitknot.shapes.shape;

public record GridCellCoordinate(double x, double y, int size) {

	public boolean isInsideCell(double posX, double posY) {
		double distanceToX = Math.abs(posX - x);
		double distanceToY = Math.abs(posY - y);

		return distanceToX <= size >> 1 && distanceToY <= size >> 1;
	}
}
