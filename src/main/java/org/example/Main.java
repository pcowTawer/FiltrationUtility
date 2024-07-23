package org.example;
import org.apache.commons.cli.*;
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

    public static void main(String[] args) throws Exception {

        final Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        String filePrefix = cmd.getOptionValue("prefix");
        String outputFilePath = cmd.getOptionValue("output");
        if (cmd.hasOption("statistics")) {
            System.out.println("Need statistics");
        }
        if (cmd.hasOption("fullStatistics")) {
            System.out.println("Need full statistics");
        }
        System.out.println(filePrefix);
        System.out.println(outputFilePath);
    }

    private static Options getOptions() {
        Options options = new Options();

        Option prefix = new Option("p", "prefix", true, "output file prefix");
        prefix.setRequired(false);
        options.addOption(prefix);

        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(false);
        options.addOption(output);

        Option statistics = new Option("s", "statistics", false, "statistics about files");
        statistics.setRequired(false);
        options.addOption(statistics);

        Option fullStatistics = new Option("f", "fullStatistics", false, "full statistics about files");
        fullStatistics.setRequired(false);
        options.addOption(fullStatistics);
        return options;
    }


}