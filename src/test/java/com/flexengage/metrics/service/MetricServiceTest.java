package com.flexengage.metrics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexengage.metrics.daoImpl.MetricsDaoImpl;
import com.flexengage.metrics.helper.MetricValue;
import com.flexengage.metrics.helper.Stats;
import com.flexengage.metrics.model.Metrics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MetricServiceTest {
    private MetricService metricService;

    @Mock
    private MetricsDaoImpl mockDao;

    private ObjectMapper objectMapper;
    private ArgumentCaptor<List<Double>> listCaptor;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockDao = mock(MetricsDaoImpl.class);
        listCaptor = ArgumentCaptor.forClass(List.class);
        metricService = new MetricService(mockDao, objectMapper);
    }

    @Test
    public void test_createMetrics() {
        final Metrics metrics = new Metrics();
        metrics.setNames(Arrays.asList("name1", "name2"));
        metricService.createMetrics(metrics);
        verify(mockDao, times(2)).createNewMetric(any(String.class));
    }

    @Test
    public void test_addMetricValues() {
        final List<MetricValue> metricValues = new ArrayList<>();
        metricValues.add(new MetricValue("name1", 1.0));
        metricValues.add(new MetricValue("name2", 3.0));
        metricValues.add(new MetricValue("name1", 6.0));
        metricValues.add(new MetricValue("name2", 4.0));
        when(mockDao.getStatsForMetric("name2")).thenReturn(null);
        when(mockDao.getStatsForMetric("name1")).thenReturn(new Stats());
        final Set<String> validName = metricService.addMetricValues(metricValues);
        Assert.assertEquals(1, validName.size());
        verify(mockDao).addValuesForMetric(any(Stats.class), listCaptor.capture());
        Assert.assertEquals(1.0, listCaptor.getValue().get(0), 0.01f);
        Assert.assertEquals(6.0, listCaptor.getValue().get(1), 0.01f);
    }

    @Test
    public void test_getStats() throws IOException {
        when(mockDao.getStatsForMetric("name1")).thenReturn(new Stats());
        Stats stats = new Stats();
        stats.addValue(1.1);
        when(mockDao.getStatsForMetric("name2")).thenReturn(stats);
        when(mockDao.getStatsForMetric("name3")).thenReturn(null);
        final String metric1 = metricService.getStats("name1");
        final String metric2 = metricService.getStats("name2");
        final String metric3 = metricService.getStats("name3");
        Assert.assertEquals("{}", metric1);
        Assert.assertTrue(metric2.length() > 0);
        Assert.assertNull(metric3);
        stats = objectMapper.readValue(metric2, Stats.class);
        Assert.assertEquals(stats.getMin(), 1.1, 0.01f);
        Assert.assertEquals(stats.getMax(), 1.1, 0.01f);
        Assert.assertEquals(stats.getMean(), 1.1, 0.01f);
        Assert.assertEquals(stats.getMedian(), 1.1, 0.01f);
    }
}
