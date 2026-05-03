package transfermarket.Util;

import transfermarket.File.FileManager;

import java.io.IOException;
import java.util.*;

public class Tools {
    public static int idGenerator(String filename) throws IOException {
        ArrayList<String> lines = FileManager.readFromFile(filename);
        Set<Integer> ids = new HashSet<>();

        for(String line : lines){
            if (line == null || line.isBlank()) {
                continue;
            }
            String[] cred = line.split(",");
            try {
                ids.add(Integer.parseInt(cred[0]));
            } catch(Exception e){
                System.out.println("Skipping invalid ID in " + filename + ": " + line);
            }
        }
        int id = 1;
        while(ids.contains(id))
        {
            id++;
        }
        return id;
    }

    //checks if the input is an integer or not.
    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (!scanner.hasNextInt()) {
                    scanner.nextLine(); // Clear the buffer
                    System.out.println("Invalid input! Please enter a valid integer.");
                    continue;
                }
                int value = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear the buffer
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    //checks if the input is a double or not.
    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                if (!scanner.hasNextDouble()) {
                    scanner.nextLine(); // Clear the buffer
                    System.out.println("Invalid input! Please enter a valid number.");
                    continue;
                }
                double value = scanner.nextDouble();
                scanner.nextLine(); // Clear the buffer
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear the buffer
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    //checks if the input is a string or not.
    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
