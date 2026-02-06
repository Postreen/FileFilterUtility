package com.sazonov.utility.service.io.reader.handler;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.DataType;
import com.sazonov.utility.service.io.writer.OutputWriterService;
import com.sazonov.utility.service.io.statistic.tracker.StatisticTracker;
import com.sazonov.utility.utils.LineClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LineHandler {
    private final OutputWriterService outputWriterService;
    private final StatisticTracker statisticTracker;

    public void handle(String line) {
        DataType type = LineClassifier.classify(line);
        switch (type) {
            case INTEGER -> {
                log.debug("Detected INTEGER: {}", line);
                if (outputWriterService.writeInteger(line)) {
                    statisticTracker.recordInteger(line.trim());
                }
            }
            case FLOAT -> {
                log.debug("Detected FLOAT: {}", line);
                if (outputWriterService.writeFloat(line)) {
                    statisticTracker.recordFloat(line.trim());
                }
            }
            case STRING -> {
                log.debug("Detected STRING: {}", line);
                if (outputWriterService.writeString(line)) {
                    statisticTracker.recordString(line);
                }
            }
            default -> log.debug("Unrecognized line type: {}", line);
        }
    }
}