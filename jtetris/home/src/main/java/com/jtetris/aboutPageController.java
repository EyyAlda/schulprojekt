package com.jtetris;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class aboutPageController {
    private Startpage startpage;

    public void setMainApp(Startpage startpage) {
        this.startpage = startpage;
    }

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> {
            System.out.println("Clicked");
                if (startpage != null) {
                    // Show the main menu
                    try {
                        startpage.showMainMenu();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
    }
}
