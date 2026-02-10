package com.sazonov.utility.service.io.statistic.printer;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SummaryStatisticPrinterImpl implements StatisticPrinterService {

    @Override
    public void print(String label, NumericStats stats) {
        log.info("{}:", label);
        log.info("- Count: {}", stats.getCount());
    }

    @Override
    public void print(String label, StringStats stats) {
        log.info("{}:", label);
        log.info("- Count: {}", stats.getCount());
    }
}
