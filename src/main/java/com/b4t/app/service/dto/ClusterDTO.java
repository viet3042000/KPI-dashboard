package com.b4t.app.service.dto;

import java.util.Objects;

public class ClusterDTO implements Cloneable {
    private double minValue;
    private double maxValue;

    public ClusterDTO() {
    }


    public ClusterDTO(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public ClusterDTO clone() throws CloneNotSupportedException {
        return (ClusterDTO) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClusterDTO that = (ClusterDTO) o;
        return Double.compare(that.minValue, minValue) == 0 &&
            Double.compare(that.maxValue, maxValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minValue, maxValue);
    }
}
