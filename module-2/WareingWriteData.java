/*
 * Name: Kristian Wareing
 * Date: November 1, 2026
 * Assignment: CSD-420 Module 2.2 Programming Assignment
 *
 * Purpose: Generates an array of five random integers and an array of five
 * random doubles, then writes them to "wareing datafile.dat" in binary form.
 * If the file does not exist, it is created. If it already exists, the new
 * data is appended to the end of the file.
 *
 * Author: Kristian Wareing, with assistance from Claude (Anthropic).
 */
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class WareingWriteData {

    private static final int MAX_INT = 100;        // integers range from 0 to 99
    private static final double MAX_DOUBLE = 100.0; // doubles range from 0.0 to just under 100.0

    public static void main(String[] args) {
        Random random = new Random();

        int[] integers = createRandomIntegers(random, DataFileConfig.ARRAY_SIZE);
        double[] doubles = createRandomDoubles(random, DataFileConfig.ARRAY_SIZE);

        try {
            appendToFile(integers, doubles);
            System.out.println("Data written to \"" + DataFileConfig.FILE_NAME + "\":");
            System.out.println("Integers: " + Arrays.toString(integers));
            System.out.println("Doubles:  " + Arrays.toString(doubles));
        } catch (IOException e) {
            System.err.println("Error: could not write to \"" + DataFileConfig.FILE_NAME
                    + "\". " + e.getMessage());
        }
    }

    /**
     * Creates an array of random integers from 0 to MAX_INT - 1.
     *
     * @param random the random number generator to use
     * @param size   the number of values to create
     * @return the array of random integers
     */
    private static int[] createRandomIntegers(Random random, int size) {
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(MAX_INT);
        }
        return values;
    }

    /**
     * Creates an array of random doubles from 0.0 up to MAX_DOUBLE.
     *
     * @param random the random number generator to use
     * @param size   the number of values to create
     * @return the array of random doubles
     */
    private static double[] createRandomDoubles(Random random, int size) {
        double[] values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextDouble() * MAX_DOUBLE;
        }
        return values;
    }

    /**
     * Appends one record (the integers followed by the doubles) to the data file.
     * Opening FileOutputStream with append set to true creates the file if it is
     * missing and adds to the end of it if it already exists.
     *
     * @param integers the integers to write
     * @param doubles  the doubles to write
     * @throws IOException if the file cannot be opened or written
     */
    private static void appendToFile(int[] integers, double[] doubles) throws IOException {
        try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(DataFileConfig.FILE_NAME, true)))) {
            for (int value : integers) {
                output.writeInt(value);
            }
            for (double value : doubles) {
                output.writeDouble(value);
            }
        }
    }
}
