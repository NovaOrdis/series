package com.novaordis.series.metric;

import com.novaordis.series.Metric;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class StringMetric implements Metric
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    public static final StringMetric INTERPOLATION_PLACEHOLDER = new StringMetric("N/A");

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private String value;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public StringMetric(String s)
    {
        this.value = s;
    }

    // Metric implementation ---------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isInterpolable()
    {
        return false;
    }

    @Override
    public StringMetric getInterpolationPlaceholder()
    {
        return INTERPOLATION_PLACEHOLDER;
    }

    @Override
    public Class getType()
    {
        return String.class;
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

    public String getString()
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

        if (value == null)
        {
            return false;
        }

        if (!(o instanceof StringMetric))
        {
            return false;
        }

        StringMetric that = (StringMetric)o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode()
    {
        if (value == null)
        {
            return 0;
        }

        return value.hashCode();
    }

    @Override
    public String toString()
    {
        return value;
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
