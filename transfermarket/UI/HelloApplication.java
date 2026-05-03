package transfermarket.UI;

import javafx.application.Application;
import javafx.stage.Stage;
import transfermarket.File.FileManager;
import transfermarket.File.Lists;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FileManager.FileInitialization();
        // Load in-memory lists from files so UI controllers can read data immediately
        Lists.syncAllFiles();
        SceneManager.getInstance().setPrimaryStage(stage);
        SceneManager.getInstance().showLoginScene();
    }
}
