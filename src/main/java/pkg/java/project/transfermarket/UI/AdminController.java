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
    @FXML private VBox contentArea;
    
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
            // Persist admin credentials
            pkg.java.project.transfermarket.File.FileManager.overwriteFile(pkg.java.project.transfermarket.File.FileManager.ADMIN_FILE, admin.toString());
            System.out.println("admin.txt updated");
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
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Team");
        dialog.setHeaderText("Enter team and owner details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField teamNameField = new TextField();
        ComboBox<String> managerChoice = new ComboBox<>();
        TextField budgetField = new TextField();
        TextField priceField = new TextField();
        TextField ownerNameField = new TextField();
        PasswordField ownerPasswordField = new PasswordField();
        TextField ownerBudgetField = new TextField();

        // Populate managers
        try {
            java.util.ArrayList<Manager> managers = Lists.getManagerList();
            for (Manager m : managers) {
                managerChoice.getItems().add(m.getId() + " - " + m.getName() + (m.getIsAvailable() ? " (Available)" : " (Booked)"));
            }
        } catch (Exception e) {
            outputArea.appendText("Error loading managers: " + e.getMessage() + "\n");
        }

        grid.add(new Label("Team Name:"), 0, 0);
        grid.add(teamNameField, 1, 0);
        grid.add(new Label("Manager (choose ID - name):"), 0, 1);
        grid.add(managerChoice, 1, 1);
        grid.add(new Label("Team Budget:"), 0, 2);
        grid.add(budgetField, 1, 2);
        grid.add(new Label("Team Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Owner Name:"), 0, 4);
        grid.add(ownerNameField, 1, 4);
        grid.add(new Label("Owner Password:"), 0, 5);
        grid.add(ownerPasswordField, 1, 5);
        grid.add(new Label("Owner Budget:"), 0, 6);
        grid.add(ownerBudgetField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String teamName = teamNameField.getText();
                    String managerSelection = managerChoice.getValue();
                    if (teamName == null || teamName.isBlank() || managerSelection == null) {
                        outputArea.appendText("Please fill in team name and select a manager.\n");
                        return null;
                    }
                    int managerId = Integer.parseInt(managerSelection.split(" - ")[0].trim());
                    java.util.ArrayList<Manager> mlist = Lists.getManagerList();
                    Manager selectedManager = null;
                    for (Manager m : mlist) {
                        if (m.getId() == managerId) {
                            selectedManager = m;
                            break;
                        }
                    }
                    if (selectedManager == null) {
                        outputArea.appendText("Manager not found.\n");
                        return null;
                    }
                    if (!selectedManager.getIsAvailable()) {
                        outputArea.appendText("Selected manager is already booked.\n");
                        return null;
                    }
                    double tBudget = Double.parseDouble(budgetField.getText());
                    double tPrice = Double.parseDouble(priceField.getText());
                    String ownerName = ownerNameField.getText();
                    String ownerPass = ownerPasswordField.getText();
                    double ownerBudget = Double.parseDouble(ownerBudgetField.getText());

                    // Create owner and team, update lists and files
                    Owner o = new Owner(ownerName, ownerPass, ownerBudget);
                    Lists.addOwner(o);
                    Team t = new Team(teamName, selectedManager, tBudget, tPrice, o);
                    selectedManager.setIsAvailable(false);
                    Lists.addTeam(t);
                    // Persist changes
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.MANAGER_FILE, Lists.getManagerList());
                    System.out.println("manager.txt updated");
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.TEAM_FILE, Lists.getTeamList());
                    System.out.println("teams.txt updated");
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.OWNER_FILE, Lists.getOwnerList());
                    System.out.println("owner.txt updated");

                    outputArea.appendText("Team added successfully with ID " + t.getId() + ".\n");
                } catch (Exception e) {
                    outputArea.appendText("Error creating team: " + e.getMessage() + "\n");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    
    @FXML
    protected void onRemoveTeam() {
        // First show available teams
        try {
            outputArea.clear();
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            if (teams.isEmpty()) {
                outputArea.appendText("No teams available to remove.\n");
                return;
            }
            outputArea.appendText("--- Teams ---\n");
            for (Team t : teams) {
                outputArea.appendText("ID: " + t.getId() + " | Name: " + t.getTeamName() + 
                        " | Manager: " + (t.getManager() != null ? t.getManager().getName() : "None") + 
                        " | Players: " + t.getCurrentSize() + "/11 | Owner: " + 
                        (t.getOwner() != null ? t.getOwner().getName() : "None") + "\n");
            }
        } catch (Exception e) {
            outputArea.appendText("Error loading teams: " + e.getMessage() + "\n");
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Team");
        dialog.setHeaderText("Enter team ID to remove:");
        dialog.setContentText("Team ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            Team targetTeam = null;
            for (Team t : teams) {
                if (t.getId() == id) {
                    targetTeam = t;
                    break;
                }
            }
            
            if (targetTeam == null) {
                outputArea.appendText("Team not available.\n");
                return;
            }
            
            if (targetTeam.getCurrentSize() > 0) {
                outputArea.appendText("Cannot remove team. Team still has players (" + targetTeam.getCurrentSize() + "/11).\n");
                return;
            }
            
            if (targetTeam.getManager() != null) {
                outputArea.appendText("Cannot remove team. Team still has a manager assigned.\n");
                return;
            }
            
            // Remove team and update files
            teams.remove(targetTeam);
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.TEAM_FILE, Lists.getTeamList());
            System.out.println("teams.txt updated");
            outputArea.appendText("Team removed successfully.\n");
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid team ID format.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddOwner() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Owner");
        dialog.setHeaderText("Enter owner and team details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField teamNameField = new TextField();
        ComboBox<String> managerChoice = new ComboBox<>();
        TextField budgetField = new TextField();
        TextField priceField = new TextField();
        TextField ownerNameField = new TextField();
        PasswordField ownerPasswordField = new PasswordField();
        TextField ownerBudgetField = new TextField();

        // Populate managers
        try {
            java.util.ArrayList<Manager> managers = Lists.getManagerList();
            for (Manager m : managers) {
                managerChoice.getItems().add(m.getId() + " - " + m.getName() + (m.getIsAvailable() ? " (Available)" : " (Booked)"));
            }
        } catch (Exception e) {
            outputArea.appendText("Error loading managers: " + e.getMessage() + "\n");
        }

        grid.add(new Label("Team Name:"), 0, 0);
        grid.add(teamNameField, 1, 0);
        grid.add(new Label("Manager (choose ID - name):"), 0, 1);
        grid.add(managerChoice, 1, 1);
        grid.add(new Label("Team Budget:"), 0, 2);
        grid.add(budgetField, 1, 2);
        grid.add(new Label("Team Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Owner Name:"), 0, 4);
        grid.add(ownerNameField, 1, 4);
        grid.add(new Label("Owner Password:"), 0, 5);
        grid.add(ownerPasswordField, 1, 5);
        grid.add(new Label("Owner Budget:"), 0, 6);
        grid.add(ownerBudgetField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String teamName = teamNameField.getText();
                    String managerSelection = managerChoice.getValue();
                    if (teamName == null || teamName.isBlank() || managerSelection == null) {
                        outputArea.appendText("Please fill in team name and select a manager.\n");
                        return null;
                    }
                    int managerId = Integer.parseInt(managerSelection.split(" - ")[0].trim());
                    java.util.ArrayList<Manager> mlist = Lists.getManagerList();
                    Manager selectedManager = null;
                    for (Manager m : mlist) {
                        if (m.getId() == managerId) {
                            selectedManager = m;
                            break;
                        }
                    }
                    if (selectedManager == null) {
                        outputArea.appendText("Manager not found.\n");
                        return null;
                    }
                    if (!selectedManager.getIsAvailable()) {
                        outputArea.appendText("Selected manager is already booked.\n");
                        return null;
                    }
                    double tBudget = Double.parseDouble(budgetField.getText());
                    double tPrice = Double.parseDouble(priceField.getText());
                    String ownerName = ownerNameField.getText();
                    String ownerPass = ownerPasswordField.getText();
                    double ownerBudget = Double.parseDouble(ownerBudgetField.getText());

                    // Create owner and team, update lists and files
                    Owner o = new Owner(ownerName, ownerPass, ownerBudget);
                    Lists.addOwner(o);
                    Team t = new Team(teamName, selectedManager, tBudget, tPrice, o);
                    selectedManager.setIsAvailable(false);
                    selectedManager.setTeamId(t.getId());
                    Lists.addTeam(t);
                    // Persist changes
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.MANAGER_FILE, Lists.getManagerList());
                    System.out.println("manager.txt updated");
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.TEAM_FILE, Lists.getTeamList());
                    System.out.println("teams.txt updated");
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.OWNER_FILE, Lists.getOwnerList());
                    System.out.println("owner.txt updated");

                    outputArea.appendText("Owner and team added successfully with ID " + o.getId() + " and team ID " + t.getId() + ".\n");
                } catch (Exception e) {
                    outputArea.appendText("Error creating owner and team: " + e.getMessage() + "\n");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    
    @FXML
    protected void onRemoveOwner() {
        // First show owners
        try {
            outputArea.clear();
            java.util.ArrayList<Owner> owners = Lists.getOwnerList();
            if (owners.isEmpty()) {
                outputArea.appendText("No owners available.\n");
                return;
            }
            outputArea.appendText("--- Owners ---\n");
            for (Owner o : owners) {
                outputArea.appendText("ID: " + o.getId() + " | Name: " + o.getName() + " | Budget: $" + o.getBudget() + "\n");
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Owner");
        dialog.setHeaderText("Enter owner ID to remove:");
        dialog.setContentText("Owner ID:");
        
        String idStr = dialog.showAndWait().orElse(null);
        if (idStr == null) return;
        
        try {
            int id = Integer.parseInt(idStr);
            java.util.ArrayList<Owner> owners = Lists.getOwnerList();
            Owner targetOwner = null;
            for (Owner o : owners) {
                if (o.getId() == id) {
                    targetOwner = o;
                    break;
                }
            }
            if (targetOwner == null) {
                outputArea.appendText("Owner not found.\n");
                return;
            }
            
            // Find teams owned by this owner
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            Team ownedTeam = null;
            for (Team t : teams) {
                if (t.getOwner() != null && t.getOwner().getId() == id) {
                    ownedTeam = t;
                    break;
                }
            }
            
            // If owner has a team, check constraints before removal
            if (ownedTeam != null) {
                if (ownedTeam.getCurrentSize() > 0) {
                    outputArea.appendText("Cannot remove owner. Team still has players (" + ownedTeam.getCurrentSize() + "/11).\n");
                    return;
                }
                if (ownedTeam.getManager() != null) {
                    outputArea.appendText("Cannot remove owner. Team still has a manager assigned.\n");
                    return;
                }
                // Set team owner to null instead of removing owner
                ownedTeam.setOwner(null);
                pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.TEAM_FILE, Lists.getTeamList());
                System.out.println("teams.txt updated");
                outputArea.appendText("Team owner set to null.\n");
            }
            
            // Remove owner
            owners.remove(targetOwner);
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.OWNER_FILE, Lists.getOwnerList());
            System.out.println("owner.txt updated");
            outputArea.appendText("Owner removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onViewOwners() {
        try {
            outputArea.clear();
            java.util.ArrayList<Owner> owners = Lists.getOwnerList();
            if (owners.isEmpty()) {
                outputArea.appendText("No owners available.\n");
                return;
            }
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            outputArea.appendText("--- Owners and Their Teams ---\n");
            for (Owner owner : owners) {
                outputArea.appendText("ID: " + owner.getId() + " | Name: " + owner.getName() + " | Budget: $" + owner.getBudget() + "\n");
                boolean hasTeam = false;
                for (Team t : teams) {
                    if (t.getOwner() != null && t.getOwner().getId() == owner.getId()) {
                        outputArea.appendText("   ├─ Team: " + t.getTeamName() + " (ID: " + t.getId() + ") | Manager: " + 
                                (t.getManager() != null ? t.getManager().getName() : "None") + " | Budget: $" + t.getBudget() + 
                                " | Players: " + t.getCurrentSize() + "/11\n");
                        hasTeam = true;
                    }
                }
                if (!hasTeam) {
                    outputArea.appendText("   └─ No team owned\n");
                }
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onAddOwnerWithoutTeam() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Owner");
        dialog.setHeaderText("Enter owner details:");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField ownerNameField = new TextField();
        PasswordField ownerPasswordField = new PasswordField();
        TextField ownerBudgetField = new TextField();
        
        grid.add(new Label("Owner Name:"), 0, 0);
        grid.add(ownerNameField, 1, 0);
        grid.add(new Label("Owner Password:"), 0, 1);
        grid.add(ownerPasswordField, 1, 1);
        grid.add(new Label("Owner Budget:"), 0, 2);
        grid.add(ownerBudgetField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String ownerName = ownerNameField.getText();
                    String ownerPass = ownerPasswordField.getText();
                    double ownerBudget = Double.parseDouble(ownerBudgetField.getText());
                    
                    if (ownerName == null || ownerName.isBlank() || ownerPass == null || ownerPass.isBlank()) {
                        outputArea.appendText("Please fill in all owner details.\n");
                        return null;
                    }
                    
                    Owner o = new Owner(ownerName, ownerPass, ownerBudget);
                    Lists.addOwner(o);
                    pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile(pkg.java.project.transfermarket.File.FileManager.OWNER_FILE, Lists.getOwnerList());
                    System.out.println("owner.txt updated");
                    
                    outputArea.appendText("Owner added successfully with ID " + o.getId() + ".\n");
                } catch (Exception e) {
                    outputArea.appendText("Error creating owner: " + e.getMessage() + "\n");
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    @FXML
    protected void onLogout() throws IOException {
        SceneManager.getInstance().backToLogin();
    }
}
