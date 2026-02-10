package com.sazonov.utility.service.io.reader;

import com.sazonov.utility.service.io.reader.handler.LineHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileReaderServiceImpl implements FileReaderService {
    private final LineHandler lineHandler;

    @Override
    public void readLines(List<Path> inputFiles) {
        int failedFilesCount = 0;

        for (Path inputFile : inputFiles) {
            try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineHandler.handle(line);
                }
            } catch (MalformedInputException e) {
                log.warn("File {} has incorrect encoding.", inputFile);
                failedFilesCount++;
            } catch (SecurityException e) {
                log.warn("No permission to access the file {}.", inputFile);
                failedFilesCount++;
            } catch (IOException e) {
                log.warn("Failed to read file {}: {}", inputFile, e.getMessage());
                failedFilesCount++;
            } catch (Exception e) {
                log.warn("Unexpected error while reading file {}: {}", inputFile, e.getMessage());
                failedFilesCount++;
            }
        }

        if (failedFilesCount == inputFiles.size()) {
            throw new RuntimeException("All files failed to be processed.");
        }
    }
}
