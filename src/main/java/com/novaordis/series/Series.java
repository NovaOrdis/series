package com.novaordis.series;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

/**
 * A list of timestamped lists of metrics.
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public interface Series
{
    // Accessors -------------------------------------------------------------------------------------------------------

    String getName();

    long getBeginTime();
    long getEndTime();

    /**
     * @return the number of rows in the series.
     */
    long getCount();

    boolean isEmpty();

    /**
     * If the series is header-less, it will return an "empty" headers instance.
     *
     * If a non-empty list is returned, it DOES NOT include a header for the row's timestamp, which is always present
     * at the beginning of the row and it is handled differently than the rest of the metrics in the row.
     *
     * It is up to the implementation to return the underlying list or a copy; consult the implementation documentation
     * for more details.
     */
    List<Header> getHeaders();

    Iterator<Row> iterator();

    /**
     * The output timestamp format for this series. If the series was loaded from external storage, this method usually
     * returns the original timestamp format. The implementations are encouraged to build log messages that include time
     * stamps using this format. May return null (no default timestamp format), in which case
     * Configuration.TIMESTAMP_FORMAT will be used.
     */
    SimpleDateFormat getTimestampFormat();

    // Mutators --------------------------------------------------------------------------------------------------------

    /**
     * @throws IllegalStateException thrown if the series has rows already. New headers cannot be installed if rows were
     * previously added to the series, because the existing rows might not match the headers, resulting into a invalid
     * hstate series.
     */
    void setHeaders(List<Header> headers) throws Exception;

    /**
     * If headers exist, the metrics will be matched against headers.
     *
     * @throws Exception on type mismatch between headers (if present) and the row elements.
     */
    void add(long timestamp, Metric... metrics) throws Exception;

    /**
     * If headers exist, the metrics will be matched against headers.
     *
     * @throws Exception on type mismatch between headers (if present) and the row elements.
     */
    void add(long timestamp, List<Metric> metrics) throws Exception;

    /**
     * If headers exist, the metrics will be matched against headers.
     *
     * @throws Exception on type mismatch between headers (if present) and the row elements.
     */
    void add(Row r) throws Exception;

    void setTimestampFormat(SimpleDateFormat f);

}
