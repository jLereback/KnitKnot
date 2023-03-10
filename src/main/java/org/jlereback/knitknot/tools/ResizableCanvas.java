package org.jlereback.knitknot.tools;

import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public double prefWidth(double height) {
        return 550;
    }

    @Override
    public double prefHeight(double width) {
        return 390;
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }


    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    @Override
    public void resize(double width, double height) {
        this.setWidth(width);
        this.setHeight(height);
    }
}
