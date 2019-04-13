package com.flexengage.metrics.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Metrics {
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(@NotNull List<String> names) {
        this.names = names;
    }

}
