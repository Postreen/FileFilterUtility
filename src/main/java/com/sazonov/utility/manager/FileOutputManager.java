package com.sazonov.utility.manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileOutputManager {

    private BufferedWriter integerWriter;
    private BufferedWriter floatWriter;
    private BufferedWriter stringWriter;

    public FileOutputManager() {
        try {
            integerWriter = Files.newBufferedWriter(Path.of("integers.txt"), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            floatWriter = Files.newBufferedWriter(Path.of("floats.txt"), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            stringWriter = Files.newBufferedWriter(Path.of("strings.txt"), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to open output files: " + e.getMessage());
        }
    }

    public void writeInteger(String value) {
        writeToFile(integerWriter, value);
    }

    public void writeFloat(String value) {
        writeToFile(floatWriter, value);
    }

    public void writeString(String value) {
        writeToFile(stringWriter, value);
    }

    private void writeToFile(BufferedWriter writer, String value) {
        try {
            writer.write(value);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write value: " + e.getMessage());
        }
    }

    public void closeAll() {
        try {
            if (integerWriter != null) integerWriter.close();
            if (floatWriter != null) floatWriter.close();
            if (stringWriter != null) stringWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to close output files: " + e.getMessage());
        }
    }
}
