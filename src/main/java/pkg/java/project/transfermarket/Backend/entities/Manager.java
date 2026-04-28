package pkg.java.project.transfermarket.Backend.entities;

import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;

public class Manager extends Entity{
    private int id;
    private boolean isAvailable;
    private static final String managerfile ="manager.txt";

    public Manager( String name, String password) throws IOException {
        this.id= Tools.idGenerator(managerfile);
        super(name, password);
        this.isAvailable = true;
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

    @Override
    public String toString() {
        return this.id+","+this.getName()+","+this.isAvailable+","+this.getPassword();
    }

    public void displayInfo(){
        System.out.println("Manager Info:\n ID\t\t:"+this.id+"\nName:\t\t"+this.getName()+"\nAvailability:\t"+((this.isAvailable)?"Available":"Booked"));

    }
}
