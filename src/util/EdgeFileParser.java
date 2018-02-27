package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class EdgeFileParser {

    public static void parseFile(String filepath) {
        try {
            Scanner scanner = new Scanner(new FileReader(filepath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                if (parts.length != 2) {
                    throw new InputMismatchException(
                            "line must contain exactly one colon");
                } else {
                    String listEdges = parts[1];
                    for (String edge : listEdges.split(",")) {
                        // TODO: setup node edges
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
