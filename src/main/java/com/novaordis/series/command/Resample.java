package com.novaordis.series.command;

import com.novaordis.series.Command;
import com.novaordis.series.Configuration;
import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.Series;
import com.novaordis.series.TimeInterval;
import com.novaordis.series.UserErrorException;
import com.novaordis.series.csv.CsvOutput;
import com.novaordis.series.interpolation.LinearInterpolation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Resample implements Command
{
    // Constants -----------------------------------------------------------------------------------

    /**
     * Default sampling interval (in seconds) in not otherwise specified.
     */
    public static final int DEFAULT_SAMPLING_INTERVAL_SECS = 10;

    public static String NAME = "resample";

    private static final int BEGIN_CONTENT = 0;
    private static final int END_CONTENT = 1;
    private static final int INTERVAL_CONTENT = 2;

    public static final List<SimpleDateFormat> KNOWN_TIMESTAMP_FORMATS =
            Arrays.asList(Configuration.DATE_FORMAT, Configuration.TIME_FORMAT);

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private long beginTime;
    private long endTime;

    // sampling interval in ms
    private long interval;

    // Constructors --------------------------------------------------------------------------------

    /**
     * Exposed for testing.
     */
    Resample()
    {
        beginTime = -1L;
        endTime = -1L;
        interval = DEFAULT_SAMPLING_INTERVAL_SECS * 1000L;
    }

    /**
     * @param index - the command name corresponds to the current value of the index.
     */
    public Resample(String[] args, MutableInteger index) throws Exception
    {
        this();

        if (!NAME.equals(args[index.get()]))
        {
            throw new IllegalArgumentException(
                    "argument " + index.get() + " expected to be " + NAME + " but is " +
                            args[index.get()]);
        }

        if (index.get() == args.length -1)
        {
            // last argument, nothing else to do, return
            return;
        }

        index.increment();

        int expected = -1;
        StringBuffer sb = new StringBuffer();

        for(; index.get() < args.length; index.increment())
        {
            String arg = args[index.get()];

            if (expected == BEGIN_CONTENT)
            {
                sb.append(" ").append(arg);
                beginTime = tryKnownDateFormats(sb);
                if (beginTime != -1)
                {
                    // conversion successful, move on
                    sb.setLength(0);
                    expected = -1;
                }
            }
            else if (expected == END_CONTENT)
            {
                sb.append(" ").append(arg);
                endTime = tryKnownDateFormats(sb);
                if (endTime != -1)
                {
                    // conversion successful, move on
                    sb.setLength(0);
                    expected = -1;
                }
            }
            else if (expected == INTERVAL_CONTENT)
            {
                try
                {
                    interval = Integer.parseInt(arg);
                }
                catch(Exception e)
                {
                    throw new UserErrorException(
                            "--interval followed by invalid integer value \"" + arg + "\"");
                }

                expected = -1;
            }
            else if ("--begin".equals(arg))
            {
                expected = BEGIN_CONTENT;
            }
            else if ("--end".equals(arg))
            {
                expected = END_CONTENT;
            }
            else if ("--interval".equals(arg))
            {
                expected = INTERVAL_CONTENT;
            }
            else if (arg.startsWith("--"))
            {
                throw new UserErrorException("unknown option '" + arg + "'");
            }
            else
            {
                // we advanced a little bit more, go back on step and break
                index.decrement();
                break;
            }
        }

        // sanity checks

        if (expected == BEGIN_CONTENT)
        {
            throw new UserErrorException(
                    "--begin followed by invalid time stamp value: \"" + sb.toString().trim() + "\"");
        }

        if (expected == END_CONTENT)
        {
            throw new UserErrorException(
                    "--end followed by invalid time stamp value: \"" + sb.toString().trim() + "\"");
        }
    }

    // Command implementation ----------------------------------------------------------------------

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public void execute(List<Series> sl) throws Exception
    {
        TimeInterval i = new TimeInterval();

        i.intersect(sl);

        beginTime = i.getBegin();
        endTime = i.getEnd();

        // the actual sampling limits are now stored in beginTime and endTime

        LinkedListSeries result = new LinkedListSeries();

        for(Series s: sl)
        {
            Series r = LinearInterpolation.resample(beginTime, endTime, interval, s);
            result.merge(r);
        }

        FileOutputStream fos = null;

        try
        {

            fos = new FileOutputStream(new File("result.csv"));

            CsvOutput o = new CsvOutput(fos);
            o.write(result);
        }
        finally
        {
            if (fos != null)
            {
                fos.close();
            }
        }
    }

    // Public --------------------------------------------------------------------------------------

    /**
     * @return negative or zero values mean "not defined".
     */
    public long getBeginTime()
    {
        return beginTime;
    }

    /**
     * @return negative or zero values mean "not defined".
     */
    public long getEndTime()
    {
        return endTime;
    }

    /**
     * @return re-sampling interval (in milliseconds). If zero or negative, means "not defined".
     */
    public long getInterval()
    {
        return interval;
    }

    public void setInterval(long siMs)
    {
        this.interval = siMs;
    }

    public List<SimpleDateFormat> getKnownTimestampFormats()
    {
        return KNOWN_TIMESTAMP_FORMATS;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    /**
     * @return a valid timestamp, if able to convert using one known data formats, or -1 if no
     *         conversion is possible.
     */
    private long tryKnownDateFormats(StringBuffer sb)
    {
        String s = sb.toString();

        for(SimpleDateFormat f: getKnownTimestampFormats())
        {
            try
            {
                Date d = f.parse(s);

                if (d != null)
                {
                    return d.getTime();
                }
            }
            catch(Exception e)
            {
                // that is fine, no match
            }
        }

        return -1;
    }

    // Inner classes -------------------------------------------------------------------------------
}
