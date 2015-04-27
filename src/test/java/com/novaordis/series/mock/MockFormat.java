package com.novaordis.series.mock;

import org.apache.log4j.Logger;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class MockFormat extends Format
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(MockFormat.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private String constant;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public MockFormat(String constant)
    {
        this.constant = constant;
    }

    // Format overrides --------------------------------------------------------------------------------------------------------------------

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        return new StringBuffer(constant);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos)
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



