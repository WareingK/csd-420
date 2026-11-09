/*
 * Kristian Wareing
 * November 8, 2026
 * CSD-420 Advanced Java Programming
 * Module 3.2 Programming Assignment - Generics
 *
 * Purpose: Fills an ArrayList with 50 random integers from 1 to 20, then
 * uses a generic static method to return a new ArrayList containing each
 * value only once, in the order it first appeared. The original list is
 * left unchanged.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RemoveDuplicatesTest {

    public static void main(String[] args) {
        Random random = new Random();

        // Build the original list: 50 random values from 1 to 20.
        // Duplicates are guaranteed since there are only 20 possible values.
        ArrayList<Integer> original = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            original.add(random.nextInt(20) + 1); // nextInt(20) gives 0-19, +1 shifts to 1-20
        }

        ArrayList<Integer> noDuplicates = removeDuplicates(original);

        System.out.println("Original list (" + original.size() + " values):");
        System.out.println(original);
        System.out.println();
        System.out.println("List with duplicates removed (" + noDuplicates.size() + " values):");
        System.out.println(noDuplicates);
        System.out.println();

        // Self-check: the new list should have no repeats and contain
        // every distinct value from the original list.
        boolean noRepeats = new HashSet<>(noDuplicates).size() == noDuplicates.size();
        boolean allValuesKept = new HashSet<>(noDuplicates).equals(new HashSet<>(original));
        System.out.println("Test - no repeated values in new list: " + (noRepeats ? "PASS" : "FAIL"));
        System.out.println("Test - every original value is kept:   " + (allValuesKept ? "PASS" : "FAIL"));
    }

    /**
     * Returns a new ArrayList containing the elements of the given list with
     * duplicates removed. Each element is kept the first time it appears, so
     * the original order is preserved. The given list is not modified.
     *
     * A HashSet tracks which elements have already been seen, so each
     * "seen before?" check runs in constant time instead of scanning a list.
     *
     * @param list the list to remove duplicates from
     * @param <E>  the type of elements in the list
     * @return a new ArrayList with each distinct element appearing once
     */
    public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
        ArrayList<E> result = new ArrayList<>();
        HashSet<E> seen = new HashSet<>();

        for (E item : list) {
            // add() returns true only if the item was NOT already in the set,
            // so this checks "seen before?" and records it in a single step.
            if (seen.add(item)) {
                result.add(item);
            }
        }

        return result;
    }
}
