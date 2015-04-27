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
public class LongHeaderTest extends HeaderTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(LongHeaderTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void type() throws Exception
    {
        LongHeader h = getHeaderToTest("test", null);
        assertEquals(Long.class, h.getType());
    }

    @Test
    public void format() throws Exception
    {
        Format f = new DecimalFormat("#0.00");
        LongHeader h = new LongHeader("test", "KM", f);

        log.info(h);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected LongHeader getHeaderToTest(String label, String measureUnit)
    {
        return new LongHeader(label, measureUnit);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



