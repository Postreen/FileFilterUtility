package com.sazonov.utility.stats;

import com.sazonov.utility.model.StatisticsMode;

public class DefaultStatsPresenterFactory implements StatsPresenterFactory {
    @Override
    public StatsPresenter create(StatisticsMode statisticsMode) {
        return new StatsPresenter(statisticsMode);
    }
}
