package transfermarket.UI;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import transfermarket.Backend.actions.ManagerLogin;
import transfermarket.Backend.entities.Manager;
import transfermarket.Backend.entities.Player;
import transfermarket.Backend.entities.Team;
import transfermarket.File.FileManager;
import transfermarket.File.Lists;

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
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Credentials");
            dialog.setHeaderText("First, enter your Manager ID:");
            dialog.setContentText("Manager ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int id = Integer.parseInt(idStr);
            Manager targetManager = findManagerById(id);
            
            if (targetManager == null) {
                outputArea.appendText("Manager not found.\n");
                return;
            }
            
            dialog.setContentText("New Name:");
            String newName = dialog.showAndWait().orElse(null);
            if (newName == null) return;
            
            dialog.setContentText("New Password:");
            String newPassword = dialog.showAndWait().orElse(null);
            if (newPassword == null) return;
            
            targetManager.setName(newName);
            targetManager.setPassword(newPassword);
            FileManager.overWriteObjectFile(FileManager.MANAGER_FILE, Lists.getManagerList());
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            outputArea.appendText("Manager credentials updated successfully!\n");
            this.managerName = newName;
            managerTitle.setText("Manager Dashboard - " + newName);
            
        } catch (NumberFormatException e) {
            outputArea.appendText("Invalid manager ID format.\n");
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private Manager findManagerById(int id) {
        for (Manager m : Lists.getManagerList()) {
            if (m.getId() == id) return m;
        }
        return null;
    }
    
    @FXML
    protected void onViewMyTeam() {
        try {
            outputArea.clear();
            Team myTeam = getManagerTeam();
            
            if (myTeam == null) {
                outputArea.appendText("No team found for this manager.\n");
                return;
            }
            
            displayTeamDetails(myTeam);
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void displayTeamDetails(Team t) {
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
    }
    
    @FXML
    protected void onBuyPlayers() {
        try {
            outputArea.clear();
            ArrayList<Player> availablePlayers = getAvailablePlayersForDisplay();
            displayAvailablePlayersForBuying(availablePlayers);
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Buy Player");
            dialog.setHeaderText("Enter player ID to buy:");
            dialog.setContentText("Player ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int playerId = Integer.parseInt(idStr);
            Team myTeam = getManagerTeam();
            
            if (myTeam == null) {
                outputArea.appendText("Team not found.\n");
                return;
            }
            
            String result = attemptPlayerPurchase(myTeam, playerId);
            outputArea.appendText(result);
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onSellPlayer() {
        try {
            outputArea.clear();
            Team myTeam = getManagerTeam();
            
            if (myTeam == null) {
                outputArea.appendText("Team not found.\n");
                return;
            }
            
            if (myTeam.getCurrentSize() == 0) {
                outputArea.appendText("No players in team to sell.\n");
                return;
            }
            
            displayTeamPlayers(myTeam);
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Sell Player");
            dialog.setHeaderText("Enter player ID to sell:");
            dialog.setContentText("Player ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int playerId = Integer.parseInt(idStr);
            String result = attemptPlayerSale(myTeam, playerId);
            outputArea.appendText(result);
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    @FXML
    protected void onComparePlayers() {
        try {
            outputArea.clear();
            ArrayList<Player> players = Lists.getPlayerList();
            displayAllPlayersWithStats(players);
            
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
            
            Player p1 = findPlayerById(players, id1);
            Player p2 = findPlayerById(players, id2);
            
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
    
    // Helper methods
    private Team getManagerTeam() {
        ArrayList<Team> teams = Lists.getTeamList();
        for (Team t : teams) {
            if (t.getManager() != null && t.getManager().getName().equals(managerName)) {
                return t;
            }
        }
        return null;
    }
    
    private ArrayList<Player> getAvailablePlayersForDisplay() {
        ArrayList<Player> available = new ArrayList<>();
        for (Player p : Lists.getPlayerList()) {
            if (p.getIsAvailable()) {
                available.add(p);
            }
        }
        return available;
    }
    
    private void displayAvailablePlayersForBuying(ArrayList<Player> players) {
        outputArea.appendText("--- Available Players ---\n");
        for (Player p : players) {
            outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                " | Position: " + p.getPosition() + " | Price: $" + p.getPrice() + "\n");
        }
    }
    
    private void displayTeamPlayers(Team team) {
        outputArea.appendText("--- Your Players ---\n");
        for (Player p : team.getPlayers()) {
            outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + "\n");
        }
    }
    
    private void displayAllPlayersWithStats(ArrayList<Player> players) {
        outputArea.appendText("--- Available Players ---\n");
        for (Player p : players) {
            outputArea.appendText("ID: " + p.getId() + " | Name: " + p.getName() + 
                                " | Goals: " + p.getGoal() + " | Matches: " + p.getMatches() + "\n");
        }
    }
    
    private Player findPlayerById(ArrayList<Player> players, int id) {
        for (Player p : players) {
            if (p.getId() == id) return p;
        }
        return null;
    }
    
    private String attemptPlayerPurchase(Team team, int playerId) throws IOException {
        ArrayList<Player> players = Lists.getPlayerList();
        Player selectedPlayer = findPlayerById(players, playerId);
        
        if (selectedPlayer == null || !selectedPlayer.getIsAvailable()) {
            return "Player not found or already sold.\n";
        }
        
        if (team.getCurrentSize() >= 11) {
            return "Team is full (11/11).\n";
        }
        
        if (team.getBudget() < selectedPlayer.getPrice()) {
            return "Insufficient budget. Required: $" + selectedPlayer.getPrice() + 
                   " | Available: $" + team.getBudget() + "\n";
        }
        
        team.getPlayers().add(selectedPlayer);
        team.setCurrentSize(team.getPlayers().size());
        team.setBudget(team.getBudget() - selectedPlayer.getPrice());
        selectedPlayer.setIsAvailable(false);
        selectedPlayer.setTeamId(team.getId());
        
        FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
        FileManager.overWriteObjectFile(FileManager.PLAYER_FILE, Lists.getPlayerList());
        
        return "Player bought successfully!\n";
    }
    
    private String attemptPlayerSale(Team team, int playerId) throws IOException {
        Player selectedPlayer = null;
        for (Player p : team.getPlayers()) {
            if (p.getId() == playerId) {
                selectedPlayer = p;
                break;
            }
        }
        
        if (selectedPlayer == null) {
            return "Player not found in team.\n";
        }
        
        team.getPlayers().remove(selectedPlayer);
        team.setCurrentSize(team.getPlayers().size());
        team.setBudget(team.getBudget() + selectedPlayer.getPrice());
        selectedPlayer.setIsAvailable(true);
        selectedPlayer.setTeamId(0);
        
        FileManager.overWriteObjectFile(FileManager.PLAYER_FILE, Lists.getPlayerList());
        FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
        
        return "Player sold successfully! Budget: $" + team.getBudget() + "\n";
    }
    
    @FXML
    protected void onLogout() throws IOException {
        SceneManager.getInstance().backToLogin();
    }
}


