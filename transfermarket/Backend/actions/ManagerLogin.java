package transfermarket.Backend.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import transfermarket.Backend.entities.Manager;
import transfermarket.Backend.entities.Player;
import transfermarket.Backend.entities.Team;
import transfermarket.File.FileManager;
import transfermarket.File.Lists;
import transfermarket.Util.Tools;

public class ManagerLogin implements  Compare{
    private static final String playerfile = FileManager.PLAYER_FILE;
    private static final String teamfile = FileManager.TEAM_FILE;
    private static final String adminFile = FileManager.ADMIN_FILE;
    private static final String managerfile = FileManager.MANAGER_FILE;

    static Scanner in = new Scanner(System.in);
    public static boolean checkManager(String name, String password) throws IOException {
        ArrayList<String> lines = FileManager.readFromFile(managerfile);
        for(String line : lines){
            String[] cred = line.split(",");
            if(cred.length >= 4 &&
                    cred[1].strip().equals(name) && cred[3].strip().equals(password)){
                return true;
            }
        }
        return false;
    }

    public static void viewMyTeam(String name){
        ArrayList<Team> list = Lists.getTeamList();

        for(Team t : list){
            if(t.getManager() != null && t.getManager().getName().equals(name)) {

                if (t.getCurrentSize() == 0) {
                    System.out.println("No player in the team.");
                    return;
                }
                if (t.getCurrentSize()>0) {
                    System.out.println("Team players:");

                    t.teamPlayersList();
                    System.out.println("Budget: " + t.getBudget());
                    System.out.println("Size: " + t.getCurrentSize());
                    return;
                }
            }
        }
        System.out.println("Team not found.");
    }

    public static void sellPlayer(String managerName) throws IOException {

        ArrayList<Team> teams = Lists.getTeamList();
        ArrayList<Player> players = Lists.getPlayerList();

        Team myTeam = null;

        for (Team t : teams) {
            if (t.getManager() != null &&
                    t.getManager().getName().equals(managerName)) {
                myTeam = t;
                break;
            }
        }

        if (myTeam == null) {
            System.out.println("Team not found.");
            return;
        }

        if (myTeam.getCurrentSize() == 0) {
            System.out.println("No players in team.");
            return;
        }

        System.out.println("My players:");
        for (Player p : myTeam.getPlayers()) {
            System.out.println("ID: " + p.getId() + " Name: " + p.getName());
        }

        int id = Tools.readInt(in, "Enter Player ID to sell: ");

        Player selected = null;

        for (Player p : myTeam.getPlayers()) {
            if (p.getId() == id && p.getIsAvailable() == false) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Player not found in team.");
            return;
        }

        myTeam.getPlayers().remove(selected);
        myTeam.setCurrentSize(myTeam.getPlayers().size());
        myTeam.setBudget(myTeam.getBudget() + selected.getPrice());
        selected.setIsAvailable(true);
        selected.setTeamId(0);

        System.out.println("Player sold successfully.");

        FileManager.overWriteObjectFile(playerfile, Lists.getPlayerList());
        FileManager.overWriteObjectFile(teamfile,Lists.getTeamList());
    }

    public static void buyPlayer(String managerName) throws IOException {

        AdminLogin.viewPlayers();

        int id = Tools.readInt(in, "Enter Player ID to buy: ");

        ArrayList<Player> players = Lists.getPlayerList();
        ArrayList<Team> teams = Lists.getTeamList();

        Team myTeam = null;
        for(Team t : teams){
            if(t.getManager() != null && t.getManager().getName().equals(managerName)){
                myTeam = t;
                break;
            }
        }
        if(myTeam == null){
            System.out.println("Team not found.");
            return;
        }

        Player selected = null;
        for(Player p : players){
            if(p.getId() == id && p.getIsAvailable()){
                selected = p;
                break;
            }
        }

        if(selected == null){
            System.out.println("Player not found or already sold.");
            return;
        }

        if(myTeam.getCurrentSize() >= 11){
            System.out.println("Team is full.");
            return;
        }

        if(myTeam.getBudget() < selected.getPrice()){
            System.out.println("Insufficient budget.");
            return;
        }

        myTeam.getPlayers().add(selected);
        myTeam.setCurrentSize(myTeam.getPlayers().size());
        myTeam.setBudget(myTeam.getBudget() - selected.getPrice());
        selected.setIsAvailable(false);
        selected.setTeamId(myTeam.getId());

        System.out.println("Player bought successfully.");

        // update files
        FileManager.overWriteObjectFile(teamfile,Lists.getTeamList());
        FileManager.overWriteObjectFile(playerfile,Lists.getPlayerList());
    }


    public String comparePlayer(Player player1, Player player2) {
        if (player1 == null || player2 == null) {
            return "Error: One or both players are null.";
        }

        double player1Ratio = 0;
        double player2Ratio = 0;

        if (player1.getMatches() > 0) {
            player1Ratio = (double) player1.getGoal() / player1.getMatches();
        }

        if (player2.getMatches() > 0) {
            player2Ratio = (double) player2.getGoal() / player2.getMatches();
        }

        if (player1Ratio > player2Ratio) {
            return "Player " + player1.getName() + " has more per match goal ratio than player " + player2.getName();
        } else if (player2Ratio > player1Ratio) {
            return "Player " + player2.getName() + " has more per match goal ratio than player " + player1.getName();
        } else {
            return "Both players have the same per match goal ratio.";
        }
    }

    public static void comparePlayersMenu() {
        try {
            AdminLogin.viewPlayers();
            int id1 = Tools.readInt(in, "Enter first Player ID: ");
            int id2 = Tools.readInt(in, "Enter second Player ID: ");

            ArrayList<Player> players = Lists.getPlayerList();
            Player player1 = null;
            Player player2 = null;

            for (Player p : players) {
                if (p.getId() == id1) {
                    player1 = p;
                }
                if (p.getId() == id2) {
                    player2 = p;
                }
            }

            if (player1 == null || player2 == null) {
                System.out.println("One or both players not found.");
                return;
            }

            ManagerLogin manager = new ManagerLogin();
            System.out.println(manager.comparePlayer(player1, player2));
        } catch (IOException e) {
            System.out.println("Error comparing players: " + e.getMessage());
        }
    }


    public static void managerFeatures(Scanner sc, String name) throws IOException {

        boolean running = true;

        while (running) {

            System.out.println("""
                1. Update manager credentials
                2. View my team
                3. Buy players
                4. Sell player
                5. Compare players
                6. Log out
                """);

            int choice = Tools.readInt(sc, "Choose an option: ");

            switch (choice) {

                case 1 -> {
                    try {
                        int id = Tools.readInt(sc, "Enter Manager ID: ");

                        ArrayList<Manager> list = Lists.getManagerList();
                        boolean found = false;

                        for (Manager m : list) {
                            if (m.getId() == id) {
                                String newName = Tools.readString(sc, "Enter new name: ");
                                String newPassword = Tools.readString(sc, "Enter new password: ");

                                m.setName(newName);
                                m.setPassword(newPassword);
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            FileManager.overWriteObjectFile(managerfile,Lists.getManagerList());
                        } else {
                            System.out.println("Manager not found.");
                        }

                    } catch (IOException e) {
                        System.out.println("Error updating manager: " + e.getMessage());
                    }
                }

                case 2 -> viewMyTeam(name);

                case 3 -> {
                    try {
                        buyPlayer(name);
                    } catch (IOException e) {
                        System.out.println("Buy error: " + e.getMessage());
                    }
                }

                case 4 -> {
                    try {
                        sellPlayer(name);
                    } catch (IOException e) {
                        System.out.println("Sell error: " + e.getMessage());
                    }
                }

                case 5 -> comparePlayersMenu();

                case 6 -> {
                    System.out.println("Logging out...");
                    running = false;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }
}
