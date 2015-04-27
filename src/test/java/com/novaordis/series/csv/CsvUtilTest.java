package com.novaordis.series.csv;

import com.novaordis.series.Header;
import com.novaordis.series.Metric;
import com.novaordis.series.UserErrorException;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringMetric;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class CsvUtilTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CsvUtilTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    @AfterClass
    public static void scratchCleanup() throws Exception
    {
        Tests.cleanup();
    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // headers tests -----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testNullLine() throws Exception
    {
        try
        {
            CsvUtil.toHeaders(null);
            fail("should fail with IllegalArgumentException");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void emptyLine() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders("");
        assertEquals(0, h.size());
    }

    @Test
    public void spaceFilledLine() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders("     ");
        assertEquals(0, h.size());
    }

    @Test
    public void noComma() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders(" something    ");
        assertEquals(1, h.size());
        assertEquals("something", h.get(0).getName());
    }

    @Test
    public void firstTokenIsTimestamp_NoOtherToken() throws Exception
    {
        try
        {
            CsvUtil.toHeaders("02/08/13 06:42:14 PM");
            fail("should throw exception as the line starts with a recognized timestamp");
        }
        catch(Exception e)
        {
            assertFalse(e instanceof UserErrorException);

            log.info(e.getMessage());
        }
    }

    @Test
    public void firstTokenIsTimestamp_OtherTokensFollow() throws Exception
    {
        try
        {
            CsvUtil.toHeaders("02/08/13 06:42:14 PM, something, something else");
            fail("should throw exception as the line starts with a recognized timestamp");
        }
        catch(Exception e)
        {
            assertFalse(e instanceof UserErrorException);

            log.info(e.getMessage());
        }
    }

    @Test
    public void simplestHeader() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders("time,                 a,  b,  c");

        assertEquals(4, h.size());

        assertEquals("time", h.get(0).getName());

        assertEquals("a", h.get(1).getName());

        assertEquals("b", h.get(2).getName());

        assertEquals("c", h.get(3).getName());
    }

    @Test
    public void emptyHeader() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders("time, ,b,c");

        assertEquals(4, h.size());

        assertEquals("time", h.get(0).getName());

        assertEquals("", h.get(1).getName());

        assertEquals("b", h.get(2).getName());

        assertEquals("c", h.get(3).getName());
    }

    @Test
    public void trailingComma() throws Exception
    {
        List<Header> h = CsvUtil.toHeaders("time, ");

        assertEquals(1, h.size());

        assertEquals("time", h.get(0).getName());
    }

    //
    // parseMetrics() ----------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void simpleLine() throws Exception
    {
        StringTokenizer st = new StringTokenizer("10, 20, 30", ",");

        List<Metric> metrics = CsvUtil.parseMetrics(st, null);

        assertEquals(3, metrics.size());

        assertEquals(new LongMetric(10L), metrics.get(0));
        assertEquals(new LongMetric(20L), metrics.get(1));
        assertEquals(new LongMetric(30L), metrics.get(2));
    }

    // metric tests ------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testThatNoNullsArePermittedAndTheyAreReplacedWithEmptyMetrics() throws Exception
    {
        String s = "a, ,c";

        StringTokenizer st = new StringTokenizer(s, ",");

        List<Metric> metrics = CsvUtil.parseMetrics(st, null);

        assertEquals(3, metrics.size());

        assertEquals(new StringMetric("a"), metrics.get(0));
        assertEquals(Metric.EMPTY_METRIC, metrics.get(1));
        assertEquals(new StringMetric("c"), metrics.get(2));
    }



    //
    // TODO - take the metric type into account when testing equals()
    //



    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
