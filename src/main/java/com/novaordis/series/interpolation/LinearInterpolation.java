package com.novaordis.series.interpolation;

import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.ListRow;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Series;
import com.novaordis.series.metric.DoubleMetric;
import com.novaordis.series.metric.LongMetric;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Linear interpolation logic.
 *
 * TODO: polynomial interpolation, spline interpolation, gaussian interpolation
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LinearInterpolation
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(LinearInterpolation.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    /**
     * Re-samples the given series using a <b>linear interpolation</b> algorithm.
     */
    public static Series resample(long from, long to, long intervalMs, Series s) throws Exception
    {
        LinkedListSeries result = new LinkedListSeries();

        Slider slider = new Slider(s);
        long t = from;

        while(t <= to)
        {
            Row r;

            // advance to the closest samples in the data series

            if (t > slider.getTime())
            {
                // advance in the data series to the newest sample that is older than t

                if (!slider.advance(t))
                {
                    log.debug("reached the end of series");
                }
            }

            if (t == slider.getTime())
            {
                // exact match, no need to compute anything, place the corresponding value directly into result

                r = slider.getRow();
            }
            else
            {
                // slider is ahead of t, this is the expected result of slider.advance(t), so calculate the interpolated value OR
                // slider is behind t, even after an advance attempt - this means we're at the end of the series and we need to pad with
                // the last row values, the interpolation function will do that.
                r = LinearInterpolation.compute(t, slider);
            }

            result.add(r.getTime(), r.getMetrics());

            // advance with to the next sample
            t += intervalMs;
        }

        return result;
    }

    /**
     * Linear interpolation:
     *
     * <pre>
     *    y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
     * </pre>
     *
     * @param x - time value to interpolate for.
     */
    public static Row compute(long x, Slider slider) throws Exception
    {
        // linear interpolation for all interpolable metrics in the line

        Row prev = slider.getPreviousRow();
        Row crt = slider.getRow();

        if (slider.reachedEnd() && x > crt.getTime())
        {
            // pad with the last value instead of continuing to interpolate
            prev = null;
        }

        return interpolateRow(x, prev, crt);
    }

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // Package protected -------------------------------------------------------------------------------------------------------------------

    /**
     * Linear interpolation:
     *
     * <pre>
     *    y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
     * </pre>
     *
     * The current implementation assumes "homogeneous rows" - rows with the same number of metrics and same type of metric for a certain
     * index. Anything that does not comply will trigger an IllegalArgumentException.
     *
     * If any of the input row is null, the result is just the "extension" of the non-null row, with an updated timestamp. Both rows cannot
     * be null, if that happens, the method will throw an IllegalArgumentException.
     *
     * @param x: time value to interpolate for.
     * @param ry1: x1 = y1.getTime(). ry1 may be null.
     * @param ry2: x2 = y2.getTime(). ry2 may be null.
     *
     * @exception IllegalArgumentException if both rows are null or non-null rows have different lengths.
     */
    static Row interpolateRow(long x, Row ry1, Row ry2) throws Exception
    {
        if (ry1 == null && ry2 == null)
        {
            throw new IllegalArgumentException("both rows are null");
        }

        Row sole = ry1 == null ? ry2 : (ry2 == null ? ry1 : null);

        if (sole != null)
        {
            Row result = sole.copy(x);
            // scan the result and replace non-interpolable metrics with placeholders
            List<Metric> ms = result.getMetrics();
            for(int i = 0; i < ms.size(); i ++)
            {
                Metric m = ms.get(i);

                if (!m.isInterpolable())
                {
                    result.get(i, m.getInterpolationPlaceholder());
                }
            }
            return result;
        }

        List<Metric> my1s = ry1.getMetrics();
        List<Metric> my2s = ry2.getMetrics();

        if (my1s.size() != my2s.size())
        {
            throw new IllegalArgumentException("different length rows: " + my1s.size() + " and " + my2s.size());
        }

        int size = my1s.size();

        List<Metric> interpolatedMetrics = new ArrayList<Metric>(size);

        for(int i = 0; i < size; i ++)
        {
            Metric y1 = my1s.get(i);
            Metric y2 = my2s.get(i);

            // make sure the metric can be interpolated
            if (!y1.isInterpolable())
            {
                interpolatedMetrics.add(y1.getInterpolationPlaceholder());
            }
            else
            {
                // we don't care about y2 at this point, the rows are assumed homogeneous, if they're not, the interpolation function will
                // throw an exception

                interpolatedMetrics.add(interpolateScalar(x, ry1.getTime(), y1, ry2.getTime(), y2));
            }
        }

        return new ListRow(x, null, interpolatedMetrics);
    }

    /**
     * Linear interpolation:
     *
     * <pre>
     *    y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
     * </pre>
     *
     * The current implementation assumes "homogeneous metrics", y1 and y2 should be the same type, anything else will trigger an
     * IllegalArgumentException.
     *
     * Package-exposed for testing.
     *
     * @exception IllegalArgumentException if metrics to be interpolated are not homogeneous
     *
     * @exception ArithmeticException if x1 == x2 - cannot interpolate between the same points.
     */
    static Metric interpolateScalar(long x, long x1, Metric y1, long x2, Metric y2) throws Exception
    {
        if (x1 == x2)
        {
            throw new ArithmeticException("attempt to interpolate between two equal x1, x2 (" + x1 + ")");
        }

        if (!y1.getClass().equals(y2.getClass()))
        {
            throw new IllegalArgumentException(
                "attempt to interpolate metrics of different types: " + y1.getClass().getName() + ",  " + y2.getClass().getName());
        }

        // since we're not supporting a large number of types for the time being, just case; if we have to return to this and change the
        // code, come up with something smarter

        if (y1 instanceof LongMetric)
        {
            long ly1 = ((LongMetric)y1).getLong();
            long ly2 = ((LongMetric)y2).getLong();

            long y = ly1 + (long)(((double)ly2 - (double)ly1) * ((double)x - (double)x1) / ((double)x2 - (double)x1));

            return new LongMetric(y);
        }
        else if (y1 instanceof DoubleMetric)
        {
            double dy1 = ((DoubleMetric)y1).getDouble();
            double dy2 = ((DoubleMetric)y2).getDouble();

            double y = dy1 + (dy2 - dy1) * ((double)x - (double)x1) / ((double)x2 - (double)x1);

            return new DoubleMetric(y);
        }
        else
        {
            throw new IllegalArgumentException("cannot interpolate " + y1.getClass().getName() + " values");
        }
    }

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
