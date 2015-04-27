package com.novaordis.series.command;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class MutableInteger
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private int i;

    // Constructors --------------------------------------------------------------------------------

    public MutableInteger(int i)
    {
        this.i = i;
    }

    // Public --------------------------------------------------------------------------------------

    public int get()
    {
        return i;
    }

    public void set(int i)
    {
        this.i = i;
    }

    public void increment()
    {
        i ++;
    }

    public void decrement()
    {
        i --;
    }

    @Override
    public String toString()
    {
        return "" + i;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
