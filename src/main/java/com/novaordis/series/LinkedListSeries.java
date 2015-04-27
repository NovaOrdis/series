package com.novaordis.series;

import com.novaordis.series.storage.LinkedListStorage;
import com.novaordis.series.storage.Storage;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A series created internally, usually the result of processing (re-sampling, merging, etc.). It delegates to an
 * underlying linked-list Storage.
 *
 * @see Series
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LinkedListSeries implements Series
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(LinkedListSeries.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;
    private Storage storage;
    private List<Header> headers;
    private SimpleDateFormat timestampFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * By default, the storage implementation DOES NOT accept duplicate timestamps.
     *
     * @see com.novaordis.series.LinkedListSeries(boolean)
     */
    public LinkedListSeries()
    {
        this(false, (Header[])null);
    }

    /**
     * @param acceptsDuplicateTimestamps if true, the underlying storage will accept adding two or more rows with the
     *                                   same timestamp. If false, the underlying storage implementation will throw
     *                                   DuplicateTimestampException if rows with the same timestamps are detected.
     */
    public LinkedListSeries(boolean acceptsDuplicateTimestamps)
    {
        this(acceptsDuplicateTimestamps, (Header[])null);
    }

    /**
     * By default, the storage implementation DOES NOT accept duplicate timestamps.
     *
     * @see com.novaordis.series.LinkedListSeries(boolean, Header...)
     */
    public LinkedListSeries(Header... headers)
    {
        this(false, headers);
    }

    /**
     * @param acceptsDuplicateTimestamps if true, the underlying storage will accept adding two or more rows with the
     *                                   same timestamp. If false, the underlying storage implementation will throw
     *                                   DuplicateTimestampException if rows with the same timestamps are detected.
     */
    public LinkedListSeries(boolean acceptsDuplicateTimestamps, Header... headers)
    {
        this.headers = new ArrayList<Header>();
        this.storage = new LinkedListStorage(acceptsDuplicateTimestamps);

        if (headers != null)
        {
            Collections.addAll(this.headers, headers);
        }

        log.debug(this + " constructed");
    }

    public LinkedListSeries(Row... rows) throws Exception
    {
        this(false, (Header[])null);

        if (rows != null)
        {
            for(Row r: rows)
            {
                storage.add(r);
            }
        }

        log.debug(this + " constructed");
    }

    // Series implementation -------------------------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public long getBeginTime()
    {
        return storage.getBeginTime();
    }

    @Override
    public long getEndTime()
    {
        return storage.getEndTime();
    }

    @Override
    public long getCount()
    {
        return storage.getLineCount();
    }

    @Override
    public boolean isEmpty()
    {
        return storage.isEmpty();
    }

    /**
     * This implementation returns the underlying list, so handle with care.
     *
     * @see com.novaordis.series.Series#getHeaders()
     */
    @Override
    public List<Header> getHeaders()
    {
        return headers;
    }

    @Override
    public Iterator<Row> iterator()
    {
        return storage.iterator();
    }

    /**
     * @see Series#setHeaders(java.util.List)
     */
    @Override
    public void setHeaders(List<Header> headers) throws Exception
    {
        // check if rows weren't added already
        if (!storage.isEmpty())
        {
            throw new IllegalStateException("cannot install new headers when rows are already present");
        }

        // replace existing
        if (headers == null)
        {
            if (!this.headers.isEmpty())
            {
                this.headers = new ArrayList<Header>();
            }
        }
        else
        {
            this.headers = headers;
        }
    }

    /**
     * @see Series#add(long, Metric...)
     */
    @Override
    public void add(long timestamp, Metric... metrics) throws Exception
    {
        add(timestamp, Arrays.asList(metrics));
    }

    /**
     * @see Series#add(long, java.util.List)
     */
    @Override
    public void add(long timestamp, List<Metric> metrics) throws Exception
    {
        ListRow r = new ListRow(timestamp, null, metrics);
        add(r);
    }

    /**
     * @see Series#add(long, java.util.List)
     */
    @Override
    public void add(Row r) throws Exception
    {
        // do header matching if headers exist

        if (headers != null && !headers.isEmpty())
        {
            ListRow.matchHeaders(headers, r.getMetrics(), null);
        }

        storage.add(r);

        r.setSeries(this);
    }

    @Override
    public SimpleDateFormat getTimestampFormat()
    {
        return timestampFormat;
    }

    @Override
    public void setTimestampFormat(SimpleDateFormat f)
    {
        this.timestampFormat = f;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setName(String name)
    {
        this.name = name;
    }

    public void merge(Series s) throws Exception
    {
        throw new RuntimeException("NOT YET IMPLEMENTED merge(" + s + ")");
    }

    @Override
    public String toString()
    {
        return "" + name + " (" + storage.getLineCount() + ")";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected Storage getStorage()
    {
        return storage;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}



