package com.novaordis.series.metric;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class LongHeader extends HeaderBase
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public LongHeader(String label)
    {
        this(label, null, null);
    }

    public LongHeader(String label, String measureUnit)
    {
        this(label, measureUnit, null);
    }

    public LongHeader(String label, Format format)
    {
        this(label, null, format);
    }

    public LongHeader(String label, String measureUnit, Format format)
    {
        super(label, measureUnit, Long.class, format);
    }

    // Header implementation ---------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return getLabel() + "[LONG]";
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



