package com.sazonov.utility.service;

import com.sazonov.utility.commandline.CliParser;
import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.service.io.FileProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingCoordinatorService {

    private final FileProcessorService fileProcessorService;
    private final CliParser cliParser;
    private final Configuration configuration;

    public void process(String[] args) {
        log.info("Start file processing.");

        try {
            cliParser.parse(args);
            fileProcessorService.processFiles(configuration.getInputFiles());

            log.info("File processing completed.");
        } catch (Exception e) {
            log.error("File processing failed: {}", e.getMessage());
        }
    }
}