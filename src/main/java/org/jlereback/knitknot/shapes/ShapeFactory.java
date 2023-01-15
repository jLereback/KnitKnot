package org.jlereback.knitknot.shapes;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class ShapeFactory {
	public Shape getShape(ShapeType shapeType, double width, double height) {
		return switch (shapeType) {
			case CIRCLE -> new Ellipse(width/2, height/2);
			case SQUARE -> new Rectangle(width, height);
		};
	}
}