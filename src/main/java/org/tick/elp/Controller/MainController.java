package org.tick.elp.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    public static MainController instance;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        instance = this;
        showDictionary();
    }

    @FXML
    public void showDictionary() {
        loadView("/org/tick/elp/hello-view.fxml");
    }

    @FXML
    public void showCollection() {
        loadView("/org/tick/elp/collection-view.fxml");
    }

    public void showDetail(String word, String fromView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/tick/elp/detail-view.fxml"));
            Parent view = loader.load();
            DetailController controller = loader.getController();
            controller.initData(word, fromView);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
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
