package com.novaordis.series.metric;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class StringHeader extends HeaderBase
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public StringHeader(String label)
    {
        this(label, null, null);
    }

    public StringHeader(String label, String measureUnit)
    {
        this(label, measureUnit, null);
    }

    public StringHeader(String label, Format format)
    {
        this(label, null, format);
    }

    public StringHeader(String label, String measureUnit, Format format)
    {
        super(label, measureUnit, String.class, format);
    }

    // Header implementation ---------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return getLabel() + "[STRING]";
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



