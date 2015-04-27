package com.novaordis.series;

import com.novaordis.series.metric.DoubleHeader;
import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongHeader;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringHeader;
import com.novaordis.series.metric.StringMetric;
import com.novaordis.series.mock.MockRow;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public abstract class SeriesTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(SeriesTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    //
    // iterator tests ----------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void iterator_emptySeries() throws Exception
    {
        Series s = getSeriesToTest();

        Iterator<Row> i = s.iterator();

        assertFalse(i.hasNext());

        assertTrue(s.isEmpty());
    }

    @Test
    public void iterator_oneElementSeries() throws Exception
    {
        Series s = getSeriesToTest(new MockRow(1L, 100L));

        Iterator<Row> i = s.iterator();

        assertTrue(i.hasNext());

        Row r = i.next();

        assertEquals(1L, r.getLineNumber().longValue());
        assertEquals(100L, r.getTime());

        assertFalse(i.hasNext());
    }

    @Test
    public void iterator_twoElementSeries() throws Exception
    {
        Series s = getSeriesToTest(new MockRow(1L, 100L), new MockRow(2L, 200L));

        Iterator<Row> i = s.iterator();

        assertTrue(i.hasNext());

        Row r = i.next();

        assertEquals(1L, r.getLineNumber().longValue());
        assertEquals(100L, r.getTime());

        assertTrue(i.hasNext());

        Row r2 = i.next();

        assertEquals(2L, r2.getLineNumber().longValue());
        assertEquals(200L, r2.getTime());

        assertFalse(i.hasNext());
    }

    // setHeaders() tests ------------------------------------------------------------------------------------------------------------------

    @Test
    public void setHeaders() throws Exception
    {
        Series s = getSeriesToTest();

        List<Header> headers = Arrays.asList((Header)new LongHeader("test"));

        s.setHeaders(headers);

        List<Header> headers2 = s.getHeaders();

        assertArrayEquals(headers.toArray(), headers2.toArray());
    }

    @Test
    public void setHeadersNull() throws Exception
    {
        Series s = getSeriesToTest();

        s.setHeaders(null);

        List<Header> headers = s.getHeaders();

        assertTrue(headers.isEmpty());
    }

    // add() tests -------------------------------------------------------------------------------------------------------------------------

    @Test
    public void add_NoHeaders() throws Exception
    {
        Series s = getSeriesToTest();

        List<Metric> metrics = Arrays.asList((Metric)new StringMetric("blas"));

        s.add(1L, metrics);

        Iterator<Row> rows = s.iterator();

        assertTrue(rows.hasNext());

        Row r = rows.next();
        assertFalse(rows.hasNext());
        assertEquals(s, r.getSeries());

        assertEquals(1L, r.getTime());
        List<Metric> ms = r.getMetrics();
        assertEquals(1, ms.size());

        assertEquals(new StringMetric("blas"), ms.get(0));
    }

    @Test
    public void add_NoHeaders_TwoMetrics_FirstLong() throws Exception
    {
        Series s = getSeriesToTest();

        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(10L), new StringMetric("blas"));

        s.add(1L, metrics);

        Iterator<Row> rows = s.iterator();

        assertTrue(rows.hasNext());

        Row r = rows.next();
        assertFalse(rows.hasNext());

        assertEquals(s, r.getSeries());
        assertEquals(1L, r.getTime());
        List<Metric> ms = r.getMetrics();
        assertEquals(2, ms.size());

        assertEquals(new LongMetric(10L), ms.get(0));
        assertEquals(new StringMetric("blas"), ms.get(1));
    }

    @Test
    public void addWithHeaders_TypeMatch_SameLength_Array() throws Exception
    {
        Series s = getSeriesToTest();

        List<Header> headers = Arrays.asList((Header)new LongHeader("A Long"), new DoubleHeader("A Double"), new StringHeader("A String"));

        s.setHeaders(headers);

        s.add(10L, new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("blah"));

        Iterator<Row> ri = s.iterator();

        assertTrue(ri.hasNext());

        Row r = ri.next();
        assertEquals(s, r.getSeries());

        assertFalse(ri.hasNext());

        assertEquals(10L, r.getTime());

        List<Metric> metrics2 = r.getMetrics();

        assertArrayEquals(new Metric[] {new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("blah")}, metrics2.toArray());
    }

    @Test
    public void addWithHeaders_TypeMatch_SameLength_List() throws Exception
    {
        Series s = getSeriesToTest();

        List<Header> headers = Arrays.asList((Header)new LongHeader("A Long"), new DoubleHeader("A Double"), new StringHeader("A String"));

        s.setHeaders(headers);

        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("blah"));

        s.add(10L, metrics);

        Iterator<Row> ri = s.iterator();

        assertTrue(ri.hasNext());

        Row r = ri.next();
        assertEquals(s, r.getSeries());

        assertFalse(ri.hasNext());

        assertEquals(10L, r.getTime());

        List<Metric> metrics2 = r.getMetrics();

        assertArrayEquals(metrics.toArray(), metrics2.toArray());
    }


    @Test
    public void addWithHeaders_TypeMatch_MoreHeaders() throws Exception
    {
        Series s = getSeriesToTest();

        List<Header> headers = Arrays.asList((Header)new LongHeader("A Long"), new DoubleHeader("A Double"), new StringHeader("A String"));

        s.setHeaders(headers);

        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D));

        s.add(10L, metrics);

        Iterator<Row> ri = s.iterator();

        assertTrue(ri.hasNext());

        Row r = ri.next();

        assertFalse(ri.hasNext());

        assertEquals(s, r.getSeries());

        assertEquals(10L, r.getTime());

        List<Metric> metrics2 = r.getMetrics();

        assertArrayEquals(metrics.toArray(), metrics2.toArray());
    }

    @Test
    public void addWithHeaders_Mismatch() throws Exception
    {
        Series s = getSeriesToTest();

        List<Header> headers = Arrays.asList((Header)new LongHeader("A String"));

        s.setHeaders(headers);

        List<Metric> metrics = Arrays.asList((Metric)new DoubleMetric(1.0D));

        try
        {
            s.add(10L, metrics);
            fail("should fail, type mismatch");
        }
        catch(Exception e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void addHeadersToASeriesThatHasRowsAlready() throws Exception
    {
        Row r = new ListRow(1L, 10L, new LongMetric(1L));
        Series s = getSeriesToTest(r);

        List<Header> headers = Arrays.asList((Header)new LongHeader("a"));
        try
        {
            s.setHeaders(headers);
            fail("should fail because there are rows already");
        }
        catch(IllegalStateException e)
        {
            log.info(e.getMessage());
        }
    }

    // timestamp format --------------------------------------------------------------------------------------------------------------------

    @Test
    public void timestampFormat() throws Exception
    {
        Series s = getSeriesToTest(new ListRow(1L, 1L, new LongMetric(1L)));

        assertNull(s.getTimestampFormat());

        s.setTimestampFormat(new SimpleDateFormat("SSS"));

        SimpleDateFormat f = s.getTimestampFormat();

        assertEquals(new SimpleDateFormat("SSS"), f);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    /**
     * The initial population - may be empty. The implementation are free to convert the rows to
     * their native row types.
     */
    protected abstract Series getSeriesToTest(Row... rows) throws Exception;

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
