package com.novaordis.series.storage;

import com.novaordis.series.Row;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class DuplicateTimestampException extends Exception
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private Row storedRow;

    // Constructors --------------------------------------------------------------------------------

    public DuplicateTimestampException(Row storedRow)
    {
        this(storedRow, null);
    }

    public DuplicateTimestampException(Row storedRow, String s)
    {
        super(s);
        this.storedRow = storedRow;
    }

    // Public --------------------------------------------------------------------------------------

    /**
     * @return the already stored line that created the problem
     */
    public Row getStoredRow()
    {
        return storedRow;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
