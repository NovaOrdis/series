package com.novaordis.series.csv;

import com.novaordis.series.Configuration;
import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.ListRow;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Series;
import com.novaordis.series.metric.DoubleHeader;
import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongHeader;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringHeader;
import com.novaordis.series.metric.StringMetric;
import com.novaordis.series.mock.MockFormat;
import com.novaordis.utilities.Files;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class CsvOutputTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CsvOutputTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    private static long timestamp(String s) throws Exception
    {
        return Configuration.TIMESTAMP_FORMAT.parse(s).getTime();
    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @After
    public void scratchCleanup() throws Exception
    {
        Tests.cleanup();
    }

    @Test
    public void nullOutputStream() throws Exception
    {
        try
        {
            new CsvOutput(null);
            fail("should fail, cannot write a directory as a comma-separated values file");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void writeNullSeries() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertEquals(0, baos.size());

        CsvOutput o = new CsvOutput(baos);

        try
        {
            o.write(null);
            fail("should fail with IllegalArgumentException");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }


        assertEquals(0, baos.size());
    }

    @Test
    public void writeHeadersNoData() throws Exception
    {
        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b"), new DoubleHeader("c"));

        assertTrue(s.isEmpty());

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        String hs = Files.read(f).trim();

        assertEquals("Time, a, b, c", hs);
    }

    @Test
    public void writeHeadersAndData_OneLine() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b"), new DoubleHeader("c"));

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;

        assertTrue(st.hasMoreTokens());

        line = st.nextToken();
        assertEquals("Time, a, b, c", line);

        assertTrue(st.hasMoreTokens());

        String expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void writeHeadersAndData_OneLine_HeadersHaveMeasureUnits() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b", "ms"), new DoubleHeader("c", "MB"));

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;

        assertTrue(st.hasMoreTokens());

        line = st.nextToken();
        assertEquals("Time, a, b (ms), c (MB)", line);

        assertTrue(st.hasMoreTokens());

        String expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void writeHeadersAndData_TwoLines() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b"), new DoubleHeader("c"));

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));
        s.add(timestamp("05/01/13 10:10:10.101"),
                Arrays.asList((Metric) new StringMetric("B"), new LongMetric(2), new DoubleMetric(2.2D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        line = st.nextToken();
        assertEquals("Time, a, b, c", line);

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.101")) + ", B, 2, 2.2";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void writeNoHeadersButData_OneLine() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries();

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void writeNoHeadersButData_TwoLines() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries();

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));
        s.add(timestamp("05/01/13 10:10:10.101"),
                Arrays.asList((Metric) new StringMetric("B"), new LongMetric(2), new DoubleMetric(2.2D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.101")) + ", B, 2, 2.2";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void testWriteHeadersTrue_NoHeaders() throws Exception
    {
        Series s = new LinkedListSeries();

        s.add(timestamp("05/01/13 10:10:10.100"), Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s, true);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void testWriteHeadersFalse_HeadersPresent() throws Exception
    {
        // no preferred timestamp format - using default Configuration.TIMESTAMP_FORMAT

        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b"), new DoubleHeader("c"));

        s.add(timestamp("05/01/13 10:10:10.100"), Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s, false);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;

        assertTrue(st.hasMoreTokens());

        String expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    // default and preferred timestamp format ----------------------------------------------------------------------------------------------

    @Test
    public void defaultTimestampFormat() throws Exception
    {
        LinkedListSeries s = new LinkedListSeries();

        assertNull(s.getTimestampFormat());

        s.add(new SimpleDateFormat("MM-dd-yy hh-mm-ss-SSS").parse("05-01-13 01-12-13-145").getTime(),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test3.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        // this is the default Configuration format
        expected = "05/01/13 01:12:13.145, A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    @Test
    public void preferredTimestampFormat() throws Exception
    {
        LinkedListSeries s = new LinkedListSeries();

        SimpleDateFormat preferred = new SimpleDateFormat("yy-MM-dd hh-mm-ss a");

        s.setTimestampFormat(preferred);

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.1D)));

        File f = new File(Tests.getScratchDirectory(), "test4.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;
        String expected;

        assertTrue(st.hasMoreTokens());

        expected = "13-05-01 10-10-10 AM, A, 1, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    // header formats ----------------------------------------------------------------------------------------------------------------------

    @Test
    public void writeHeadersAndData_OneLine_HeadersHaveFormats() throws Exception
    {
        Series s = new LinkedListSeries(
                new StringHeader("a", null, new MockFormat("blah")),
                new LongHeader("b", null, new DecimalFormat("0000")),
                new DoubleHeader("c", null, new DecimalFormat("#.0")));

        s.add(timestamp("05/01/13 10:10:10.100"),
                Arrays.asList((Metric) new StringMetric("A"), new LongMetric(1), new DoubleMetric(1.123D)));

        File f = new File(Tests.getScratchDirectory(), "test5.csv");

        assertFalse(f.exists());

        FileOutputStream fos = new FileOutputStream(f);

        CsvOutput o = new CsvOutput(fos);

        o.write(s);

        fos.close();

        StringTokenizer st = new StringTokenizer(Files.read(f), "\n");
        String line;

        assertTrue(st.hasMoreTokens());

        line = st.nextToken();
        assertEquals("Time, a, b, c", line);

        assertTrue(st.hasMoreTokens());

        String expected = Configuration.TIMESTAMP_FORMAT.format(timestamp("05/01/13 10:10:10.100")) + ", blah, 0001, 1.1";
        line = st.nextToken();
        assertEquals(expected, line);

        assertFalse(st.hasMoreTokens());
    }

    // rowToString() -----------------------------------------------------------------------------------------------------------------------

    @Test
    public void rowToString_nullFormats() throws Exception
    {
        Row r = new ListRow(99L, 1L, new LongMetric(10L));
        String s = CsvOutput.rowToString(r, new SimpleDateFormat("SSS"), null);
        assertEquals("099, 10", s);
    }

    @Test
    public void rowToString_formatArrayContainsNulls() throws Exception
    {
        Row r = new ListRow(99L, 1L, new LongMetric(10L), new StringMetric("blah"), new DoubleMetric(1.1D));
        Format[] formats = new Format[3];

        formats[0] = new DecimalFormat("000");
        formats[1] = null;
        formats[2] = new DecimalFormat("#.000");

        String s = CsvOutput.rowToString(r, new SimpleDateFormat("SSS"), formats);

        assertEquals("099, 010, blah, 1.100", s);
    }

    @Test
    public void rowToString_moreMetricsThanFormats() throws Exception
    {
        Row r = new ListRow(99L, 1L, new LongMetric(10L), new StringMetric("blah"), new DoubleMetric(1.1D));

        Format[] formats = new Format[1];
        formats[0] = new DecimalFormat("000");

        String s = CsvOutput.rowToString(r, new SimpleDateFormat("SSS"), formats);

        assertEquals("099, 010, blah, 1.1", s);
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



