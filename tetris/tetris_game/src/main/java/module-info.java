module com.tetris.tetris_game {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    opens com.tetris.tetris_game to javafx.fxml;
    exports com.tetris.tetris_game;
}
