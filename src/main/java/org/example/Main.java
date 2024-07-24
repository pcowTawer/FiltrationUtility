package org.example;
import org.apache.commons.cli.*;
import java.io.*;

public class Main {
    private static void writeFile(String str, String filePath, Boolean isAppending)
            throws IOException {
        if (str.isEmpty()) return;
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, isAppending));
        writer.write(str);
        writer.close();
    }

    private static void filter(String filePath, String filePrefix, String outputPath, Boolean isAppending)
            throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder integers = new StringBuilder();
        StringBuilder floats = new StringBuilder();
        StringBuilder strings = new StringBuilder();
        while ((line = br.readLine()) != null) {
            try {
                Long.parseLong(line);
                integers.append(line).append("\n");
                continue;
            }
            catch (NumberFormatException ignored) {}
            try {
                Double.parseDouble(line);
                floats.append(line).append("\n");
                continue;
            }
            catch (NumberFormatException ignored) {}
            strings.append(line).append(line.isEmpty() ? "" : "\n");
        }
        br.close();

        writeFile(integers.toString(), outputPath+"/"+filePrefix +"integers.txt", isAppending);
        writeFile(floats.toString(), outputPath+"/"+filePrefix+"floats.txt", isAppending);
        writeFile(strings.toString(), outputPath +"/"+filePrefix+"strings.txt", isAppending);
    }

    public static void main(String[] args) throws IOException {

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
        String outputPath = cmd.getOptionValue("output");
        String[] filePaths = cmd.getArgs();

        for (String filePath: filePaths) {
            try {
                filter(filePath, filePrefix, outputPath, cmd.hasOption("append"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (cmd.hasOption("statistics")) getStatistics(outputPath+"/"+filePrefix);
        cmd.hasOption("fullStatistics");// TODO: getFullStatistics(outputPath+"/"+filePrefix);
    }

    private static Options getOptions() {
        Options options = new Options();

        Option prefix = new Option("p", "prefix", true, "output file prefix");
        options.addOption(prefix);

        Option output = new Option("o", "output", true, "output file path");
        options.addOption(output);

        Option statistics = new Option("s", "statistics", false, "statistics about files");
        options.addOption(statistics);

        Option fullStatistics = new Option("f", "fullStatistics", false, "full statistics about files");
        options.addOption(fullStatistics);

        Option append = new Option("a", "append", false, "append to file");
        options.addOption(append);

        return options;
    }

    private static void getStatistics(String fileFullPrefix) throws IOException {
        System.out.println("Integers count: " + getFileLinesCount(fileFullPrefix+"integers.txt"));
        System.out.println("Floats count: " + getFileLinesCount(fileFullPrefix+"floats.txt"));
        System.out.println("Strings count: " + getFileLinesCount(fileFullPrefix+"strings.txt"));
    }

    private static int getFileLinesCount(String filePath) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }


}