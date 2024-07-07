package org.jtetris;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class Controller {

    @FXML
    public void handleHyperlinkAction(ActionEvent event) {
        if (event.getSource() instanceof Hyperlink) {
            Hyperlink hyperlink = (Hyperlink) event.getSource();
            openWebpage(hyperlink.getText());
        }
    }

    public void openWebpage(String urlString) {
        try {
            URI uri = new URI(urlString);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            } else {
                throw new UnsupportedOperationException("Desktop is not supported on this platform");
            }
        } catch (URISyntaxException | IOException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}
