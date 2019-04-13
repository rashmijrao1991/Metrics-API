package com.flexengage.metrics.controller;

import com.flexengage.metrics.helper.MetricValue;
import com.flexengage.metrics.model.Metrics;
import com.flexengage.metrics.service.MetricService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/metrics")
public class MetricAppController {
    private final MetricService metricService;

    public MetricAppController(final MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createMetrics(@RequestBody Metrics metrics) {
        if (metrics == null || metrics.getNames() == null || metrics.getNames().size() == 0)
            return ResponseEntity.badRequest().body("Invalid metric name");
        metricService.createMetrics(metrics);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/values")
    public ResponseEntity<String> addMetricValues(@RequestBody List<MetricValue> metricValues) {
        if (metricValues == null || metricValues.size() == 0) {
            return ResponseEntity.badRequest().body("Invalid message body");
        }

        final Set<String> metricNames = metricService.addMetricValues(metricValues);
        if (metricNames == null) {
            return ResponseEntity.badRequest().body("Invalid message body");
        }
        return ResponseEntity.ok().body(metricNames.toString());
    }

    @GetMapping(value = "/stats")
    public ResponseEntity<String> getStats(@RequestParam("name") String metricName) throws Exception {
        if (metricName == null || metricName.length() == 0)
            return ResponseEntity.badRequest().body("Invalid metric name");
        final String responseBody = metricService.getStats(metricName);
        if (responseBody == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(responseBody);

    }

}
