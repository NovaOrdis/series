package com.novaordis.series.metric;

import com.novaordis.series.Header;
import org.apache.log4j.Logger;

import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
abstract class HeaderBase implements Header
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(HeaderBase.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private String name;
    private String measureUnit;
    private Class type;
    private Format format;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    /**
     * @exception IllegalArgumentException on null name.
     */
    protected HeaderBase(String name, String measureUnit, Class type, Format format)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("null header name");
        }

        this.name = name;
        this.measureUnit = measureUnit;
        this.type = type;
        this.format = format;

        log.debug(this + " constructed");
    }

    // Header implementation ---------------------------------------------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getMeasureUnit()
    {
        return measureUnit;
    }

    @Override
    public String getLabel()
    {
        StringBuilder sb = new StringBuilder(name);

        if (measureUnit != null)
        {
            sb.append(" (").append(measureUnit).append(")");
        }

        return sb.toString();
    }

    @Override
    public Class getType()
    {
        return type;
    }

    @Override
    public Format getFormat()
    {
        return format;
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(getLabel());

        sb.append("[");

        Class type = getType();

        sb.append(type.getName().substring(type.getName().lastIndexOf(".") + 1).toUpperCase());

        sb.append("]");

        sb.append("[");

        if (format == null)
        {
            sb.append("no format");
        }
        else
        {
            sb.append(format.getClass().getName().substring(format.getClass().getName().lastIndexOf(".") + 1));
        }

        sb.append("]");

        return sb.toString();
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



