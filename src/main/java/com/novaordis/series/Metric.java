package com.novaordis.series;

import com.novaordis.series.metric.EmptyMetric;

import java.text.Format;

/**
 * Make sure the implementing classes implement equals()/hashCode().
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public interface Metric
{
    public static final Metric EMPTY_METRIC = new EmptyMetric();

    boolean isInterpolable();

    /**
     * If the metric instance is not interpolable (the instance returns false when isInterpolable() is invoked on it), then the instance
     * may be queried for an "interpolation placeholder" - a metric of the same type to be injected in interpolation results, so
     * interpolation on rows containing interpolable metrics and non-interpolable metrics won't break.
     *
     * @return an interpolation placeholder for metrics that are not interpolable, or null for interpolable metrics.
     *
     * @see Metric#isInterpolable()
     */
    Metric getInterpolationPlaceholder();

    Class getType();

    /**
     * Create a String representation using the given format. If the format is null, then the method is equivalent with toString().
     */
    String toString(Format f);
}
