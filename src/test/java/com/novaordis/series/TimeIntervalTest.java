package com.novaordis.series;

import com.novaordis.series.metric.LongMetric;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class TimeIntervalTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimeIntervalTest.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void testIntersection() throws Exception
    {
        List<Series> series = new ArrayList<Series>();

        LinkedListSeries s =
                new LinkedListSeries(
                        new ListRow(100L, null, new LongMetric(100L)),
                        new ListRow(500L, null, new LongMetric(500L)));

        LinkedListSeries s2 =
                new LinkedListSeries(
                        new ListRow(300L, null, new LongMetric(300L)),
                        new ListRow(700L, null, new LongMetric(700L)));

        series.add(s);
        series.add(s2);

        TimeInterval i = new TimeInterval();

        i.intersect(series);

        assertEquals(300L, i.getBegin().longValue());
        assertEquals(500L, i.getEnd().longValue());

        log.debug(".");
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}
