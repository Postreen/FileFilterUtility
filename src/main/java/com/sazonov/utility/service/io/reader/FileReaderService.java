package com.sazonov.utility.service.io.reader;

import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.service.io.writer.OutputWriterService;

import java.nio.file.Path;
import java.util.List;

public interface FileReaderService {
    void readLines(List<Path> inputFiles,
                   OutputWriterService outputWriterService,
                   StatisticTracker statisticTracker
    );
}
