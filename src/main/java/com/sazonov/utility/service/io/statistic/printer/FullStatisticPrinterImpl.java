package com.sazonov.utility.service.io.statistic.printer;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FullStatisticPrinterImpl implements StatisticPrinterService {

    @Override
    public void print(String label, NumericStats stats) {
        log.info("""
                        {}:
                        - Min: {}
                        - Max: {}
                        - Average: {}
                        - Sum: {}
                        - Count: {}
                        """,
                label,
                formatNullable(stats.getMin()),
                formatNullable(stats.getMax()),
                stats.average(),
                stats.getSum(),
                stats.getCount());
    }

    @Override
    public void print(String label, StringStats stats) {
        log.info("""
                        {}:
                        - Min length: {}
                        - Max length: {}
                        - Count: {}
                        """,
                label,
                stats.getCount() == 0 ? "N/A" : stats.getMinLength(),
                stats.getCount() == 0 ? "N/A" : stats.getMaxLength(),
                stats.getCount());
    }

    private String formatNullable(Object value) {
        return value == null ? "N/A" : value.toString();
    }
}
