package pkg.java.project.transfermarket.Backend.actions;

import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.*;
import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;
import java.util.*;


public class OwnerLogin {

    private static final String playerfile = "players.txt";
    private static final String teamfile = "teams.txt";
    private static final String adminFile = "admin.txt";
    private static final String managerfile ="manager.txt";
    private static final String ownerfile ="owner.txt";

    static Scanner in = new Scanner(System.in);

    public static boolean checkOwner(String name, String password) throws IOException {
        ArrayList<String> lines = FileManager.readFromFile(ownerfile);

        for(String line : lines){
            String[] cred = line.split(",");
            if(cred.length >= 5 &&
                    cred[1].strip().equals(name) && cred[3].strip().equals(password)){
                return true;
            }
        }
        return false;
    }

    public static void viewMyTeam(String name){
        for(Team t : Lists.getTeamList()){
            if(t.getOwner() != null &&
                    t.getOwner().getName().equals(name)){

                System.out.println("Team players:");
                t.teamPlayersList();

                System.out.println("Manager: " +
                        (t.getManager() != null ? t.getManager().getName() : "None"));

                System.out.println("Budget: " + t.getBudget());
                return;
            }
        }
        System.out.println("Team not found.");
    }

    // case 13:
    public static void editTeam(Team t) throws IOException {
        System.out.println("""
                1. Remove Manager.
                2. Buy player.
                3. Sell player.
                """);
        int c = Tools.readInt(in, "Enter choice:");
        switch (c){
            case 1 -> {
                Manager m = t.getManager();
                if (m == null) {
                    System.out.println("No manager assigned to this team.");
                    return;
                }
                t.setManager(null);
                m.setIsAvailable(true);
                FileManager.overWriteObjectFile(managerfile, Lists.getManagerList());
                FileManager.overWriteObjectFile(teamfile, Lists.getTeamList());
                System.out.println("Manager removed successfully.");
            }
            case 2 -> buyPlayerAsOwner(t);
            case 3 -> sellPlayerAsOwner(t);
            default -> System.out.println("Invalid Option.");
        }
    }

    public static void buyPlayerAsOwner(Team t) throws IOException {
        AdminLogin.viewPlayers();
        int id = Tools.readInt(in, "Enter Player ID to buy: ");

        ArrayList<Player> players = Lists.getPlayerList();
        Player selected = null;

        for (Player p : players) {
            if (p.getId() == id && p.getIsAvailable()) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Player not found or already sold.");
            return;
        }

        if (t.getCurrentSize() >= 11) {
            System.out.println("Team is full.");
            return;
        }

        if (t.getBudget() < selected.getPrice()) {
            System.out.println("Insufficient budget.");
            return;
        }

        t.getPlayers().add(selected);
        t.setCurrentSize(t.getPlayers().size());
        t.setBudget(t.getBudget() - selected.getPrice());
        selected.setIsAvailable(false);
        selected.setTeamId(t.getId());

        System.out.println("Player bought successfully.");

        // update files
        FileManager.overWriteObjectFile(teamfile, Lists.getTeamList());
        FileManager.overWriteObjectFile(playerfile, Lists.getPlayerList());
    }

    public static void sellPlayerAsOwner(Team t) throws IOException {
        if (t.getCurrentSize() == 0) {
            System.out.println("No players in team.");
            return;
        }

        System.out.println("Team players:");
        for (Player p : t.getPlayers()) {
            System.out.println("ID: " + p.getId() + " Name: " + p.getName());
        }

        int id = Tools.readInt(in, "Enter Player ID to sell: ");

        Player selected = null;

        for (Player p : t.getPlayers()) {
            if (p.getId() == id && !p.getIsAvailable()) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Player not found in team.");
            return;
        }

        t.getPlayers().remove(selected);
        t.setCurrentSize(t.getPlayers().size());
        t.setBudget(t.getBudget() + selected.getPrice());
        selected.setIsAvailable(true);
        selected.setTeamId(0);

        System.out.println("Player sold successfully.");

        FileManager.overWriteObjectFile(playerfile, Lists.getPlayerList());
        FileManager.overWriteObjectFile(teamfile, Lists.getTeamList());
    }

    public static void ownerFeatures(Scanner sc, String name) throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("""
                    1. View my team
                    2. Log out
                    3. Edit team.
                    """);

            int choice = Tools.readInt(sc, "Choose an option: ");

            switch (choice) {
                case 1 -> viewMyTeam(name);
                case 2 -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                case 3 -> {
                    viewMyTeam(name);

                    Team target = null;

                    ArrayList<Team> teamList = Lists.getTeamList();

                    for (Team t : teamList) {
                        if (t.getOwner().getName().equals(name)) {
                            target = t;
                            break;
                        }
                    }

                    if (target == null) {
                        System.out.println("Team not found!");
                    } else {
                        editTeam(target);
                    }
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
