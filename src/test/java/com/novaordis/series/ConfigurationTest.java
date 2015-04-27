package com.novaordis.series;

import com.novaordis.series.command.Info;
import com.novaordis.series.command.Resample;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class ConfigurationTest extends Assert
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(ConfigurationTest.class);

    // Static --------------------------------------------------------------------------------------

    @AfterClass
    public static void scratchCleanup() throws Exception
    {
        Tests.cleanup();
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    @Test
    public void multipleFilesConfiguration() throws Exception
    {
        Tests.createFile("file1.csv", "a, b, c");
        Tests.createFile("dir1/file2.csv", "d, e, f");
        Tests.createFile("dir1/dir2/file3.csv", "g, h, i");
        Tests.createFile("file4.csv", "1, 2, 3");

        File dir = Tests.getScratchDirectory();

        Configuration c = new Configuration(
            new String[]
                {
                    dir.toString() + "/file1.csv",
                    dir.toString() + "/dir1/file2.csv",
                    dir.toString() + "/dir1/dir2/file3.csv",
                    dir.toString() + "/file4.csv"
                });


        List<File> sources = c.getSources();

        assertEquals(4, sources.size());

        assertEquals(new File(dir, "file1.csv"), sources.get(0));
        assertEquals(new File(dir, "dir1/file2.csv"), sources.get(1));
        assertEquals(new File(dir, "dir1/dir2/file3.csv"), sources.get(2));
        assertEquals(new File(dir, "file4.csv"), sources.get(3));
    }

    @Test
    public void nonExistentFile() throws Exception
    {
        try
        {
            new Configuration(
                new String[]
                    {
                        "/there/is/no/such/file.csv"
                    });
            fail("should have failed with UserErrorException");
        }
        catch(UserErrorException e)
        {
            log.info(e.getMessage());
        }
    }

    @Test
    public void spaceInCommandLineArgument_Quotas_NoBackslashes() throws Exception
    {
        Tests.createFile("This File Name Contains Spaces.csv", "something");

        File dir = Tests.getScratchDirectory();

        // the shell wrapper encloses an argument that contains spaces in quotas

        Configuration c = new Configuration(
            new String[]
                {
                    "\"" + dir.toString() + "/This",
                    "File",
                    "Name",
                    "Contains",
                    "Spaces.csv\""
                });


        List<File> sources = c.getSources();

        assertEquals(1, sources.size());

        assertEquals(new File(dir, "This File Name Contains Spaces.csv"), sources.get(0));
    }

    @Test
    public void spaceInCommandLineArgument_Both_Quotas_And_Backslashes() throws Exception
    {
        Tests.createFile("This File Name Contains Spaces.csv", "something");

        File dir = Tests.getScratchDirectory();

        // the shell wrapper encloses an argument that contains spaces in quotas
        // if back-slashed spaces are found, the backslashes are sent, too

        Configuration c = new Configuration(
            new String[]
                {
                    "\"" + dir.toString() + "/This\\",
                    "File\\",
                    "Name\\",
                    "Contains\\",
                    "Spaces.csv\""
                });


        Command comm = c.getCommand();

        assertTrue(comm instanceof Info);

        List<File> sources = c.getSources();

        assertEquals(1, sources.size());

        assertEquals(new File(dir, "This File Name Contains Spaces.csv"), sources.get(0));
    }

    @Test
    public void spaceInCommandLineArgument_TwoArguments() throws Exception
    {
        Tests.createFile("This File Name Contains Spaces.csv", "something");
        Tests.createFile("This File Name Contains Spaces 2.csv", "something2");
        Tests.createFile("third_file.csv", "something3");

        File dir = Tests.getScratchDirectory();

        // the shell wrapper encloses an argument that contains spaces in quotas

        Configuration c = new Configuration(
            new String[]
                {
                    "\"" + dir.toString() + "/This",
                    "File",
                    "Name",
                    "Contains",
                    "Spaces.csv\"",
                    "\"" + dir.toString() + "/This",
                    "File",
                    "Name",
                    "Contains",
                    "Spaces",
                    "2.csv\"",
                    dir.toString() + "/third_file.csv"
                });

        Command comm = c.getCommand();

        assertTrue(comm instanceof Info);

        List<File> sources = c.getSources();

        assertEquals(3, sources.size());

        assertEquals(new File(dir, "This File Name Contains Spaces.csv"), sources.get(0));
        assertEquals(new File(dir, "This File Name Contains Spaces 2.csv"), sources.get(1));
        assertEquals(new File(dir, "third_file.csv"), sources.get(2));
    }

    @Test
    public void resample() throws Exception
    {
        Tests.createFile("file1.csv", "");
        Tests.createFile("file2.csv", "");
        File dir = Tests.getScratchDirectory();

        Configuration c = new Configuration(
            new String[]
                {
                    "resample",
                    "--begin", "2013/01/10", "01:10:10", "PM" ,
                    "--end", "2013/01/10", "01:10:11", "PM" ,
                    "--interval", "20",
                    dir.toString() + "/file1.csv",
                    dir.toString() + "/file1.csv",
                });


        Resample r = (Resample)c.getCommand();

        assertEquals(20, r.getInterval());
        assertEquals(toDate("2013/01/10 01:10:10 PM"), r.getBeginTime());
        assertEquals(toDate("2013/01/10 01:10:11 PM"), r.getEndTime());

        List<File> sources = c.getSources();
        assertEquals(2, sources.size());
        assertEquals(new File(dir, "file1.csv"), sources.get(0));
        assertEquals(new File(dir, "file1.csv"), sources.get(1));
    }


    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    private static long toDate(String s) throws Exception
    {
        return Configuration.DATE_FORMAT.parse(s).getTime();
    }

    // Inner classes -------------------------------------------------------------------------------
}
