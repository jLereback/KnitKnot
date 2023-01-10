package org.jlereback.knitknot.shapes.shape;

import javafx.scene.canvas.GraphicsContext;
import org.jlereback.knitknot.shapes.ShapeParameter;
import org.jlereback.knitknot.shapes.ShapeType;

import java.util.Locale;

import static org.jlereback.knitknot.shapes.ShapeType.*;

public final class Square extends Shape {
    double halfSideSize = getSize()/2;

    String colorAsString = getColor().toString().substring(2, 10);
    public Square(ShapeParameter parameter) {
        super(parameter);
    }

    @Override
    public Shape getShapeDuplicate() {
        return new Square(getDuplicate());
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.fillRect(getX(), getY(), getSize(), getSize());
    }

    @Override
    public Boolean isInside(double posX, double posY) {
        double distanceToX = Math.abs(posX - getX());
        double distanceToY = Math.abs(posY - getY());

        return distanceToX <= halfSideSize && distanceToY <= halfSideSize;
    }
    @Override
    public ShapeType getType() {
        return SQUARE;
    }

    @Override
    public String toString() {
        return "<rect x=\"" + String.format(Locale.UK,"%.2f",(getX())) + "\" " +
                "y=\"" + String.format(Locale.UK, "%.2f", (getY())) + "\" " +
                "width=\"" + getSize() + "\" " +
                "height=\"" + getSize() + "\" " +
                "fill=\"#" + colorAsString + "\" />";
    }
}