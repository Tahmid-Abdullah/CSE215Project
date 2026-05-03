package pkg.java.project.transfermarket.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pkg.java.project.transfermarket.Backend.actions.ManagerLogin;
import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.*;
import java.util.*;
import java.io.IOException;

public class ManagerController {
    @FXML private Label managerTitle;
    @FXML private TextArea outputArea;
    @FXML private VBox contentArea;
    
    private String managerName;
    
    public void setManagerName(String name) {
        this.managerName = name;
        managerTitle.setText("Manager Dashboard - " + name);
    }
    
    @FXML
    protected void onUpdateCredentials() {
        try {
            ArrayList<Manager> managers = Lists.getManagerList();
            Manager targetManager = null;
            
            for (Manager m : managers) {
                if (m.getName().equals(managerName)) {
                    targetManager = m;
                    break;
                }
            }
            
            if (targetManager == null) {
                outputArea.appendText("Manager not found.\n");
                return;
            }

            TextInputDialog dialog = new TextInputDialog(targetManager.getName());
            dialog.setTitle("Update Credentials");
            dialog.setHeaderText("Enter your new manager name:");
            dialog.setContentText("New Name:");
            String newName = dialog.showAndWait().orElse(null);
            if (newName == null || newName.isBlank()) return;
            
            dialog.getEditor().clear();
            dialog.setHeaderText("Enter your new password:");
            dialog.setContentText("New Password:");
            String newPassword = dialog.showAndWait().orElse(null);
            if (newPassword == null || newPassword.isBlank()) return;
            
            targetManager.setName(newName);
            targetManager.setPassword(newPassword);
            FileManager.overWriteObjectFile("manager.txt",Lists.getManagerList());
            // also persist teams since manager names are referenced in teams
            FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            outputArea.appendText("Manager credentials updated successfully!\n");
            this.managerName = newName;
            managerTitle.setText("Manager Dashboard - " + newName);
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onViewMyTeam() {
        try {
            outputArea.clear();
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            boolean found = false;
            
            for (Team t : teams) {
                if (t.getManager() != null && t.getManager().getName().equals(managerName)) {
                    found = true;
                    outputArea.appendText("Team: " + t.getTeamName() + "\n");
                    outputArea.appendText("Manager: " + t.getManager().getName() + "\n");
                    outputArea.appendText("Owner: " + (t.getOwner() != null ? t.getOwner().getName() : "None") + "\n");
                    outputArea.appendText("Budget: $" + t.getBudget() + "\n");
                    outputArea.appendText("Current Size: " + t.getCurrentSize() + "/11\n");
                    outputArea.appendText("\n--- Team Players ---\n");
                    
                    if (t.getCurrentSize() == 0) {
                        outputArea.appendText("No players in team.\n");
                    } else {
                        for (Player p : t.getPlayers()) {
                            outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                                " | Position: " + p.getPosition() + " | Age: " + p.getAge() + "\n");
                        }
                    }
                    break;
                }
            }
            
            if (!found) {
                outputArea.appendText("No team found for this manager.\n");
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onBuyPlayers() {
        try {
            outputArea.clear();
            outputArea.appendText("--- Available Players ---\n");
            
            ArrayList<Player> players = Lists.getPlayerList();
            for (Player p : players) {
                if (p.getIsAvailable()) {
                    outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                        " | Position: " + p.getPosition() + " | Price: $" + p.getPrice() + "\n");
                }
            }
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Buy Player");
            dialog.setHeaderText("Enter player ID to buy:");
            dialog.setContentText("Player ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int playerId = Integer.parseInt(idStr);
            
            ArrayList<Team> teams = Lists.getTeamList();
            Team myTeam = null;
            
            for (Team t : teams) {
                if (t.getManager() != null && t.getManager().getName().equals(managerName)) {
                    myTeam = t;
                    break;
                }
            }
            
            if (myTeam == null) {
                outputArea.appendText("Team not found.\n");
                return;
            }
            
            Player selectedPlayer = null;
            for (Player p : players) {
                if (p.getId() == playerId && p.getIsAvailable()) {
                    selectedPlayer = p;
                    break;
                }
            }
            
            if (selectedPlayer == null) {
                outputArea.appendText("Player not found or already sold.\n");
                return;
            }
            
            if (myTeam.getCurrentSize() >= 11) {
                outputArea.appendText("Team is full (11/11).\n");
                return;
            }
            
            if (myTeam.getBudget() < selectedPlayer.getPrice()) {
                outputArea.appendText("Insufficient budget. Required: $" + selectedPlayer.getPrice() + 
                                    " | Available: $" + myTeam.getBudget() + "\n");
                return;
            }
            
            myTeam.getPlayers().add(selectedPlayer);
            myTeam.setCurrentSize(myTeam.getPlayers().size());
            myTeam.setBudget(myTeam.getBudget() - selectedPlayer.getPrice());
            selectedPlayer.setIsAvailable(false);
            selectedPlayer.setTeamId(myTeam.getId());
            
            FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            FileManager.overWriteObjectFile("players.txt", Lists.getPlayerList());
            
            outputArea.appendText("Player bought successfully!\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onSellPlayer() {
        try {
            outputArea.clear();
            
            ArrayList<Team> teams = Lists.getTeamList();
            Team myTeam = null;
            
            for (Team t : teams) {
                if (t.getManager() != null && t.getManager().getName().equals(managerName)) {
                    myTeam = t;
                    break;
                }
            }
            
            if (myTeam == null) {
                outputArea.appendText("Team not found.\n");
                return;
            }
            
            if (myTeam.getCurrentSize() == 0) {
                outputArea.appendText("No players in team to sell.\n");
                return;
            }
            
            outputArea.appendText("--- Your Players ---\n");
            for (Player p : myTeam.getPlayers()) {
                outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + "\n");
            }
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Sell Player");
            dialog.setHeaderText("Enter player ID to sell:");
            dialog.setContentText("Player ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int playerId = Integer.parseInt(idStr);
            Player selectedPlayer = null;
            
            for (Player p : myTeam.getPlayers()) {
                if (p.getId() == playerId) {
                    selectedPlayer = p;
                    break;
                }
            }
            
            if (selectedPlayer == null) {
                outputArea.appendText("Player not found in team.\n");
                return;
            }
            
            myTeam.getPlayers().remove(selectedPlayer);
            myTeam.setCurrentSize(myTeam.getPlayers().size());
            myTeam.setBudget(myTeam.getBudget() + selectedPlayer.getPrice());
            selectedPlayer.setIsAvailable(true);
            selectedPlayer.setTeamId(0);
            
            FileManager.overWriteObjectFile("players.txt", Lists.getPlayerList());
            FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            
            outputArea.appendText("Player sold successfully! Budget: $" + myTeam.getBudget() + "\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onComparePlayers() {
        try {
            outputArea.clear();
            outputArea.appendText("--- Available Players ---\n");
            
            ArrayList<Player> players = Lists.getPlayerList();
            for (Player p : players) {
                outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                    " | Goals: " + p.getGoal() + " | Matches: " + p.getMatches() + "\n");
            }
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Compare Players");
            dialog.setHeaderText("Enter first player ID:");
            dialog.setContentText("Player ID 1:");
            
            String id1Str = dialog.showAndWait().orElse(null);
            if (id1Str == null) return;
            
            dialog.setContentText("Player ID 2:");
            String id2Str = dialog.showAndWait().orElse(null);
            if (id2Str == null) return;
            
            int id1 = Integer.parseInt(id1Str);
            int id2 = Integer.parseInt(id2Str);
            
            Player p1 = null, p2 = null;
            for (Player p : players) {
                if (p.getId() == id1) p1 = p;
                if (p.getId() == id2) p2 = p;
            }
            
            if (p1 == null || p2 == null) {
                outputArea.appendText("One or both players not found.\n");
                return;
            }
            
            ManagerLogin ml = new ManagerLogin();
            outputArea.appendText(ml.comparePlayer(p1, p2) + "\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onLogout() throws IOException {
        SceneManager.getInstance().backToLogin();
    }
}


