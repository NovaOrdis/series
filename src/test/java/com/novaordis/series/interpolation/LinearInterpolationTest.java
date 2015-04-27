package com.novaordis.series.interpolation;

import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.ListRow;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Series;
import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongMetric;
import com.novaordis.series.metric.StringMetric;
import com.novaordis.series.mock.MockMetric;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LinearInterpolationTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(LinearInterpolationTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    //
    // resample tests ----------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void resample() throws Exception
    {
        LinkedListSeries in = new LinkedListSeries();

        Row original1 = new ListRow(1000L, null, new LongMetric(1000L), new DoubleMetric(1000.0), new StringMetric("test1"));
        Row original2 = new ListRow(2000L, null, new LongMetric(2000L), new DoubleMetric(2000.0), new StringMetric("test2"));

        in.add(original1);
        in.add(original2);

        Series out = LinearInterpolation.resample(500L, 2500L, 500L, in);

        assertNull(out.getName());
        assertEquals(500L, out.getBeginTime());
        assertEquals(2500L, out.getEndTime());

        assertEquals(5, out.getCount());

        assertTrue(out.getHeaders().isEmpty());

        Iterator<Row> i = out.iterator();

        Row row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(500L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1000L), row.get(0));
        assertEquals(new DoubleMetric(1000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(1000L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1000L), row.get(0));
        assertEquals(new DoubleMetric(1000.0D), row.get(1));
        assertEquals(new StringMetric("test1"), row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(1500L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1500L), row.get(0));
        assertEquals(new DoubleMetric(1500.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2000L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(new StringMetric("test2"), row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2500L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        assertFalse(i.hasNext());
    }

    @Test
    public void resample_MultipleInitialPadding() throws Exception
    {
        LinkedListSeries in = new LinkedListSeries(
                new ListRow(1000L, null, new LongMetric(1000L), new DoubleMetric(1000.0), new StringMetric("test1")),
                new ListRow(2000L, null, new LongMetric(2000L), new DoubleMetric(2000.0), new StringMetric("test2")));

        Series out = LinearInterpolation.resample(800L, 1100L, 100L, in);

        assertEquals(800L, out.getBeginTime());
        assertEquals(1100L, out.getEndTime());

        assertEquals(4, out.getCount());

        Iterator<Row> i = out.iterator();

        Row row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(800L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1000L), row.get(0));
        assertEquals(new DoubleMetric(1000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(900L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1000L), row.get(0));
        assertEquals(new DoubleMetric(1000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(1000L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(1000L), row.get(0));
        assertEquals(new DoubleMetric(1000.0D), row.get(1));
        assertEquals(new StringMetric("test1"), row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(1100L, row.getTime());
        assertEquals(3, row.getLength());
        LongMetric lm = (LongMetric)row.get(0);
        assertEquals(1100L, lm.getLong());
        DoubleMetric dm = (DoubleMetric)row.get(1);
        assertEquals(1100D, dm.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        assertFalse(i.hasNext());
    }

    @Test
    public void resample_MultipleFinalPadding() throws Exception
    {
        LinkedListSeries in = new LinkedListSeries(
                new ListRow(1000L, null, new LongMetric(1000L), new DoubleMetric(1000.0), new StringMetric("test1")),
                new ListRow(2000L, null, new LongMetric(2000L), new DoubleMetric(2000.0), new StringMetric("test2")));

        Series out = LinearInterpolation.resample(1900L, 2300L, 100L, in);

        assertEquals(1900L, out.getBeginTime());
        assertEquals(2300L, out.getEndTime());

        assertEquals(5, out.getCount());

        Iterator<Row> i = out.iterator();

        Row row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(1900L, row.getTime());
        assertEquals(3, row.getLength());
        LongMetric lm = (LongMetric)row.get(0);
        assertEquals(1900L, lm.getLong());
        DoubleMetric dm = (DoubleMetric)row.get(1);
        assertEquals(1900D, dm.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2000L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(new StringMetric("test2"), row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2100L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2200L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        row = i.next();

        assertEquals(out, row.getSeries());
        assertEquals(2300L, row.getTime());
        assertEquals(3, row.getLength());
        assertEquals(new LongMetric(2000L), row.get(0));
        assertEquals(new DoubleMetric(2000.0D), row.get(1));
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, row.get(2));

        assertFalse(i.hasNext());
    }

    //
    // interpolateRow tests ----------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void interpolateRow_differentLengthRows() throws Exception
    {
        Row y1 = new ListRow(10L, null, new LongMetric(10), new DoubleMetric(100.0), new StringMetric("blah"));

        Row y2 = new ListRow(20L, null, new LongMetric(20), new DoubleMetric(200.0));

        try
        {
            LinearInterpolation.interpolateRow(15L, y1, y2);
            fail("should fail because of different length rows");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void interpolateRow() throws Exception
    {
        Row y1 = new ListRow(500L, null, new LongMetric(10), new DoubleMetric(100.0), new StringMetric("blah"));

        Row y2 = new ListRow(1500L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row result = LinearInterpolation.interpolateRow(1000L, y1, y2);

        assertEquals(1000L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(15L, m1.getLong());
        assertEquals(150.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_BothRowsNull() throws Exception
    {
        try
        {
            LinearInterpolation.interpolateRow(500L, null, null);
            fail("should throw IllegalArgumentException");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void interpolateRow_FirstRowNull_x_smaller() throws Exception
    {
        Row y1 = null;

        Row y2 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row result = LinearInterpolation.interpolateRow(500L, y1, y2);

        assertEquals(500L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_FirstRowNull_x_equal() throws Exception
    {
        Row y1 = null;

        Row y2 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row result = LinearInterpolation.interpolateRow(1000L, y1, y2);

        assertEquals(1000L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_FirstRowNull_x_bigger() throws Exception
    {
        Row y1 = null;

        Row y2 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row result = LinearInterpolation.interpolateRow(1500L, y1, y2);

        assertEquals(1500L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_SecondRowNull_x_smaller() throws Exception
    {
        Row y1 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row y2 = null;

        Row result = LinearInterpolation.interpolateRow(500L, y1, y2);

        assertEquals(500L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_SecondRowNull_x_equal() throws Exception
    {
        Row y1 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row y2 = null;

        Row result = LinearInterpolation.interpolateRow(1000L, y1, y2);

        assertEquals(1000L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    @Test
    public void interpolateRow_SecondRowNull_x_bigger() throws Exception
    {
        Row y1 = new ListRow(1000L, null, new LongMetric(20), new DoubleMetric(200.0), new StringMetric("potatoes"));

        Row y2 = null;

        Row result = LinearInterpolation.interpolateRow(1500L, y1, y2);

        assertEquals(1500L, result.getTime());

        List<Metric> metrics = result.getMetrics();

        assertEquals(3, metrics.size());

        LongMetric m1 = (LongMetric)metrics.get(0);
        DoubleMetric m2 = (DoubleMetric)metrics.get(1);
        StringMetric m3 = (StringMetric)metrics.get(2);

        assertEquals(20L, m1.getLong());
        assertEquals(200.0, m2.getDouble(), 0.0001);
        assertEquals(StringMetric.INTERPOLATION_PLACEHOLDER, m3);
    }

    //
    // interpolateScalar tests -------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void interpolateScalar_x1equalsToX2() throws Exception
    {
        try
        {
            LinearInterpolation.interpolateScalar(20L, 10L, new MockMetric(), 10L, new MockMetric());
            fail("should fail, same x");
        }
        catch(ArithmeticException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void interpolateScalar_DifferentTypes() throws Exception
    {
        try
        {
            LinearInterpolation.interpolateScalar(20L, 10L, new LongMetric(10L), 30L, new DoubleMetric(30));
            fail("should fail, metrics have different types");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void interpolateScalar_Long_InsideInterval() throws Exception
    {
        LongMetric m = (LongMetric)LinearInterpolation.interpolateScalar(20L, 10L, new LongMetric(10), 30L, new LongMetric(30L));

        assertEquals(20L, m.getLong());
    }

    @Test
    public void interpolateScalar_Long_IntervalLowerBound() throws Exception
    {
        LongMetric m = (LongMetric)LinearInterpolation.interpolateScalar(10L, 10L, new LongMetric(10), 30L, new LongMetric(30L));

        assertEquals(10L, m.getLong());
    }

    @Test
    public void interpolateScalar_Long_IntervalUpperBound() throws Exception
    {
        LongMetric m = (LongMetric)LinearInterpolation.interpolateScalar(30L, 10L, new LongMetric(10), 30L, new LongMetric(30L));

        assertEquals(30L, m.getLong());
    }

    @Test
    public void interpolateScalar_Long_OutsideInterval() throws Exception
    {
        LongMetric m = (LongMetric)LinearInterpolation.interpolateScalar(50L, 10L, new LongMetric(10), 30L, new LongMetric(30L));

        assertEquals(50L, m.getLong());
    }

    @Test
    public void interpolateScalar_Long_Arbitrary1() throws Exception
    {
        LongMetric m = (LongMetric)LinearInterpolation.interpolateScalar(
            53454353L, 0L, new LongMetric(0L), 85345343943947L, new LongMetric(85345343943947L));

        assertEquals(53454353L, m.getLong());
    }

    @Test
    public void interpolateScalar_Double_InsideInterval() throws Exception
    {
        DoubleMetric m = (DoubleMetric)LinearInterpolation.interpolateScalar(20L, 10L, new DoubleMetric(10.0), 30L, new DoubleMetric(30.0));

        assertEquals(20.0, m.getDouble(), 0.0001);
    }

    @Test
    public void interpolateScalar_Double_IntervalLowerBound() throws Exception
    {
        DoubleMetric m = (DoubleMetric)LinearInterpolation.interpolateScalar(10L, 10L, new DoubleMetric(10.0), 30L, new DoubleMetric(30.0));

        assertEquals(10.0, m.getDouble(), 0.0001);
    }

    @Test
    public void interpolateScalar_Double_IntervalUpperBound() throws Exception
    {
        DoubleMetric m = (DoubleMetric)LinearInterpolation.interpolateScalar(30L, 10L, new DoubleMetric(10.0), 30L, new DoubleMetric(30.0));

        assertEquals(30.0, m.getDouble(), 0.0001);
    }

    @Test
    public void interpolateScalar_Double_OutsideInterval() throws Exception
    {
        DoubleMetric m = (DoubleMetric)LinearInterpolation.interpolateScalar(50L, 10L, new DoubleMetric(10.0), 30L, new DoubleMetric(30.0));

        assertEquals(50.0, m.getDouble(), 0.0001);
    }

    @Test
    public void interpolateScalar_Double_Arbitrary1() throws Exception
    {
        DoubleMetric m = (DoubleMetric)LinearInterpolation.interpolateScalar(
            53454353L, 0L, new DoubleMetric(0.0), 85345343943947L, new DoubleMetric(85345343943947.0));

        assertEquals(53454353.0, m.getDouble(), 0.01);
    }

    @Test
    public void interpolateScalar_String() throws Exception
    {
        try
        {
            LinearInterpolation.interpolateScalar(20L, 10L, new StringMetric("something"), 30L, new StringMetric("something else"));
            fail("should fail, we cannot interpolate string values");
        }
        catch(IllegalArgumentException e)
        {
            log.info(e.getMessage());
        }
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
