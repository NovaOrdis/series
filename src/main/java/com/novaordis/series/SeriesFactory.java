package com.novaordis.series;

import com.novaordis.series.csv.CsvFileSeries;

import java.io.File;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class SeriesFactory
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    public static Series createSeries(File f) throws Exception
    {
        // for the time being we assume CSV, if more is to be supported, here is where
        // the heuristics is to be plugged in

        return new CsvFileSeries(f);
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    private SeriesFactory()
    {
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
