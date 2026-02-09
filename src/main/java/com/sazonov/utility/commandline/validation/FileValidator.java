package com.sazonov.utility.commandline.validation;

import com.sazonov.utility.exception.FileValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FileValidator {

    public List<Path> validateAndFilterFiles(List<String> inputFiles) {
        if (inputFiles == null || inputFiles.isEmpty()) {
            log.error("No input files provided.");
            throw new FileValidationException("No input files provided.");
        }

        List<Path> validFiles = new ArrayList<>();

        for (String filePath : inputFiles) {
            Path path = Path.of(filePath);
            if (isValid(path)) validFiles.add(path);
        }

        if (validFiles.isEmpty()) {
            log.error("Cannot continue: no readable input files were provided.");
            throw new FileValidationException(
                    "The provided files did not pass validation checks."
            );
        }

        return validFiles;
    }

    public boolean isValid(Path inputFile) {
        return exists(inputFile) && isRegularFile(inputFile) && isReadable(inputFile);
    }

    private boolean exists(Path inputFile) {
        if (!Files.exists(inputFile)) {
            log.warn("Input file not found: {}", inputFile);
            return false;
        }
        return true;
    }

    private boolean isRegularFile(Path inputFile) {
        if (!Files.isRegularFile(inputFile)) {
            log.warn("Input path is not a file: {}", inputFile);
            return false;
        }
        return true;
    }

    private boolean isReadable(Path inputFile) {
        if (!Files.isReadable(inputFile)) {
            log.warn("Input file is not readable: {}", inputFile);
            return false;
        }
        return true;
    }
}