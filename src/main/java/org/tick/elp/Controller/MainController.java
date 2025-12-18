package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showDictionary();
    }

    @FXML
    private void showDictionary() {
        loadView("/org/tick/elp/hello-view.fxml");
    }

    @FXML
    private void showCollection() {
        loadView("/org/tick/elp/collection-view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
