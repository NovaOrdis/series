package com.novaordis.series.mock;

import com.novaordis.series.Row;
import com.novaordis.series.Metric;
import com.novaordis.series.Series;

import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class MockRow implements Row
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private long timestamp;
    private String pseudoContent;


    private long lineNumber;
    private Series series;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public MockRow(long lineNumber, long timestamp)
    {
        this(lineNumber, timestamp, null, null);
    }

    public MockRow(long lineNumber, long timestamp, String pseudoContent)
    {
        this(lineNumber, timestamp, pseudoContent, null);
    }

    /**
     * @param pseudoContent - the equals()/hashCode() of the pseudocontent will be used in
     *        implementing valid MockRow equals()/hashCode().
     */
    public MockRow(long lineNumber, long timestamp, String pseudoContent, Series series)
    {
        this.lineNumber = lineNumber;
        this.timestamp = timestamp;
        this.pseudoContent = pseudoContent;
        this.series = series;
    }

    // Row implementation ------------------------------------------------------------------------------------------------------------------

    @Override
    public int getLength()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public long getTime()
    {
        return timestamp;
    }

    @Override
    public Long getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public List<Metric> getMetrics()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public Series getSeries()
    {
        return series;
    }

    @Override
    public void setSeries(Series s)
    {
        this.series = s;
    }

    @Override
    public Row copy(long time)
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public Metric get(int index, Metric m)
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public Metric get(int index)
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (pseudoContent == null)
        {
            return false;
        }

        if (!(o instanceof MockRow))
        {
            return false;
        }

        MockRow that = (MockRow)o;

        return timestamp == that.timestamp && pseudoContent.equals(that.pseudoContent);
    }

    @Override
    public int hashCode()
    {
        return (int)timestamp + 17 * (pseudoContent == null ? 0 : pseudoContent.hashCode());
    }

    // Public --------------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return lineNumber + " " + timestamp;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
