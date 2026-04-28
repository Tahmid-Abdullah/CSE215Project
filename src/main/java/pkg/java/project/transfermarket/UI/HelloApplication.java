package pkg.java.project.transfermarket.UI;

import javafx.application.Application;
import javafx.stage.Stage;
import pkg.java.project.transfermarket.File.FileManager;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FileManager.FileInitialization();
        SceneManager.getInstance().setPrimaryStage(stage);
        SceneManager.getInstance().showLoginScene();
    }
}
