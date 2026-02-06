package com.sazonov.utility.service.io;

import com.sazonov.utility.service.io.reader.FileReaderService;
import com.sazonov.utility.service.io.statistic.StatisticReportService;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void processFiles(List<Path> inputFiles) {

        try {
            fileReaderService.readLines(inputFiles, outputWriterService, statisticTracker);
        } catch (Exception e) {
            log.error("Unexpected error during processing: {}", e.getMessage(), e);
        } finally {
            try {
                outputWriterService.closeAll();
            } catch (Exception e) {
                log.error("Error while closing output files: {}", e.getMessage(), e);
            }
        }

        log.info("File processing completed.");
        statisticReportService.print(statisticTracker);
    }
}
