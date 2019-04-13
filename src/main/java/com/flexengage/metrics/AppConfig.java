package com.flexengage.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexengage.metrics.controller.MetricAppController;
import com.flexengage.metrics.daoImpl.MetricsDaoImpl;
import com.flexengage.metrics.helper.Stats;
import com.flexengage.metrics.service.MetricService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    private final Map<String, Stats> metricMap = Collections.synchronizedMap(new HashMap<>());

    @Bean
    public MetricAppController metricAppController() {
        return new MetricAppController(metricService());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MetricService metricService() {
        return new MetricService(metricsDaoImpl(), objectMapper());
    }

    @Bean
    public MetricsDaoImpl metricsDaoImpl() {
        return new MetricsDaoImpl(metricMap);
    }

}
