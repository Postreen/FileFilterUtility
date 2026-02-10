package com.sazonov.utility.service.io.statistic.printer;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;

public interface StatisticPrinterService {
    void print(String label, NumericStats stats);
    void print(String label, StringStats stats);
}
