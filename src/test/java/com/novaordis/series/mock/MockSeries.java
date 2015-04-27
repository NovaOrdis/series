package com.novaordis.series.mock;

import com.novaordis.series.Header;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Series;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class MockSeries implements Series
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private long beginTime;
    private long endTime;
    private List<Header> headers;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    public MockSeries(List<Header> headers)
    {
        this(-1L, -1L);
        this.headers = headers;
    }

    public MockSeries(long beginTime, long endTime)
    {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    // Series implementation ---------------------------------------------------------------------------------------------------------------

    @Override
    public String getName()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public long getBeginTime()
    {
        return beginTime;
    }

    @Override
    public long getEndTime()
    {
        return endTime;
    }

    @Override
    public long getCount()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isEmpty()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public List<Header> getHeaders()
    {
        return headers;
    }

    @Override
    public SimpleDateFormat getTimestampFormat()
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void setHeaders(List<Header> headers) throws Exception
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void add(long timestamp, Metric... row) throws Exception
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void add(long timestamp, List<Metric> row) throws Exception
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void add(Row r) throws Exception
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public void setTimestampFormat(SimpleDateFormat f)
    {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    @Override
    public Iterator<Row> iterator()
    {
        List<Row> empty = Collections.emptyList();
        return empty.iterator();
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    public void setBeginTime(long v)
    {
        this.beginTime = v;
    }

    public void setEndTime(long v)
    {
        this.endTime = v;
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------

}
