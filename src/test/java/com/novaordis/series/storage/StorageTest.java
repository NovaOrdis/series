package com.novaordis.series.storage;

import com.novaordis.series.Row;
import com.novaordis.series.UserErrorException;
import com.novaordis.series.mock.MockRow;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public abstract class StorageTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(StorageTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void addLineConstraints_NoLinesWithSameNumber() throws Exception
    {
        Storage s = getStorageToTest();

        Row ml = new MockRow(1, 1000L, "doesn't matter");

        s.add(ml);

        assertEquals(1, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(1000L, s.getEndTime());

        try
        {
            ml = new MockRow(1, 2000L, "something else entirely");
            s.add(ml);
            fail("should fail because a line with same number exists");
        }
        catch(DuplicateLineNumberException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(1, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(1000L, s.getEndTime());
    }

    @Test
    public void addLineConstraints_NoLinesWithSameNumber_MultipleLinesExist() throws Exception
    {
        Storage s = getStorageToTest();

        Row ml = new MockRow(1, 1L, "doesn't matter");

        s.add(ml);

        assertEquals(1, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(1L, s.getEndTime());

        Row m2 = new MockRow(100, 100L, "doesn't matter");

        s.add(m2);

        assertEquals(2, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m3 = new MockRow(50, 50L, "doesn't matter");

        s.add(m3);

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m = null;

        try
        {
            m = new MockRow(1, 10000L, "doesn't matter");
            s.add(m);
            fail("should fail because a line with same number exists");
        }
        catch(DuplicateLineNumberException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        try
        {
            m = new MockRow(50, 10000L, "doesn't matter");
            s.add(m);
            fail("should fail because a line with same number exists");
        }
        catch(DuplicateLineNumberException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        try
        {
            m = new MockRow(100, 10000L, "doesn't matter");
            s.add(m);
            fail("should fail because a line with same number exists");
        }
        catch(DuplicateLineNumberException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m4 = new MockRow(200, 200L, "doesn't matter");

        s.add(m4);

        assertEquals(4, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(200L, s.getEndTime());
    }


    @Test
    public void addLineConstraints_DuplicateTimestamp_OneLine() throws Exception
    {
        Storage s = getStorageToTest();

        Row ml = new MockRow(1, 1L, "something");
        s.add(ml);

        assertEquals(1, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(1L, s.getEndTime());

        try
        {
            ml = new MockRow(2, 1L, "something");
            s.add(ml);
            fail("should fail because a line with same timestamp exists");
        }
        catch(DuplicateTimestampException e)
        {
            log.info(e.getMessage());

            assertEquals(new MockRow(1, 1L, "something"), e.getStoredRow());
        }

        assertEquals(1, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(1L, s.getEndTime());
    }

    @Test
    public void addLineConstraints_DuplicateTimestamp_MultipleLines() throws Exception
    {
        Storage s = getStorageToTest();

        Row ml = new MockRow(1, 1L, "doesn't matter");

        s.add(ml);

        assertEquals(1, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(1L, s.getEndTime());

        Row m2 = new MockRow(100, 100L, "blah100");

        s.add(m2);

        assertEquals(2, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m3 = new MockRow(50, 50L, "doesn't matter");

        s.add(m3);

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m = null;

        try
        {
            m = new MockRow(1000, 1L, "doesn't matter");
            s.add(m);
            fail("should fail because the timestamp is out of sequence");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        try
        {
            m = new MockRow(1000, 50L, "doesn't matter");
            s.add(m);
            fail("should fail because a line with same timestamp exists");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        try
        {
            m = new MockRow(1000, 100L, "doesn't matter");
            s.add(m);
            fail("should fail because a line with same timestamp exists");
        }
        catch(DuplicateTimestampException e)
        {
            log.info(e.getMessage());

            assertEquals(new MockRow(0, 100L, "blah100"), e.getStoredRow());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(100L, s.getEndTime());

        Row m4 = new MockRow(200, 200L, "doesn't matter");

        s.add(m4);

        assertEquals(4, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(200L, s.getEndTime());
    }

    @Test
    public void addLineConstraints_LineBackInTime_Beginning() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = new MockRow(1, 1000L, "");
        s.add(m);

        assertEquals(1, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(1000L, s.getEndTime());

        try
        {
            m = new MockRow(2, 999L, "");
            s.add(m);
            fail("should fail because a line is older that the latest added one");
        }
        catch(UserErrorException e)
        {
            // this is a "user" error, the external storage may come that way
            log.info(e.getMessage());
        }

        assertEquals(1, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(1000L, s.getEndTime());
    }

    @Test
    public void addLineConstraints_LineBackInTime_Middle() throws Exception
    {
        Storage s = getStorageToTest();

        Row ml = new MockRow(1, 1000L, "");
        s.add(ml);

        assertEquals(1, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(1000L, s.getEndTime());

        Row m2 = new MockRow(10, 2000L, "");
        s.add(m2);

        assertEquals(2, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(2000L, s.getEndTime());


        try
        {
            Row m = new MockRow(20, 1500L, "");
            s.add(m);
            fail("should fail because a line is older that the latest added one");
        }
        catch(UserErrorException e)
        {
            // this is a "user" error, the external storage may come that way
            log.info(e.getMessage());
        }

        assertEquals(2, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(2000L, s.getEndTime());
    }

    @Test
    public void addLineConstraints_DuplicateLineInThePast_DifferentTimestamp() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1000L, "");
        s.add(m);
        m = new MockRow(2, 2000L, "");
        s.add(m);
        m = new MockRow(3, 3000L, "");
        s.add(m);

        m = new MockRow(2, 1500L, "");

        try
        {

            s.add(m);
            fail("should fail because a line is older that the latest added one");
        }
        catch(DuplicateLineNumberException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(3000L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(3, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_DuplicateLineInThePast_SameTimestamp() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1000L, "a");
        s.add(m);
        m = new MockRow(2, 2000L, "b");
        s.add(m);
        m = new MockRow(3, 3000L, "c");
        s.add(m);

        m = new MockRow(2, 2000L, "d");

        try
        {

            s.add(m);
            fail("should fail because a line is older that the latest added one");
        }
        catch(DuplicateTimestampException e)
        {
            log.info(e.getMessage());

            assertEquals(new MockRow(-1, 2000L, "b"), e.getStoredRow());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1000L, s.getBeginTime());
        assertEquals(3000L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(3, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_InsertInTheMiddle_TimeLow() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1L, "");
        s.add(m);
        m = new MockRow(2, 2L, "");
        s.add(m);
        m = new MockRow(4, 4L, "");
        s.add(m);

        try
        {
            m = new MockRow(3, 1L, "");
            s.add(m);
            fail("should fail because the newly added line is older than an existing one");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(4L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(4, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_InsertInTheMiddle_SameTimeAsPrevious() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1L, "x");
        s.add(m);
        m = new MockRow(2, 2L, "y");
        s.add(m);
        m = new MockRow(4, 4L, "z");
        s.add(m);

        try
        {
            m = new MockRow(3, 2L, "");
            s.add(m);
            fail("should fail because the newly added line is older than an existing one");
        }
        catch(DuplicateTimestampException e)
        {
            log.info(e.getMessage());

            assertEquals(new MockRow(-1, 2L, "y"), e.getStoredRow());
        }

        assertEquals(3, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(4L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(4, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_InsertInTheMiddleAllGood() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1L, "");
        s.add(m);
        m = new MockRow(2, 2L, "");
        s.add(m);
        m = new MockRow(4, 4L, "");
        s.add(m);
        m = new MockRow(5, 5L, "");
        s.add(m);

        m = new MockRow(3, 3L, "");
        s.add(m);

        assertEquals(5, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(5L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(5, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_InsertInTheMiddle_TimeHigh() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1L, "");
        s.add(m);
        m = new MockRow(2, 2L, "");
        s.add(m);
        m = new MockRow(4, 4L, "");
        s.add(m);
        m = new MockRow(5, 5L, "");
        s.add(m);

        try
        {
            m = new MockRow(3, 10L, "");
            s.add(m);
            fail("should fail, timestamp too high for the position in list");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }

        assertEquals(4, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(5L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(5, s.getMaxLineNumber());
    }

    @Test
    public void addLineConstraints_InsertInTheMiddle_SameTimeAsCurrent() throws Exception
    {
        Storage s = getStorageToTest();

        Row m = null;

        m = new MockRow(1, 1L, "red");
        s.add(m);
        m = new MockRow(2, 2L, "blue");
        s.add(m);
        m = new MockRow(4, 4L, "green");
        s.add(m);
        m = new MockRow(5, 5L, "white");
        s.add(m);

        try
        {
            m = new MockRow(3, 4L, "");
            s.add(m);
            fail("should fail, two lines with the same timestamp");
        }
        catch(DuplicateTimestampException e)
        {
            log.info(e.getMessage());

            assertEquals(new MockRow(-1, 4L, "green"), e.getStoredRow());
        }

        assertEquals(4, s.getLineCount());
        assertEquals(1L, s.getBeginTime());
        assertEquals(5L, s.getEndTime());
        assertEquals(1, s.getMinLineNumber());
        assertEquals(5, s.getMaxLineNumber());
    }

    //
    // iterator tests ----------------------------------------------------------------------------------------------------------------------
    //

    @Test
    public void emptyStorageIterator() throws Exception
    {
        Storage s = getStorageToTest();

        Iterator<Row> i = s.iterator();

        assertFalse(i.hasNext());
    }

    @Test
    public void oneElementStorageIterator() throws Exception
    {
        Storage s = getStorageToTest();

        MockRow mr = new MockRow(1L, 1L, "something");
        s.add(mr);

        Iterator<Row> i = s.iterator();

        assertTrue(i.hasNext());
        assertEquals(mr, i.next());
        assertFalse(i.hasNext());
    }

    @Test
    public void multipleElementsStorageIterator() throws Exception
    {
        Storage s = getStorageToTest();

        MockRow mr = new MockRow(1L, 1L, "something");
        s.add(mr);

        MockRow mr2 = new MockRow(2L, 2L, "something else");
        s.add(mr2);


        Iterator<Row> i = s.iterator();

        assertTrue(i.hasNext());
        assertEquals(mr, i.next());
        assertTrue(i.hasNext());
        assertEquals(mr2, i.next());
        assertFalse(i.hasNext());
    }

    // isEmpty() ---------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testIsEmpty() throws Exception
    {
        Storage s = getStorageToTest();

        assertTrue(s.isEmpty());
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    protected abstract Storage getStorageToTest() throws Exception;

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
