package com.sazonov.utility.stats;

import com.sazonov.utility.model.StatisticsMode;

public interface StatsPresenterFactory {
    StatsPresenter create(StatisticsMode statisticsMode);
}
