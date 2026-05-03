package transfermarket.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transfermarket.Backend.actions.AdminLogin;
import transfermarket.Backend.actions.ManagerLogin;
import transfermarket.Backend.actions.OwnerLogin;
import transfermarket.File.Lists;

import java.io.IOException;

public class LoginController {
    @FXML private VBox loginPanel;
    @FXML private Label loginTitle;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    
    private String currentLoginType = "admin";
    
    @FXML
    protected void onAdminLoginClick() {
        currentLoginType = "admin";
        loginTitle.setText("Admin Login");
        usernameField.setPromptText("Enter username");
        passwordField.setPromptText("Enter password");
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
    
    @FXML
    protected void onManagerLoginClick() {
        currentLoginType = "manager";
        loginTitle.setText("Manager Login");
        usernameField.setPromptText("Enter name");
        passwordField.setPromptText("Enter password");
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
    
    @FXML
    protected void onOwnerLoginClick() {
        currentLoginType = "owner";
        loginTitle.setText("Owner Login");
        usernameField.setPromptText("Enter name");
        passwordField.setPromptText("Enter password");
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
    
    @FXML
    protected void onLoginClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        try {
            boolean loginSuccess = false;
            
            if ("admin".equals(currentLoginType)) {
                if (AdminLogin.checkAdmin(username, password)) {
                    loginSuccess = true;
                    Lists.syncAllFiles();
                    SceneManager.getInstance().showAdminDashboard();
                }
            } else if ("manager".equals(currentLoginType)) {
                if (ManagerLogin.checkManager(username, password)) {
                    loginSuccess = true;
                    Lists.syncAllFiles();
                    SceneManager.getInstance().showManagerDashboard(username);
                }
            } else if ("owner".equals(currentLoginType)) {
                if (OwnerLogin.checkOwner(username, password)) {
                    loginSuccess = true;
                    Lists.syncAllFiles();
                    SceneManager.getInstance().showOwnerDashboard(username);
                }
            }
            
            if (loginSuccess) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                usernameField.clear();
                passwordField.clear();
            } else {
                messageLabel.setText("Invalid credentials. Please try again.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (IOException e) {
            messageLabel.setText("Error: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    @FXML
    protected void onClearClick() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}


