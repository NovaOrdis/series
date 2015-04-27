package com.novaordis.series.metric;

import com.novaordis.series.mock.MockFormat;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class StringMetricTest extends MetricTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(StringMetricTest.class);

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
        StringMetric m = new StringMetric("a");

        StringMetric m2 = new StringMetric("a");

        assertTrue(m.equals(m2));

        log.debug(".");
    }

    @Test
    public void notEquals_1() throws Exception
    {
        StringMetric m = new StringMetric("a");

        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(m.equals("a"));
    }

    @Test
    public void notEquals_2() throws Exception
    {
        StringMetric m = new StringMetric("a");

        StringMetric m2 = new StringMetric("b");

        assertFalse(m.equals(m2));
    }

    @Test
    public void getString() throws Exception
    {
        StringMetric m = new StringMetric("a");

        assertEquals("a", m.getString());
        assertEquals(String.class, m.getType());
    }

    // toString(Format) --------------------------------------------------------------------------------------------------------------------

    @Test
    public void toStringFormat_NullFormat() throws Exception
    {
        assertEquals("a", new StringMetric("a").toString(null));
    }

    @Test
    public void toStringFormat_ValidFormat() throws Exception
    {
        assertEquals("blah", new StringMetric("a").toString(new MockFormat("blah")));
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected StringMetric getMetricToTest(String s) throws Exception
    {
        return s == null ? new StringMetric("test") : new StringMetric(s);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
