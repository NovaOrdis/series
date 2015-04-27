package com.novaordis.series;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Main
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Main.class);

    // Static --------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception
    {
        try
        {
            Configuration c = new Configuration(args);

            List<Series> all = new ArrayList<Series>();

            for(File f: c.getSources())
            {
                Series s = SeriesFactory.createSeries(f);
                all.add(s);
            }

            Command comm = c.getCommand();

            comm.execute(all);
        }
        catch(UserErrorException e)
        {
            log.error(e.getMessage());
        }
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
