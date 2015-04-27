package com.novaordis.series.metric;

import com.novaordis.series.Metric;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public abstract class MetricTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(MetricTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void isInterpolable_getInterpolationPlaceholder_Relationship() throws Exception
    {
        Metric m = getMetricToTest(null);

        if (m.isInterpolable())
        {
            assertNull(m.getInterpolationPlaceholder());
        }
        else
        {
            assertNotNull(m.getInterpolationPlaceholder());
        }
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    /**
     * @param s - null should be a valid option, if null is specified, an arbitrary metric of that type should be returned.
     * @throws Exception
     */
    protected abstract Metric getMetricToTest(String s) throws Exception;

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
