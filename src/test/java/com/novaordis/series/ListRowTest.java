package com.novaordis.series;

import com.novaordis.series.metric.DoubleHeader;
import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongHeader;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringHeader;
import com.novaordis.series.metric.StringMetric;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class ListRowTest extends RowTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(ListRowTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // header-to-metrics matching tests ----------------------------------------------------------------------------------------------------

    @Test
    public void nullHeaders() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric) new LongMetric(1L), new DoubleMetric(2.0D), new StringMetric("cv"));
        ListRow.matchHeaders(null, metrics, null);
    }

    @Test
    public void emptyHeaders() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(2.0D), new StringMetric("cv"));
        ListRow.matchHeaders(new ArrayList<Header>(), metrics, null);
    }

    @Test
    public void headersMatching() throws Exception
    {
        List<Header> headers = Arrays.asList((Header)new LongHeader("a"), new DoubleHeader("b"), new StringHeader("c"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(2.0D), new StringMetric("cv"));

        ListRow.matchHeaders(headers, metrics, null);
    }

    @Test
    public void headersNotMatching_1() throws Exception
    {

        List<Header> headers = Arrays.asList((Header)new StringHeader("a"), new DoubleHeader("b"), new StringHeader("c"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(2.0D), new StringMetric("cv"));

        try
        {
            ListRow.matchHeaders(headers, metrics, 10L);

            fail("should fail");
        }
        catch(Exception e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void headersNotMatching_2() throws Exception
    {
        List<Header> headers = Arrays.asList((Header)new LongHeader("a"), new DoubleHeader("b"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new StringMetric("cv"));

        try
        {
            ListRow.matchHeaders(headers, metrics, 11L);

            fail("should fail");
        }
        catch(Exception e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void longerHeaderList() throws Exception
    {
        List<Header> headers = Arrays.asList((Header)new LongHeader("a"), new DoubleHeader("b"), new StringHeader("c"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(2.0D));

        // we should be fine, the extra headers are ignored
        ListRow.matchHeaders(headers, metrics, null);
    }

    @Test
    public void longerMetricList() throws Exception
    {
        List<Header> headers = Arrays.asList((Header)new LongHeader("a"), new DoubleHeader("b"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(2.0D), new StringMetric("cv"));

        // we should be fine, the extra metrics are ignored

        ListRow.matchHeaders(headers, metrics, null);
    }

    // EMPTY_METRIC ------------------------------------------------------------------------------------------------------------------------

    @Test
    public void emptyMetric() throws Exception
    {
        List<Header> headers = Arrays.asList((Header)new LongHeader("a"), new DoubleHeader("b"), new StringHeader("c"));
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), Metric.EMPTY_METRIC, new StringMetric("cv"));

        ListRow.matchHeaders(headers, metrics, null);

        // should pass
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected ListRow getRowToTest(long timestamp, Long lineNumber,  List<Metric> metrics) throws Exception
    {
        return new ListRow(timestamp, lineNumber, metrics);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
