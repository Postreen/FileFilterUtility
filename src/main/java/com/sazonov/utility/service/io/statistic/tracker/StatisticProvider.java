package com.sazonov.utility.service.io.statistic.tracker;

import com.sazonov.utility.service.io.statistic.tracker.datastats.NumericStats;
import com.sazonov.utility.service.io.statistic.tracker.datastats.StringStats;

public interface StatisticProvider {
    NumericStats getIntegerStats();
    NumericStats getFloatStats();
    StringStats getStringStats();
}
