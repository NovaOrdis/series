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
public class DoubleHeaderTest extends HeaderTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(DoubleHeaderTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void type() throws Exception
    {
        DoubleHeader h = getHeaderToTest("test", null);
        assertEquals(Double.class, h.getType());
    }

    @Test
    public void format() throws Exception
    {
        Format f = new DecimalFormat("#0.00");
        DoubleHeader h = new DoubleHeader("test", "KM", f);

        log.info(h);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected DoubleHeader getHeaderToTest(String label, String measureUnit)
    {
        return new DoubleHeader(label, measureUnit);
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



