package pkg.java.project.transfermarket.Backend.entities;

import pkg.java.project.transfermarket.File.FileManager;

import java.io.File;
import java.io.IOException;

public class Admin extends Entity{

    public Admin(String name, String password) {
        super(name,password);
    }
    private static Admin instance;
    //Singleton object creation so that only one admin cam be created.
    public static Admin getInstance() throws IOException {
        if(instance == null){
            instance = new Admin("admin","admin123"); // new admin creation

            File file = new File(FileManager.ADMIN_FILE);
            if (!file.exists() || file.length() == 0) {
                FileManager.writeToFile(FileManager.ADMIN_FILE, instance.toString());
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return this.getName()+","+this.getPassword();
    }
}
