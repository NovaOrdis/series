package com.novaordis.series.metric;

import com.novaordis.series.Metric;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LongMetric implements Metric
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private long value;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public LongMetric(long l)
    {
        this.value = l;
    }

    public LongMetric(String s) throws NumberFormatException
    {
        this.value = Long.valueOf(s);
    }

    // Metric implementation ---------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isInterpolable()
    {
        return true;
    }

    @Override
    public LongMetric getInterpolationPlaceholder()
    {
        return null;
    }

    @Override
    public Class getType()
    {
        return Long.class;
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

    public long getLong()
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

        if (!(o instanceof LongMetric))
        {
            return false;
        }

        LongMetric that = (LongMetric)o;

        return value == that.value;
    }

    @Override
    public int hashCode()
    {
        int result = 29;
        return result + 17 * (int)value;
    }

    @Override
    public String toString()
    {
        return Long.toString(value);
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
