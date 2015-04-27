package com.novaordis.series.command;

import com.novaordis.series.Configuration;
import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.ListRow;
import com.novaordis.series.Series;
import com.novaordis.series.UserErrorException;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.mock.MockSeries;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class ResampleCommandTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(ResampleCommandTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    @AfterClass
    public static void scratchCleanup() throws Exception
    {
        Tests.cleanup();
    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    //
    // defaults ----------------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void defaultSamplingInterval()
    {
        Resample r = new Resample();
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());
    }

    //
    // command line tests ------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void noArgumentCommand() throws Exception
    {
        String[] args = toArgArray("resample");
        Configuration c = new Configuration(args);

        Resample r = (Resample)c.getCommand();

        assertTrue(r.getBeginTime() <= 0);
        assertTrue(r.getEndTime() <= 0);
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());
    }

    @Test
    public void noArgumentCommandAndRemnants_OneFile() throws Exception
    {
        Tests.createFile("file1.csv", "");

        String[] args = toArgArray("resample " + Tests.getScratchDir().toString() + "/file1.csv");
        Configuration c = new Configuration(args);

        Resample r = (Resample)c.getCommand();

        assertTrue(r.getBeginTime() <= 0);
        assertTrue(r.getEndTime() <= 0);
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());

        List<File> sources = c.getSources();
        assertEquals(1, sources.size());
        assertEquals(new File(Tests.getScratchDir().toString() + "/file1.csv"), sources.get(0));
    }

    @Test
    public void noArgumentCommandAndRemnants_TwoFiles() throws Exception
    {
        Tests.createFile("file1.csv", "");
        Tests.createFile("file2.csv", "");

        String[] args = toArgArray(
            "resample " +
            Tests.getScratchDir().toString() + "/file1.csv " +
            Tests.getScratchDir().toString() + "/file2.csv");

        Configuration c = new Configuration(args);

        Resample r = (Resample)c.getCommand();

        assertTrue(r.getBeginTime() <= 0);
        assertTrue(r.getEndTime() <= 0);
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());

        List<File> sources = c.getSources();
        assertEquals(2, sources.size());
        assertEquals(new File(Tests.getScratchDir().toString() + "/file1.csv"), sources.get(0));
        assertEquals(new File(Tests.getScratchDir().toString() + "/file2.csv"), sources.get(1));
    }

    @Test
    public void testInvalidBegin() throws Exception
    {
        String cl = "resample --begin blah blah blah" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        try
        {
            CommandFactory.createCommand(args, i);
            fail("should fail, invalid timestamp follows --begin");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void testBeginOnly() throws Exception
    {
        String cl = "resample --begin 2013/02/13 11:24:18 PM somefile.csv" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        Resample r = (Resample)CommandFactory.createCommand(args, i);

        assertEquals(Configuration.DATE_FORMAT.parse("2013/02/13 11:24:18 PM").getTime(),
                     r.getBeginTime());
        assertTrue(r.getEndTime() <= 0);
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());

        assertEquals(4, i.get()); // turn back one index, it will advanced by Configuration again
    }

    @Test
    public void testInvalidEnd() throws Exception
    {
        String cl = "resample --end blah blah blah" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        try
        {
            CommandFactory.createCommand(args, i);
            fail("should fail, invalid timestamp follows --end");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void testEndOnly() throws Exception
    {
        String cl = "resample --end 2013/02/13 11:24:19 PM somefile.csv" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        Resample r = (Resample)CommandFactory.createCommand(args, i);

        assertTrue(r.getBeginTime() <= 0);
        assertEquals(Configuration.DATE_FORMAT.parse("2013/02/13 11:24:19 PM").getTime(),
                     r.getEndTime());
        assertEquals(Resample.DEFAULT_SAMPLING_INTERVAL_SECS * 1000L, r.getInterval());

        assertEquals(4, i.get()); // turn back one index, it will advanced by Configuration again
    }

    @Test
    public void testInvalidInterval() throws Exception
    {
        String cl = "resample --interval blah" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        try
        {
            CommandFactory.createCommand(args, i);
            fail("should fail, invalid integer follows --interval");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void testIntervalOnly() throws Exception
    {
        String cl = "resample --interval 11 somefile.csv" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        Resample r = (Resample)CommandFactory.createCommand(args, i);

        assertTrue(r.getBeginTime() <= 0);
        assertTrue(r.getEndTime() <= 0);
        assertEquals(11, r.getInterval());

        assertEquals(2, i.get()); // turn back one index, it will advanced by Configuration again
    }

    @Test
    public void testCommandLine() throws Exception
    {
        Tests.createFile("file1.csv", "");

        String cl =
            "resample --begin 2013/02/13 11:24:18 PM --end 2013/02/13 11:24:19 PM --interval 12 " +
            Tests.getScratchDir().toString() + "/file1.csv";

        String[] args = toArgArray(cl);

        Configuration c = new Configuration(args);

        Resample r = (Resample)c.getCommand();

        assertEquals(Configuration.DATE_FORMAT.parse("2013/02/13 11:24:18 PM").getTime(),
                     r.getBeginTime());

        assertEquals(Configuration.DATE_FORMAT.parse("2013/02/13 11:24:19 PM").getTime(),
                     r.getEndTime());

        assertEquals(12, r.getInterval());

        List<File> sources = c.getSources();
        assertEquals(1, sources.size());
        assertEquals(new File(Tests.getScratchDir().toString() + "/file1.csv"), sources.get(0));
    }

    @Test
    public void testIntervalOptionLabel() throws Exception
    {
        String cl = "resample --nosuchlabel" ;
        String[] args = toArgArray(cl);
        MutableInteger i = new MutableInteger(0);

        try
        {
            CommandFactory.createCommand(args, i);
            fail("should fail, invalid option");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    //
    // execute tests -----------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void simpleExecute() throws Exception
    {
        Resample r = new Resample();

        List<Series> series = new ArrayList<Series>();

        LinkedListSeries s =
                new LinkedListSeries(
                        new ListRow(100L, null, new LongMetric(100L)),
                        new ListRow(500L, null, new LongMetric(500L)));

        LinkedListSeries s2 =
                new LinkedListSeries(
                        new ListRow(300L, null, new LongMetric(300L)),
                        new ListRow(700L, null, new LongMetric(700L)));

        series.add(s);
        series.add(s2);

        r.execute(series);

        fail("not yet finished, test something");
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    private static String[] toArgArray(String s)
    {
        List<String> l = new ArrayList<String>();

        for(StringTokenizer st = new StringTokenizer(s); st.hasMoreElements(); )
        {
            l.add(st.nextToken());
        }
        String[] args = new String[l.size()];
        return l.toArray(args);
    }

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
