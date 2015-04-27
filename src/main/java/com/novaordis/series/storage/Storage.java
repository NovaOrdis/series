package com.novaordis.series.storage;

import com.novaordis.series.Row;

import java.util.Iterator;

/**
 * The interface exposed by the components that store Row instances. Row counter starts from 1 - the first line in the file is line 1.
 *
 * Non-successive lines are acceptable - lines may be discarded, contain something else than metrics, be comments, etc.
 *
 * The storage does not know what those Rows are - it does not have the concept of Header. Headers are maintained by the layer above
 * the storage.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public interface Storage
{
    /**
     * The storage implementation must make sure that the timestamp of the row being added is
     * strictly bigger than the timestamp of any stored row that has a smaller row number.
     *
     * At the same time, the timestamp of the row being added is strictly smaller that the
     * timestamp of any stored row that has a bigger row number.
     *
     * The implementation relies on Row implementations correctly implementing equals() and
     * hashCode().
     *
     * @exception DuplicateTimestampException
     * @exception DuplicateLineNumberException
     */
    void add(Row row) throws Exception;

    long getBeginTime();
    long getEndTime();

    long getLineCount();
    boolean isEmpty();

    long getMinLineNumber();
    long getMaxLineNumber();

    Iterator<Row> iterator();
}
