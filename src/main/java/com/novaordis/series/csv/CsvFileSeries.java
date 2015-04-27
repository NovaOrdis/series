package com.novaordis.series.csv;

import com.novaordis.series.LinkedListSeries;
import com.novaordis.series.ListRow;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Timestamps;
import com.novaordis.series.UserErrorException;
import com.novaordis.series.storage.DuplicateTimestampException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A series backed by a CSV File.
 *
 * The first line in the file may contain the headers. This is not required, the file could start with data, and be header-less.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class CsvFileSeries extends LinkedListSeries
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CsvFileSeries.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private File source;
    private boolean dataLine;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    /**
     * There's nothing to close - the constructor takes care internally of temporarily held resources.
     */
    public CsvFileSeries(File f) throws Exception
    {
        super();
        this.source = f;
        parseCsvFile(f);
        setName(extractName(f));
    }

    /**
     * Package protected, used for testing.
     */
    CsvFileSeries() throws Exception
    {
        this(null);
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    public File getSource()
    {
        return source;
    }

    @Override
    public String toString()
    {
        return getName() + "(" + getCount() + ")";
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    private void parseCsvFile(File f) throws Exception
    {
        if (f == null)
        {
            // we're OK, an "empty" series
            log.debug("null file, building an empty series");
            return;
        }

        BufferedReader br = null;

        try
        {
            br = new BufferedReader(new FileReader(f));

            String line = null;
            long lineNumber = 1;

            while((line = br.readLine()) != null)
            {
                parseLine(line, lineNumber ++);
            }
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
        }
    }

    private void parseLine(String line, long lineNumber) throws Exception
    {
        if (!dataLine)
        {
            // attempt to extract headers

            try
            {
                setHeaders(CsvUtil.toHeaders(line));
                // found header, line is well used
                return;
            }
            catch(UserErrorException e)
            {
                // this line seems to be a header line, but there's an error on it
                throw e;
            }
            catch(Exception e)
            {
                // other exceptions are just logged - it is not a header line
                log.debug(this + " does not have a header line, " + e.getMessage());
            }
            finally
            {
                // there is one and only one attempt to extract headers from the main file, if that
                // fails, we won't try it again
                dataLine = true;
            }
        }

        // data line
        parseDataLine(line, lineNumber);
    }

    private void parseDataLine(String dataLine, long lineNumber) throws Exception
    {
        StringTokenizer st = new StringTokenizer(dataLine, ",");

        if (!st.hasMoreTokens())
        {
            // we always need a first token - the timestamp
            throw new UserErrorException(
                getSource() + " row " + lineNumber + ": missing timestamp");
        }

        long ts = handleTimestamp(st.nextToken(), lineNumber);

        List<Metric> metrics = CsvUtil.parseMetrics(st, getHeaders());

        Row r = new ListRow(ts, lineNumber, metrics);

        try
        {
            add(r);
        }
        catch(DuplicateTimestampException e)
        {
            // if the lines have an identical content, issue a warning and go on, otherwise stop the parsing
            Row storedRow = e.getStoredRow();

            if (r.equals(storedRow))
            {
                log.warn(
                    "lines " + storedRow.getLineNumber() + ", " + r.getLineNumber() + " are identical, dropping row " + r.getLineNumber());
                return;
            }

            throw new UserErrorException(
                "lines " + storedRow.getLineNumber() + ", " + r.getLineNumber() + " have the same timestamp (" +
                (getTimestampFormat() == null ? r.getTime() : getTimestampFormat().format(r.getTime())) + ") but different content");
        }
    }

    private String extractName(File f)
    {
        if (f == null)
        {
            return null;
        }

        String result = f.getName();

        // strip off extension

        int i = result.lastIndexOf(".");

        if (i != -1)
        {
            result = result.substring(0, i);
        }

        return result;
    }

    private long handleTimestamp(String timestamp, long lineNumber) throws Exception
    {
        long ts = -1;

        SimpleDateFormat defaultTimeFormat = null;

        if ((defaultTimeFormat = getTimestampFormat()) == null)
        {
            // this is the first data line, establish the format
            SimpleDateFormat f = Timestamps.getKnownFormat(timestamp);

            if (f == null)
            {
                throw new UserErrorException(getSource() + " line " + lineNumber + ": unknown format for timestamp \"" + timestamp + "\"");
            }

            // we're good, we established our timestamp format, so use it to build our line
            setTimestampFormat(f);

            ts = f.parse(timestamp).getTime();
        }
        else
        {
            // use the existing timestamp format to parse the timestamp

            try
            {
                ts = defaultTimeFormat.parse(timestamp).getTime();
            }
            catch(Exception e)
            {
                // not a valid timestamp
                log.debug("failed to parse timestamp", e);
                throw new UserErrorException(
                    getSource() + " line " + lineNumber + ": \"" + timestamp +
                    "\" does not match the format used for timestamps so far");
            }
        }

        return ts;
    }

    // Inner classes -----------------------------------------------------------------------------------------------------------------------

}
