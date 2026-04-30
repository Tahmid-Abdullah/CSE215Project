package pkg.java.project.transfermarket.Backend;

import pkg.java.project.transfermarket.Backend.actions.AdminLogin;
import pkg.java.project.transfermarket.Backend.actions.ManagerLogin;
import pkg.java.project.transfermarket.Backend.actions.OwnerLogin;
import pkg.java.project.transfermarket.Backend.entities.*;
import pkg.java.project.transfermarket.File.*;
import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            FileManager.FileInitialization();

            if(FileManager.readFromFile(FileManager.ADMIN_FILE).isEmpty()) {
                Admin admin = Admin.getInstance();
                System.out.println("Admin account created with default credentials: username='admin', password='admin123'");
            }

            boolean bool = true;
            do {
                try {
                    System.out.println("\nChoose an option: \n1. Admin Login\n2. Manager Login\n3. Team Owner Login.\n4. Exit");
                    int c = Tools.readInt(in, "Enter your choice: ");
                    switch (c) {
                        case 1:
                            try {
                                System.out.println("Admin Login");
                                String adminUsername = Tools.readString(in, "Enter username: ");
                                String adminPassword = Tools.readString(in, "Enter password: ");
                                if(AdminLogin.checkAdmin(adminUsername, adminPassword)) {
                                    System.out.println("Login successful!");
                                    Lists.syncAllFiles();
                                    AdminLogin.adminFeatures();
                                }
                                else {
                                    System.out.println("Invalid credentials. Please try again.");
                                }
                            }
                            catch (IOException e) {
                                System.out.println("Error during admin login: " + e.getMessage());
                            }
                            break;

                        case 2:
                            try {
                                System.out.println("Manager Login");
                                String managerName = Tools.readString(in, "Enter name: ");
                                String managerPassword = Tools.readString(in, "Enter password: ");
                                if(ManagerLogin.checkManager(managerName, managerPassword)) {
                                    System.out.println("Login successful!");
                                    Lists.syncAllFiles();
                                    ManagerLogin.managerFeatures(in, managerName);
                                }
                                else {
                                    System.out.println("Invalid credentials. Please try again.");
                                }
                            }
                            catch (IOException e) {
                                System.out.println("Error during manager login: " + e.getMessage());
                            }
                            break;

                        case 3:

                            try {
                                System.out.println("Owner Login");
                                String ownerName = Tools.readString(in, "Enter name: ");
                                String ownerPassword = Tools.readString(in, "Enter password: ");
                                if(OwnerLogin.checkOwner(ownerName, ownerPassword)) {
                                    System.out.println("Login successful!");
                                    Lists.syncAllFiles();
                                    OwnerLogin.ownerFeatures(in, ownerName);
                                }
                                else {
                                    System.out.println("Invalid credentials. Please try again.");
                                }
                            } catch (IOException e) {
                                System.out.println("Error during team registration: " + e.getMessage());
                            }
                            break;

                        case 4:
                            System.out.println("Exiting... Transfermarket is closing. See you next time!");
                            bool = false;
                            break;

                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }while(bool);
        }
        catch (IOException e) {
            System.out.println("Error initializing system: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}
