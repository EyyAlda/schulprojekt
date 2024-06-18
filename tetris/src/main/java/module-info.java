module com.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.tetris to javafx.fxml;
    exports com.tetris;
}