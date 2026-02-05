package com.sazonov.utility;

import com.sazonov.utility.manager.FileOutputManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No input files provided.");
            return;
        }

        FileOutputManager outputManager = new FileOutputManager();

        processFiles(List.of(args), outputManager);

        outputManager.closeAll();
    }

    private static void processFiles(List<String> inputFiles, FileOutputManager outputManager) {
        for (String filePath : inputFiles) {
            Path inputFile = Path.of(filePath);
            if (!Files.exists(inputFile)) {
                System.err.println("Input file not found: " + inputFile);
                continue;
            }
            if (!Files.isReadable(inputFile)) {
                System.err.println("Input file is not readable: " + inputFile);
                continue;
            }

            try {
                Files.lines(inputFile).forEach(line -> {
                    processLine(line, outputManager);
                });
            } catch (IOException e) {
                System.err.println("Failed to read file " + inputFile + ": " + e.getMessage());
            }
        }
    }

    private static void processLine(String line, FileOutputManager outputManager) {
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        if (line.matches("[+-]?\\d+")) {
            outputManager.writeInteger(line);
        } else if (line.matches("[+-]?\\d*\\.\\d+([eE][+-]?\\d+)?")) {
            outputManager.writeFloat(line);
        } else {
            outputManager.writeString(line);
        }
    }
}
