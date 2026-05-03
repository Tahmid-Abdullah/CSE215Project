package pkg.java.project.transfermarket.Backend.entities;

import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;

public class Manager extends Entity{
    private int id;
    private boolean isAvailable;
    private int teamId;
    private static final String managerfile ="manager.txt";

    public Manager( String name, String password) throws IOException {
        super(name, password);
        this.id= Tools.idGenerator(managerfile);
        this.isAvailable = true;
        this.teamId = 0;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return this.id+","+this.getName()+","+this.isAvailable+","+this.getPassword()+","+this.teamId;
    }

    public void displayInfo(){
        System.out.println("Manager Info:\n ID\t\t:"+this.id+"\nName:\t\t"+this.getName()+"\nAvailability:\t"+((this.isAvailable)?"Available":"Booked"));

    }
}
