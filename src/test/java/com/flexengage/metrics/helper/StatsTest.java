package com.flexengage.metrics.helper;

import org.junit.Assert;
import org.junit.Test;

public class StatsTest {
    private Stats stats;
    private double[] case1 = {3.5, 1.0, 2, 6.5, 4.123, 7.6, 5};
    private double[] case2 = {3, 0, 2, 6, 4, 7};
    private double[] case3 = {9, 6, 1, 2, 6, 7, 6, 9};
    private double[] case4 = {1};
    private double[] case5 = {0.00000056, 0.00000056};

    @Test
    public void test_case1() {
        stats = new Stats();
        for (double val : case1) {
            stats.addValue(val);
        }
        Assert.assertEquals(1, stats.getMin(), 0.01f);
        Assert.assertEquals(7.6, stats.getMax(), 0.01f);
        Assert.assertEquals(4.123, stats.getMedian(), 0.01f);
        Assert.assertEquals(4.24, stats.getMean(), 0.01f);
    }

    @Test
    public void test_case2() {
        stats = new Stats();
        for (double val : case2) {
            stats.addValue(val);
        }
        Assert.assertEquals(0, stats.getMin(), 0.01f);
        Assert.assertEquals(7, stats.getMax(), 0.01f);
        Assert.assertEquals(3.5, stats.getMedian(), 0.01f);
        Assert.assertEquals(3.66, stats.getMean(), 0.01f);
    }

    @Test
    public void test_case3() {
        stats = new Stats();
        for (double val : case3) {
            stats.addValue(val);
        }
        Assert.assertEquals(1, stats.getMin(), 0.01f);
        Assert.assertEquals(9, stats.getMax(), 0.01f);
        Assert.assertEquals(6, stats.getMedian(), 0.01f);
        Assert.assertEquals(5.75, stats.getMean(), 0.01f);
    }

    @Test
    public void test_case4() {
        stats = new Stats();
        for (double val : case4) {
            stats.addValue(val);
        }
        Assert.assertEquals(1, stats.getMin(), 0.01f);
        Assert.assertEquals(1, stats.getMax(), 0.01f);
        Assert.assertEquals(1, stats.getMedian(), 0.01f);
        Assert.assertEquals(1, stats.getMean(), 0.01f);
    }

    @Test
    public void test_case5() {
        stats = new Stats();
        for (double val : case5) {
            stats.addValue(val);
        }
        Assert.assertEquals(0.00000056, stats.getMin(), 0.00000001f);
        Assert.assertEquals(0.00000056, stats.getMax(), 0.00000001f);
        Assert.assertEquals(0.00000056, stats.getMedian(), 0.00000001f);
        Assert.assertEquals(0.00000056, stats.getMean(), 0.00000001f);
    }

}
