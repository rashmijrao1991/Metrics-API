package com.flexengage.metrics.controller;


import com.flexengage.metrics.helper.MetricValue;
import com.flexengage.metrics.model.Metrics;
import com.flexengage.metrics.service.MetricService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class MetricAppControllerTest {

    private MetricAppController metricAppController;
    @Mock
    private MetricService mockMetricService;
    private ArgumentCaptor<List<String>> listCaptor;

    @Before
    public void setUp() {
        mockMetricService = mock(MetricService.class);
        metricAppController = new MetricAppController(mockMetricService);
        listCaptor = ArgumentCaptor.forClass(List.class);
    }

    @Test
    public void test_createMetrics() {
        final Metrics metrics = new Metrics();
        metrics.setNames(Arrays.asList("name1", "name2", "name3"));
        final ResponseEntity<String> response = metricAppController.createMetrics(metrics);
        verify(mockMetricService).createMetrics(metrics);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_createMetrics_emptyMetricName() {
        final Metrics metrics = new Metrics();
        final ResponseEntity<String> response = metricAppController.createMetrics(metrics);
        verifyNoMoreInteractions(mockMetricService);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_addMetricValues() {
        final List<MetricValue> metricValues = new ArrayList<>();
        metricValues.add(new MetricValue("name1", 1.1));
        metricValues.add(new MetricValue("name2", 2.2));
        when(mockMetricService.addMetricValues(metricValues)).thenReturn(new HashSet<String>(Arrays.asList("name1", "name2")));
        final ResponseEntity<String> response = metricAppController.addMetricValues(metricValues);
        verify(mockMetricService).addMetricValues(metricValues);
        Assert.assertTrue(response.getBody().contains("name1"));
        Assert.assertTrue(response.getBody().contains("name2"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void test_addMetricValues_emptyMetricValue() {
        final List<MetricValue> metricValues = new ArrayList<>();
        final ResponseEntity<String> response = metricAppController.addMetricValues(metricValues);
        verifyNoMoreInteractions(mockMetricService);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void test_getStats() throws Exception {
        String json = "{\"min\":1.1,\"max\":2.2,\"mean\":2, \"median\":2.1}";
        when(mockMetricService.getStats("name1")).thenReturn(json);
        when(mockMetricService.getStats("name2")).thenReturn(null);
        final ResponseEntity<String> response1 = metricAppController.getStats("name1");
        final ResponseEntity<String> response2 = metricAppController.getStats("name2");
        Assert.assertEquals(response1.getBody(), json);
        Assert.assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void test_getStats_emptyMetricName() throws Exception {
        final ResponseEntity<String> response = metricAppController.getStats("");
        verifyNoMoreInteractions(mockMetricService);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
