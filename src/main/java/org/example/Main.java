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

    private static void readData(String filePath, String filePrefix, String outputPath, Boolean isAppending)
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

    public static void main(String[] args) {

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
        cmd.hasOption("statistics");                            //TODO: Realise statistics function
        cmd.hasOption("fullStatistics");                        //TODO: Realise fullStatistics function
        Boolean isAppending = cmd.hasOption("a");
        String[] filePaths = cmd.getArgs();

        for (String filePath: filePaths) {
            try {
                readData(filePath, filePrefix, outputPath, isAppending);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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


}