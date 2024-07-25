package org.example;
import org.apache.commons.cli.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    private static void writeFile(String str, String filePath, Boolean isAppending)
            throws IOException {
        if (str.isEmpty()) return;
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, isAppending));
        } catch (IOException e) {
            System.out.println("Can't write file by path " + filePath);
            return;
        }
        writer.write(str);
        writer.close();
    }

    private static void filter(String filePath, String filePrefix, String outputPath, Boolean isAppending)
            throws IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File " + filePath + " not found. ");
            return;
        }
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
            } catch (IOException ignored) {}
        }
        if (cmd.hasOption("statistics")) getStatistics(outputPath+"/"+filePrefix);
        if (cmd.hasOption("fullStatistics")) getFullStatistics(outputPath+"/"+filePrefix);
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

    private static void getStatistics(String fileFullPrefix) {
        System.out.println("Integers count: " + getFileLinesCount(fileFullPrefix+"integers.txt"));
        System.out.println("Floats count: " + getFileLinesCount(fileFullPrefix+"floats.txt"));
        System.out.println("Strings count: " + getFileLinesCount(fileFullPrefix+"strings.txt") + "\n");
    }

    private static int getFileLinesCount(String filePath){
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) lines++;
            reader.close();
            return lines;
        }
        catch (IOException e) {
            return lines;
        }
    }

    private static void getFullStatistics(String fileFullPrefix) throws IOException {
        getIntegersStatistics(fileFullPrefix);
        getFloatStatistics(fileFullPrefix);
        getStringsStatistics(fileFullPrefix);
    }

    private static void getIntegersStatistics(String fileFullPrefix) throws IOException{
        BufferedReader integerReader;

        try {
            integerReader = new BufferedReader(new FileReader(fileFullPrefix+"integers.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("INTEGERS");
            System.out.println("COUNT: 0\n");
            return;
        }
        String line = integerReader.readLine();
        if (line == null) {
            System.out.println("INTEGERS");
            System.out.println("COUNT: 0\n");
            return;
        }

        int lines = 1;
        long max = Long.parseLong(line);
        long min = Long.parseLong(line);
        long sum = Long.parseLong(line);
        double avg = Double.parseDouble(line);

        while (true) {
            line = integerReader.readLine();
            if (line == null) break;
            lines++;
            long current = Long.parseLong(line);
            if (max < current) max = current;
            if (min > current) min = current;
            sum += current;
            avg = (double) sum / lines;
        }

        System.out.println("INTEGERS");
        System.out.println("COUNT: " + lines);
        System.out.println("SUM: " + sum);
        System.out.println("MAX: " + max);
        System.out.println("MIN: " + min);
        System.out.println("AVG: " + avg + "\n");
        integerReader.close();
    }

    private static void getFloatStatistics(String fileFullPrefix) throws IOException{
        BufferedReader floatReader;

        try {
            floatReader = new BufferedReader(new FileReader(fileFullPrefix+"floats.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("FLOAT");
            System.out.println("COUNT: 0");
            return;
        }
        String line = floatReader.readLine();
        if (line == null) {
            System.out.println("FLOAT");
            System.out.println("COUNT: 0");
            return;
        }

        int lines = 1;
        double max = Double.parseDouble(line);
        double min = Double.parseDouble(line);
        double sum = Double.parseDouble(line);
        double avg = Double.parseDouble(line);

        while (true) {
            line = floatReader.readLine();
            if (line == null) break;
            lines++;
            double current = Double.parseDouble(line);
            if (max < current) max = current;
            if (min > current) min = current;
            sum += current;
            avg = sum / lines;
        }

        System.out.println("FLOAT");
        System.out.println("COUNT: " + lines);
        System.out.println("SUM: " + sum);
        System.out.println("MAX: " + max);
        System.out.println("MIN: " + min);
        System.out.println("AVG: " + avg + "\n");
        floatReader.close();
    }

    private static void getStringsStatistics(String fileFullPrefix) throws IOException {
        BufferedReader stringsReader;
        try {
            //Need for reading russian symbols
            stringsReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileFullPrefix+"strings.txt"), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.out.println("STRINGS");
            System.out.println("COUNT: 0");
            return;
        }
        String line = stringsReader.readLine();
        if (line == null) {
            System.out.println("STRINGS");
            System.out.println("COUNT: 0");
            return;
        }
        int lines = 1;
        int maxLength = line.length();
        int minLength = line.length();
        while (true) {
            line = stringsReader.readLine();
            if (line == null) break;
            lines++;
            if (maxLength < line.length()) maxLength = line.length();
            if (minLength > line.length()) minLength = line.length();
        }
        System.out.println("STRINGS");
        System.out.println("COUNT: " + lines);
        System.out.println("MAX LENGTH: " + maxLength);
        System.out.println("MIN LENGTH: " + minLength + "\n");
        stringsReader.close();
    }
}