package com.novaordis.series;

import com.novaordis.series.metric.DoubleHeader;
import com.novaordis.series.metric.LongHeader;
import com.novaordis.series.metric.StringHeader;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class LinkedListSeriesTest extends SeriesTest
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testHeadersInConstructor() throws Exception
    {
        Series s = new LinkedListSeries(new StringHeader("a"), new LongHeader("b"), new DoubleHeader("c"));

        List<Header> headers = s.getHeaders();

        assertEquals(3, headers.size());

        assertEquals("a", ((StringHeader)headers.get(0)).getName());
        assertEquals("b", ((LongHeader)headers.get(1)).getName());
        assertEquals("c", ((DoubleHeader)headers.get(2)).getName());

        Iterator<Row> ri = s.iterator();
        assertFalse(ri.hasNext());
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected LinkedListSeries getSeriesToTest(Row... rows) throws Exception
    {
        LinkedListSeries s = new LinkedListSeries();

        for(Row r: rows)
        {
            s.add(r);
        }

        return s;
    }

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
