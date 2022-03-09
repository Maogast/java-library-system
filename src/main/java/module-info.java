/**
 * Configuration module
 */
module a1.assignment1fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.logging;
    //requires org.testng;
    //requires org.junit.jupiter.api;

    opens a1.assignment1fx to javafx.fxml;
    exports a1.assignment1fx;
    exports a1.assignment1fx.frontend;
    opens a1.assignment1fx.frontend to javafx.fxml;
    exports a1.assignment1fx.backend;
    opens a1.assignment1fx.backend to javafx.fxml;
}