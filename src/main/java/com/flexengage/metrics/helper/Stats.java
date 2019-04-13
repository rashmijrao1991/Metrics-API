package com.flexengage.metrics.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Stats {
    @JsonIgnore
    private final List<Double> values;
    @JsonIgnore
    private final PriorityQueue<Double> minHeap;
    @JsonIgnore
    private final PriorityQueue<Double> maxHeap;
    private double min;
    private double max;
    private double mean;
    private double median;


    public Stats() {
        values = new ArrayList<>();
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        mean = 0;
        median = -1;
        minHeap = new PriorityQueue();
        maxHeap = new PriorityQueue<>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                if (o1.compareTo(o2) < 0)
                    return 1;
                if (o1.compareTo(o2) > 0)
                    return -1;
                return 0;
            }
        });
    }

    /**
     * Everytime a new value if added, the corresponding statistics for that metric are updated.
     *
     * @param value
     */
    public void addValue(@NotNull final double value) {
        updateMean(value);
        updateMin(value);
        updateMax(value);
        updateMedian(value);
        values.add(value);
    }

    /**
     * min keeps track of the minimum most value seen so far
     *
     * @param value
     */
    private void updateMin(@NotNull final double value) {
        if (min > value) {
            min = value;
        }
    }

    /**
     * max keeps track of the maximum value seen so far
     *
     * @param value
     */
    private void updateMax(@NotNull final double value) {
        if (max < value) {
            max = value;
        }

    }

    /**
     * Use max heap to store the elements in lower half and min heap to store the elements in upper half of the list.
     *
     * @param value
     */
    private void updateMedian(@NotNull final double value) {
        if (minHeap.size() > maxHeap.size()) {
            if (value > median) {
                maxHeap.add(minHeap.poll());
                minHeap.add(value);
            } else {
                maxHeap.add(value);
            }
            median = (minHeap.peek() + maxHeap.peek()) / 2.0;
        } else if (maxHeap.size() > minHeap.size()) {
            if (value < median) {
                minHeap.add(maxHeap.poll());
                maxHeap.add(value);
            } else {
                minHeap.add(value);
            }
            median = (minHeap.peek() + maxHeap.peek()) / 2.0;
        } else {
            if (value > median) {
                minHeap.add(value);
                median = minHeap.peek();
            } else {
                maxHeap.add(value);
                median = maxHeap.peek();
            }
        }
    }

    /**
     * Use previous sum and size to find the current mean.
     * This needs to be called before adding the new value to the list
     *
     * @param value
     */
    private void updateMean(@NotNull final double value) {
        double sum = mean * values.size();
        mean = (sum + value) / (values.size() + 1.0);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getMean() {
        return mean;
    }

    public double getMedian() {
        return median;
    }

    public List<Double> getValues() {
        return values;
    }

}
