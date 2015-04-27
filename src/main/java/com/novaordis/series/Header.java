package com.novaordis.series;

import java.text.Format;

/**
 * The internal representation of a series header. Usually it contains a name and a measure unit.
 *
 * Example:
 *
 * "Duration (ms)"
 *
 * or
 *
 * "Old Generation Capacity (MB)"
 *
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2013 Nova Ordis LLC
 */
public interface Header
{
    String getName();

    /**
     * May return null if there is no measure unit.
     */
    String getMeasureUnit();

    /**
     * @return the header's external label, value to use in external representations, such as file headers, etc. Aside from name
     *         it may include measure unit, etc.
     */
    String getLabel();

    /**
     * Null is acceptable, we'll use default formatting
     */
    Format getFormat();

    Class getType();

}
