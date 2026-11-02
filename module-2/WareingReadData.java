/*
 * Name: Kristian Wareing
 * Date: November 1, 2026
 * Assignment: CSD-420 Module 2.2 Programming Assignment
 *
 * Purpose: Reads "wareing datafile.dat", which was created by WareingWriteData,
 * and displays every record it contains. Each record holds five integers
 * followed by five doubles, and the file may hold several records because
 * the writer appends a new record every time it runs.
 *
 * Author: Kristian Wareing, with assistance from Claude (Anthropic).
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class WareingReadData {

    public static void main(String[] args) {
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(
                new FileInputStream(DataFileConfig.FILE_NAME)))) {
            displayAllRecords(input);
        } catch (FileNotFoundException e) {
            System.err.println("Error: \"" + DataFileConfig.FILE_NAME
                    + "\" was not found. Run WareingWriteData first to create it.");
        } catch (IOException e) {
            System.err.println("Error: could not read \"" + DataFileConfig.FILE_NAME
                    + "\". " + e.getMessage());
        }
    }

    /**
     * Reads and prints records until the end of the file is reached.
     *
     * @param input the stream to read from
     * @throws IOException if the file cannot be read or a record is incomplete
     */
    private static void displayAllRecords(DataInputStream input) throws IOException {
        int recordNumber = 0;

        // available() returns 0 once every byte in the file has been read.
        while (input.available() > 0) {
            recordNumber++;
            try {
                int[] integers = readIntegers(input, DataFileConfig.ARRAY_SIZE);
                double[] doubles = readDoubles(input, DataFileConfig.ARRAY_SIZE);

                System.out.println("Record " + recordNumber + ":");
                System.out.println("  Integers: " + Arrays.toString(integers));
                System.out.println("  Doubles:  " + Arrays.toString(doubles));
            } catch (EOFException e) {
                throw new IOException("Record " + recordNumber
                        + " is incomplete. The file may be corrupted.", e);
            }
        }

        if (recordNumber == 0) {
            System.out.println("\"" + DataFileConfig.FILE_NAME + "\" is empty.");
        } else {
            System.out.println("Total records read: " + recordNumber);
        }
    }

    /**
     * Reads a fixed number of integers from the stream.
     *
     * @param input the stream to read from
     * @param size  the number of integers to read
     * @return the integers that were read
     * @throws IOException if the integers cannot be read
     */
    private static int[] readIntegers(DataInputStream input, int size) throws IOException {
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = input.readInt();
        }
        return values;
    }

    /**
     * Reads a fixed number of doubles from the stream.
     *
     * @param input the stream to read from
     * @param size  the number of doubles to read
     * @return the doubles that were read
     * @throws IOException if the doubles cannot be read
     */
    private static double[] readDoubles(DataInputStream input, int size) throws IOException {
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = input.readDouble();
        }
        return values;
    }
}
