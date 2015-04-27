package com.novaordis.series.csv;

import com.novaordis.series.Configuration;
import com.novaordis.series.Header;
import com.novaordis.series.Metric;
import com.novaordis.series.Row;
import com.novaordis.series.Series;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class that writes series to streams using a Comma Separated Value formats.
 *
 *
 * Typical usage:
 *
 * <pre>
 *
 *     Series s = ...;
 *
 *     CsvOutput o = new CsvOutput(System.out);
 *
 *     o.write(s);
 *
 *     o.close();
 *
 * </pre>
 *
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public class CsvOutput
{
    // Constants ---------------------------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CsvOutput.class);

    // Static ------------------------------------------------------------------------------------------------------------------------------

    // Attributes --------------------------------------------------------------------------------------------------------------------------

    private BufferedWriter bw;
    private SimpleDateFormat timeFormat;

    // Constructors ------------------------------------------------------------------------------------------------------------------------

    /**
     * Expects the stream to be open. Can be used to write repeatedly to it. The stream must be closed externally when not needed anymore.
     *
     * @exception IllegalArgumentException on invalid stream.
     */
    public CsvOutput(OutputStream os)
    {
        if (os == null)
        {
            throw new IllegalArgumentException("null output stream");
        }

        bw = new BufferedWriter(new OutputStreamWriter(os));
        log.debug(this + " created");
    }

    // Public ------------------------------------------------------------------------------------------------------------------------------

    /**
     * Overloaded method that always writes headers, if available.
     *
     * @see CsvOutput#write(com.novaordis.series.Series, boolean)
     */
    public void write(Series s) throws Exception
    {
        write(s, true);
    }

    /**
     * @param writeHeaders - if true, and the series given as argument has headers, the headers will be written on the stream. If no headers
     *        are available, the argument is ignored. If false, no headers are written even if the series has headers.
     *
     * @throws Exception
     */
    public void write(Series s, boolean writeHeaders) throws Exception
    {
        if (s == null)
        {
            throw new IllegalArgumentException("cannot write a null series");
        }

        List<Header> headers = s.getHeaders();


        if (writeHeaders && !headers.isEmpty())
        {
            // we have headers and we have been instructed to write them
            writeHeaders(headers);
        }

        if (timeFormat == null)
        {
            // initialize it with the timestamp format preferred by the series, and if that is not available, with the default timestamp
            // format

            timeFormat = s.getTimestampFormat();

            if (timeFormat == null)
            {
                timeFormat = Configuration.TIMESTAMP_FORMAT;
            }
        }

        // collect formats, if they exist and prepare them to be sent to the row writing section; we lazy instantiate, so if there
        // are no formats, we don't submit an array, but a null

        Format[] formats = null;

        for(int i = 0; i < headers.size(); i ++)
        {
            Format f = headers.get(i).getFormat();

            if (f != null)
            {
                if (formats == null)
                {
                    // lazy instantiation
                    formats = new Format[headers.size()];
                }

                formats[i] = f;
            }
        }

        Iterator<Row> ri = s.iterator();

        while(ri.hasNext())
        {
            Row r = ri.next();
            writeRow(r, timeFormat, formats);
        }

        // flush the buffered writer upon exit (but not close it, we don't want to close the underlying output stream)
        bw.flush();
    }

    // Package protected -------------------------------------------------------------------------------------------------------------------

    /**
     * Package-exposed for testing.
     *
     * @param timeFormat - must be not null
     *
     * @param formats - the formats to use when writing the row. It may be null, in which case it must be ignored. It may contain null
     *        elements, in which case the corresponding row elements should be converted to string using toString(). It may also contain
     *        less elements than row elements.
     */
    static String rowToString(Row r, SimpleDateFormat timeFormat, Format[] formats)
    {
        StringBuilder sb = new StringBuilder();

        long t = r.getTime();

        sb.append(timeFormat.format(t));

        List<Metric> metrics = r.getMetrics();

        if (metrics != null && !metrics.isEmpty())
        {
            sb.append(", ");

            Iterator<Metric> mi = metrics.iterator();

            int i = 0;

            while(mi.hasNext())
            {
                Metric m = mi.next();

                Format f = null;

                if (formats != null && i < formats.length)
                {
                    f = formats[i];
                }

                sb.append(m.toString(f));

                if (mi.hasNext())
                {
                    sb.append(", ");
                }

                i ++;
            }
        }

        return sb.toString();
    }

    // Protected ---------------------------------------------------------------------------------------------------------------------------

    // Private -----------------------------------------------------------------------------------------------------------------------------

    private void writeHeaders(List<Header> headers) throws Exception
    {
        if (headers == null || headers.isEmpty())
        {
            return;
        }

        StringBuilder sb = new StringBuilder(Configuration.CSV_WRITER_TIME_HEADER_DEFAULT);

        Iterator<Header> hi = headers.iterator();

        while(hi.hasNext())
        {
            sb.append(", ");

            Header h = hi.next();

            sb.append(h.getLabel());
        }

        bw.write(sb.toString());
        bw.newLine();
        bw.flush();
    }

    /**
     * @param timeFormat - must be not null.
     *
     * @param formats - the formats to use when writing the row. It may be null, in which case it must be ignored. It may contain null
     *        elements, in which case the corresponding row elements should be converted to string using toString(). It may also contain
     *        less elements than row elements.
     */
    private void writeRow(Row r, SimpleDateFormat timeFormat, Format[] formats) throws Exception
    {
        String line = rowToString(r, timeFormat, formats);
        bw.write(line);
        bw.newLine();
    }

    // Inner classes -----------------------------------------------------------------------------------------------------------------------
}



