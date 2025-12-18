package org.tick.elp.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DictionaryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication.class.getResource("/org/tick/elp/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 360);
        stage.setTitle("ELP Dictionary");
        stage.setScene(scene);
        stage.show();
    }
}
