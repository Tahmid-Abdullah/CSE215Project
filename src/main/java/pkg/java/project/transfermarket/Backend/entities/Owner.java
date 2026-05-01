package pkg.java.project.transfermarket.Backend.entities;

import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;

public class Owner extends  Entity{
    private int id;
    private double ownersBudget;
    private static final String ownerfile ="owner.txt";
    private Team team;

    public Owner(String name, String password, double budget) throws IOException {
        super(name, password);
        this.id= Tools.idGenerator(ownerfile);
        this.ownersBudget = budget;
        this.team=null;
    }

    public Owner(String name, String password, double budget,Team t) throws IOException {
        super(name, password);
        this.id= Tools.idGenerator(ownerfile);
        this.ownersBudget = budget;
        this.team=t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getOwnersBudget() {
        return ownersBudget;
    }

    public void setOwnersBudget(double ownersBudget) {
        this.ownersBudget = ownersBudget;
    }

    public double getBudget() {
        return ownersBudget;
    }

    public void setBudget(double budget) {
        this.ownersBudget = budget;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return this.id+","+this.getName()+","+this.ownersBudget+","+this.getPassword()+","+(this.team != null ? this.team.getTeamName() : "None");
    }

    public void displayInfo(){
        System.out.println("Owner Info:\n ID\t:"+this.id+"\nName:\t"+this.getName()+"\nOwner Budget:\t"+this.ownersBudget+"\nTeam owns:\t"+(this.team != null ? this.team.getTeamName() : "None"));

    }
}
