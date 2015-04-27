package com.novaordis.series;

import java.util.List;

/**
 * A list of Metric instances, all sharing the same timestamp. Roughly equivalent to a line in a CSV file.
 *
 * A Series contains Metrics instances with the same size and type composition.
 *
 * Note: make sure implementations override equals() and hashCode() so two lines with the same timestamp and set of metrics are "equal".
 * Row number should be ignored.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public interface Row
{
    /**
     * The metrics' timestamp.
     */
    long getTime();

    /**
     * May return null if the row cannot be associated with an external text file line number.
     */
    Long getLineNumber();

    int getLength();

    /**
     * @return never null, possibly an empty list. You will have to consult the implementation's javadoc to find out if a copy or the
     *         underlying list is returned.
     */
    List<Metric> getMetrics();


    /**
     * The series this row belongs to. May return null if the row is not associated with any series. This accessor is used by different
     * row processors that need information about the series while only having access to rows (for example, date formats, etc).
     */
    Series getSeries();

    void setSeries(Series s);

    /**
     * Returns a shallow copy of this row, with a different timestamp (for more details of what is actually copied, see the sub-class
     * implementation documentation. The copy is made even if the timestamp coincides with this instance's timestamp.
     */
    Row copy(long time);

    /**
     * Replaces the metric identified by the corresponding index with the given metric. The replacement metric must have the same type
     * otherwise IllegalArgumentException is thrown.
     *
     * @return the replaced metric.
     *
     * @exception IllegalArgumentException if an attempt to use a metric of different type is made.
     * @exception IndexOutOfBoundsException
     */
    Metric get(int index, Metric m);

    /**
     * @exception IndexOutOfBoundsException
     */
    Metric get(int index);

}
