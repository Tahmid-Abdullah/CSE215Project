package pkg.java.project.transfermarket.File;

import pkg.java.project.transfermarket.Backend.entities.*;

import java.io.IOException;
import java.util.*;

public class Lists {

    private static final String playerfile = FileManager.PLAYER_FILE;
    private static final String teamfile = FileManager.TEAM_FILE;
    private static final String adminFile = FileManager.ADMIN_FILE;
    private static final String managerfile = FileManager.MANAGER_FILE;
    private static final String ownerfile = FileManager.OWNER_FILE;

    private static ArrayList<Player> playerList = new ArrayList<>();
    private static ArrayList<Manager> managerList = new ArrayList<>();
    private static ArrayList<Team> teamList = new ArrayList<>();
    private static ArrayList<Owner> ownerList = new ArrayList<>();

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static ArrayList<Manager> getManagerList() {
        return managerList;
    }

    public static ArrayList<Team> getTeamList() {
        return teamList;
    }

    public static ArrayList<Owner> getOwnerList() {
        return ownerList;
    }

    public static void addPlayer(Player p) throws IOException {
        playerList.add(p);
        FileManager.writeToFile(playerfile, p.toString());
        System.out.println("Player added and written to file successfully.");
    }

    public static void addManager(Manager m) throws IOException {
        managerList.add(m);
        FileManager.writeToFile(managerfile, m.toString());
        System.out.println("Manager added and written to file successfully.");
    }

    public static void addTeam(Team t) throws IOException {
        teamList.add(t);
        FileManager.writeToFile(teamfile, t.toString());
        System.out.println("Team added and written to file successfully.");
    }

    public static void addOwner(Owner o) throws IOException {
        ownerList.add(o);
        FileManager.writeToFile(ownerfile, o.toString());
        System.out.println("Owner added and written to file successfully.");
    }

    public static void removePlayer(int id) throws IOException {
        Player target = null;
        for (Player p : playerList) {
            if (p.getId() == id) {
                target = p;
                break;
            }
        }
        if (target == null) {
            System.out.println("No player found with this ID.");
            return;
        }
        playerList.remove(target);
        System.out.println("Player Removed successfully.");
        FileManager.overWriteObjectFile(playerfile, playerList);
        System.out.println("File overwritten successfully.");
    }

    public static void loadManagers() throws IOException {
        managerList.clear();
        List<String> lines = FileManager.readFromFile(managerfile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                boolean isAvailable = Boolean.parseBoolean(parts[2]);
                String password = parts[3];
                Manager manager = new Manager(name, password);
                manager.setId(id);
                manager.setIsAvailable(isAvailable);
                managerList.add(manager);
            }
        }
    }

    public static void removeManager(int id) throws IOException {
        loadManagers();
        Manager target = null;
        for (Manager m : managerList) {
            if (m.getId() == id) {
                target = m;
                break;
            }
        }
        if (target == null) {
            System.out.println("No manager found with this ID.");
            return;
        }
        if (target.getIsAvailable()) {
            managerList.remove(target);
            FileManager.overWriteObjectFile(managerfile, managerList);
            System.out.println("manager.txt updated");
        } else {
            System.out.println("Manager is already booked.");
        }
    }

    public static void removeTeam(int id) throws IOException {
        Team target = null;
        for (Team t : teamList) {
            if (t.getId() == id) {
                target = t;
                break;
            }
        }
        if (target == null) {
            System.out.println("No team found with this ID.");
            return;
        }
        if (target.getCurrentSize() == 0 && target.getManager() == null) {
            teamList.remove(target);
            FileManager.overWriteObjectFile(teamfile, teamList);
        } else {
            System.out.println("Team already has players/manager.");
        }
    }

    public static void removeOwner(int id) throws IOException {
        Owner target = null;
        for (Owner o : ownerList) {
            if (o.getId() == id) {
                target = o;
                break;
            }
        }
        if (target == null) {
            System.out.println("No owner found with this ID.");
            return;
        }
        for (Team t : teamList) {
            if (t.getOwner() != null && t.getOwner().getId() == id) {
                System.out.println("Owner already owns a team.");
                return;
            }
        }
        ownerList.remove(target);
        FileManager.overWriteObjectFile(ownerfile, ownerList);
    }

    public static void syncAllFiles() throws IOException {
        loadPlayers();
        loadManagers();
        loadOwners();
        loadTeams();
        FileManager.overWriteObjectFile(playerfile, playerList);
        FileManager.overWriteObjectFile(managerfile, managerList);
        FileManager.overWriteObjectFile(teamfile, teamList);
        FileManager.overWriteObjectFile(ownerfile, ownerList);
    }

    public static void loadPlayers() throws IOException {
        playerList.clear();
        List<String> lines = FileManager.readFromFile(playerfile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 9) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                String position = parts[3];
                double price = Double.parseDouble(parts[4]);
                int goal = Integer.parseInt(parts[5]);
                int matches = Integer.parseInt(parts[6]);
                boolean isAvailable = Boolean.parseBoolean(parts[7]);
                int teamId = Integer.parseInt(parts[8]);
                Player player = new Player(name, age, position, price);
                player.setId(id);
                player.setGoal(goal);
                player.setMatches(matches);
                player.setIsAvailable(isAvailable);
                player.setTeamId(teamId);
                playerList.add(player);
            }
        }
    }

    public static void loadTeams() throws IOException {
        teamList.clear();
        List<String> lines = FileManager.readFromFile(teamfile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String managerName = parts[2];
                int currentSize = Integer.parseInt(parts[3]);
                double budget = Double.parseDouble(parts[4]);
                String ownerName = parts[5];
                // Note: Loading teams requires loading managers and owners first
                // For simplicity, assuming managers and owners are loaded
                Manager manager = null;
                for (Manager m : managerList) {
                    if (m.getName().equals(managerName)) {
                        manager = m;
                        break;
                    }
                }
                Owner owner = null;
                for (Owner o : ownerList) {
                    if (o.getName().equals(ownerName)) {
                        owner = o;
                        break;
                    }
                }
                if (manager != null && owner != null) {
                    Team team = new Team(name, manager, budget, 0, owner); // Price not stored, set to 0
                    team.setId(id);
                    team.setCurrentSize(currentSize);
                    // Load players
                    for (Player p : playerList) {
                        if (p.getTeamId() == id) {
                            team.getPlayers().add(p);
                        }
                    }
                    if (team.getPlayers().isEmpty() && currentSize > 0) {
                        // For old data, assign not available players
                        int count = 0;
                        for (Player p : playerList) {
                            if (!p.getIsAvailable() && p.getTeamId() == 0 && count < currentSize) {
                                team.getPlayers().add(p);
                                p.setTeamId(id);
                                count++;
                            }
                        }
                    }
                    team.setCurrentSize(team.getPlayers().size());
                    teamList.add(team);
                }
            }
        }
    }

    public static void loadOwners() throws IOException {
        ownerList.clear();
        List<String> lines = FileManager.readFromFile(ownerfile);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double budget = Double.parseDouble(parts[2]);
                String password = parts[3];
                String teamName = parts[4];
                Owner owner = new Owner(name, password, budget);
                owner.setId(id);
                ownerList.add(owner);
            }
        }
    }
}