package com.novaordis.series.metric;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.text.DecimalFormat;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LongMetricTest extends MetricTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(LongMetricTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    //
    // equals() testing --------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void equals() throws Exception
    {
        LongMetric m = new LongMetric(10L);

        LongMetric m2 = new LongMetric(10L);

        assertTrue(m.equals(m2));

        log.debug(".");
    }

    @Test
    public void notEquals_1() throws Exception
    {
        LongMetric m = new LongMetric(10L);

        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(m.equals(new Long(10L)));
    }

    @Test
    public void notEquals_2() throws Exception
    {
        LongMetric m = new LongMetric(10L);

        LongMetric m2 = new LongMetric(11L);

        assertFalse(m.equals(m2));
    }

    @Test
    public void getLong() throws Exception
    {
        LongMetric m = new LongMetric(112L);

        assertEquals(112L, m.getLong());
        assertEquals(Long.class, m.getType());
    }

    // toString(Format) --------------------------------------------------------------------------------------------------------------------

    @Test
    public void toStringFormat_NullFormat() throws Exception
    {
        assertEquals("123", new LongMetric(123L).toString(null));
    }

    @Test
    public void toStringFormat_ValidFormat() throws Exception
    {
        assertEquals("00014", new LongMetric(14L).toString(new DecimalFormat("00000")));
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected LongMetric getMetricToTest(String s) throws Exception
    {
        return s == null ? new LongMetric(7L) : new LongMetric(s);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
