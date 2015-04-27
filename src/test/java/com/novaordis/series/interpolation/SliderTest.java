package com.novaordis.series.interpolation;

import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.mock.MockRow;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class SliderTest extends Assert
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    // Public ------------------------------------------------------------------------------------------------------------------------------

    @Test
    public void emptySeries() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(-1L, s.getTime());
        assertNull(s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(1L));

        assertEquals(-1L, s.getTime());
        assertNull(s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.reachedEnd());
    }

    @Test
    public void oneRowSeries_InvalidAdvanceInThePast() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(999L));
        assertTrue(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());
    }

    @Test
    public void oneRowSeries_AdvanceInPlace() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(1000L));
        assertTrue(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());
    }

    @Test
    public void oneRowSeries_AdvanceInTheFuture() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(2000L));
        assertTrue(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());

        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());
    }

    @Test
    public void twoRowSeries_NormalAdvance() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);
        MockRow mr2 = new MockRow(2L, 2000L, "mock2");
        lls.add(mr2);

        assertEquals(2, lls.getCount());

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(999L));

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertFalse(s.advance(1000L));

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.advance(1001L));

        assertTrue(s.reachedEnd());

        assertEquals(2000L, s.getTime());
        assertEquals(mr2, s.getRow());
        assertEquals(1000L, s.getPreviousTime());
        assertEquals(mr, s.getPreviousRow());

        assertFalse(s.advance(1L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(1999L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2000L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2001L));
        assertTrue(s.reachedEnd());
    }

    @Test
    public void twoRowSeries_AdvanceToNextRow() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);
        MockRow mr2 = new MockRow(2L, 2000L, "mock2");
        lls.add(mr2);

        assertEquals(2, lls.getCount());

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.advance(2000L));

        assertTrue(s.reachedEnd());

        assertEquals(2000L, s.getTime());
        assertEquals(mr2, s.getRow());
        assertEquals(1000L, s.getPreviousTime());
        assertEquals(mr, s.getPreviousRow());

        assertFalse(s.advance(1L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(1999L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2000L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2001L));
        assertTrue(s.reachedEnd());
    }

    @Test
    public void twoRowSeries_AdvanceOverNextRow() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);
        MockRow mr2 = new MockRow(2L, 2000L, "mock2");
        lls.add(mr2);

        assertEquals(2, lls.getCount());

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.advance(2001L));
        assertTrue(s.reachedEnd());


        assertEquals(2000L, s.getTime());
        assertEquals(mr2, s.getRow());
        assertEquals(1000L, s.getPreviousTime());
        assertEquals(mr, s.getPreviousRow());

        assertFalse(s.advance(1L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(1999L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2000L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2001L));
        assertTrue(s.reachedEnd());
    }

    @Test
    public void threeRowSeries_AdvanceOverNextRow() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);
        MockRow mr2 = new MockRow(2L, 2000L, "mock2");
        lls.add(mr2);
        MockRow mr3 = new MockRow(3L, 3000L, "mock3");
        lls.add(mr3);

        assertEquals(3, lls.getCount());

        Slider s = new Slider(lls);

        assertFalse(s.reachedEnd());

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.advance(2001L));

        assertTrue(s.reachedEnd());

        assertEquals(3000L, s.getTime());
        assertEquals(mr3, s.getRow());
        assertEquals(2000L, s.getPreviousTime());
        assertEquals(mr2, s.getPreviousRow());

        assertFalse(s.advance(1L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(2999L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(3000L));
        assertTrue(s.reachedEnd());

        assertFalse(s.advance(3001L));
        assertTrue(s.reachedEnd());
    }

    @Test
    public void threeRowSeries_JumpBeyondEnd() throws Exception
    {
        LinkedListSeries lls = new LinkedListSeries();

        MockRow mr = new MockRow(1L, 1000L, "mock");
        lls.add(mr);
        MockRow mr2 = new MockRow(2L, 2000L, "mock2");
        lls.add(mr2);
        MockRow mr3 = new MockRow(3L, 3000L, "mock3");
        lls.add(mr3);

        assertEquals(3, lls.getCount());

        Slider s = new Slider(lls);

        assertEquals(1000L, s.getTime());
        assertEquals(mr, s.getRow());
        assertEquals(-1L, s.getPreviousTime());
        assertNull(s.getPreviousRow());

        assertTrue(s.advance(10000L));

        assertEquals(3000L, s.getTime());
        assertEquals(mr3, s.getRow());
        assertEquals(2000L, s.getPreviousTime());
        assertEquals(mr2, s.getPreviousRow());

        assertFalse(s.advance(1L));
        assertFalse(s.advance(2999L));
        assertFalse(s.advance(3000L));
        assertFalse(s.advance(3001L));
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
