module se.iths.labb {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.jlereback.knitknot to javafx.fxml;
    exports org.jlereback.knitknot;

    opens org.jlereback.knitknot.shapes to javafx.fxml;
    exports org.jlereback.knitknot.shapes;

    opens org.jlereback.knitknot.tools;
    exports org.jlereback.knitknot.tools;
    exports org.jlereback.knitknot.shapes.shape;
    opens org.jlereback.knitknot.shapes.shape to javafx.fxml;
	exports org.jlereback.knitknot.command;
	opens org.jlereback.knitknot.command;
}