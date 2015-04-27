package com.novaordis.series.interpolation;

import com.novaordis.series.Row;
import com.novaordis.series.Series;

import java.util.Iterator;

/**
 * Data structure that helps simulating "sliding" along a data series - helps with the linear interpolation process. It essentially
 * maintains a "current" row and a "previous" row.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Slider
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private Iterator<Row> i;

    private Row prev, crt;

    private boolean reachedEnd;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public Slider(Series s)
    {
        i = s.iterator();

        // position on the first row

        prev = null;

        if (!i.hasNext())
        {
            // empty series
            crt = null;
        }
        else
        {
            crt = i.next();
        }
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    /**
     * The following expression is always true: getRow().getTime() == getTime()
     *
     * @return the time corresponding to the "current" series element.
     *
     * @see com.novaordis.series.interpolation.Slider#getRow()
     */
    public long getTime()
    {
        return crt == null ? -1L : crt.getTime();
    }

    /**
     * The following expression is always true: getRow().getTime() == getTime()
     *
     * @see com.novaordis.series.interpolation.Slider#getRow()
     */
    public Row getRow()
    {
        return crt;
    }

    /**
     * The following expression is always true: getPreviousRow().getTime() == getPreviousTime()
     *
     * @see com.novaordis.series.interpolation.Slider#getPreviousRow()
     */
    public long getPreviousTime()
    {
        return prev == null ? -1L : prev.getTime();
    }

    /**
     * The following expression is always true: getPreviousRow().getTime() == getPreviousTime()
     *
     * @see com.novaordis.series.interpolation.Slider#getPreviousTime()
     */
    public Row getPreviousRow()
    {
        return prev;
    }

    /**
     * Iterate over the series until the current element's time is equal or bigger than t.
     *
     * If t reaches over the upper limit of the series, the slider is positioned on (one-before-last, last).
     *
     * @return true if a change in position actually occurred AND the current element's time is equal or bigger than t. Returns false if
     *         we reached the end of the series and we cannot advance to the next element OR we did advance, but the last element's time is
     *         smaller than t.
     */
    public boolean advance(long t)
    {
        if (crt == null)
        {
            // empty series - cannot advance anywhere
            reachedEnd = true;
            return false;
        }

        if (crt.getTime() >= t)
        {
            // the time we're being told to advance to is in the past or it's the time we're at
            reachedEnd = !i.hasNext();
            return false;
        }

        if (!i.hasNext())
        {
            // cannot advance, no row to advance to
            reachedEnd = true;
            return false;
        }

        boolean moved = false;

        while(i.hasNext())
        {
            prev = crt;

            crt = i.next();

            moved = true;

            if (crt.getTime() >= t)
            {
                if (!i.hasNext())
                {
                    reachedEnd = true;
                }

                return true;
            }
        }

        // we did reach the end of the series, and "advancing" did take place, but the current element's time is not bigger or equal with t
        reachedEnd = true;
        return moved;
    }

    /**
     * In order to return a value different from "false", at least one advance() invocation is required. Upon invoking advance() at least
     * one time, the "reached end" status is updated as follows: if the current row is the last row in the series, the method returns true,
     * otherwise returns false.
     */
    public boolean reachedEnd()
    {
        return reachedEnd;
    }

    @Override
    public String toString()
    {
        return "previous=" + getPreviousTime() + ", time=" + getTime();
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
