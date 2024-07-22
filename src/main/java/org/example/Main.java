package org.example;

import java.io.*;

public class Main {
    private static void readFile(String filePath, String fileNamePrefix, String writePath, Boolean isAppending)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line = null;
        String integers = "";
        String floats = "";
        String strings = "";
        while ((line = br.readLine()) != null) {
            try {
                long foo = Long.parseLong(line);
                integers += line + "\n";
                continue;
            }
            catch (NumberFormatException ignored) {}
            try {
                double foo = Double.parseDouble(line);
                floats += line + "\n";
                continue;
            }
            catch (NumberFormatException ignored) {}
            strings += line + "\n";
        }
        br.close();

        System.out.println("Integers:\n" + integers);
        if (!integers.isEmpty()) {
            BufferedWriter integersWriter = new BufferedWriter(
                    new FileWriter(writePath + "/" + fileNamePrefix + "integers", true));
            if (isAppending) {
                integersWriter.append(integers);
            } else {
                integersWriter.write(integers);
            }
            integersWriter.close();
        }
        System.out.println("Floats:\n" + floats);
        if (!floats.isEmpty()) {
            BufferedWriter floatsWriter = new BufferedWriter(
                    new FileWriter(writePath + "/" + fileNamePrefix + "floats", true));
            if (isAppending) {
                floatsWriter.append(floats);
            } else {
                floatsWriter.write(floats);
            }
            floatsWriter.close();
        }
        System.out.println("String:\n" + floats);
        if (!strings.isEmpty()) {
            BufferedWriter stringsWriter = new BufferedWriter(
                    new FileWriter(writePath + "/" + fileNamePrefix + "strings", true));
            if (isAppending) {
                stringsWriter.append(strings);
            } else {
                stringsWriter.write(strings);
            }
            stringsWriter.close();
        }
    }

    public static void main(String[] args) {
        try {
            Main.readFile("src/test/resources/in1.txt", "", "src/test/resources", true);
            Main.readFile("src/test/resources/in2.txt", "", "src/test/resources", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}