/*
 * Name: Kristian Wareing
 * Date: November 1, 2026
 * Assignment: CSD-420 Module 2.2 Programming Assignment
 *
 * Purpose: Holds the settings shared by WareingWriteData and WareingReadData
 * so the file name and record size are defined in one place (DRY).
 *
 * Author: Kristian Wareing, with assistance from Claude (Anthropic).
 */
public final class DataFileConfig {

    /** Name of the binary data file both programs use. */
    public static final String FILE_NAME = "wareing datafile.dat";

    /** Number of integers and number of doubles stored in each record. */
    public static final int ARRAY_SIZE = 5;

    // Prevents this utility class from being instantiated.
    private DataFileConfig() {
    }
}
