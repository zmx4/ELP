package org.tick.elp.Controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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

    @FXML
    public void showTest() {
        loadView("/org/tick/elp/test-view.fxml");
    }

    @FXML
    public void showMistake() {
        loadView("/org/tick/elp/mistake-view.fxml");
    }

    public void showDetail(String word, String fromView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/tick/elp/detail-view.fxml"));
            Parent view = loader.load();
            DetailController controller = loader.getController();
            controller.initData(word, fromView);
            swapContentWithFade(view);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            swapContentWithFade(view);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void swapContentWithFade(Node newContent) {
        if (contentArea == null) {
            return;
        }

        contentArea.getChildren().setAll(newContent);

        newContent.setOpacity(0.0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(180), newContent);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}
