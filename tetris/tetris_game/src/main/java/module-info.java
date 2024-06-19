module com.tetris_game {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    opens com.tetris to javafx.fxml;
    exports com.tetris;
}
