package com.flexengage.metrics.dao;

import com.flexengage.metrics.helper.Stats;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MetricsDao {
    Stats getStatsForMetric(@NotNull final String metricName);

    void addValuesForMetric(@NotNull final Stats stats, @NotNull final List<Double> values);

}
