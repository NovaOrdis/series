package com.novaordis.series.metric;

import com.novaordis.series.Header;
import com.novaordis.series.Metric;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class MetricFactory
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    public static Metric createMetric(String s, Header h) throws Exception
    {
        // very crude implementation - attempt to convert to Long and if we can't leave it as
        // string

        try
        {
            long value = Long.parseLong(s);
            return new LongMetric(value);
        }
        catch(Exception e)
        {
            // try double

            try
            {
                double value = Double.parseDouble(s);
                return new DoubleMetric(value);
            }
            catch(Exception e2)
            {
                // then it's String, unless it's an EmptyMetric
                if (s.length() == 0)
                {
                    return Metric.EMPTY_METRIC;
                }

                return new StringMetric(s);
            }
        }
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    private MetricFactory()
    {
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
