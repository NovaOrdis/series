package com.novaordis.series.metric;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class MetricFactoryTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(MetricFactoryTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testLongNoHeader() throws Exception
    {
        LongMetric m = (LongMetric)MetricFactory.createMetric("1", null);
        assertEquals(1L, m.getLong());
    }

    @Test
    public void testDoubleNoHeader() throws Exception
    {
        DoubleMetric m = (DoubleMetric)MetricFactory.createMetric("1.0", null);
        assertEquals(1.0D, m.getDouble(), 0.0001);
    }

    @Test
    public void testStringNoHeader() throws Exception
    {
        StringMetric m = (StringMetric)MetricFactory.createMetric("blah", null);
        assertEquals("blah", m.getString());
    }

    @Test
    public void testEmptyMetric() throws Exception
    {
        EmptyMetric m = (EmptyMetric)MetricFactory.createMetric("", null);
        assertNotNull(m);
    }

    @Test
    public void testMultiSpaceMetric() throws Exception
    {
        StringMetric m = (StringMetric)MetricFactory.createMetric("   ", null);
        assertEquals("   ", m.getString());
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



