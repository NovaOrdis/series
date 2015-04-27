package com.novaordis.series.command;

import com.novaordis.series.Command;
import com.novaordis.series.Configuration;
import com.novaordis.series.Series;

import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Info implements Command
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Command implementation ----------------------------------------------------------------------

    @Override
    public String getName()
    {
        return "info";
    }

    @Override
    public void execute(List<Series> series) throws Exception
    {
        for(Series s: series)
        {
            stdout(s.getName() + " starts at " +
                   Configuration.DATE_FORMAT.format(s.getBeginTime()) + ", ends at " +
                   Configuration.DATE_FORMAT.format(s.getEndTime()) + " and contains " +
                   s.getCount() + " samples");
        }
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    private static void stdout(String s)
    {
        System.out.println(s);
    }

    // Inner classes -------------------------------------------------------------------------------
}
