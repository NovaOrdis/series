package com.novaordis.series;

/**
 * Error that may be corrected by user action. The message should be human readable and explanatory.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class UserErrorException extends Exception
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    public UserErrorException()
    {
        super();
    }

    public UserErrorException(String message)
    {
	super(message);
    }

    public UserErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UserErrorException(Throwable cause)
    {
        super(cause);
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
