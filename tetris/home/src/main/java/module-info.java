module com.tetris.home {
    requires com.tetris;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.tetris.home to javafx.fxml;
    exports com.tetris.home;
    
}
