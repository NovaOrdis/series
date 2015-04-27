package com.novaordis.series.metric;

import com.novaordis.series.Metric;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class DoubleMetric implements Metric
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private double value;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public DoubleMetric(double v)
    {
        this.value = v;
    }

    public DoubleMetric(String s) throws NumberFormatException
    {
        this.value = Double.valueOf(s);
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
        return Double.class;
    }

    @Override
    public DoubleMetric getInterpolationPlaceholder()
    {
        return null;
    }

    /**
     * TODO this is replicated in all metric implementations - it requires a base class.
     */
    @Override
    public String toString(Format f)
    {
        if (f != null)
        {
            return f.format(value);
        }
        else
        {
            return toString();
        }
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    public double getDouble()
    {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof DoubleMetric))
        {
            return false;
        }

        DoubleMetric that = (DoubleMetric)o;

        return value == that.value;
    }

    @Override
    public int hashCode()
    {
        int result = 31;
        return result + 19 * (int)value;
    }

    @Override
    public String toString()
    {
        return Double.toString(value);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
