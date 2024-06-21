module com.tetris.home{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.tetris.home to javafx.fxml;
    requires com.tetris.backend;
    requires com.tetris.tetris_game;
    exports com.tetris.home;
    
}
