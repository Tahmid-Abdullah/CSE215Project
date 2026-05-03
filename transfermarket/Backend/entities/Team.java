package transfermarket.Backend.entities;

import transfermarket.Util.Tools;
import transfermarket.File.FileManager;

import java.io.IOException;
import java.util.ArrayList;

public class Team {
    private static final String teamfile = FileManager.TEAM_FILE;


    private int id;
    private String teamName;
    private final int teamSize=11;
    private Manager manager;
    private int currentSize;
    private double budget;
    private double teamPrice;
    private Owner owner;
    ArrayList<Player> players= new ArrayList<>(teamSize);

    public Team(String teamName, Manager manager, double budget,double teamPrice,Owner o) throws IOException {
        this.id= Tools.idGenerator(teamfile);
        this.teamName = teamName;
        this.manager = manager;
        this.budget = budget;
        this.currentSize = 0;
        this.teamPrice=teamPrice;
        this.owner=o;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Manager getManager() {
        return manager;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public double getTeamPrice() {
        return teamPrice;
    }

    public double getBudget() {
        return budget;
    }

    public Owner getOwner() {
        return owner;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setTeamPrice(double teamPrice) {
        this.teamPrice = teamPrice;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String toString() {
        String managerName = this.manager != null ? this.manager.getName() : "None";
        String ownerName = this.owner != null ? this.owner.getName() : "None";
        return this.id+","+this.teamName+","+managerName+","+this.currentSize+","+this.budget+","+ownerName+","+this.teamPrice;
    }

    public void displayTeam(){
        System.out.println("ID: " + id);
        System.out.println("Team Name: " + teamName);
        System.out.println("Team size: " + currentSize);
        System.out.println("Team Budget: $" + budget);
        System.out.println("Manager: " + (manager != null ? manager.getName() : "None"));
        System.out.println("Team price: "+this.teamPrice);
        System.out.println("Team Owner: "+(owner != null ? owner.getName() : "None"));
        System.out.println("Team players: \n");
        teamPlayersList();
    }

    public void teamPlayersList(){
        for(Player p: players){
            System.out.println("Name:"+p.getName()+", Position: "+p.getPosition()+", Age: "+p.getAge()+".");
        }
    }
}
