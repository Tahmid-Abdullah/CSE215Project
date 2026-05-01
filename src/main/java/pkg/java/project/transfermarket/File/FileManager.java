package pkg.java.project.transfermarket.File;


import java.io.*;
import java.util.ArrayList;

public class FileManager {
    public static final String PLAYER_FILE = "players.txt";
    public static final String TEAM_FILE = "teams.txt";
    public static final String ADMIN_FILE = "admin.txt";
    public static final String MANAGER_FILE = "manager.txt";
    public static final String OWNER_FILE = "owner.txt";

    public static void FileInitialization() throws IOException{


            File player = new File(PLAYER_FILE);
            File team = new File(TEAM_FILE);
            File admin = new File(ADMIN_FILE);
            File owner = new File(OWNER_FILE);
            File manager= new File(MANAGER_FILE);

            if (player.createNewFile()) {
                System.out.println("Player file created successfully.");
            }
            if (team.createNewFile()) {
                System.out.println("Teams file created successfully.");
            }
            if (admin.createNewFile()) {
                System.out.println("Admin  file created successfully.");
            }
            if (manager.createNewFile()) {
                System.out.println("Managers file created successfully.");
            }
            if (owner.createNewFile()) {
                System.out.println("Owners file created successfully.");
            }


            if (player.exists()) {
                System.out.println("Players file loaded successfully.");
            }
            if (team.exists()) {
                System.out.println("Teams file loaded successfully.");
            }
            if (admin.exists()) {
                System.out.println("Admin  file loaded successfully.");
            }
            if (manager.exists()) {
                System.out.println("Managers file loaded successfully.");
            }
            if (owner.exists()) {
                System.out.println("Owners file loaded successfully.");
            }
        }

    // Writing in a file method:
    public static void writeToFile(String filename,String data) throws IOException {
        java.nio.file.Path path = java.nio.file.Paths.get(filename);
        if (path.getParent() != null) {
            java.nio.file.Files.createDirectories(path.getParent());
        }
        try(BufferedWriter bw= new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(data);
            bw.newLine();
        }
    }

    // Reading from a file method:
    public static ArrayList<String> readFromFile(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        java.io.File f = new java.io.File(filename);
        if (!f.exists()) {
            // create the file and return empty list instead of throwing
            f.getParentFile();
            f.createNewFile();
            return lines;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // Overwriting a file method:
    public static void overwriteFile(String filename, String data) throws IOException {
        java.io.File f = new java.io.File(filename);
        if (!f.exists()) {
            f.createNewFile();
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            bw.write(data);
            bw.newLine();
        }
    }

    // Overwriting a file with a list of data:
    public static void overWriteFile(String filename, ArrayList<String> dataList) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for(String data : dataList) {
                bw.write(data);
                bw.newLine();
            }
        }
    }

    public static <T> void overWriteObjectFile(String filename, ArrayList<T> list) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for (T obj : list) {
                bw.write(obj.toString());
                bw.newLine();
            }
        }
    }

}
