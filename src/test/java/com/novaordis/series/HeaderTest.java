package com.novaordis.series;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public abstract class HeaderTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(HeaderTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void nullLabel() throws Exception
    {
        try
        {
            getHeaderToTest(null, "s");
            fail("should fail with null illegal argument exception");

        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }

    }

    @Test
    public void label() throws Exception
    {
        Header h = getHeaderToTest("test", null);
        assertEquals("test", h.getName());
        assertEquals("test", h.getLabel());
        assertNull(h.getMeasureUnit());
    }

    @Test
    public void labelAndUnit() throws Exception
    {
        Header h = getHeaderToTest("test", "s");
        assertEquals("test", h.getName());
        assertEquals("test (s)", h.getLabel());
        assertEquals("s", h.getMeasureUnit());
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    protected abstract Header getHeaderToTest(String label, String unit);

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



