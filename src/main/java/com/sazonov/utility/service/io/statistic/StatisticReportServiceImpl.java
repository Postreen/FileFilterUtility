package com.sazonov.utility.service.io.statistic;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.model.StatisticsMode;
import com.sazonov.utility.service.io.statistic.printer.FullStatisticPrinterImpl;
import com.sazonov.utility.service.io.statistic.printer.StatisticPrinterService;
import com.sazonov.utility.service.io.statistic.printer.SummaryStatisticPrinterImpl;
import com.sazonov.utility.service.io.statistic.tracker.StatisticProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticReportServiceImpl implements StatisticReportService{
    private final Configuration configuration;
    private final FullStatisticPrinterImpl fullPrinter;
    private final SummaryStatisticPrinterImpl summaryPrinter;
    private final StatisticProvider statisticProvider;

    @Override
    public void print() {
        if (configuration.getStatisticsMode() == StatisticsMode.NONE) {
            log.info("Statistics are not printed because the mode is set to NONE.");
            return;
        }

        StatisticPrinterService printer = getPrinterForMode();

        printer.print("Integers", statisticProvider.getIntegerStats());
        printer.print("Floats", statisticProvider.getFloatStats());
        printer.print("Strings", statisticProvider.getStringStats());
    }

    private StatisticPrinterService getPrinterForMode() {
        return switch (configuration.getStatisticsMode()) {
            case FULL -> fullPrinter;
            case SUMMARY -> summaryPrinter;
            default -> throw new IllegalStateException("Unexpected value: " + configuration.getStatisticsMode());
        };
    }
}