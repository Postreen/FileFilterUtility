package com.sazonov.utility.service.io;

import com.sazonov.utility.service.io.reader.FileReaderService;
import com.sazonov.utility.service.io.statistic.StatisticReportService;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessorService {
    private final OutputWriterService outputWriterService;
    private final FileReaderService fileReaderService;
    private final StatisticReportService statisticReportService;
    private final StatisticTracker statisticTracker;

    public void processFiles(List<Path> inputFiles) throws Exception {
        boolean hasError = false;

        try {
            fileReaderService.readLines(inputFiles, outputWriterService, statisticTracker);
        } catch (IOException e) {
            log.error("Error during file reading process: {}", e.getMessage(), e);
            hasError = true;
        } catch (RuntimeException e) {
            log.error("All files failed to be processed: {}", e.getMessage(), e);
            hasError = true;
        } catch (Exception e) {
            log.error("Unexpected error during file processing: {}", e.getMessage(), e);
            hasError = true;
        } finally {
            try {
                outputWriterService.closeAll();
            } catch (Exception e) {
                log.error("Error while closing output files: {}", e.getMessage(), e);
                throw new IOException("Failed to close output files.", e);
            }
        }

        if (hasError) {
            log.warn("File processing completed with errors.");
        } else {
            statisticReportService.print(statisticTracker);
        }
    }
}
