package com.novaordis.series;

import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class TimeInterval
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private Long begin;
    private Long end;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public TimeInterval()
    {
    }

    public TimeInterval(long begin, long end)
    {
        this.begin = begin;
        this.end = end;
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    /**
     * If not null, the beginning of the interval in milliseconds. null means there's no beginning, the interval is open ended at the left.
     */
    public Long getBegin()
    {
        return begin;
    }

    /**
     * If not null, the end of the interval in milliseconds. null means there's no end, the interval is open ended at the right.
     */
    public Long getEnd()
    {
        return end;
    }

    public void intersect(Series s)
    {
        if (begin == null)
        {
            begin = s.getBeginTime();
        }
        else
        {
            begin = Math.max(begin, s.getBeginTime());
        }

        if (end == null)
        {
            end = s.getEndTime();
        }
        else
        {
            end = Math.min(end, s.getEndTime());
        }

    }

    public void intersect(List<Series> ss)
    {
        for(Series s: ss)
        {
            intersect(s);
        }
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



