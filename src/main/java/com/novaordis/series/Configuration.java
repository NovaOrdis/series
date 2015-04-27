package com.novaordis.series;

import com.novaordis.series.command.CommandFactory;
import com.novaordis.series.command.Info;
import com.novaordis.series.command.MutableInteger;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Configuration
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Configuration.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yy/MM/dd hh:mm:ss a");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss,SSS a");

    // this is a timestamp understood by the Excel CSV reader
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("MM/dd/yy hh:mm:ss.SSS");

    public static final String CSV_WRITER_TIME_HEADER_DEFAULT = "Time";

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private List<File> sources;

    private Command command;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public Configuration(String[] args) throws Exception
    {
        sources = new ArrayList<File>();
        parseCommandLine(args);

        log.debug("sources: " + sources);
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    /**
     * @return the source file in the order they were specified on the command line. The files
     *         have been checked to exist and be readable.
     */
    public List<File> getSources()
    {
        return sources;
    }

    public Command getCommand()
    {
        return command;
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    /**
     * Checks the files to exist, be readable and not be directories
     *
     * @exception UserErrorException - error that may be corrected by user action. The message is
     *            human readable and explanatory.
     */
    private void parseCommandLine(String[] args) throws Exception
    {
        for(int i = 0; i < args.length; i ++)
        {
            log.debug("command line argument " + i + ": [" + args[i] + "]");
        }

        StringBuffer spaceContainingArg = null;

        for(MutableInteger i = new MutableInteger(0); i.get() < args.length; i.increment())
        {
            String arg = args[i.get()];

            if (spaceContainingArg != null)
            {
                spaceContainingArg.append(" ");

                if (arg.endsWith("\""))
                {
                    // space containing sequence ends
                    arg = arg.substring(0, arg.length() - 1);
                    arg = trimTrailingBackSlash(arg);
                    spaceContainingArg.append(arg);
                    arg = spaceContainingArg.toString();
                    spaceContainingArg = null;
                }
                else
                {
                    spaceContainingArg.append(trimTrailingBackSlash(arg));
                    continue;
                }
            }
            else if (arg.startsWith("\""))
            {
                // the beginning of an argument containing spaces
                spaceContainingArg = new StringBuffer();
                spaceContainingArg.append(trimTrailingBackSlash(arg.substring(1)));
                continue;
            }

            if (command == null)
            {
                command = CommandFactory.createCommand(args, i);

                if (command != null)
                {
                    continue;
                }

                command = new Info();
            }

            File f = new File(arg);

            if (!f.isFile())
            {
                throw new UserErrorException("file " + f + " does not exist");
            }

            if (!f.canRead())
            {
                throw new UserErrorException("file " + f + " is not readable");
            }

            sources.add(f);
        }
    }

    /**
     * Removes a trailing backslash, if present.
     */
    private String trimTrailingBackSlash(String s)
    {
        if (!s.endsWith("\\"))
        {
            return s;
        }

        return s.substring(0, s.length() - 1);
    }

    // Inner classes -------------------------------------------------------------------------------
}
