package com.novaordis.series;

import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public interface Command
{
    String getName();

    void execute(List<Series> series) throws Exception;
}
