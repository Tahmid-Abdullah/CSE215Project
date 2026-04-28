package pkg.java.project.transfermarket.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.Lists;

import java.io.IOException;

public class OwnerController {
    @FXML private Label ownerTitle;
    @FXML private TextArea outputArea;
    @FXML private VBox contentArea;
    
    private String ownerName;
    
    public void setOwnerName(String name) {
        this.ownerName = name;
        ownerTitle.setText("Owner Dashboard - " + name);
    }
    
    @FXML
    protected void onViewMyTeam() {
        try {
            outputArea.clear();
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            boolean found = false;
            
            for (Team t : teams) {
                if (t.getOwner() != null && t.getOwner().getName().equals(ownerName)) {
                    found = true;
                    outputArea.appendText("Team: " + t.getTeamName() + "\n");
                    outputArea.appendText("Manager: " + (t.getManager() != null ? t.getManager().getName() : "None") + "\n");
                    outputArea.appendText("Owner: " + t.getOwner().getName() + "\n");
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
                outputArea.appendText("No team found for this owner.\n");
            }
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onEditTeam() {
        try {
            outputArea.clear();
            
            java.util.ArrayList<Team> teams = Lists.getTeamList();
            Team targetTeam = null;
            
            for (Team t : teams) {
                if (t.getOwner() != null && t.getOwner().getName().equals(ownerName)) {
                    targetTeam = t;
                    break;
                }
            }
            
            if (targetTeam == null) {
                outputArea.appendText("Team not found.\n");
                return;
            }
            
            showEditMenu(targetTeam);
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void showEditMenu(Team team) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Team");
        alert.setHeaderText("What would you like to do?");
        alert.setContentText("1. Remove Manager\n2. Buy Player\n3. Sell Player");
        
        ButtonType removeManagerBtn = new ButtonType("1. Remove Manager");
        ButtonType buyPlayerBtn = new ButtonType("2. Buy Player");
        ButtonType sellPlayerBtn = new ButtonType("3. Sell Player");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(removeManagerBtn, buyPlayerBtn, sellPlayerBtn, cancelBtn);
        
        alert.showAndWait().ifPresent(result -> {
            if (result == removeManagerBtn) {
                removeManager(team);
            } else if (result == buyPlayerBtn) {
                buyPlayer(team);
            } else if (result == sellPlayerBtn) {
                sellPlayer(team);
            }
        });
    }
    
    private void removeManager(Team team) {
        try {
            if (team.getManager() == null) {
                outputArea.appendText("No manager assigned to this team.\n");
                return;
            }
            
            Manager m = team.getManager();
            team.setManager(null);
            m.setIsAvailable(true);
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("manager.txt", Lists.getManagerList());
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            outputArea.appendText("Manager removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void buyPlayer(Team team) {
        try {
            outputArea.clear();
            outputArea.appendText("--- Available Players ---\n");
            
            java.util.ArrayList<Player> players = Lists.getPlayerList();
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
            
            if (team.getCurrentSize() >= 11) {
                outputArea.appendText("Team is full (11/11).\n");
                return;
            }
            
            if (team.getBudget() < selectedPlayer.getPrice()) {
                outputArea.appendText("Insufficient budget. Required: $" + selectedPlayer.getPrice() + 
                                    " | Available: $" + team.getBudget() + "\n");
                return;
            }
            
            team.getPlayers().add(selectedPlayer);
            team.setCurrentSize(team.getPlayers().size());
            team.setBudget(team.getBudget() - selectedPlayer.getPrice());
            selectedPlayer.setIsAvailable(false);
            selectedPlayer.setTeamId(team.getId());
            
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("players.txt", Lists.getPlayerList());
            
            outputArea.appendText("Player bought successfully!\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void sellPlayer(Team team) {
        try {
            outputArea.clear();
            
            if (team.getCurrentSize() == 0) {
                outputArea.appendText("No players in team to sell.\n");
                return;
            }
            
            outputArea.appendText("--- Your Players ---\n");
            for (Player p : team.getPlayers()) {
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
            
            for (Player p : team.getPlayers()) {
                if (p.getId() == playerId) {
                    selectedPlayer = p;
                    break;
                }
            }
            
            if (selectedPlayer == null) {
                outputArea.appendText("Player not found in team.\n");
                return;
            }
            
            team.getPlayers().remove(selectedPlayer);
            team.setCurrentSize(team.getPlayers().size());
            team.setBudget(team.getBudget() + selectedPlayer.getPrice());
            selectedPlayer.setIsAvailable(true);
            selectedPlayer.setTeamId(0);
            
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("players.txt", Lists.getPlayerList());
            pkg.java.project.transfermarket.File.FileManager.overWriteObjectFile("teams.txt", Lists.getTeamList());
            
            outputArea.appendText("Player sold successfully! Budget: $" + team.getBudget() + "\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onLogout() throws IOException {
        SceneManager.getInstance().backToLogin();
    }
}


