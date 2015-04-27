package com.novaordis.series.command;

import com.novaordis.series.Command;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class CommandFactory
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    /**
     * @param index - the command name corresponds to the current value of the index.
     *
     * @return null if no known command is identified - we assume the default command
     */
    public static Command createCommand(String[] args, MutableInteger index) throws Exception
    {
        String command = args[index.get()];

        if (Resample.NAME.equals(command))
        {
            return new Resample(args, index);
        }
        else
        {
            // we did not recognize the command, so we assume it's the default command and what
            // we tried to interpret as a command name is actually a file name
            return null;
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
