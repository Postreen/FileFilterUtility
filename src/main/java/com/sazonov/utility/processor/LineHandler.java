package com.sazonov.utility.processor;

import com.sazonov.utility.io.writer.OutputWriter;
import com.sazonov.utility.model.DataType;
import com.sazonov.utility.stats.tracker.StatsTracker;
import com.sazonov.utility.utils.LineClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LineHandler {
    private final OutputWriter outputWriter;
    private final StatsTracker statsTracker;

    public void handle(String line) {
        DataType type = LineClassifier.classify(line);
        switch (type) {
            case INTEGER -> {
                log.debug("Detected INTEGER: {}", line);
                if (outputWriter.writeInteger(line)) {
                    statsTracker.recordInteger(line.trim());
                }
            }
            case FLOAT -> {
                log.debug("Detected FLOAT: {}", line);
                if (outputWriter.writeFloat(line)) {
                    statsTracker.recordFloat(line.trim());
                }
            }
            case STRING -> {
                log.debug("Detected STRING: {}", line);
                if (outputWriter.writeString(line)) {
                    statsTracker.recordString(line);
                }
            }
            default -> log.debug("Unrecognized line type: {}", line);
        }
    }
}