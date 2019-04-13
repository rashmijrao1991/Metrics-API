package com.flexengage.metrics.daoImpl;

import com.flexengage.metrics.helper.Stats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class MetricsDaoImplTest {
    private MetricsDaoImpl metricsDaoImpl;
    private Map<String, Stats> metricMap;

    @Before
    public void setUp() {
        metricMap = Collections.synchronizedMap(new HashMap<>());
        metricsDaoImpl = new MetricsDaoImpl(metricMap);
    }

    @Test
    public void test_getStatsForMetric() {
        Stats stats = new Stats();
        stats.addValue(1.1);
        metricMap.put("name1", stats);
        Stats stats_result = metricsDaoImpl.getStatsForMetric("name1");
        Assert.assertNotNull(stats_result);
        Assert.assertEquals(stats.getMin(), stats_result.getMin(), 0.01f);
        Assert.assertEquals(stats.getMin(), stats_result.getMax(), 0.01f);
        Assert.assertEquals(stats.getMin(), stats_result.getMean(), 0.01f);
        Assert.assertEquals(stats.getMin(), stats_result.getMedian(), 0.01f);
        stats = metricsDaoImpl.getStatsForMetric("name2");
        Assert.assertNull(stats);
    }

    @Test
    public void test_createNewMetric() {
        metricsDaoImpl.createNewMetric("name1");
        Assert.assertNotNull(metricMap.get("name1"));
    }

    @Test
    public void test_addValuesForMetric() {
        Stats stats = new Stats();
        List<Double> values = Arrays.asList(1.1, 2.2, 3.3);
        metricsDaoImpl.addValuesForMetric(stats, values);
        Assert.assertEquals(3, stats.getValues().size());
        Assert.assertEquals(1.1, stats.getValues().get(0), 0.01f);
        Assert.assertEquals(2.2, stats.getValues().get(1), 0.01f);
        Assert.assertEquals(3.3, stats.getValues().get(2), 0.01f);
    }
}
