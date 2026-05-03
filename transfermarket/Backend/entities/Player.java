package transfermarket.Backend.entities;

import transfermarket.Util.Tools;
import transfermarket.File.FileManager;

import java.io.IOException;

public class Player {

    private static final String playerfile = FileManager.PLAYER_FILE;

    private int id;
    private String name;
    private int age;
    private String position;
    private double price;
    private boolean isAvailable;
    private int matches;
    private int goal;
    private int teamId;

    public Player(String name, int age, String position, double price) throws IOException {
        this.name=name;
        this.id= Tools.idGenerator(playerfile);
        this.age=age;
        this.position=position;
        this.price=price;
        this.isAvailable=true;
        this.matches=0;
        this.goal=0;
        this.teamId=0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getPosition() {
        return this.position;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    public int getMatches() {
        return this.matches;
    }

    public int getGoal() {
        return this.goal;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String toString(){
        return this.id+","+this.name+","+this.age+","+this.position+","+this.price+","+this.goal+","+this.matches+","+this.isAvailable+","+this.teamId;
    }

    public void displayPlayers(){
        System.out.println(" Player Info:\n ID\t\t:"+this.id+"\nName:\t\t"+this.name+"\nAge\t\t:"+this.age+"\nPosition\t\t:"+this.position+"\nPrice\t\t:"+this.price+"\nMatches played\t\t:"+this.matches+"\nGoal Scored\t\t:"+this.goal+"\nAvailability:\t"+((this.isAvailable)?"Available":"Sold"));
    }
}
