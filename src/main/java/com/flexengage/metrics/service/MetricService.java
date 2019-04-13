package com.flexengage.metrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexengage.metrics.daoImpl.MetricsDaoImpl;
import com.flexengage.metrics.helper.MetricValue;
import com.flexengage.metrics.helper.Stats;
import com.flexengage.metrics.model.Metrics;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.*;


public class MetricService {

    private final MetricsDaoImpl metricsDaoImpl;
    private final ObjectMapper objectMapper;

    public MetricService(@NotNull final MetricsDaoImpl metricsDaoImpl, @NotNull final ObjectMapper objectMapper) {
        this.metricsDaoImpl = metricsDaoImpl;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new metric if not existing
     *
     * @param metrics
     */
    public void createMetrics(Metrics metrics) {
        for (String name : metrics.getNames()) {
            final Stats stats = metricsDaoImpl.getStatsForMetric(name);
            if (stats == null)
                metricsDaoImpl.createNewMetric(name);
        }
    }

    /**
     * Adds the metric value if existing metric. Only existing metric names for which values have been added
     * are returned
     *
     * @param metricValues
     * @return
     */
    public Set<String> addMetricValues(List<MetricValue> metricValues) {

        final Map<String, List<Double>> groupMetrics = new HashMap<>();
        final Set<String> validSet = new HashSet<>();

        for (MetricValue metricValue : metricValues) {
            if (metricValue.getName() == null || metricValue.getValue() == null)
                return null;

            List<Double> currentVals;
            if (groupMetrics.containsKey(metricValue.getName()))
                currentVals = groupMetrics.get(metricValue.getName());
            else
                currentVals = new ArrayList<>();
            currentVals.add(metricValue.getValue());
            groupMetrics.put(metricValue.getName(), currentVals);
        }

        for (String metricName : groupMetrics.keySet()) {
            final Stats stats = metricsDaoImpl.getStatsForMetric(metricName);
            if (stats != null) {
                metricsDaoImpl.addValuesForMetric(stats, groupMetrics.get(metricName));
                validSet.add(metricName);
            }
        }

        return validSet;
    }

    /**
     * Returns the min, max, median and mean for a given metric
     *
     * @param metricName
     * @return
     */
    public String getStats(String metricName) {
        Stats stats = metricsDaoImpl.getStatsForMetric(metricName);
        String json = null;
        try {
            if (stats != null) {
                if (stats.getValues().size() > 0) {
                    json = objectMapper.writeValueAsString(stats);
                } else {
                    json = new JSONObject().toString();
                }
            }
            return json;
        } catch (Exception e) {
            return null;
        }
    }

}
