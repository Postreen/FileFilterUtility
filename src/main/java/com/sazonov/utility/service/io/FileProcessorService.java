package com.sazonov.utility.service.io;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.service.io.reader.FileReaderService;
import com.sazonov.utility.service.io.reader.FileReaderServiceImpl;
import com.sazonov.utility.service.io.statistic.StatisticReportService;
import com.sazonov.utility.service.io.statistic.tracker.StatsTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import com.sazonov.utility.service.io.writer.OutputWriterServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FileProcessorService {

    public void processFiles(List<Path> inputFiles, Configuration config) {
        StatsTracker statsTracker = new StatsTracker();

        OutputWriterService outputWriterService = new OutputWriterServiceImpl(config);
        FileReaderService fileReaderService = new FileReaderServiceImpl();
        StatisticReportService statisticReportService = new StatisticReportService(config.statisticsMode());

        try {
            fileReaderService.readLines(inputFiles, outputWriterService, statsTracker);
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
        statisticReportService.print(statsTracker);
    }
}
