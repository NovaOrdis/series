package com.novaordis.series;

import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringMetric;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public abstract class RowTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(RowTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testStandard() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("m"));

        Row r = getRowToTest(10L, 1L, metrics);

        assertEquals(10l, r.getTime());
        assertEquals(1L, r.getLineNumber().longValue());

        List<Metric> metrics2 = r.getMetrics();
        assertEquals(3, metrics2.size());
        assertEquals(3, r.getLength());

        assertEquals(new LongMetric(1L), metrics2.get(0));
        assertEquals(new DoubleMetric(1.0D), metrics2.get(1));
        assertEquals(new StringMetric("m"), metrics2.get(2));
    }

    @Test
    public void testSimplified() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("m"));

        Row r = getRowToTest(11L, null, metrics);

        assertEquals(11l, r.getTime());
        assertNull(r.getLineNumber());
        assertNull(r.getSeries());
        assertEquals(3, r.getLength());

        List<Metric> metrics2 = r.getMetrics();
        assertEquals(3, metrics2.size());

        assertEquals(new LongMetric(1L), metrics2.get(0));
        assertEquals(new DoubleMetric(1.0D), metrics2.get(1));
        assertEquals(new StringMetric("m"), metrics2.get(2));
    }

    //
    // header-to-metric matching is tested in ListRowTest ----------------------------------------------------------------------------------
    //

    // copy tests --------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCopy_DifferentTime() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);
        assertEquals(3, r.getLength());

        Row r2 = r.copy(2L);
        assertEquals(3, r2.getLength());

        assertEquals(2L, r2.getTime());
        assertEquals(new Long(11L), r2.getLineNumber());
        assertNull(r2.getSeries());

        List<Metric> copy = r2.getMetrics();

        assertTrue(metrics != copy);

        assertEquals(3, copy.size());
        assertEquals(new LongMetric(1L), copy.get(0));
        assertEquals(new DoubleMetric(1.0D), copy.get(1));
        assertEquals(new StringMetric("1"), copy.get(2));
    }

    @Test
    public void testCopy_SameTime() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);
        assertEquals(3, r.getLength());

        Row r2 = r.copy(1L);
        assertEquals(3, r2.getLength());

        assertEquals(1L, r2.getTime());
        assertEquals(new Long(11L), r2.getLineNumber());
        assertNull(r2.getSeries());

        List<Metric> copy = r2.getMetrics();

        assertTrue(metrics != copy);

        assertEquals(3, copy.size());
        assertEquals(new LongMetric(1L), copy.get(0));
        assertEquals(new DoubleMetric(1.0D), copy.get(1));
        assertEquals(new StringMetric("1"), copy.get(2));
    }

    // get() -------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testSetMetric() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);

        Metric rm0 = r.get(0, new LongMetric(2L));
        Metric rm1 = r.get(1, new DoubleMetric(2.0D));
        Metric rm2 = r.get(2, new StringMetric("2"));

        assertEquals(new LongMetric(2L), r.get(0));
        assertEquals(new DoubleMetric(2D), r.get(1));
        assertEquals(new StringMetric("2"), r.get(2));

        assertEquals(new LongMetric(1L), rm0);
        assertEquals(new DoubleMetric(1D), rm1);
        assertEquals(new StringMetric("1"), rm2);
    }

    @Test
    public void testSetMetric_DifferentType() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);

        try
        {
            r.get(0, new DoubleMetric(2D));
            fail("should have failed, metric of different type");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }

        try
        {
            r.get(1, new StringMetric("s"));
            fail("should have failed, metric of different type");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }

        try
        {
            r.get(2, new LongMetric(1L));
            fail("should have failed, metric of different type");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(new LongMetric(1L), r.get(0));
        assertEquals(new DoubleMetric(1D), r.get(1));
        assertEquals(new StringMetric("1"), r.get(2));
    }

    @Test
    public void testSetMetric_IndexOutOfBounds() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);

        try
        {
            r.get(3, new DoubleMetric(2D));
            fail("should have failed, overshot the index");
        }
        catch(IndexOutOfBoundsException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void testGetMetric_IndexOutOfBounds() throws Exception
    {
        List<Metric> metrics = Arrays.asList((Metric)new LongMetric(1L), new DoubleMetric(1.0D), new StringMetric("1"));

        Row r = getRowToTest(1L, 11L, metrics);

        try
        {
            r.get(3);
            fail("should have failed, overshot the index");
        }
        catch(IndexOutOfBoundsException e)
        {
            log.info(e.getMessage());
        }
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    protected abstract Row getRowToTest(long timestamp, Long lineNumber, List<Metric> metrics) throws Exception;

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
