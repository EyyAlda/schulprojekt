module com.tetris.home{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires javafx.base; 
    requires java.desktop;
    opens com.tetris.home to javafx.fxml;
    requires com.tetris.backend;
    requires com.tetris.tetris_game;
    exports com.tetris.home;
    
}
