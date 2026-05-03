package transfermarket.UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transfermarket.Backend.entities.*;
import transfermarket.File.*;
import java.util.*;
import java.io.IOException;

public class OwnerController {
    @FXML private Label ownerTitle;
    @FXML private TextArea outputArea;
    
    private String ownerName;
    
    public void setOwnerName(String name) {
        this.ownerName = name;
        ownerTitle.setText("Owner Dashboard - " + name);
    }
    
    @FXML
    protected void onViewMyTeam() {
        try {
            outputArea.clear();
            ArrayList<Team> teams = Lists.getTeamList();
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
            
            ArrayList<Team> teams = Lists.getTeamList();
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

    @FXML
    protected void onUpdateCredentials() {
        try {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Update Credentials");
            nameDialog.setHeaderText("Enter new name:");
            String newName = nameDialog.showAndWait().orElse(null);
            if (newName == null || newName.isBlank()) return;

            TextInputDialog passDialog = new TextInputDialog();
            passDialog.setTitle("Update Credentials");
            passDialog.setHeaderText("Enter new password:");
            String newPass = passDialog.showAndWait().orElse(null);
            if (newPass == null || newPass.isBlank()) return;

            ArrayList<Owner> owners = Lists.getOwnerList();
            Owner target = null;
            for (Owner o : owners) {
                if (o.getName().equals(ownerName)) {
                    target = o;
                    break;
                }
            }
            if (target == null) {
                outputArea.appendText("Owner not found.\n");
                return;
            }
            target.setName(newName);
            target.setPassword(newPass);
            // Persist owners and teams (owner name appears in teams)
            FileManager.overWriteObjectFile(FileManager.OWNER_FILE, Lists.getOwnerList());
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            this.ownerName = newName;
            ownerTitle.setText("Owner Dashboard - " + newName);
            outputArea.appendText("Credentials updated successfully.\n");
        } catch (Exception e) {
            outputArea.appendText("Error updating credentials: " + e.getMessage() + "\n");
        }
    }
    
    private void showEditMenu(Team team) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Team");
        alert.setHeaderText("What would you like to do?");
        alert.setContentText("1. Remove Manager\n2. Buy Player\n3. Sell Player\n4. Assign Manager");
        
        ButtonType removeManagerBtn = new ButtonType("1. Remove Manager");
        ButtonType buyPlayerBtn = new ButtonType("2. Buy Player");
        ButtonType sellPlayerBtn = new ButtonType("3. Sell Player");
        ButtonType assignManagerBtn = new ButtonType("4. Assign Manager");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(removeManagerBtn, buyPlayerBtn, sellPlayerBtn, assignManagerBtn, cancelBtn);
        
        alert.showAndWait().ifPresent(result -> {
            if (result == removeManagerBtn) {
                removeManager(team);
            } else if (result == buyPlayerBtn) {
                buyPlayer(team);
            } else if (result == sellPlayerBtn) {
                sellPlayer(team);
            } else if (result == assignManagerBtn) {
                assignManager(team);
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
            m.setTeamId(0);
            FileManager.overWriteObjectFile(FileManager.MANAGER_FILE, Lists.getManagerList());
            System.out.println("manager.txt updated");
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            System.out.println("teams.txt updated");
            outputArea.appendText("Manager removed successfully.\n");
        } catch (IOException e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void buyPlayer(Team team) {
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
            
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            System.out.println("teams.txt updated");
            FileManager.overWriteObjectFile(FileManager.PLAYER_FILE, Lists.getPlayerList());
            System.out.println("players.txt updated");
            
            outputArea.appendText("Player bought successfully!\n");
            System.out.println("Player bought: " + selectedPlayer.getName() + " by team: " + team.getTeamName());
            System.out.println("New team budget: $" + team.getBudget());
            System.out.println("Player details updated: " + selectedPlayer);
            
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
            
           FileManager.overWriteObjectFile(FileManager.PLAYER_FILE, Lists.getPlayerList());
            System.out.println("players.txt updated");
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            System.out.println("teams.txt updated");
            
            outputArea.appendText("Player sold successfully! Budget: $" + team.getBudget() + "\n");
            
        } catch (Exception e) {
            outputArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }
    
    private void assignManager(Team team) {
        try {
            outputArea.clear();
            outputArea.appendText("--- Available Managers ---\n");
            
            ArrayList<Manager> managers = Lists.getManagerList();
            for (Manager m : managers) {
                if (m.getIsAvailable()) {
                    outputArea.appendText("ID: " + m.getId() + " | Name: " + m.getName() + 
                                        " | Team: " + (m.getTeamId() != 0 ? m.getTeamId() : "Free") + "\n");
                }
            }
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Assign Manager");
            dialog.setHeaderText("Enter manager ID to assign:");
            dialog.setContentText("Manager ID:");
            
            String idStr = dialog.showAndWait().orElse(null);
            if (idStr == null) return;
            
            int managerId = Integer.parseInt(idStr);
            Manager selectedManager = null;
            
            for (Manager m : managers) {
                if (m.getId() == managerId && m.getIsAvailable()) {
                    selectedManager = m;
                    break;
                }
            }
            
             if (selectedManager == null) {
                 outputArea.appendText("Manager not found or already assigned to a team.\n");
                 return;
             }
             
             if (team.getManager() != null) {
                 team.getManager().setIsAvailable(true);
                 team.getManager().setTeamId(0);
             }
             
             team.setManager(selectedManager);
             selectedManager.setIsAvailable(false);
             selectedManager.setTeamId(team.getId());
            
            FileManager.overWriteObjectFile(FileManager.MANAGER_FILE, Lists.getManagerList());
            System.out.println("manager.txt updated");
            FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
            System.out.println("teams.txt updated");
            
            outputArea.appendText("Manager assigned successfully!\n");
            
         } catch (Exception e) {
             outputArea.appendText("Error: " + e.getMessage() + "\n");
         }
     }
     
     @FXML
     protected void onBuyTeam() {
         try {
             // First, get the owner
             ArrayList<Owner> owners = Lists.getOwnerList();
             Owner currentOwner = null;
             for (Owner o : owners) {
                 if (o.getName().equals(ownerName)) {
                     currentOwner = o;
                     break;
                 }
             }
             
             if (currentOwner == null) {
                 outputArea.appendText("Owner not found.\n");
                 return;
             }
             
             // Check if owner already has a team
             ArrayList<Team> teams = Lists.getTeamList();
             for (Team t : teams) {
                 if (t.getOwner() != null && t.getOwner().getId() == currentOwner.getId()) {
                     outputArea.appendText("You already own a team. You can only own one team.\n");
                     return;
                 }
             }
             
             // Show available teams
             outputArea.clear();
             outputArea.appendText("--- Available Teams for Purchase ---\n");
             ArrayList<Team> availableTeams = new ArrayList<>();
             for (Team t : teams) {
                 if (t.getOwner() == null && t.getCurrentSize() == 0 && t.getManager() == null) {
                     outputArea.appendText("Team ID: " + t.getId() + " | Name: " + t.getTeamName() + 
                             " | Price: $" + t.getTeamPrice() + " | Budget: $" + t.getBudget() + "\n");
                     availableTeams.add(t);
                 }
             }
             
             if (availableTeams.isEmpty()) {
                 outputArea.appendText("No teams available for purchase.\n");
                 return;
             }
             
             // Ask for team ID
             TextInputDialog dialog = new TextInputDialog();
             dialog.setTitle("Buy Team");
             dialog.setHeaderText("Enter team ID to buy:");
             dialog.setContentText("Team ID:");
             
             String teamIdStr = dialog.showAndWait().orElse(null);
             if (teamIdStr == null) return;
             
             int teamId = Integer.parseInt(teamIdStr);
             Team selectedTeam = null;
             for (Team t : availableTeams) {
                 if (t.getId() == teamId) {
                     selectedTeam = t;
                     break;
                 }
             }
             
             if (selectedTeam == null) {
                 outputArea.appendText("Team not available for purchase.\n");
                 return;
             }
             
             if (currentOwner.getBudget() < selectedTeam.getTeamPrice()) {
                 outputArea.appendText("Insufficient budget. Required: $" + selectedTeam.getTeamPrice() + 
                         " | Available: $" + currentOwner.getBudget() + "\n");
                 return;
             }
             
             // Purchase team
             currentOwner.setBudget(currentOwner.getBudget() - selectedTeam.getTeamPrice());
             selectedTeam.setOwner(currentOwner);
             
             // Update lists and files
             FileManager.overWriteObjectFile(FileManager.OWNER_FILE, Lists.getOwnerList());
             System.out.println("owner.txt updated");
             FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());
             System.out.println("teams.txt updated");
             
             outputArea.appendText("Team purchased successfully! Your new budget: $" + currentOwner.getBudget() + "\n");
             outputArea.appendText("You can now assign a manager and edit your team.\n");
         } catch (Exception e) {
             outputArea.appendText("Error: " + e.getMessage() + "\n");
         }
     }
     
     @FXML
     protected void onLogout() throws IOException {
         SceneManager.getInstance().backToLogin();
     }
}
