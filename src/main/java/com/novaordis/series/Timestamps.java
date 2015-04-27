package com.novaordis.series;

import java.text.SimpleDateFormat;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Timestamps
{
    // Constants -----------------------------------------------------------------------------------

    private static final SimpleDateFormat[] KNOWN_TIMESTAMP_FORMATS =
        new SimpleDateFormat[]
            {
                // 01/31/13 01:30:59 PM
                new SimpleDateFormat("MM/dd/yy hh:mm:ss a"),
            };

    // Static --------------------------------------------------------------------------------------

    /**
     * @return a known format or null if the given string does not match any known format.
     */
    public static SimpleDateFormat getKnownFormat(String s)
    {
        for(SimpleDateFormat f: KNOWN_TIMESTAMP_FORMATS)
        {
            try
            {
                if (f.parse(s) != null)
                {
                    return f;
                }
            }
            catch(Exception e)
            {
                // fine, doesn't match, go to the next one
            }
        }

        return null;
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    private Timestamps()
    {
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
