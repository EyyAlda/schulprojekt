module com.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.tetris to javafx.fxml;
    opens com.tetrisGame to javafx.fxml;
    exports com.tetris;
    exports com.backend;
    exports com.tetrisGame;
    exports com.tetris.backend;
}