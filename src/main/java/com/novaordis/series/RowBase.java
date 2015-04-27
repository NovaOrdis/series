package com.novaordis.series;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public abstract class RowBase implements Row
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private long time;
    private Long lineNumber;
    private Series series;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    /**
     * @param lineNumber - null is acceptable, it means we're not maintaining line numbers, which is very usual in case of synthetic
     *        series, for example.
     */
    protected RowBase(long time, Long lineNumber)
    {
        this.time = time;
        this.lineNumber = lineNumber;
    }

    // Row implementation ------------------------------------------------------------------------------------------------------------------

    @Override
    public long getTime()
    {
        return time;
    }

    /**
     * May return null.
     */
    @Override
    public Long getLineNumber()
    {
        return lineNumber;
    }

    @Override
    public Series getSeries()
    {
        return series;
    }

    @Override
    public void setSeries(Series s)
    {
        this.series = s;
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



