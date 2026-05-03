package transfermarket.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/transfermarket/UI/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle("Transfer Market - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showAdminDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/transfermarket/UI/admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle("Transfer Market - Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showManagerDashboard(String managerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/transfermarket/UI/manager-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        ManagerController controller = fxmlLoader.getController();
        controller.setManagerName(managerName);
        primaryStage.setTitle("Transfer Market - Manager Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showOwnerDashboard(String ownerName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/transfermarket/UI/owner-view.fxml"));
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
}


