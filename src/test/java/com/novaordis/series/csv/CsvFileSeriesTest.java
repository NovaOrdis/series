package com.novaordis.series.csv;

import com.novaordis.series.Configuration;
import com.novaordis.series.Header;
import com.novaordis.series.Row;
import com.novaordis.series.SeriesTest;
import com.novaordis.series.UserErrorException;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class CsvFileSeriesTest extends SeriesTest
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CsvFileSeriesTest.class);

    // Static --------------------------------------------------------------------------------------

    @AfterClass
    public static void scratchCleanup() throws Exception
    {
        Tests.cleanup();
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    @Test
    public void missingTimestampOnDataLine() throws Exception
    {
        File f = Tests.createFile("broken.csv",
                                  "time, \n" +
                                  "\n" +
                                  "02/08/13 01:02:04 PM, 10, 20, 30\n");

        try
        {
            new CsvFileSeries(f);
            fail("should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void invalidTimestampOnFirstDataLine() throws Exception
    {
        File f = Tests.createFile("broken2.csv",
                                  "time, \n" +
                                  "invalid-time-stamp\n" +
                                  "02/08/13 01:02:04 PM, 10, 20, 30\n");

        try
        {
            new CsvFileSeries(f);
            fail("should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void invalidTimestampOnADataLine() throws Exception
    {
        File f = Tests.createFile("broken3.csv",
                                  "time, \n" +
                                  "02/08/13 01:02:04 PM, 10, 20, 30\n" +
                                  "invalid-timestamp, ");

        try
        {
            new CsvFileSeries(f);
            fail("should fail with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void simplestOneLineCsvFile() throws Exception
    {
        File f = Tests.createFile("simplest.csv",
                                  "02/08/13 01:02:03 PM, 1,  2,  3");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals("simplest", s.getName());

        assertEquals(new File(Tests.getScratchDirectory(), "simplest.csv"), s.getSource());

        assertEquals(0, s.getHeaders().size());

        assertEquals(1, s.getCount());

        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getBeginTime());

        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getEndTime());
    }

    @Test
    public void simplestCsvFile() throws Exception
    {
        File f = Tests.createFile("simplest.csv",
                                  "time,                 a,  b,  c\n" +
                                  "02/08/13 01:02:03 PM, 1,  2,  3\n" +
                                  "02/08/13 01:02:04 PM, 10, 20, 30\n");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals("simplest", s.getName());

        assertEquals(new File(Tests.getScratchDirectory(), "simplest.csv"), s.getSource());

        List<Header> h = s.getHeaders();

        assertEquals(4, h.size());

        assertEquals("time", h.get(0).getName());
        assertEquals("a", h.get(1).getName());
        assertEquals("b", h.get(2).getName());
        assertEquals("c", h.get(3).getName());

        assertEquals(2, s.getCount());

        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:04 PM").getTime(),
                     s.getEndTime());
    }

    @Test
    public void csvFile_NoCROnLastLine() throws Exception
    {
        File f = Tests.createFile("1.csv",
                                  "time,                 a,  b,  c\n" +
                                  "02/08/13 01:02:03 PM, 1,  2,  3");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals("1", s.getName());
        assertEquals(new File(Tests.getScratchDirectory(), "1.csv"), s.getSource());

        List<Header> h = s.getHeaders();
        assertEquals(4, h.size());

        assertEquals(1, s.getCount());

        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getEndTime());
    }

    @Test
    public void headerlessCsvFile() throws Exception
    {
        File f = Tests.createFile("1.csv",
                                  "02/08/13 01:02:03 PM, 1,  2,  3");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals("1", s.getName());
        assertEquals(new File(Tests.getScratchDirectory(), "1.csv"), s.getSource());

        List<Header> h = s.getHeaders();

        assertEquals(0, h.size());

        assertEquals(1, s.getCount());
        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("13/02/08 01:02:03 PM").getTime(),
                     s.getEndTime());
    }

    //
    // equal timestamps
    //

    @Test
    public void equalTimestamps_EqualMetrics() throws Exception
    {
        //
        // warning is issue and line is ignored
        //

        File f = Tests.createFile("equal-timestamps-warning.csv",
                                  "01/01/11 01:01:01 PM, 1,  2,  3\n" +
                                  "01/01/11 01:01:01 PM, 1,  2,  3\n" +
                                  "02/02/22 02:02:02 PM, 1,  2,  3");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals("equal-timestamps-warning", s.getName());

        assertEquals(2, s.getCount());

        assertEquals(Configuration.DATE_FORMAT.parse("11/01/01 01:01:01 PM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("22/02/02 02:02:02 PM").getTime(),
                     s.getEndTime());
    }

    @Test
    public void equalTimestamps_DifferentMetrics() throws Exception
    {
        //
        // parsing stops, UserErrorException is thrown
        //

        File f = Tests.createFile("equal-timestamps-warning.csv",
                                  "01/01/11 01:01:01 PM, 1,  2,  3\n" +
                                  "01/01/11 01:01:01 PM, 1,  2,  4\n");
        try
        {
            new CsvFileSeries(f);
            fail("should have failed, two different metric sets with same timestamp");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void equalTimestamps_DifferentMetricCount() throws Exception
    {
        //
        // parsing stops, UserErrorException is thrown
        //

        File f = Tests.createFile("equal-timestamps-warning.csv",
                                  "01/01/11 01:01:01 PM, 1,  2,  3\n" +
                                  "01/01/11 01:01:01 PM, 1,  2,  3, 4\n");
        try
        {
            new CsvFileSeries(f);
            fail("should have failed, two different metric count with same timestamp");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    //
    // miscellanea
    //

    @Test
    public void miscellanea_1() throws Exception
    {
        File f = Tests.createFile(
            "equal-timestamps-warning.csv",
            "02/08/13 04:06:13 AM, 757, 2480, 319\n"+
            "02/08/13 04:06:27 AM, 763, 2485, 319\n"+
            "02/08/13 04:06:27 AM, 763, 2485, 319\n"+
            "02/08/13 04:06:27 AM, 763, 2486, 319\n");

        // last line has same time stamp but different metrics, should trigger a UserErrorException

        try
        {
            new CsvFileSeries(f);
            fail("should fail because last line has same time stamp but different metrics");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void miscellanea_2() throws Exception
    {
        File f = Tests.createFile(
            "equal-timestamps-warning.csv",
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals(1, s.getCount());
        assertEquals(Configuration.DATE_FORMAT.parse("01/01/01 01:01:01 AM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("01/01/01 01:01:01 AM").getTime(),
                     s.getEndTime());
    }

    @Test
    public void miscellanea_3() throws Exception
    {
        File f = Tests.createFile(
            "equal-timestamps-warning.csv",
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1\n"+
            "01/01/01 01:01:01 AM, 1\n" +
            "01/01/01 01:01:02 AM, 2");

        CsvFileSeries s = new CsvFileSeries(f);

        assertEquals(2, s.getCount());
        assertEquals(Configuration.DATE_FORMAT.parse("01/01/01 01:01:01 AM").getTime(),
                     s.getBeginTime());
        assertEquals(Configuration.DATE_FORMAT.parse("01/01/01 01:01:02 AM").getTime(),
                     s.getEndTime());
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected CsvFileSeries getSeriesToTest(Row... rows) throws Exception
    {
        CsvFileSeries result = new CsvFileSeries();

        for(Row r: rows)
        {
            result.add(r);
        }

        return result;
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
