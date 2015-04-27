package com.novaordis.series.metric;

import com.novaordis.series.HeaderTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class StringHeaderTest extends HeaderTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(StringHeaderTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void type() throws Exception
    {
        StringHeader h = getHeaderToTest("test", null);
        assertEquals(String.class, h.getType());
    }

    @Test
    public void format() throws Exception
    {
        Format f = new DecimalFormat("#0.00");
        StringHeader h = new StringHeader("test", "KM", f);

        log.info(h);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected StringHeader getHeaderToTest(String label, String measureUnit)
    {
        return new StringHeader(label, measureUnit);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



