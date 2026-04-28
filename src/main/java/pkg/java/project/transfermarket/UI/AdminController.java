package pkg.java.project.transfermarket.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.Lists;

import java.io.IOException;

public class AdminController {
    @FXML private TextArea outputArea;
    
    @FXML
    protected void onUpdateCredentials() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Admin Credentials");
        dialog.setHeaderText("Enter new username:");
        dialog.setContentText("Username:");
        
        String username = dialog.showAndWait().orElse(null);
        if (username == null) return;
        
        dialog.setContentText("Password:");
        String password = dialog.showAndWait().orElse(null);
        if (password == null) return;
        
        try {
            Admin admin = Admin.getInstance();
            admin.setName(username);
            admin.setPassword(password);
            outputArea.appendText("Admin credentials updated successfully!\n");
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onViewManagers() {
        try {
            outputArea.clear();
            java.util.ArrayList<Manager> managers = Lists.getManagerList();
            if (managers.isEmpty()) {
                outputArea.appendText("No managers available.\n");
            } else {
                for (Manager m : managers) {
                    outputArea.appendText("ID: " + m.getId() + " | Name: " + m.getName() + 
                                        " | Available: " + (m.getIsAvailable() ? "Yes" : "No") + "\n");
                }
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onViewTeams() {
        try {
            outputArea.clear();
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            if (teams.isEmpty()) {
                outputArea.appendText("No teams available.\n");
            } else {
                for (Team t : teams) {
                    outputArea.appendText("ID: " + t.getId() + " | Name: " + t.getTeamName() + 
                                        " | Manager: " + (t.getManager() != null ? t.getManager().getName() : "None") +
                                        " | Size: " + t.getCurrentSize() + " | Budget: $" + t.getBudget() + "\n");
                }
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onViewPlayers() {
        try {
            outputArea.clear();
            java.util.ArrayList<Player> players = Lists.getPlayerList();
            if (players.isEmpty()) {
                outputArea.appendText("No players available.\n");
            } else {
                for (Player p : players) {
                    outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                        " | Position: " + p.getPosition() + " | Age: " + p.getAge() +
                                        " | Price: $" + p.getPrice() + " | Available: " + 
                                        (p.getIsAvailable() ? "Yes" : "No") + "\n");
                }
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddPlayer() {
        Dialog<Player> dialog = new Dialog<>();
        dialog.setTitle("Add Player");
        dialog.setHeaderText("Enter player details:");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField positionField = new TextField();
        TextField priceField = new TextField();
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    Player p = new Player(nameField.getText(), Integer.parseInt(ageField.getText()),
                                        positionField.getText(), Double.parseDouble(priceField.getText()));
                    Lists.addPlayer(p);
                    outputArea.appendText("Player added successfully with ID " + p.getId() + ".\n");
                } catch (IOException e) {
                    outputArea.appendText("Error adding player: " + e.getMessage() + "\n");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    @FXML
    protected void onRemovePlayer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Player");
        dialog.setHeaderText("Enter player ID to remove:");
        dialog.setContentText("Player ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Lists.removePlayer(id);
            outputArea.appendText("Player removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddManager() {
        Dialog<Manager> dialog = new Dialog<>();
        dialog.setTitle("Add Manager");
        dialog.setHeaderText("Enter manager details:");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField nameField = new TextField();
        PasswordField passwordField = new PasswordField();
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    Manager m = new Manager(nameField.getText(), passwordField.getText());
                    Lists.addManager(m);
                    outputArea.appendText("Manager added successfully with ID " + m.getId() + ".\n");
                } catch (IOException e) {
                    outputArea.appendText("Error adding manager: " + e.getMessage() + "\n");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    @FXML
    protected void onRemoveManager() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Manager");
        dialog.setHeaderText("Enter manager ID to remove:");
        dialog.setContentText("Manager ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Lists.removeManager(id);
            outputArea.appendText("Manager removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddTeam() {
        outputArea.appendText("Team creation requires manager and owner selection. Use console for now.\n");
    }
    
    @FXML
    protected void onRemoveTeam() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Team");
        dialog.setHeaderText("Enter team ID to remove:");
        dialog.setContentText("Team ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Lists.removeTeam(id);
            outputArea.appendText("Team removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddOwner() {
        outputArea.appendText("Owner creation is handled during team creation.\n");
    }
    
    @FXML
    protected void onRemoveOwner() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Owner");
        dialog.setHeaderText("Enter owner ID to remove:");
        dialog.setContentText("Owner ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            Lists.removeOwner(id);
            outputArea.appendText("Owner removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onLogout() throws IOException {
        SceneManager.getInstance().backToLogin();
    }
}


