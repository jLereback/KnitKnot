package org.jlereback.knitknot.tools;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jlereback.knitknot.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SVGWriter {
    FileChooser fileChooser = new FileChooser();
    Path filePath;

    private static final SVGWriter svgWriter = new SVGWriter();

    public static SVGWriter getSVGWriter() {
        return svgWriter;
    }

    public void save(Model model, Stage stage) {
        prepareFileChooser(stage);
        writeToSvg(model);
    }

    private void prepareFileChooser(Stage stage) {
        fileChooser.setInitialFileName("myMasterPiece");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG File", "*.svg"));
        try {
            filePath = fileChooser.showSaveDialog(stage.getOwner()).toPath();
        } catch (NullPointerException ignored) {}
    }

    private void writeToSvg(Model model) {
        try {
            Files.write(filePath, getSvgAsStrings(model));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getSvgAsStrings(Model model) {
        List<String> strings = new ArrayList<>();
        addStrings(model, strings);
        return strings;
    }

    private static void addStrings(Model model, List<String> strings) {
        addInitiateString(model, strings);
        addAllShapesAsStrings(model, strings);
        addFinalString(strings);
    }

    private static void addInitiateString(Model model, List<String> strings) {
        strings.add("<svg width=\"" + model.getCanvasWidth() +
                "\" height=\"" + model.getCanvasHeight() +
                "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
    }

    private static void addAllShapesAsStrings(Model model, List<String> strings) {
        model.getCellList().forEach(shape -> strings.add(String.join(" ", shape.toString())));
    }

    private static void addFinalString(List<String> strings) {
        strings.add("</svg>");
    }
}