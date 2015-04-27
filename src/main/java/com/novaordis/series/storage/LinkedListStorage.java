package com.novaordis.series.storage;

import com.novaordis.series.Row;
import com.novaordis.series.UserErrorException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation is not thread safe, and for the time being, it does not make sense to make it so as the file loading
 * is done serially.
 *
 * @see Storage
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LinkedListStorage implements Storage
{
    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private boolean acceptDuplicateTimestamps;
    private LinkedList<Row> rows;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * By default, the storage implementation DOES NOT accept duplicate timestamps.
     *
     * @see com.novaordis.series.storage.LinkedListStorage#LinkedListStorage(boolean)
     */
    public LinkedListStorage()
    {
        this(false);
    }

    /**
     * @param acceptsDuplicateTimestamps if true, this instance will accept adding two or more rows with the same
     *                                   timestamp. If false, the implementation will throw DuplicateTimestampException
     *                                   if rows with the same timestamps are detected.
     */
    public LinkedListStorage(boolean acceptsDuplicateTimestamps)
    {
        this.acceptDuplicateTimestamps = acceptsDuplicateTimestamps;
        this.rows = new LinkedList<Row>();
    }


    // Storage implementation ------------------------------------------------------------------------------------------

    /**
     * Implementation is optimized for sequential text file parsing - rows are added in a monotonically increasing
     * manner.
     *
     * @see Storage#add(com.novaordis.series.Row)
     *
     * @throws Exception
     */
    @Override
    public void add(Row crt) throws Exception
    {
        if (rows.isEmpty())
        {
            // simply add it and return
            rows.add(crt);
            return;
        }

        Row last = rows.getLast();

        SimpleDateFormat tsf = crt.getSeries() == null ? null : crt.getSeries().getTimestampFormat();

        if (crt.getLineNumber() == null || crt.getLineNumber() > last.getLineNumber())
        {
            // the case we optimize for

            if (crt.getTime() > last.getTime())
            {
                // we're good, constraints are met, add to storage
                rows.add(crt);
            }
            else if (crt.getTime() == last.getTime() )
            {
                if (!acceptDuplicateTimestamps)
                {
                    throw new DuplicateTimestampException(last,
                        "rows " + last.getLineNumber() + " and " + crt.getLineNumber() +
                            " have the same timestamp " +
                            (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()));
                }

                // we're in the situation where we accept multiple rows with the same timestamp, add to storage
                rows.add(crt);
            }
            else
            {
                // current time stamp is smaller than last time stamp
                throw new UserErrorException(
                    "line " + crt.getLineNumber() + " timestamp " +
                        (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) +
                        " falls behind previous line " + last.getLineNumber() + " timestamp " +
                        (tsf != null ? tsf.format(last.getTime()) : last.getTime()));
            }
        }
        else if (crt.getLineNumber() != null && crt.getLineNumber().equals(last.getLineNumber()))
        {
            throw new DuplicateLineNumberException(
                "attempt to add line " + crt.getLineNumber() + " but it seems the line was already added to storage");
        }
        else
        {
            // smaller line number - unusual case, but possible if the parsing is done non-sequentially - go back

            // VERY INEFFICIENT - the performance can be improved with a double-linked list and starting from the last
            // element that was added - but this will work, for the time being

            Row p = null; // previous
            boolean done = false;
            LinkedList<Row> newRows = new LinkedList<Row>();
            for(Iterator<Row> i = rows.iterator(); i.hasNext(); )
            {
                Row c = i.next(); // current within *this* loop

                if (done)
                {
                    newRows.add(c);
                    continue;
                }

                if (c.getLineNumber() < crt.getLineNumber())
                {
                    newRows.add(c);
                    p = c;
                }
                else if (c.getLineNumber() != null && c.getLineNumber().equals(crt.getLineNumber()))
                {
                    // line number already used

                    // compare timestamps - if timestamps are identical, throw "less serious"
                    // DuplicateTimestampException, otherwise throw DuplicateLineNumberException

                    if (c.getTime() == crt.getTime())
                    {
                        throw new DuplicateTimestampException(c,
                            "duplicate line " + crt.getLineNumber() + " with the same timestamp ("
                                + (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) + ")");
                    }
                    else
                    {
                        throw new DuplicateLineNumberException(
                            "attempt to add line " + crt.getLineNumber() +
                                " but it seems the line was already added to storage for timestamp " +
                                (tsf != null ? tsf.format(c.getTime()) : c.getTime()) );
                    }
                }
                else
                {
                    // insert before c, but only if the timestamp is right
                    if (crt.getTime() < p.getTime())
                    {
                        throw new UserErrorException(
                            "line " + crt.getLineNumber() + " has a timestamp (" +
                                (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) +
                                ") that precedes the timestamp of line " + p.getLineNumber() + " (" +
                                (tsf != null ? tsf.format(p.getTime()) : p.getTime()) + ")");
                    }
                    else if (crt.getTime() == p.getTime() && !acceptDuplicateTimestamps)
                    {
                        throw new DuplicateTimestampException(p,
                            "rows " + crt.getLineNumber() + " and " + p.getLineNumber() +
                                " have the same timestamp (" +
                                (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) + ")");
                    }
                    else if (crt.getTime() < c.getTime())
                    {
                        // we're fine, insert
                        newRows.add(crt);
                        newRows.add(c);
                        done = true;
                    }
                    else if (crt.getTime() == c.getTime())
                    {
                        throw new DuplicateTimestampException(c,
                            "rows " + crt.getLineNumber() + " and " + c.getLineNumber() +
                                " have the same timestamp (" +
                                (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) + ")");
                    }
                    else
                    {
                        throw new UserErrorException(
                            "line " + crt.getLineNumber() + " has a timestamp (" +
                                (tsf != null ? tsf.format(crt.getTime()) : crt.getTime()) +
                                ") that succeeds the timestamp of line " + c.getLineNumber() + " (" +
                                (tsf != null ? tsf.format(c.getTime()) : c.getTime()) + ")");
                    }
                }
            }

            // we made it all the way to the end of the list, the new line was inserted, just switch the list - the old
            // one will be GCed
            rows = newRows;
        }
    }

    @Override
    public long getBeginTime()
    {
        return rows.isEmpty() ? -1L : rows.getFirst().getTime();
    }

    @Override
    public long getEndTime()
    {
        return rows.isEmpty() ? -1L : rows.getLast().getTime();
    }

    @Override
    public long getLineCount()
    {
        return rows.size();
    }

    @Override
    public boolean isEmpty()
    {
        return rows.isEmpty();
    }

    @Override
    public long getMinLineNumber()
    {
        return rows.isEmpty() ? -1L : rows.getFirst().getLineNumber();
    }

    @Override
    public long getMaxLineNumber()
    {
        return rows.isEmpty() ? -1L : rows.getLast().getLineNumber();
    }

    @Override
    public Iterator<Row> iterator()
    {
        if (rows == null)
        {
            List<Row> empty = Collections.emptyList();
            return empty.iterator();
        }

        return rows.iterator();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
