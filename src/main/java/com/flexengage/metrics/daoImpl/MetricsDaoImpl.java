package com.flexengage.metrics.daoImpl;

import com.flexengage.metrics.dao.MetricsDao;
import com.flexengage.metrics.helper.Stats;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class MetricsDaoImpl implements MetricsDao {

    private final Map<String, Stats> metricMap;

    public MetricsDaoImpl(Map<String, Stats> metricMap) {
        this.metricMap = metricMap;
    }

    public Stats getStatsForMetric(@NotNull final String metricName) {
        return metricMap.get(metricName);
    }

    public void createNewMetric(@NotNull final String metricName) {
        Stats stats = new Stats();
        metricMap.put(metricName, stats);
    }

    public void addValuesForMetric(@NotNull final Stats stats, @NotNull final List<Double> values) {
        synchronized (stats) {
            for (Double value : values) {
                stats.addValue(value);
            }
        }
    }

}
