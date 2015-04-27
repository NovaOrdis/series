package com.novaordis.series.csv;

import com.novaordis.series.Header;
import com.novaordis.series.Metric;
import com.novaordis.series.metric.LongHeader;
import com.novaordis.series.Timestamps;
import com.novaordis.series.metric.MetricFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class CsvUtil
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    /**
     * @throws com.novaordis.series.UserErrorException - thrown when the line has a chance of being a header line, but some user-correctable
     *         error is found.
     *
     * @throws Exception - thrown when the line cannot be a header line - this usually happens for header-less files.
     * @throws IllegalArgumentException - null input.
     */
    public static List<Header> toHeaders(String line) throws Exception
    {
        if (line == null)
        {
            throw new IllegalArgumentException("null line");
        }

        List<Header> result = new ArrayList<Header>();

        StringTokenizer st = new StringTokenizer(line, ",");

        if (!st.hasMoreTokens())
        {
            // empty header
            return result;
        }

        String first = st.nextToken().trim();

        if (first.length() == 0)
        {
            // emtpy header
            return result;
        }

        if (isTimestamp(first))
        {
            // we're not a header line
            throw new Exception("line starts with time stamp: " + first + ", not a header line");
        }

        Header h = new LongHeader(first);

        result.add(h);

        while(st.hasMoreTokens())
        {
            String label = st.nextToken().trim();

            if (label.length() == 0 && !st.hasMoreTokens())
            {
                break;
            }

            h = new LongHeader(label);
            result.add(h);
        }

        return result;
    }

    /**
     * When parsing, no nulls are allowed in the internal list, use EmptyMetric if necessary.
     *
     * @param headers - may be null, in which case can be (obviously) disregarded.
     *
     * @throws Exception
     */
    public static List<Metric> parseMetrics(StringTokenizer restOfTheLine, List<Header> headers) throws Exception
    {
        List<Metric> metrics = new ArrayList<Metric>();

        Iterator<Header> hi = null;

        if (headers != null)
        {
            hi = headers.iterator();
        }

        while(restOfTheLine.hasMoreTokens())
        {
            String tok = restOfTheLine.nextToken().trim();
            Header h = hi != null && hi.hasNext() ? hi.next() : null;
            Metric m = (MetricFactory.createMetric(tok, h));
            metrics.add(m);
        }

        return metrics;
    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    private static boolean isTimestamp(String token)
    {
        return Timestamps.getKnownFormat(token) != null;
    }

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
