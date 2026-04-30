package pkg.java.project.transfermarket.Backend.actions;

import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.*;
import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;
import java.util.*;

import static pkg.java.project.transfermarket.Backend.actions.OwnerLogin.editTeam;

public class AdminLogin {
    static Scanner in = new Scanner(System.in);
    private static final String playerfile = FileManager.PLAYER_FILE;
    private static final String teamfile = FileManager.TEAM_FILE;
    private static final String adminFile = FileManager.ADMIN_FILE;
    private static final String managerfile = FileManager.MANAGER_FILE;
    private static final String ownerfile = FileManager.OWNER_FILE;

    public AdminLogin(){

    }

    // Check admin ...
    public static boolean checkAdmin(String username, String password) throws IOException {
        ArrayList<String> lines = FileManager.readFromFile(adminFile);
        if (lines.isEmpty()) {
            return false;
        }
        String adminDataLine = lines.get(0);
        String[] credentials = adminDataLine.split(",");
        return credentials.length >= 2 && credentials[0].strip().equals(username) && credentials[1].strip().equals(password);
    }

    //Case 2: View Managers
    public static void viewManagers() throws IOException {
        ArrayList<String> dataLines= FileManager.readFromFile(managerfile);
        if (dataLines.isEmpty()) {
            System.out.println("No managers data available.");
            return;
        }
        for(String singleLine:dataLines){
            String[] cred= singleLine.split(",");
            if(cred.length >= 3){
                boolean available = Boolean.parseBoolean(cred[2]);
                System.out.println(
                        "ID: " + cred[0] + " | Manager name: " + cred[1] + " | Availability: " + (available ? "Available" : "Sold"));
            }
        }
    }

    // Case 3: View Registered teams
    public static void viewTeams() throws IOException {
        ArrayList<String> dataLines= FileManager.readFromFile(teamfile);
        if (dataLines.isEmpty()) {
            System.out.println("No team data available.");
            return;
        }
        for(String singleLine:dataLines){
            String[] cred= singleLine.split(",");
            if(cred.length>=6) {
                System.out.println("Team ID: " + cred[0] + " | Team name: " + cred[1] + " | Manager name: " + cred[2] + " | Team Size: " + cred[3] +" | Team Budget: "+cred[4]+" | Team owner: "+cred[5]);
            }
            else {
                System.out.println("Invalid team data: " + singleLine);
            }
        }
    }

    // Case 4: Viewing the list of available players.
    public static void viewPlayers() throws IOException {
        ArrayList<String> playerDataLines = FileManager.readFromFile(playerfile);
        if (playerDataLines.isEmpty()) {
            System.out.println("No players available.");
            return;
        }
        for (String singleLine : playerDataLines) {
            String[] playerInfo = singleLine.split(",");
            if (playerInfo.length >= 8) {
                boolean available = Boolean.parseBoolean(playerInfo[7]);
                System.out.println("ID: " + playerInfo[0] + ", Name: " + playerInfo[1] + ", Age: " + playerInfo[2] + ", Position: " + playerInfo[3] + ", Price: $" + playerInfo[4]+", Goal scored: "+playerInfo[5]+", Matches played: "+playerInfo[6]+" | Availability: " + (available ? "Available" : "Sold"));
            } else {
                System.out.println("Invalid player data: " + singleLine);
            }
        }
    }

    //case 4: view owners
    public static void viewOwners() throws IOException {
        ArrayList<String> dataLines= FileManager.readFromFile(ownerfile);
        if (dataLines.isEmpty()) {
            System.out.println("No owner data available.");
            return;
        }
        for(String singleLine:dataLines){
            String[] cred= singleLine.split(",");
            if(cred.length >= 5){

                System.out.println(
                        "ID: " + cred[0] + " | Manager name: " + cred[1] + " | Budget: "+cred[2]+" | Team name: "+cred[4]);
            }
        }
    }


    //Case 5: adding player
    public static void addPlayer() throws IOException {
        try {
            String name = Tools.readString(in, "Enter player name: ");
            int age = Tools.readInt(in, "Enter player age: ");
            String position = Tools.readString(in, "Enter player position: ");
            double price = Tools.readDouble(in, "Enter player price: ");

            Player pl = new Player(name, age, position, price);
            Lists.addPlayer(pl);
            System.out.println("Player added successfully with ID " + pl.getId() + ".");
        } catch (IOException e) {
            System.out.println("Error adding player: " + e.getMessage());
        }
    }

    // case 6: Removing a player from the file, and it's object if unsold:
    public static void removePlayer() throws IOException {
        try {
            viewPlayers();
            int id = Tools.readInt(in, "Enter player ID to remove: ");
            Lists.removePlayer(id);
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Case 7: Adding manager:
    public static void addManager() throws IOException {
        try {
            String name = Tools.readString(in, "Enter Manager name: ");
            String password = Tools.readString(in, "Enter manager password: ");
            Manager m = new Manager(name,password);
            Lists.addManager(m);
            System.out.println("Manager added successfully with ID " + m.getId() + ".");
        } catch (IOException e) {
            System.out.println("Error adding manager: " + e.getMessage());
        }
    }

    //Case 8: removing manager
    public static void removeManager() throws IOException {
        try {
            viewManagers();
            ArrayList<String> line= FileManager.readFromFile(managerfile);
            if(line.isEmpty()){
                return;
            }
            int id = Tools.readInt(in, "Enter Manager ID to remove: ");
            Lists.removeManager(id);
        } catch (IOException e) {
            System.out.println("Error removing manager: " + e.getMessage());
        }
    }


    //case 9:  Add team
    public static void addTeam() throws IOException {
        try {
            String name = Tools.readString(in, "Enter Team name: ");
            viewManagers();
            int mid = Tools.readInt(in,"Select manager id:");
            ArrayList<Manager> mlist = Lists.getManagerList();
            if(mlist == null || mlist.isEmpty()){
                System.out.println("No managers available. Please add a manager first.");
                return;
            }
            Manager selectedManager = null;
            for(Manager m : mlist){
                if(m.getId() == mid){
                    selectedManager = m;
                    break;
                }
            }
            if(selectedManager == null){
                System.out.println("Manager not found.");
                return;
            }
            if(!selectedManager.getIsAvailable()){
                System.out.println("Manager is booked by another team.");
                return;
            }
            double b = Tools.readDouble(in,"Enter team budget: ");
            double p = Tools.readDouble(in,"Enter team price: ");

            String ownerName= Tools.readString(in,"Enter owner name: ");
            String password=Tools.readString(in,"Enter owners new password: ");
            double ownerBudget= Tools.readDouble(in,"Enter Owners budget: ");
            Owner o= new Owner(ownerName,password,ownerBudget);
            Lists.addOwner(o);
            Team t = new Team(name,selectedManager,b,p,o);
            selectedManager.setIsAvailable(false);
            Lists.addTeam(t);
            // Update manager file
            FileManager.overWriteObjectFile(managerfile,Lists.getManagerList());
            System.out.println("Team added successfully with ID " + t.getId() + ".");
        } catch (IOException e) {
            System.out.println("Error adding team: " + e.getMessage());
        }
    }

    // Case 10: Removing team
    public static void removeTeam() throws IOException {
        try {
            viewTeams();
            int id = Tools.readInt(in, "Enter Team ID to remove: ");
          Lists.removeTeam(id);
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid data format in team file: " + e.getMessage());
        }
        catch (IOException e){
        System.out.println("Error removing team: " + e.getMessage());
        }
    }

    //case 11:
    public static void addOwner() throws IOException {
        try {
            String name = Tools.readString(in, "Enter owner name: ");
            String password = Tools.readString(in, "Enter owner password: ");
            double budget = Tools.readDouble(in, "Enter owner budget: ");
            String Tname = Tools.readString(in, "Enter team name: ");
            double price = Tools.readDouble(in, "Enter team price: ");
            double Tbudget = Tools.readDouble(in, "Enter team budget: ");
            viewManagers();
            int Id=Tools.readInt(in,"Enter manager ID to select:");
            ArrayList<Manager> mlist = Lists.getManagerList();
            if(mlist == null || mlist.isEmpty()){
                System.out.println("No managers available. Please add a manager first.");
                return;
            }
            Manager selectedManager = null;
            for(Manager m : mlist){
                if(m.getId() == Id){
                    selectedManager = m;
                    break;
                }
            }
            if(selectedManager == null){
                System.out.println("Manager not found.");
                return;
            }
            if(!selectedManager.getIsAvailable()){
                System.out.println("Manager is booked by another team.");
                return;
            }
            Team t= new Team(Tname,selectedManager,Tbudget,price,new Owner(name,password,budget));
            selectedManager.setIsAvailable(false);
            Lists.addTeam(t);
            // Update manager file
            FileManager.overWriteObjectFile(managerfile,Lists.getManagerList());
            System.out.println("Team added successfully with ID " + t.getId() + ".");
            Lists.addOwner(t.getOwner());
            System.out.println("Owner added successfully with ID " + t.getOwner().getId() + ".");

        } catch (IOException e) {
            System.out.println("Error adding manager: " + e.getMessage());
        }
    }
    //case 12:
    public static void removeOwner() throws IOException {
        try {
            viewOwners();
            int id = Tools.readInt(in, "Enter Owner ID to remove: ");
            Lists.removeOwner(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    // Admin Menu:
    public static void adminFeatures() throws IOException {
        boolean bool = true;
        while (bool) {

            System.out.println("""
                    
                    1.Update admin credentials.
                    2.View Managers.
                    3.View registered teams.
                    4.View player list.
                    5.Add Player.
                    6.Remove Player.
                    7.Add Manager.
                    8.Remove Manager.
                    9.Add team.
                    10.Remove team.
                    11.Add Owner.
                    12.Remove Owner.
                    13.Log out.
                    
                    """);

            int c = Tools.readInt(in, "Choose an option: ");
            switch (c) {
                case 1:
                    try {
                        String username = Tools.readString(in, "Enter new username: ");
                        String password = Tools.readString(in, "Enter new password: ");
                        Admin admin = Admin.getInstance();
                        admin.setName(username);
                        admin.setPassword(password);
                        FileManager.overwriteFile(adminFile,admin.toString());
                        System.out.println("Admin credentials updated successfully!");
                    } catch (IOException e) {
                        System.out.println("Error updating admin credentials: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Manager list:");
                        viewManagers();
                    } catch (IOException e) {
                        System.out.println("Error viewing managers: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.println("Registered teams:");
                        viewTeams();
                    } catch (IOException e) {
                        System.out.println("Error viewing teams: " + e.getMessage());
                    }
                    break;

                case 4:
                    try {
                        System.out.println("Available players:");
                        viewPlayers();
                    } catch (IOException e) {
                        System.out.println("Error viewing players: " + e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        System.out.println("Adding player...");
                        addPlayer();
                    } catch (IOException e) {
                        System.out.println("Error adding player: " + e.getMessage());
                    }
                    break;

                case 6:
                    try {
                        System.out.println("Removing player...");
                        removePlayer();
                    } catch (IOException e) {
                        System.out.println("Error removing player: " + e.getMessage());
                    }
                    break;

                case 7:
                    try {
                        System.out.println("Adding Manager...");

                        addManager();
                    } catch (IOException e) {
                        System.out.println("Error adding manager: " + e.getMessage());
                    }
                    break;

                case 8:
                    try {
                        System.out.println("Removing manager...");
                        removeManager();
                    } catch (IOException e) {
                        System.out.println("Error removing manager: " + e.getMessage());
                    }
                    break;

                case 9:
                    try {
                        System.out.println("Adding team...");
                        addTeam();
                    } catch (IOException e) {
                        System.out.println("Error adding team: " + e.getMessage());
                    }
                    break;

                case 10:
                    try {
                        System.out.println("Removing team...");
                        removeTeam();
                    } catch (IOException e) {
                        System.out.println("Error removing team: " + e.getMessage());
                    }
                    break;

                case 11:
                    try {
                        System.out.println("Adding Owner...");
                        addOwner();
                    } catch (IOException e) {
                        System.out.println("Error adding owner: " + e.getMessage());
                    }
                    break;

                case 12:
                    try {
                        System.out.println("Removing owner...");
                        removeOwner();
                    } catch (IOException e) {
                        System.out.println("Error removing owner: " + e.getMessage());
                    }
                    break;
                case 13:
                    System.out.println("Logging out...");
                    bool = false;
                    break;


                default:
                    System.out.println("Invalid option. Please try again.");

            }
        }
    }
}
