module com.tetris.home{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens com.tetris.home to javafx.fxml;
    exports com.tetris.home;
    
}