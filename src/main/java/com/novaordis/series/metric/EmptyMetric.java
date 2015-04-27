package com.novaordis.series.metric;

import com.novaordis.series.Metric;

import java.text.Format;

/**
 * Use only one instance:
 *
 * Metric.EMPTY_METRIC.
 *
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class EmptyMetric implements Metric
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public EmptyMetric()
    {
    }

    // Metric implementation ---------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isInterpolable()
    {
        return true;
    }

    @Override
    public Class getType()
    {
        return EmptyMetric.class;
    }

    @Override
    public EmptyMetric getInterpolationPlaceholder()
    {
        return null;
    }

    /**
     * TODO this is replicated in all metric implementations - it requires a base class.
     */
    @Override
    public String toString(Format f)
    {
        return toString();
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof EmptyMetric))
        {
            return false;
        }

       return true;
    }

    @Override
    public int hashCode()
    {
        return 11;
    }

    @Override
    public String toString()
    {
        return "";
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
