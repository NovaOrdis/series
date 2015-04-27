package com.novaordis.series.metric;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.text.DecimalFormat;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class DoubleMetricTest extends MetricTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(DoubleMetricTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    //
    // equals() testing---------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void equals() throws Exception
    {
        DoubleMetric m = new DoubleMetric(10.2);

        DoubleMetric m2 = new DoubleMetric(10.2);

        assertTrue(m.equals(m2));

        log.debug(".");
    }

    @Test
    public void notEquals_1() throws Exception
    {
        DoubleMetric m = new DoubleMetric(11.1);

        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(m.equals(new Double(11.1)));
    }

    @Test
    public void notEquals_2() throws Exception
    {
        DoubleMetric m = new DoubleMetric(12.3);

        DoubleMetric m2 = new DoubleMetric(12.4);

        assertFalse(m.equals(m2));
    }

    @Test
    public void getDouble() throws Exception
    {
        DoubleMetric m = new DoubleMetric(45.34);

        assertTrue(45.34 == m.getDouble());
        assertEquals(Double.class, m.getType());
    }

    // toString(Format) --------------------------------------------------------------------------------------------------------------------

    @Test
    public void toStringFormat_NullFormat() throws Exception
    {
        assertEquals("1.1", new DoubleMetric(1.1D).toString(null));
    }

    @Test
    public void toStringFormat_ValidFormat() throws Exception
    {
        assertEquals("003.45", new DoubleMetric(3.449D).toString(new DecimalFormat("000.00")));
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected DoubleMetric getMetricToTest(String s) throws Exception
    {
        return s == null ? new DoubleMetric(7.0) : new DoubleMetric(s);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
