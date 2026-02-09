package com.sazonov.utility.commandline.validation;

import com.sazonov.utility.exception.DirectoryCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class DirectoryValidator {

    /**
     * Основной метод, который проверяет и создает директорию
     */
    public Path ensureOutputDirectory(String outputDirectory) {
        Path directory;
        try {
            directory = Paths.get(outputDirectory);
            if (!Files.exists(directory)) {
                return createDirectory(directory);
            }
        } catch (Exception e) {
            log.error("Failed to create output directory at {}. Trying to create in the project root.", outputDirectory);
            return createFallbackDirectory();
        }
        return directory;
    }

    /**
     * Метод для создания директории по указанному пути
     */
    private Path createDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
            log.debug("Output directory created: {}", directory);
            return directory;
        } catch (Exception e) {
            return createFallbackDirectory();
        }
    }

    /**
     * Метод для создания fallback директории в корне проекта
     */
    private Path createFallbackDirectory() {
        Path fallbackDirectory = getFallbackDirectory();
        try {
            Files.createDirectories(fallbackDirectory);
            log.debug("Output directory created in project root: {}", fallbackDirectory);
            return fallbackDirectory;
        } catch (Exception ex) {
            log.error("Failed to create output directory in the project root: {}", fallbackDirectory, ex);
            throw new DirectoryCreationException(
                    "Failed to create directory at fallback location: " + fallbackDirectory
            );
        }
    }

    /**
     * Метод для получения fallback директории в корне проекта
     */
    private Path getFallbackDirectory() {
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        log.debug("Using fallback directory: {}", projectRoot);
        return projectRoot.resolve(".");
    }
}