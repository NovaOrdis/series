package com.novaordis.series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A general purpose row, delegating to a List.
 *
 * @see Row
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class ListRow extends RowBase
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    /**
     * Matches headers to the actual row and throws exceptions if there are mismatches.
     *
     * @throws Exception
     */
    public static void matchHeaders(List<Header> headers, List<Metric> metrics, Long lineNumber) throws Exception
    {
        if (headers == null)
        {
            // no headers, ignore
            return;
        }

        Iterator<Header> hi = headers.iterator();
        Iterator<Metric> mi = metrics.iterator();
        int i = 0;

        while(hi.hasNext())
        {
            i ++;

            Header h = hi.next();

            if (!mi.hasNext())
            {
                // if we made it so far, we're good, ignoring the headers we don't have metrics for
                return;
            }

            Metric m = mi.next();

            if (Metric.EMPTY_METRIC.equals(m) || h.getType().equals(m.getType()))
            {
                // we're fine
                continue;
            }

            throw new Exception("metric " + i + " (" + m + ") does not match header " + h +
                    (lineNumber == null ? "" : " on line " + lineNumber));
        }

        // if there are not sufficient headers to match with, we're also fine, we simply ignore the extra metrics

    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private List<Metric> metrics;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public ListRow(long timestamp, Long lineNumber, Metric... metrics)
    {
        this(timestamp, lineNumber, Arrays.asList(metrics));
    }

    public ListRow(long timestamp, Long lineNumber, List<Metric> metrics)
    {
        super(timestamp, lineNumber);

        this.metrics = metrics;

        if (this.metrics == null)
        {
            this.metrics = new ArrayList<Metric>();
        }
    }

    // Row implementation ------------------------------------------------------------------------------------------------------------------

    @Override
    public int getLength()
    {
        if (metrics == null)
        {
            return 0;
        }

        return metrics.size();
    }

    /**
     * Returns the underlying list.
     *
     * @see com.novaordis.series.Row#getMetrics()
     */
    @Override
    public List<Metric> getMetrics()
    {
        return metrics;
    }

    @Override
    public Row copy(long t)
    {
        List<Metric> shallowMetricsCopy = new ArrayList<Metric>(metrics);
        return new ListRow(t, getLineNumber(), shallowMetricsCopy);
    }

    @Override
    public Metric get(int index, Metric m)
    {
        Metric crt = metrics.get(index);

        if (!crt.getClass().equals(m.getClass()))
        {
            throw new IllegalArgumentException("attempt to replace a " + crt.getClass().getName() + " with " + m.getClass().getName());
        }

        return metrics.set(index, m);
    }

    @Override
    public Metric get(int index)
    {
        return metrics.get(index);
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof ListRow))
        {
            return false;
        }

        ListRow that = (ListRow)o;

        if (getTime() != that.getTime())
        {
            return false;
        }

        if (metrics.size() != that.metrics.size())
        {
            return false;
        }

        for(int i = 0; i < metrics.size(); i ++)
        {
            if (!metrics.get(i).equals(that.metrics.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result += getTime() / 7035;

        for(Metric m: metrics)
        {
            result += 19 * m.hashCode();
        }

        return result;
    }

    @Override
    public String toString()
    {
        return getTime() + " " + getMetrics();
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------

}
