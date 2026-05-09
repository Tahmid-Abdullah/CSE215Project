package transfermarket.UI;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    
    private SceneManager() {}
    
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    public void showLoginScene() throws IOException {
        FXMLLoader fxmlLoader = createLoader("login-view.fxml");
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle("Transfer Market - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showAdminDashboard() throws IOException {
        FXMLLoader fxmlLoader = createLoader("admin-view.fxml");
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle("Transfer Market - Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showManagerDashboard(String managerName) throws IOException {
        FXMLLoader fxmlLoader = createLoader("manager-view.fxml");
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        ManagerController controller = fxmlLoader.getController();
        controller.setManagerName(managerName);
        primaryStage.setTitle("Transfer Market - Manager Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showOwnerDashboard(String ownerName) throws IOException {
        FXMLLoader fxmlLoader = createLoader("owner-view.fxml");
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        OwnerController controller = fxmlLoader.getController();
        controller.setOwnerName(ownerName);
        primaryStage.setTitle("Transfer Market - Owner Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void backToLogin() throws IOException {
        showLoginScene();
    }

    private FXMLLoader createLoader(String fxmlFile) throws IOException {
        return new FXMLLoader(resolveFxml(fxmlFile));
    }

    private URL resolveFxml(String fxmlFile) throws IOException {
        String resourcePath = "/transfermarket/UI/" + fxmlFile;
        URL resource = getClass().getResource(resourcePath);
        if (resource != null) {
            return resource;
        }

        Path sourcePath = Path.of("transfermarket", "UI", fxmlFile);
        if (Files.exists(sourcePath)) {
            return sourcePath.toUri().toURL();
        }

        throw new FileNotFoundException("Could not find " + resourcePath + " or " + sourcePath.toAbsolutePath());
    }
}


