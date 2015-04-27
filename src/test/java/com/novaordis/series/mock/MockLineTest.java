package com.novaordis.series.mock;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class MockLineTest extends Assert
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    @Test
    public void equals_identical() throws Exception
    {
        MockRow l1 = new MockRow(1, 1000L, "something");
        MockRow l2 = new MockRow(1, 1000L, "something");

        assertTrue(l1.equals(l2));
    }

    @Test
    public void equals_differentLineNumber() throws Exception
    {
        MockRow l1 = new MockRow(1, 1000L, "something");
        MockRow l2 = new MockRow(2, 1000L, "something");

        assertTrue(l1.equals(l2));
    }

    @Test
    public void notEqual_differentTime() throws Exception
    {
        MockRow l1 = new MockRow(1, 1000L, "something");
        MockRow l2 = new MockRow(1, 1001L, "something");

        assertFalse(l1.equals(l2));
    }

    @Test
    public void notEqual_differentContent() throws Exception
    {
        MockRow l1 = new MockRow(1, 1000L, "something");
        MockRow l2 = new MockRow(1, 1000L, "something else");

        assertFalse(l1.equals(l2));
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
